package tianchi.com.risksourcecontrol2.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Kevin on 2018/3/9.
 */

public class FoldersConfig {
    public final static String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String PROJ_FOLDER = "RiskControlPic";
    public final static String USER_HEAD_PATH = SD_CARD + "/" + PROJ_FOLDER + "/" +"UserHead/";
    public final static String PRO_SAFETY_PIC_PATH = SD_CARD + "/" + PROJ_FOLDER + "/" + "ProductionSafety/";
    public final static String SAFETY_PATROL_PIC_PATH = SD_CARD + "/" + PROJ_FOLDER + "/" + "SafetyPatrol/";
    public final static String RISK_REFORM_PIC_PATH = SD_CARD + "/" + PROJ_FOLDER + "/" + "RiskReform/";
    public final static String PATROL_SIGNIN_PIC_PATH = SD_CARD + "/" + PROJ_FOLDER + "/" + "PatrolSignIn/";
    public static final String DEFAULT_TECDIS_FILE_PATH = SD_CARD+"/"+PROJ_FOLDER+"/"+"TechnologyFile/";  //技术交底路径
    public static final String SD_FILE = SD_CARD+"/"+PROJ_FOLDER+"/"+"UpFile/";  //包含通知上传文件的文件夹
    public static final String
            APK_FLODE = SD_CARD+"/"+PROJ_FOLDER+"/"+"APK/"; //app软件安装包路径

    public final static String NOTICEFY = SD_CARD + "/" +PROJ_FOLDER + "/" + "notice/";

    public static void initFolders() {
        String folders[] = new String[]{USER_HEAD_PATH,PRO_SAFETY_PIC_PATH,SAFETY_PATROL_PIC_PATH,PATROL_SIGNIN_PIC_PATH,
                RISK_REFORM_PIC_PATH,DEFAULT_TECDIS_FILE_PATH,SD_FILE,APK_FLODE,NOTICEFY};
        for (String _folder : folders) {
            File f = new File(_folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }
}
