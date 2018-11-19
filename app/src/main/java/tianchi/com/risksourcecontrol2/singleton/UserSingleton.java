package tianchi.com.risksourcecontrol2.singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;

/**
 * Created by Kevin on 2018/1/2.
 * 用户单例
 */

public class UserSingleton {

    private static boolean isInited;
    private static UserInfo m_user;
    private static List<String> s_sectionList;
    private static List<String> tempList;
    private static UserSingleton m_Instance;
    private static List<String> s_ownerList;//业主方名单
    private static Map<String, List<String>> s_supervisorList;//监理方名单
    private static Map<String, List<String>> s_constructionList;//施工方名单

    private UserSingleton() {

    }

    public static void getInstance() {
        if (!isInited) {
            m_Instance = new UserSingleton();
            m_user = new UserInfo();
            isInited = true;
        } else {
            throw new ExceptionInInitializerError("用户单例尚未初始化");
        }
    }

    public static UserInfo getUserInfo() {
        return m_user;
    }

    public static int getUserRoid(){
        return m_user.getRoleId();
    }

    public static void setUserInfo(UserInfo userInfo) {
        m_user = userInfo;
    }

    /**
     * 日志Spinner获取标段适配
     */
    public static List<String> getSectionList() {
        if (s_sectionList == null) {
            s_sectionList = new ArrayList<String>();
            s_sectionList = Arrays.asList(m_user.getSectionList().split("#"));
        }
        tempList = new ArrayList(s_sectionList);
        if (tempList.contains("0")) {
            tempList.remove("0");
        }
        List<String> section = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            section.add(ServerConfig.getMapSection().get(tempList.get(i)));
        }
        section.add("");
        return section;
    }

    /**
     * 设置业主列表
     * @param list
     */
    public static void setOwnerList(List<String> list) {
        s_ownerList = list;
    }

    /**
     * 获取业主列表
     * @return
     */
    public static List<String> getOwnerList() {
        if (s_ownerList == null) {
            s_ownerList = new ArrayList<>();
        }
        return s_ownerList;
    }

    /**
     * 获取监理列表
     * @return
     */
    public static Map<String, List<String>> getSupervisorList() {
        if (s_supervisorList == null)
            s_supervisorList = new HashMap<>();
        return s_supervisorList;
    }

    /**
     * 设置监理列表
     * @param supervisorList
     */
    public static void setSupervisorList(Map<String, List<String>> supervisorList) {
        s_supervisorList = supervisorList;
    }

    /**
     * 获取施工方列表
     * @return
     */
    public static Map<String, List<String>> getConstructionList() {
        if (s_constructionList == null)
            s_constructionList = new HashMap<>();
        return s_constructionList;
    }

    /**
     * 设置施工方列表
     * @param constructionList
     */
    public static void setConstructionList(Map<String, List<String>> constructionList) {
        s_constructionList = constructionList;
    }
}
