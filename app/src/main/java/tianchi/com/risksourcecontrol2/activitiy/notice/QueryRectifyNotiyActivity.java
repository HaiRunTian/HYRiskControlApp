package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyDatePicker;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.view.IQueryNotifyView;


/**
 * Created by hairun.tian on 2018-06-25.
 * 查询草稿状态的整改通知单
 */

public class QueryRectifyNotiyActivity extends BaseActivity implements View.OnClickListener, IQueryNotifyView {
    private static final int GET_RELATIONSHIP = 0;
    private TextView m_tvBack;
    private EditText m_edtSubmiter;  //提交人
    private TextView m_tvStrDate; //开始时间
    private TextView m_tvEndDate; //终止时间
    private Button m_btnQuery; //开始查询
    private RadioButton m_rbtAllNotify, m_rbtSend, m_rbtDele, m_rbtWaitcheck, m_rbtChecked, m_rbtAllReply, m_rdbsend, m_rdbreply;
    //    private RadioGroup m_rdgNotifyState; //通知状态
    private RadioGroup m_rdgNotifyType; //通知类型
    private View m_layotNotify, m_layoutReply;
    private ProgressDialog m_progressDialog; //查询进度条
    private int notifyState = -1;
    private boolean isReplyNotify = false;
    List<RectifyNotifyInfo> m_rectifyNotifyInfoList;
    List<RectifyReplyInfo> m_rectifyReplyInfos;



