package tianchi.com.risksourcecontrol.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 拍照工具类
 * Created by Kevin on 2017/12/21.
 */

public class CameraUtils {
    /*
    * 拍照相关变量
    * */
    private static final int RESULT_OK = -1;
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 裁剪结果
    public static final String SD_CARD = Environment.getExternalStorageDirectory().getPath();
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static File file;

    public static void initPicFolder() {
        file = new File(Environment.getExternalStorageDirectory(), "RiskCtrlPic");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 只拍照不裁剪
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     * @param fileUri     文件uri
     * @return 系统相册或相机uri
     * @datetime 2018/2/3  13:43.
     */
    public static Uri getBitmapUriFromCG(int requestCode, int resultCode, Intent data, Uri fileUri) {
        Uri uri;
        switch (requestCode) {
            case CameraUtils.PHOTO_REQUEST_TAKEPHOTO://拍照
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        uri = data.getData();
                    else
                        uri = fileUri;
                    if (uri != null)
                        return uri;
                } else {
                    return null;
                }
                break;
            case CameraUtils.PHOTO_REQUEST_GALLERY://相册选
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                        return uri;
                    }
                } else {
                    return null;
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 拍照裁剪
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     * @param fileUri     文件uri
     * @param x     裁剪的宽/宽比
     * @param y     裁剪的高/高比
     * @return bitmap对象
     * @datetime 2018/2/3  13:43.
     */
    public static Bitmap getBitmapFromCG(Activity activity, int requestCode, int resultCode, Intent data, Uri fileUri, int x, int y) {
        Bitmap bitmap = null;
        switch (requestCode) {
            // 如果是拍照
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_OK) {
                    //没有指定特定存储路径的时候，data不为null
                    if (data != null) {
                        if (data.getData() != null) {
                            startPhotoZoom(activity, data.getData(), x, y);
                        } else {
                            startPhotoZoom(activity, fileUri, x, y);
                        }
                    } else {
                        startPhotoZoom(activity, fileUri, x, y);
                    }
                }
                break;
            // 如果是从相册选取
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    if (data.getData() != null) {
                        startPhotoZoom(activity, data.getData(), x, y);
                    }
                }
                break;
            //如果是裁剪完成
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                    }
                }
                return bitmap;
        }
        return null;
    }

    /**
     *  裁剪照片
     *  @param  activity 调用拍照的上下文
     *  @param  uri 资源定位符
     *  @param  aspectX 宽/宽比
     *  @param  aspectY 高/高比
     *  @return   null
     *  @datetime 2018/2/8  9:35.
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int aspectX, int aspectY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        //aspectX aspectY 是宽高的比例
        if (Build.MODEL.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9999);
            intent.putExtra("aspectY", 9998);
        } else {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        //outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", aspectX);
        intent.putExtra("outputY", aspectY);
        intent.putExtra("return-data", true);
        //        intent.putExtra("outputFormat", "JPEG");
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    /**
     *  byte[]转drawable
     *  @param  bytes 图片字节数组
     *  @return   Drawable对象
     *  @datetime 2018/2/8  9:30.
     */
    public static Drawable convertBytes2Drawable(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Drawable _drawable = new BitmapDrawable(bitmap);
        return _drawable;
    }

    /**
     * base64String转bitmap
     * @param @param  base64String
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: base64ToBitmap
     * @Description: TODO(base64l转换为Bitmap)
     */
    public static Bitmap base64ToBitmap(String base64String) {

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }

    /**
     *  压缩bundle为byte[]，影响质量
     *  @param  bundle bundle对象，一般是拍照后返回
     *  @return   byte[] 字节数组
     *  @datetime 2018/2/8  9:30.
     */
    public static byte[] compressBitmap2Bytes(Bundle bundle) {
        Bitmap photo = bundle.getParcelable("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /*
     *  bitmap转文件
     *  @param  bitmap 图片bitmap
     *  @return   File 图片文件
     *  @datetime 2018/2/8  9:32.
     */
    public static File bitMap2File(Bitmap bitmap) {
        //        BufferedOutputStream bos = null;
        byte[] picBytes = CameraUtils.Bitmap2Bytes(bitmap);
        ByteArrayInputStream bais = new ByteArrayInputStream(picBytes);
        File file = new File(Environment.getExternalStorageDirectory() + "/RiskCtrlPic");
        if (!file.exists())
            file.mkdir();
        File picture = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            //            bos = new BufferedOutputStream(new FileOutputStream(picture));
            //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            //            bos.flush();
            //            bos.close();
            FileOutputStream fos = new FileOutputStream(picture);
            byte[] buff = new byte[1024];
            int count = 0;
            while ((count = bais.read(buff)) > 0) {
                fos.write(buff, 0, count);
            }
            fos.flush();
            fos.close();
            bais.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }


    public static File bitMapToFile(Bitmap bitmap,String path,String fileName) {
        //        BufferedOutputStream bos = null;
        byte[] picBytes = CameraUtils.Bitmap2Bytes(bitmap);
        ByteArrayInputStream bais = new ByteArrayInputStream(picBytes);
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        File picture = new File(file, fileName);
        try {
            //            bos = new BufferedOutputStream(new FileOutputStream(picture));
            //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            //            bos.flush();
            //            bos.close();
            FileOutputStream fos = new FileOutputStream(picture);
            byte[] buff = new byte[1024];
            int count = 0;
            while ((count = bais.read(buff)) > 0) {
                fos.write(buff, 0, count);
            }
            fos.flush();
            fos.close();
            bais.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }

    /**
     *  把字节数组保存为一个文件
     *  @param  b 字节数组
     *  @param  outputFile 输出文件路径
     *  @return   File 输出文件
     *  @datetime 2018/2/8  9:33.
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     *  把Bitmap转Byte
     *  @param  bm Bitmap
     *  @return   byte[]
     *  @datetime 2018/2/8  9:36.
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     *  通过资源id获取bitmap对象
     *  @param  context
     *  @param  resId
     *  @return   Bitmap
     *  @datetime 2018/2/8  9:37.
     */
    public static Bitmap getBitmapFromResource(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        final Bitmap bmm = bd.getBitmap();
        return bmm;
    }

    /**
     *  bitmap转base64字符串格式
     *  @param  bit
     *  @return   String
     *  @datetime 2018/2/8  9:37.
     */
    public static String bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     *  base64字符串转byte[]字符串格式
     *  @param  base64String 图片字符串
     *  @return   byte[]
     *  @datetime 2018/2/8  9:37.
     */
    public static byte[] base642Bytes(String base64String) {
        byte[] _bytes = Base64.decode(base64String, Base64.DEFAULT);
        return _bytes;
    }

    /**
     *  byte[]转base64字符
     *  @param  b 字节数组
     *  @return   String 图片字符串
     *  @datetime 2018/2/8  9:37.
     */
    public static String byte2Base64String(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}
