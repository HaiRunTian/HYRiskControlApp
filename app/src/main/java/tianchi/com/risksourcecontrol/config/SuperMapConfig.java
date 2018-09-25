package tianchi.com.risksourcecontrol.config;

/**
 * 超图配置
 * Created by Kevin on 2017/12/26.
 */

public class SuperMapConfig {
    public static final String ASSETS_DATA_PATH = "MapData";//assets数据文件夹
    public static String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath(); //sd卡根路径
    public static final String MAP_DATA_PATH = SDCARD + "/SuperMap/Demos/HighLowTypeRiskData/HighLowTypRisk/";
    public static final String LIC_PATH = SDCARD + "/SuperMap/license/";//许可文件目录
    public static final String TEMP_PATH = SDCARD + "/SuperMap/temp/";//临时文件目录
    public static final String WEB_CACHE_PATH = SDCARD + "/SuperMap/WebCache/";//网络缓存文件目录
    public static final String LIC_NAME ="SuperMapiMobileTrial.slm";//许可文件名
    public static final String FULL_LIC_PATH = LIC_PATH + LIC_NAME;//许可文件名
    public static final String DEFAULT_WORKSPACE_PATH = MAP_DATA_PATH + "Changchun.smwu";//默认工作空间路径
    public static final String DEFAULT_WORKSPACE_NAME = "Changchun.smwu";//默认工作空间文件名

}
