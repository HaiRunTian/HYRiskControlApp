package tianchi.com.risksourcecontrol.activitiy.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyDatePicker;
import tianchi.com.risksourcecontrol.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.presenter.ModifyUserPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.CameraUtils;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.view.IModifyUserView;

/**
 * @描述 用户资料活动
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:08.
 */
public class UserProfileActivity extends BaseActivity implements MyTakePicDialog.OnItemClickListener, IModifyUserView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView m_tvBack;//返回
    private TextView m_tvEdit;//返回
    private TextView m_tvHead;//头像
    private EditText m_edtAccount;//账号
    private EditText m_edtRealName;//姓名
    //private EditText m_edtProjectRole;//职务
    private Spinner m_spProjectRole;//职务
    private EditText m_edtRegDate;//注册日期
    private EditText m_edtAddress;//地址
    private EditText m_edtEmail;//邮箱
    private EditText m_edtPhone;//手机
    private EditText m_edtOldPwd;//旧密码
    private EditText m_edtNewPwd;//新密码
    private EditText m_edtReNewPwd;//确认密码
    private TextView m_tvBirthday;//生日
    private TextView m_tvModifyPwd;//修改密码标签
    private EditText m_edtQQ;//QQ
    private RelativeLayout layoutModifyPwd;
    private ImageView m_imgvUserProfileHead;
    private RoundedBitmapDrawable m_roundedBitmapDrawable;
    private ProgressDialog m_progressDialog;
    private CheckBox cbEdit;//修改个人信息
    private CheckBox cbModifyPwd;//修改密码
    private AlertDialog m_dialog;//修改头像对话框
    //    private byte[] picBytes;//存储用户修改头像
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件
    private Bitmap picBitmap;//存储用户修改头像
    private Uri fileUri;//用户头像拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName;//照片全名xx.jpg
    private String tempUserHeadPicName;//临时照片全名xx.jpg
    //    private String picBase64String;//头像string
    private Bitmap defaultPicBitmap;//默认未修改的头像
    private boolean editTag = false;//是否修改标记
    private Map<String, String> tempUserInfo;//临时存放未修改前的用户信息
    private ModifyUserPresenter m_modifyUserPresenter = new ModifyUserPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initView();
        initValues();
        saveTempInfo();
    }

    @Override
    public void onBackPressed() {
        saveInfoRemind();
    }
    //初始化界面数据
    @Override
    public void initValues() {
        m_spProjectRole.setEnabled(false);
        if (getPicture() != null && getPicture().length() > 0) {
            pictureName = getPicture();
        }
        if (UserSingleton.getUserInfo().getLoginName() != null)
            m_edtAccount.setText(UserSingleton.getUserInfo().getLoginName());//显示账号
        if (UserSingleton.getUserInfo().getRealName() != null)
            m_edtRealName.setText(UserSingleton.getUserInfo().getRealName());//显示姓名
        String role = UserSingleton.getUserInfo().getProjectRole();
        if (role != null) {
            List<String> role_list = Arrays.asList(getResources().getStringArray(R.array.project_roles));
            m_spProjectRole.setSelection(role_list.indexOf(role));//显示职务}
        }
        if (UserSingleton.getUserInfo().getMoblie() != null)
            m_edtPhone.setText(UserSingleton.getUserInfo().getMoblie());//显示手机
        if (UserSingleton.getUserInfo().getEmail() != null)
            m_edtEmail.setText(UserSingleton.getUserInfo().getEmail());//显示邮箱
        if (UserSingleton.getUserInfo().getOicq() != null)
            m_edtQQ.setText(UserSingleton.getUserInfo().getOicq());//显示QQ
        if (UserSingleton.getUserInfo().getBirthday() != null)
            m_tvBirthday.setText(DateTimeUtils.dateToString(
                    UserSingleton.getUserInfo().getBirthday(), "yyyy-MM-dd"));//显示生日
        if (UserSingleton.getUserInfo().getRegDate() != null)
            m_edtRegDate.setText(DateTimeUtils.dateToString(
                    UserSingleton.getUserInfo().getRegDate(), "yyyy-MM-dd"));//显示注册日期
        if (UserSingleton.getUserInfo().getAddress() != null)
            m_edtAddress.setText(UserSingleton.getUserInfo().getAddress());//显示地址
        if (getPicture().length() > 0) {//如果登录的账号有头像
            File picFile = new File(FoldersConfig.USER_HEAD_PATH, getPicture());
            if (picFile.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(picFile);
                    defaultPicBitmap = BitmapFactory.decodeStream(fis);
                    setRoundedBitmapDrawable(defaultPicBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {//若读取不到用户头像则从服务器下载
                m_modifyUserPresenter.downloadImgFile();
            }
        }
    }

    //保存未更改前的用户信息
    @Override
    public void saveTempInfo() {
        tempUserInfo = new HashMap<String, String>();
        tempUserInfo.put("realName", getRealName());
        tempUserInfo.put("projectRole", getProjectRole());
        tempUserInfo.put("mobile", getPhoneNum());
        tempUserInfo.put("email", getEmail());
        tempUserInfo.put("qq", getQQNum());
        tempUserInfo.put("birthday", getBirthday());
        tempUserInfo.put("address", getAddress());
        tempUserInfo.put("oldPwd", getOldPassword());
        tempUserInfo.put("newPwd", getNewPassword());
        tempUserInfo.put("renewPwd", getReNewPassword());
        tempUserHeadPicName = UserSingleton.getUserInfo().getPicture();
    }

    //更新用户信息到实体
    @Override
    public void updateUserInfoBean() {
        if (!TextUtils.isEmpty(getNewPassword()))
            UserSingleton.getUserInfo().setPassword(getNewPassword());
        if (picBitmap != null)
            UserSingleton.getUserInfo().setPicture(pictureName);
        UserSingleton.getUserInfo().setRealName(getRealName());
        UserSingleton.getUserInfo().setProjectRole(getProjectRole());
        UserSingleton.getUserInfo().setMoblie(getPhoneNum());
        UserSingleton.getUserInfo().setEmail(getEmail());
        UserSingleton.getUserInfo().setOicq(getQQNum());
        try {
            UserSingleton.getUserInfo().setBirthday(DateTimeUtils.stringToDate(getBirthday(), "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }//生日
        UserSingleton.getUserInfo().setAddress(getAddress());
    }

    //检查是否有信息已修改过
    @Override
    public boolean isModified() {
        //        if (picBytes != null)
        if (picBitmap != null)
            editTag = true;
        if (tempUserInfo.size() > 0) {
            if (!getRealName().equals(tempUserInfo.get("realName")))
                editTag = true;
            if (!getProjectRole().equals(tempUserInfo.get("projectRole")))
                editTag = true;
            if (!getPhoneNum().equals(tempUserInfo.get("mobile")))
                editTag = true;
            if (!getEmail().equals(tempUserInfo.get("email")))
                editTag = true;
            if (!getQQNum().equals(tempUserInfo.get("qq")))
                editTag = true;
            if (!getBirthday().equals(tempUserInfo.get("birthday")))
                editTag = true;
            if (!getAddress().equals(tempUserInfo.get("address")))
                editTag = true;
            if (!getOldPassword().equals(tempUserInfo.get("oldPwd")))
                editTag = true;
            if (!getNewPassword().equals(tempUserInfo.get("newPwd")))
                editTag = true;
            if (!getReNewPassword().equals(tempUserInfo.get("renewPwd")))
                editTag = true;
        }
        return editTag;
    }

    //检查所有项是否为空
    @Override
    public boolean isNotEmpty() {
        boolean tag = true;
        if (getPhoneNum().equals("")) {
            m_edtPhone.setError("手机号码不能为空");
            tag = false;
        }
        if (getEmail().equals("")) {
            m_edtEmail.setError("邮箱不能为空");
            tag = false;
        }
        if (getQQNum().equals("")) {
            m_edtQQ.setError("QQ号码不能为空");
            tag = false;
        }
        if (getAddress().equals("")) {
            m_edtAddress.setError("联系地址不能为空");
            tag = false;
        }
        return tag;
    }

    //检查修改密码是否合法
    @Override
    public boolean checkValidity() {
        boolean tag = true;
        if (TextUtils.isEmpty(getOldPassword()) &&
                TextUtils.isEmpty(getNewPassword()) &&
                TextUtils.isEmpty(getReNewPassword())) {
            tag = true;
        } else {
            if (TextUtils.isEmpty(getOldPassword()) ||
                    TextUtils.isEmpty(getNewPassword()) ||
                    TextUtils.isEmpty(getReNewPassword())) {
                tag = false;
            }
            if (getOldPassword().equals(m_edtNewPwd.getText().toString())) {
                tag = false;
            }
            if (!getNewPassword().equals(m_edtReNewPwd.getText().toString())) {
                tag = false;
            }
        }
        return tag;
    }

    //获取修改过的用户信息
    @Override
    public Map<String, Object> getMdfUserInfo() {
        Map<String, Object> mdfUserInfo = new HashMap<String, Object>();
        mdfUserInfo.put("userId", UserSingleton.getUserInfo().getUserId());
        mdfUserInfo.put("loginName", UserSingleton.getUserInfo().getLoginName());
        mdfUserInfo.put("password", getNewPassword());
        mdfUserInfo.put("realName", getRealName());
        mdfUserInfo.put("projectRole", (getProjectRole()));
        mdfUserInfo.put("moblie", getPhoneNum());
        mdfUserInfo.put("email", getEmail());
        mdfUserInfo.put("oicq", getQQNum());
        mdfUserInfo.put("address", getAddress());
        mdfUserInfo.put("sectionList", getSectionList());
        if (picBitmap != null) {
            //            Bitmap _bitmap = CameraUtils.getBitmapFromResource(this, R.mipmap.ic_smile);
            //            picBytes = CameraUtils.Bitmap2Bytes(_bitmap);
            mdfUserInfo.put("picture", getPicture());
        } else {
            mdfUserInfo.put("picture", "");
        }
        //生日
        try {
            mdfUserInfo.put("birthday", getBirthday());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mdfUserInfo;
    }

    @Override
    public File getMdfUserHeadImgFile() {
        if (resultImgFile != null)
            return resultImgFile;
        return null;
    }

    @Override
    public Bitmap getDownloadImgFile() {
        return null;
    }


    private void initView() {
        cbEdit = (CheckBox) findViewById(R.id.cbEdit);
        cbModifyPwd = (CheckBox) findViewById(R.id.cbModifyPassword);
        m_tvHead = (TextView) findViewById(R.id.tvHead);
        m_edtAccount = (EditText) findViewById(R.id.edtReformAccount);
        m_edtRealName = (EditText) findViewById(R.id.edtName);
        //  m_edtProjectRole = (EditText) findViewById(R.id.edtProjectRole);
        m_spProjectRole = (Spinner) findViewById(R.id.spProjectRole);
        m_edtRegDate = (EditText) findViewById(R.id.edtRegDate);
        m_tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        m_imgvUserProfileHead = (ImageView) findViewById(R.id.imgvUserProfileHead);
        m_tvBack = (TextView) findViewById(R.id.tvProfileBack);
        m_edtAddress = (EditText) findViewById(R.id.edtAddress);
        m_edtEmail = (EditText) findViewById(R.id.edtEmail);
        m_tvModifyPwd = (TextView) findViewById(R.id.tvModifyPwd);
        m_edtQQ = (EditText) findViewById(R.id.edtQQ);
        m_edtPhone = (EditText) findViewById(R.id.edtPhone);
        m_edtOldPwd = (EditText) findViewById(R.id.edtOldPassWord);
        m_edtNewPwd = (EditText) findViewById(R.id.edtNewPassWord);
        m_edtReNewPwd = (EditText) findViewById(R.id.edtReNewPassWord);
        layoutModifyPwd = (RelativeLayout) findViewById(R.id.layoutModifyPwd);
        m_tvEdit = (TextView) findViewById(R.id.tvEdit);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("上传中...");
        m_progressDialog.setCancelable(false);
        m_tvBack.setOnClickListener(this);
        cbEdit.setOnCheckedChangeListener(this);
        m_tvEdit.setOnClickListener(this);
        cbModifyPwd.setOnCheckedChangeListener(this);
        m_tvBack.setOnClickListener(this);
        m_tvBirthday.setOnClickListener(this);
        m_tvHead.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvProfileBack:
                saveInfoRemind();
                break;
            case R.id.tvHead:
                MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                _takePicDialog.setOnItemClickListener(UserProfileActivity.this);
                m_dialog = _takePicDialog.showTakePicDialog(UserProfileActivity.this);
                m_dialog.show();
                break;
            case R.id.tvBirthday:
                MyDatePicker.ShowDatePicker(this, m_tvBirthday);
                break;
            case R.id.tvEdit:
                if (cbEdit.isChecked()) {
                    cbEdit.setChecked(false);
                } else {
                    cbEdit.setChecked(true);
                }
                break;
            default:
                break;
        }
    }

    private void saveInfoRemind() {
        if (isModified()) {//是否需要保存
            MyAlertDialog.showAlertDialog(this,
                    "保存个人资料", "是否保存个人资料", "确定", "直接退出", true,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (checkInput()) {
                                m_modifyUserPresenter.submitModified();
                            } else {
                                return;
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            //将所有变量置空
                            takPicFile = null;//用户头像拍照文件
                            resultImgFile = null;//最终生成的img文件
                            picBitmap = null;//存储用户修改头像
                            fileUri = null;//用户头像拍照文件uri
                            uri = null;//系统拍照或相册选取返回的uri
                            //                            downloadURL = "";//下载文件url
                            pictureName = "";//照片全名xx.jpg
                            tempUserInfo = null;
                            //回退当前用户头像图片信息
                            UserSingleton.getUserInfo().setPicture(tempUserHeadPicName);
                            finish();
                        }
                    });
            editTag = false;
        } else {
            finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
    }

    private boolean checkInput() {
        boolean isEmailOk = false;
        boolean isPhoneOk = false;
        boolean isRealNameOk = false;
        boolean isQQNumOk = false;
        boolean isProjectRoleOk = false;
        //验证邮箱
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\." +
                "[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern _pattern = Pattern.compile(str);
        Matcher _matcher = _pattern.matcher(m_edtEmail.getText().toString());
        if (_matcher.matches()) {
            isEmailOk = true;
        } else {
            isEmailOk = false;
            m_edtEmail.setError("邮箱格式不合法");
        }
        //验证电话号码
        _pattern = Pattern.compile("[0-9]*");
        _matcher = _pattern.matcher(m_edtPhone.getText().toString());
        if (_matcher.matches() && m_edtPhone.getText().toString().length() == 11) {
            isPhoneOk = true;
        } else {
            m_edtPhone.setError("请输入11位数字的电话号码");
            isPhoneOk = false;
        }
        //验证真实姓名
        if (getRealName().length() > 0) {
            isRealNameOk = true;
        } else {
            m_edtRealName.setError("请输入您的真实姓名");
            isRealNameOk = false;
        }
        //验证QQ合法性
        String regex = "[1-9][0-9]{4,14}";//第一位1-9之间的数字，第二位0-9之间的数字，数字范围4-14个之间
        //String regex2 = "[1-9]\\d{4,14}";//此句也可以
        isQQNumOk = m_edtQQ.getText().toString().matches(regex);

        //验证职务
        if (getProjectRole().length() > 0) {
            isProjectRoleOk = true;
        } else {
            MyToast.showMyToast(this, "请选择职务", Toast.LENGTH_SHORT);
            isProjectRoleOk = false;
        }
        return isEmailOk && isPhoneOk && isRealNameOk && isQQNumOk && isProjectRoleOk;
    }


    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakePicture:
                takPicFile = new File(CameraUtils.file, System.currentTimeMillis() + ".jpg");
                fileUri = Uri.fromFile(takPicFile);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnPickFromAlbum:
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CameraUtils.IMAGE_UNSPECIFIED);
                startActivityForResult(i, CameraUtils.PHOTO_REQUEST_GALLERY);
                break;
            case R.id.btnViewPicture:
                // 通过picBytes判断当前是否已经拍摄了照片或选择了照片
                if (getPicture().length() <= 0) {
                    MyToast.showMyToast(this, "还未拍摄或未选择!", Toast.LENGTH_SHORT);
                    return;
                }
                //打开照片查看
                File _file = new File(CameraUtils.file, getPicture());
                if (_file.exists()) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), CameraUtils.IMAGE_UNSPECIFIED);
                    startActivity(intent);
                } else {
                    if (resultImgFile != null && resultImgFile.exists()) {
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(_file), CameraUtils.IMAGE_UNSPECIFIED);
                        startActivity(intent);
                    } else {
                        MyToast.showMyToast(this, "臣妾找不到图片啊!", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case R.id.btnCancel:
                if (m_dialog != null)
                    m_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*裁剪不压缩，图片内存小，压缩质量*/
        if (resultCode == RESULT_CANCELED)
            return;
        if (requestCode == CameraUtils.PHOTO_REQUEST_TAKEPHOTO) {
            uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
            if (uri != null) {
                startPhotoZoom(uri);
            }
        }
        if (data == null)
            return;
        if (requestCode == CameraUtils.PHOTO_REQUEST_GALLERY) {
            uri = data.getData();
            if (uri != null) {
                startPhotoZoom(uri);
            }
        }
        if (requestCode == CameraUtils.PHOTO_REQUEST_CUT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                picBitmap = extras.getParcelable("data");
                resultImgFile = CameraUtils.bitMap2File(picBitmap);
                pictureName = resultImgFile.getName();
                m_modifyUserPresenter.submitModifiedUserHead();
            }
        }
        if (m_dialog != null)
            m_dialog.dismiss();
        /*不裁剪不压缩原图代码*/
        //        try {
        //            switch (requestCode) {
        //                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
        //                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
        //                    if (uri != null) {
        //                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        //                        if (picBitmap != null) {
        //                            resultImgFile = new File(new URI(uri.toString()));
        //                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
        //                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
        //                                if (_overLimit) {
        //                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
        //                                } else {
        //                                    pictureName = resultImgFile.getName();
        //                                    m_modifyUserPresenter.submitModifiedUserHead();
        //                                }
        //                            } else {
        //                                MyToast.showMyToast(this,"图片名不允许带特殊符号",Toast.LENGTH_SHORT);
        //                            }
        //                        }
        //                    }
        //                    break;
        //
        //                case CameraUtils.PHOTO_REQUEST_GALLERY:
        //                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
        //                    if (uri != null) {
        //                        String[] filePathColumns = {MediaStore.Images.Media.DATA};//取媒体文件路径集合
        //                        Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);//取选中图片的cursor
        //                        c.moveToFirst();
        //                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
        //                        String imgpath = c.getString(colindex);//取文件相对手机路径
        //                        c.close();
        //                        picBitmap = BitmapFactory.decodeFile(imgpath);
        //                        if (picBitmap != null) {
        //                            //                            pictureName = new File(new URI(uri.toString())).getName();
        //                            //                            resultImgFile = CameraUtils.bitMap2File(picBitmap);
        //                            //                            resultImgFile = new File(new URI(uri.toString()));
        //                            resultImgFile = new File(imgpath);
        //                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
        //                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
        //                                if (_overLimit) {
        //                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
        //                                } else {
        //                                    pictureName = resultImgFile.getName();
        //                                    m_modifyUserPresenter.submitModifiedUserHead();
        //                                }
        //                            } else {
        //                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
        //                            }
        //                        }
        //                    }
        //                    break;
        //            }
        //        } catch (Exception e) {
        //            MyToast.showMyToast(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        //        }
    }

    /*裁剪图片*/
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
    }

    //设置圆角图片
    private void setRoundedBitmapDrawable(Bitmap src) {
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()) {
            dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight() / 2, 0, src.getHeight(), src.getHeight());
        } else {
            dst = Bitmap.createBitmap(src, 0, src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(), src.getWidth());
        }
        m_roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), dst);
        m_roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
        m_roundedBitmapDrawable.setAntiAlias(true);
        m_imgvUserProfileHead.setImageDrawable(m_roundedBitmapDrawable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbEdit:
                if (isChecked) {
                    m_edtPhone.setEnabled(true);
                    m_edtEmail.setEnabled(true);
                    m_edtRealName.setEnabled(true);
                    m_spProjectRole.setEnabled(true);
                    m_edtQQ.setEnabled(true);
                    m_edtAddress.setEnabled(true);
                    m_tvBirthday.setEnabled(true);
                    m_tvBirthday.setTextColor(Color.BLACK);
                    m_tvModifyPwd.setTextColor(Color.BLACK);
                    m_tvHead.setTextColor(Color.BLACK);
                    m_tvHead.setEnabled(true);
                    cbModifyPwd.setEnabled(true);
                } else {
                    m_edtPhone.setEnabled(false);
                    m_edtEmail.setEnabled(false);
                    m_edtQQ.setEnabled(false);
                    m_edtRealName.setEnabled(false);
                    m_spProjectRole.setEnabled(false);
                    m_edtAddress.setEnabled(false);
                    m_tvBirthday.setEnabled(false);
                    m_tvBirthday.setTextColor(Color.parseColor("#C4C4C4"));
                    m_tvModifyPwd.setTextColor(Color.parseColor("#C4C4C4"));
                    m_tvHead.setTextColor(Color.parseColor("#C4C4C4"));
                    m_tvHead.setEnabled(false);
                    cbModifyPwd.setEnabled(false);
                }
                break;
            case R.id.cbModifyPassword:
                if (isChecked) {
                    layoutModifyPwd.setVisibility(View.VISIBLE);
                } else {
                    layoutModifyPwd.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void alertMessage(String msg) {//显示界面弹窗信息
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
    }

    @Override
    public void showModifiedSucceed(String msg) {//显示修改成功信息
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        updateUserInfoBean();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void showModifiedFailed(String msg) {//显示修改失败信息
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void showModifiedUserHeadSucceed(String msg) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    MyToast.showMyToast(UserProfileActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
                    setRoundedBitmapDrawable(picBitmap);
                    UserSingleton.getUserInfo().picture = pictureName;//将修改成功的照片名存到实体
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        setRoundedBitmapDrawable(picBitmap);
        //        UserSingleton.getUserInfo().picture = pictureName;//将修改成功的照片名存到实体
    }

    @Override
    public void showModifiedUserHeadFailed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                MyToast.showMyToast(UserProfileActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void showDownloadUserHeadSucceed(Bitmap bitmap) {
        if (bitmap != null) {
            defaultPicBitmap = bitmap;
            pictureName = getPicture();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRoundedBitmapDrawable(defaultPicBitmap);
                    hideProgress();
                }
            });
        }
    }

    @Override
    public void showProgress(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage(msg);
            m_progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
    }

    @Override
    public void showServerError(String msg) {//显示服务器错误信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(UserProfileActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public String getLoginName() {//取登录账号文本
        return m_edtAccount.getText().toString();
    }

    @Override
    public String getRealName() {//取真实姓名文本
        return m_edtRealName.getText().toString();
    }

    @Override
    public String getProjectRole() {//取职务信息
        if (m_spProjectRole.getSelectedItem() != null) {
            return m_spProjectRole.getSelectedItem().toString();
        }
        return "";
    }

    @Override
    public String getPhoneNum() {//取手机号码文本
        return m_edtPhone.getText().toString();
    }

    @Override
    public String getEmail() {//取Email文本
        return m_edtEmail.getText().toString();
    }

    @Override
    public String getQQNum() {//取QQ文本
        return m_edtQQ.getText().toString();
    }

    @Override
    public String getBirthday() {//取生日文本
        return m_tvBirthday.getText().toString() + " 00:00:00";
    }

    @Override
    public String getRegDate() {//取注册日期文本
        return m_edtRegDate.getText().toString();
    }

    @Override
    public String getAddress() {//取地址文本
        return m_edtAddress.getText().toString();
    }

    @Override
    public String getPicture() {//取图片名字
        //        return CameraUtils.bitmap2StrByBase64(picBitmap);
        if (UserSingleton.getUserInfo().getPicture() != null)
            return UserSingleton.getUserInfo().getPicture();
        return "";
    }

    @Override
    public String getOldPassword() {//取旧密码文本
        return m_edtOldPwd.getText().toString();
    }

    @Override
    public String getNewPassword() {//取新密码文本
        return m_edtNewPwd.getText().toString();
    }

    @Override
    public String getReNewPassword() {//取确认新密码文本
        return m_edtReNewPwd.getText().toString();
    }

    @Override
    public String getSectionList() {
        return UserSingleton.getUserInfo().getSectionList();
    }

    @Override
    public String getDownloadFileUrl() {
        if (getPicture().length() > 0)
            return ServerConfig.URL_DOWNLOAD_USER_FILE +
                    getIdLoginName() + "/" + getPicture();
        return "";
    }

    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }


    //    class MyTextWatcher implements TextWatcher {
    //        private int id;
    //
    //        public MyTextWatcher(int edtID) {
    //            id = edtID;
    //        }
    //
    //        @Override
    //        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    //
    //        }
    //
    //        @Override
    //        public void onTextChanged(CharSequence s, int start, int before, int count) {
    //
    //        }
    //
    //        @Override
    //        public void afterTextChanged(Editable s) {
    //
    //        }
    //    }

}

