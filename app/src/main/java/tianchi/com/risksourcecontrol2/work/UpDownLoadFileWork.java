package tianchi.com.risksourcecontrol2.work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.model.OnDownloadFileListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;

/**
 * Created by Kevin on 2018/2/5.
 *
 * 文件上传、下载
 */

public class UpDownLoadFileWork {
    //    public static Lock m_lock = new ReentrantLock();
    final static MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static void upLoadFile2Server(File file, String url, String keyValue, String idLoginName, OnUploadFileListener uploadFileListener) {
        String filePath = file.getName();

//        LogUtils.i("filePath", filePath);

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = null;

        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"upLoadType\""),
                        RequestBody.create(FORM_CONTENT_TYPE, keyValue))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"idLoginName\""),
                        RequestBody.create(FORM_CONTENT_TYPE, idLoginName))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file\"; filename=\"" + file.getName() + "\""), fileBody)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (uploadFileListener != null) {
                    if (e instanceof SocketTimeoutException) {//超时异常
                        uploadFileListener.uploadFailed("提交超时");
                    } else if (e instanceof ConnectException) {//连接异常
                        uploadFileListener.uploadFailed("连接服务器异常");
                    } else {
                        uploadFileListener.uploadFailed("服务器异常");
                    }
//                    LogUtils.i("uploadFile() e=" + e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonString = response.body().string();
                    if (!jsonString.contains("status") || !jsonString.contains("msg")) {
                        if (uploadFileListener != null) {
                            uploadFileListener.uploadFailed("服务器解析错误_jsonString:" + jsonString);
                            return;
                        }
                    }
                    int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");//状态码
                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");//状态码
                    //                    String msg = GsonUtils.getStringNodeJsonString(response.body().string(), "msg");//附带信息
                    switch (resultCode) {
                        case 0:
                            if (uploadFileListener != null) {
                                uploadFileListener.uploadFailed(msg);
                            }
                            break;
                        case 1:
                            if (uploadFileListener != null) {
                                uploadFileListener.uploadSucceed("上传图片成功");
                            }
                            break;
                        default:
                            if (uploadFileListener != null) {
                                uploadFileListener.uploadFailed(msg);
                            }
                            break;
                    }
                } catch (Exception e) {
                    uploadFileListener.uploadFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                }
            }
        });
    }

    public static void downloadFileFromServer(String fileURL, String fileName, OnDownloadFileListener downloadFileListener) {
        new Thread() {
            @Override
            public void run() {
                //TODOAuto-generatedmethodstub
                try {
                    //                    m_lock.lock();
                    //创建一个url对象
                    URL url = new URL(fileURL);
                    //打开URL对应的资源输入流
                    InputStream is = url.openStream();
                    //把InputStream转化成ByteArrayOutputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    is.close();//关闭输入流
                    //将ByteArrayOutputStream转化成InputStream
                    is = new ByteArrayInputStream(baos.toByteArray());
                    //将InputStream解析成Bitmap
                    Bitmap bmp;
                    bmp = BitmapFactory.decodeStream(is);
                    //再次将ByteArrayOutputStream转化成InputStream
                    is = new ByteArrayInputStream(baos.toByteArray());
                    baos.close();
                    //打开手机文件对应的输出流
                    String folderPath = "";
                    if (fileURL.contains("user")) {
                        folderPath = FoldersConfig.USER_HEAD_PATH;
                    }
                    if (fileURL.contains("safeproduce")) {
                        folderPath = FoldersConfig.PRO_SAFETY_PIC_PATH;
                    }
                    if (fileURL.contains("safepatrol")) {
                        folderPath = FoldersConfig.SAFETY_PATROL_PIC_PATH;
                    }
                    if (fileURL.contains("riskreform")) {
                        folderPath = FoldersConfig.RISK_REFORM_PIC_PATH;
                    }
                    if (fileURL.contains("recitynotify")) {
                        folderPath = FoldersConfig.NOTICEFY;
                    }

                    File myCaptureFile = new File(folderPath, fileName);
                    if (!myCaptureFile.exists()) {
                        myCaptureFile.createNewFile();
                    }
                    FileOutputStream os = new FileOutputStream(myCaptureFile);
                    byte[] buff = new byte[1024];
                    int count = 0;
                    //将URL对应的资源下载到本地
                    while ((count = is.read(buff)) > 0) {
                        os.write(buff, 0, count);
                    }
                    os.flush();
                    //关闭输入输出流
                    is.close();
                    os.close();
//                    LogUtils.i("get bitmap inputstream success!");

                    if (downloadFileListener != null) {
                        if (bmp != null) {
                            downloadFileListener.downloadSucceed(bmp);
                        } else {
                            downloadFileListener.downloadFailed("初始化图片失败");
                        }
                        //                        m_lock.unlock();
                    }
                } catch (Exception e) {
                    //TODOAuto-generatedcatchblock
                    e.printStackTrace();
                    downloadFileListener.downloadFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                    //                    m_lock.unlock();

//                    LogUtils.i("error",e.toString());
                }
            }
        }.start();
    }
}
