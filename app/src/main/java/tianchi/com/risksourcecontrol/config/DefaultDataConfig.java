package tianchi.com.risksourcecontrol.config;


import java.io.File;
import java.io.InputStream;

import tianchi.com.risksourcecontrol.base.MyApplication;
import tianchi.com.risksourcecontrol.util.FileUtils;
import tianchi.com.risksourcecontrol.util.AssetsUtils;

public class DefaultDataConfig {

    private final String m_mapData = "MapData";
    public static final String MapDataPath = MyApplication.SDCARD + "SuperM/Demos/HighLowTypeRiskData/BaseDemo/";
    public static final String LicPath = MyApplication.SDCARD + "SuperM/License/";
    public static  final String LicName = "SuperMapiMobileTrial.slm";

    /**
     * 构造函数
     */
    public DefaultDataConfig() {

    }

    /**
     * 配置数据
     */
    public void autoConfig() {
        //如果有数据了则认为用户已经清理数据盘
            String mapDataPah = MapDataPath + "/";
            String license = LicPath + LicName;

            File licenseFile = new File(license);
            if (!licenseFile.exists())
                configLic();

            File dir = new File(mapDataPah);
            if (!dir.exists()) {
            FileUtils.getInstance().mkdirs(mapDataPah);
            configMapData();
        } else {
            if (FileUtils.getInstance().isFileExsit(mapDataPah + DefaultDataManager.s_mDefaultServer)) {
                return;
            }

            boolean hasMapData = false;
            File[] datas = dir.listFiles();
            for (File data : datas) {
                if (data.getName().endsWith("SMWU") || data.getName().endsWith("smwu")
                        || data.getName().endsWith("SXWU") || data.getName().endsWith("sxwu")) {
                    //如果默认的数据被删除，那就加载第一个工作空间
                    MyApplication.getInstance().getDefaultDataManager().setWorkspaceServer(data.getAbsolutePath());
                    hasMapData = true;
                    break;
                }
            }
            if (!hasMapData) {
                configMapData();
            }
        }
    }

    /**
     * 配置许可文件
     */
    private void configLic() {
        InputStream is = AssetsUtils.getInstance().open(LicName);
        if (is != null)
            FileUtils.getInstance().copy(is, LicPath + LicName);
    }

    /**
     * 配置地图数据
     */
    private void configMapData() {
        String[] datas = AssetsUtils.getInstance().opendDir(m_mapData);
        for (String data : datas) {
            InputStream is = AssetsUtils.getInstance().open(m_mapData + "/" + data);
            String zip = MapDataPath + "/" + data;
            boolean result = FileUtils.getInstance().copy(is, zip);
            if (result) {
//                ZipUtils.unZipFolder(zip, MapDataPath);
                //删除压缩包
                File zipFile = new File(zip);
                zipFile.delete();
            }
        }


    }
}
