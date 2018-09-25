package tianchi.com.risksourcecontrol.activitiy.user;

/**
 * Created by HaiRun on 2018-09-14 9:14.
 * 用户权限列表
 */

public class UserPermission {

    public final static int OWNER_ALL = 0; //业主获取全部名单
    public final static int SUPERVISON_FIRST = 1; //监理获取监理和施工方
    public final static int SUPERVISON_SECOND = 2; //监理获取业主
    public final static int CONSTRU_FIRST = 3; //施工方获取监理和施工方
    public final static int CONSTRU_SECOND = 4; //施工方获取施工方
    public final static int SUPERVISON_THREE = 5; //只能获取监理


}
