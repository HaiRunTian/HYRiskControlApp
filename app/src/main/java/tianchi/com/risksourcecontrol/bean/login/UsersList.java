package tianchi.com.risksourcecontrol.bean.login;

import java.util.ArrayList;

/**
 * 用户列表（用户名单）
 * Created by Kevin on 2018/3/22.
 */

public class UsersList {

    public static ArrayList<String> s_userNamesList;//全局存放选取了的用户列表

    public static ArrayList<String> getList() {
        if (s_userNamesList == null)
            s_userNamesList = new ArrayList<>();
        return s_userNamesList;
    }

    public static void addUserToList(String userName) {
        if (s_userNamesList == null) {
            s_userNamesList = new ArrayList<String>();
        }
        s_userNamesList.add(userName);
    }

    public static void removeUserFromList(String userName) {
        if (s_userNamesList == null) {
            s_userNamesList = new ArrayList<String>();
        }
        s_userNamesList.remove(userName);
    }

    public static void clearList() {
        if (s_userNamesList != null) {
            s_userNamesList.clear();
        }
    }
}
