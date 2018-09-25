package tianchi.com.risksourcecontrol.base;

import android.app.Application;
import android.app.ProgressDialog;

import com.baidu.mapapi.SDKInitializer;
import com.supermap.data.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.SuperMapConfig;
import tianchi.com.risksourcecontrol.db.MySqlHelper;
import tianchi.com.risksourcecontrol.location.LocationService;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.AssetsUtils;
import tianchi.com.risksourcecontrol.util.CameraUtils;
import tianchi.com.risksourcecontrol.util.FileUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.util.SharedPreferencesUtil;
import tianchi.com.risksourcecontrol.util.ZipUtils;



/**
 * 初始化App配置
 * Created by Kevin on 2017/12/26.
 */

public class AppInitialization extends Application {
    private ProgressDialog m_progressDialog;
    private static AppInitialization s_Instance;

    //超图相关文件夹路径
    private String strFolders[] = new String[]{SuperMapConfig.LIC_PATH,
            SuperMapConfig.TEMP_PATH, SuperMapConfig.WEB_CACHE_PATH,
            SuperMapConfig.MAP_DATA_PATH};
    private File m_licenseFile;
    private boolean isOk = false;
    private boolean downloadIsOk = false;

    public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        s_Instance = this;
        initOhters();
        try {
//            initDB();
            initSuperMap();
            locationService = new LocationService(getApplicationContext());
            //mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
            SDKInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppInitialization getInstance() {
        return s_Instance;
    }

    //初始化其他
    private void initOhters() {
        SharedPreferencesUtil.init(getApplicationContext());
        AssetsUtils.init(getApplicationContext());
        FileUtils.getInstance();
        CameraUtils.initPicFolder();
        OkHttpUtils.getInstance();
        UserSingleton.getInstance();
        FoldersConfig.initFolders();
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("文件正在下载中...");
        m_progressDialog.setCancelable(false);


    }


    //    //初始化数据库
    //    private void initDB() {
    //        DataBaseOpenHelper.init(getApplicationContext(), DatabaseConfig.DB_NAME,
    //                DatabaseConfig.DATABASE_VERSION, DatabaseConfig.getTableSqlsList());
    //        DataBaseOpenHelper.initPicFolder().getWritableDatabase();
    //    }

    //初始化并连接mysql数据库
    private void initDB() throws Exception {
        new MySqlHelper().init(getApplicationContext());
    }

    //初始化超图s
    private void initSuperMap() throws IOException {
        //创建文件夹
        for (String _strFolder : strFolders) {
            File f = new File(_strFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        if (configLicense()) {
            Environment.setLicensePath(SuperMapConfig.LIC_PATH);
            Environment.setTemporaryPath(SuperMapConfig.TEMP_PATH);
            Environment.setWebCacheDirectory(SuperMapConfig.WEB_CACHE_PATH);
            Environment.initialization(this);

        }
        if (!new File(SuperMapConfig.DEFAULT_WORKSPACE_PATH).exists()) {

            configWorkSpace();
        }
    }

    //配置许可文件 //从服务器下载，判断试用许可的时间
    private boolean configLicense() {

        String license = SuperMapConfig.LIC_PATH + SuperMapConfig.LIC_NAME;
        m_licenseFile = new File(license);
        if (!m_licenseFile.exists()) {
//            downLoadLIcense();
//            return downloadIsOk;

            InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.LIC_NAME);
            if (is != null) {
                boolean isOk = FileUtils.getInstance().copy(is, SuperMapConfig.FULL_LIC_PATH);
                return isOk;
            }

        }
        return true;
    }



    /**
     * 配置工作空间文件(解压smwu到指定目录)
     *
     * @datetime 2018/1/2  13:33.
     */
    private void configWorkSpace() throws IOException {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    unZipMapData(SuperMapConfig.DEFAULT_WORKSPACE_NAME);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 解压对应地图数据zip包
     */
    private void unZipMapData(String zipFileName) throws IOException {
        String[] datas = AssetsUtils.getInstance().opendDir(SuperMapConfig.ASSETS_DATA_PATH);
        for (String data : datas) {
            if (FileUtils.getFileNameNoEx(data).equals(FileUtils.getFileNameNoEx(zipFileName))) {
                InputStream is = AssetsUtils.getInstance().open(SuperMapConfig.ASSETS_DATA_PATH + "/" + data);
                File tempFile = new File(SuperMapConfig.MAP_DATA_PATH);
                if (!tempFile.exists()) {
                    FileUtils.getInstance().mkdirs(SuperMapConfig.MAP_DATA_PATH);
                }
                String zipFilePath = SuperMapConfig.MAP_DATA_PATH + "/" + data;
                boolean result = FileUtils.getInstance().copy(is, zipFilePath);
                if (result) {
                    ZipUtils.upZipFileDir(new File(zipFilePath), SuperMapConfig.MAP_DATA_PATH);
                    //删除压缩包
                    File zipFile = new File(zipFilePath);
                    zipFile.delete();
                }
            }
        }
    }
}