  /*  @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rdbAllnotify:
                notifyState = -1;
                break;
            case R.id.rdbtnSended:
                notifyState = 1;
                break;
            case R.id.rdbtnDelete:
                notifyState = 2;
                break;

            case R.id.rdbAllReply:
                notifyState = -1;
                break;
            case R.id.rdbtnWaitCheck:
                notifyState = 0;
                break;

            case R.id.rdbchecked:
                notifyState = 1;
                break;
            case R.id.rdbsend:
                m_layotNotify.setVisibility(View.VISIBLE);
                m_layoutReply.setVisibility(View.GONE);
                break;
            case R.id.rdbtnreply:
                m_layoutReply.setVisibility(View.VISIBLE);
                m_layotNotify.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
*/
    public interface MyCallBack {
        String QuerySuccess(String string);
        String QueryFail();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_reform_query);
        initView();
        initEvent();
    }

    private void initEvent() {
        m_tvBack.setOnClickListener(this);
        m_btnQuery.setOnClickListener(this);
        m_tvStrDate.setOnClickListener(this);
        m_tvEndDate.setOnClickListener(this);
        m_edtSubmiter.setOnClickListener(this);
//        m_rdgNotifyState.setOnCheckedChangeListener(this);
        m_rbtAllNotify.setOnClickListener(this);
        m_rbtSend.setOnClickListener(this);
        m_rbtDele.setOnClickListener(this);
        m_rbtAllReply.setOnClickListener(this);
        m_rbtWaitcheck.setOnClickListener(this);
        m_rbtChecked.setOnClickListener(this);
        m_rdbsend.setOnClickListener(this);
        m_rdbreply.setOnClickListener(this);
    }

    private void initView() {

        m_tvBack = $(R.id.tvBack);
        m_edtSubmiter = $(R.id.edtLogSubmitMan);
        m_tvStrDate = $(R.id.tvStartDate);
        m_tvEndDate = $(R.id.tvEndDate);
//        m_cbAll = $(R.id.cbAll);
//        m_cbSend = $(R.id.cbSend);
//        m_cbDelete = $(R.id.cbDelete);
//        m_cbDraft = $(R.id.cbDraft);
        m_btnQuery = $(R.id.btnQuery);
//        m_rdgNotifyState = $(R.id.radioGroup);
        m_rectifyNotifyInfoList = new ArrayList<>();
        m_rectifyReplyInfos = new ArrayList<>();
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
        m_rdgNotifyType = $(R.id.radioGroup2);
        m_rdbsend = $(R.id.rdbsend);
        m_rdbreply = $(R.id.rdbtnreply);
        m_rdbsend.setChecked(true);


        m_layotNotify = $(R.id.layoutnotifycon);
        m_layoutReply = $(R.id.layoutreplycon);
        m_layoutReply.setVisibility(View.GONE);
        m_layotNotify.setVisibility(View.GONE);
        m_rbtAllNotify = $(R.id.rdbAllnotify);
        m_rbtSend = $(R.id.rdbtnSended);
        m_rbtDele = $(R.id.rdbtnDelete);
        m_rbtAllReply = $(R.id.rdbAllReply);
        m_rbtWaitcheck = $(R.id.rdbtnWaitCheck);
        m_rbtChecked = $(R.id.rdbchecked);


//        initUrl();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;

            case R.id.btnQuery:

                if (m_edtSubmiter.getText().toString().isEmpty()) {
                    MyToast.showMyToast(QueryRectifyNotiyActivity.this,"请输入提交人再查询",Toast.LENGTH_SHORT);
                    return;
                }
                m_progressDialog.setMessage("查询中...");
                String URL = initUrl();

//                LogUtils.i("url = ",URL);
                GetServeData(URL, new MyCallBack() {
                    @Override
                    public String QuerySuccess(String string) {
                        //整改通知单
                        if (!isReplyNotify) {
                            m_rectifyNotifyInfoList = GsonUtils.jsonToArrayBeans(string, "data", RectifyNotifyInfo.class);
                            if (m_rectifyNotifyInfoList.size() == 0) {
                                m_progressDialog.setMessage("没有查询到通知");

//                                MyToast.showMyToast(QueryRectifyNotiyActivity.this,"没有查询到通知",Toast.LENGTH_SHORT);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("data", string);
                                Intent intent = new Intent(QueryRectifyNotiyActivity.this, RectifyNotifyListActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } else {   //整改回复单
                            m_rectifyReplyInfos = GsonUtils.jsonToArrayBeans(string, "data", RectifyReplyInfo.class);
                            if (m_rectifyReplyInfos.size() == 0) {
                                m_progressDialog.setMessage("没有查询到通知");
//                                MyToast.showMyToast(QueryRectifyNotiyActivity.this,"没有查询到通知",Toast.LENGTH_SHORT);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("data", string);
                                Intent intent = new Intent(QueryRectifyNotiyActivity.this, RectifyReplyListActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                        m_progressDialog.dismiss();
                        return null;
                    }

                    @Override
                    public String QueryFail() {

                        return null;
                    }
                });

                break;

            case R.id.tvStartDate:

                MyDatePicker.ShowDatePicker(this, m_tvStrDate);
                break;

            case R.id.tvEndDate:
                MyDatePicker.ShowDatePicker(this, m_tvEndDate);
                break;
            case R.id.edtLogSubmitMan:
                int _roid = UserSingleton.getUserInfo().getRoleId();
                if (_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.OWNER_ALL), GET_RELATIONSHIP);
                } else if (_roid == 19) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_RELATIONSHIP);

                } else {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_RELATIONSHIP);
                }
                break;

            case R.id.rdbAllnotify:
                notifyState = -1;
                break;
            case R.id.rdbtnSended:
                notifyState = 1;
                break;
            case R.id.rdbtnDelete:
                notifyState = 2;
                break;

            case R.id.rdbAllReply:
                notifyState = -1;
                break;

            case R.id.rdbtnWaitCheck:
                notifyState = 0;
                break;

            case R.id.rdbchecked:
                notifyState = 1;
                break;

            case R.id.rdbsend:
//                m_layotNotify.setVisibility(View.VISIBLE);
//                m_layoutReply.setVisibility(View.GONE);
                break;
            case R.id.rdbtnreply:
//                m_layoutReply.setVisibility(View.VISIBLE);
//                m_layotNotify.setVisibility(View.GONE);

                break;

            default:
                break;
        }

    }

    private String initUrl() {

        String URL = null;
        if (m_rdgNotifyType.getCheckedRadioButtonId() == R.id.rdbsend) { //整改通知单
//            m_layotNotify.setVisibility(View.VISIBLE);
//            m_layoutReply.setVisibility(View.GONE);
            URL = ServerConfig.URL_QUERY_RECTIFY_NOTFIY;
            isReplyNotify = false;
        } else {
//            m_layotNotify.setVisibility(View.GONE);
//            m_layoutReply.setVisibility(View.VISIBLE);
            URL = ServerConfig.URL_QUERY_REPLY_NOTIFY;  //整改回复单
//            URL = ServerConfig.URL_QUERY_REPLY_NOTIFY;  //整改回复单
            isReplyNotify = true;
        }

        return URL;
    }

    private void GetServeData(String url, MyCallBack callBack) {
        m_progressDialog.show();
        OkHttpUtils.postAsync(url, getJSon().toString(), new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                m_progressDialog.setMessage("查询失败");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                int status = GsonUtils.getIntNoteJsonString(jsonString, "status");
                String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                if (status == -1) {
                    m_progressDialog.setMessage(msg);
                    m_progressDialog.dismiss();
                } else if (status == 0) {
                    m_progressDialog.setMessage(msg);
                    m_progressDialog.dismiss();
                } else if (status == 1) {
                    m_progressDialog.setMessage(msg);
                    callBack.QuerySuccess(jsonString);
                }
            }
        });


    }


    /*选择提交人*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case GET_RELATIONSHIP:
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }

                        m_edtSubmiter.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;
            }
        } catch (Exception e) {
            MyToast.showMyToast(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        }

    }


    /**
     * @return 获取提交人
     */
    @Override
    public String getSubmiter() {
        if (m_edtSubmiter.getText().toString().length() == 0) {
            return "";
        }
        return m_edtSubmiter.getText().toString();
    }

    /**
     * @return 获取开始时间
     */
    @Override
    public String getStartDate() {
        if (m_tvStrDate.getText().toString().length() == 0) {
            return "";
        }
        return m_tvStrDate.getText().toString() + " 00:00:00";
    }

    /**
     * @return 获取终止时间
     */
    @Override
    public String getEndDate() {
        if (m_tvEndDate.getText().toString().length() == 0) {
            return "";
        }
        return m_tvEndDate.getText().toString() + " 23:59:59";
    }

    /**
     * @return 获取通知状态
     */
    @Override
    public int getNotifyState() {
        return -1;
    }

    /**
     * @return 获取json
     */
    @Override
    public JSONObject getJSon() {
        JSONObject JsonObj = new JSONObject();

        try {
            JsonObj.put("realNames", getSubmiter());
            JsonObj.put("startTime", getStartDate());
            JsonObj.put("endTime", getEndDate());
            JsonObj.put("logState", getNotifyState());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JsonObj;
    }

/*    *//**
     * <p>Called when the checked radio button has changed. When the
     * selection is cleared, checkedId is -1.</p>
     *
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     *//*
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {

            //全部
            case 0:
                notifyState = 1;
                break;
            //已发送
            case 1:
                notifyState = 2;
                //监理审核
            case 2:
                notifyState = 3;
                break;
            //其它
            case 3:
                notifyState = 0;
                break;
            //任意状态
            case 4:
                notifyState = -1;
                break;
        }
    }*/


}
