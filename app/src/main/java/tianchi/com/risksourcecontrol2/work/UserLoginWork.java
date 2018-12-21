package tianchi.com.risksourcecontrol2.work;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.bean.UserBean;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;

/**
 * 用户登录的一些后台工作
 * Created by Kevin on 2018/1/2.
 */

public class UserLoginWork {
    /**
     * @param jsonString json字符串
     * @return 用户实体对象
     * @datetime 2018/1/20  10:57.
     */
    public static UserInfo jsonToUserInfoBean(String jsonString) {
        UserInfo _info = null;
        try {
            JSONObject _jsonObject = new JSONObject(jsonString);
            _info = GsonUtils.parseJson2Bean(_jsonObject, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _info;
    }

    //抓取所有用户列表
    public static List<String> queryAccountListFromDB() {
        /*
        *数据库耗时
        * */
        List<String> accountList = new ArrayList<String>();
        accountList.add("1111");
        accountList.add("2222");
        accountList.add("3333");
        accountList.add("4444");
        accountList.add("5555");
        accountList.add("6666");
        return accountList;
    }

    public static String mapToJsonString(Map<String, Object> objectMap) {
        JSONObject _jsonObject = new JSONObject();
        if (objectMap.size() > 0) {
            try {
                _jsonObject.put("userId", objectMap.get("userId"));
                _jsonObject.put("loginName", objectMap.get("loginName"));
                _jsonObject.put("password", objectMap.get("password"));
                _jsonObject.put("realName", objectMap.get("realName"));
                _jsonObject.put("projectRole", objectMap.get("projectRole"));
                _jsonObject.put("moblie", objectMap.get("moblie"));
                _jsonObject.put("email", objectMap.get("email"));
                _jsonObject.put("oicq", objectMap.get("oicq"));
                _jsonObject.put("address", objectMap.get("address"));
                _jsonObject.put("sectionList", objectMap.get("sectionList"));
                _jsonObject.put("picture", objectMap.get("picture"));
                _jsonObject.put("birthday", objectMap.get("birthday"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return _jsonObject.toString();
    }
    public static void distributeUserList(List<UserBean> userBean) {
        HashMap<String, List<String >> _hashMap = new HashMap<>();
        HashMap<String, List<String >> _hashMap1 = new HashMap<>();

        for (int i = 0; i < userBean.size(); i++) {
            //业主
            List<String> _j0 = userBean.get(i).getJ0();
            UserSingleton.setOwnerList(_j0);
            //监理
            _hashMap.put("J1",userBean.get(i).getJ1());
            _hashMap.put("J2",userBean.get(i).getJ2());
            _hashMap.put("J3",userBean.get(i).getJ3());
            UserSingleton.setSupervisorList(_hashMap);

            //施工方
            _hashMap1.put("TJ01",userBean.get(i).getTJ01());
            _hashMap1.put("TJ02",userBean.get(i).getTJ02());
            _hashMap1.put("TJ03",userBean.get(i).getTJ03());
            _hashMap1.put("TJ04",userBean.get(i).getTJ04());
            _hashMap1.put("TJ05",userBean.get(i).getTJ05());
            _hashMap1.put("TJ06",userBean.get(i).getTJ06());
            _hashMap1.put("TJ07",userBean.get(i).getTJ07());
            _hashMap1.put("TJ08",userBean.get(i).getTJ08());
            _hashMap1.put("TJ09",userBean.get(i).getTJ09());
            _hashMap1.put("TJ10",userBean.get(i).getTJ10());
            _hashMap1.put("TJ11",userBean.get(i).getTJ11());
            UserSingleton.setConstructionList(_hashMap1);



        }
    }
    //将取到的hashmap分配到对应的容器（业主，监理，施工方）
    public static void distributeRelationshipList(Map<String, List<String>> map) {


        for (String key : map.keySet()) {
            if (key.equals("J0")) {//业主

                List<String> _list = new ArrayList<>();
                if (map.get(key) != null) {
                    try {
                        _list = map.get(key);
                    }catch (Exception e){
                        LogUtils.i(e.toString());
                    }
                }
                if (_list.size() != 0) {
                    UserSingleton.setOwnerList(_list);
                }
            } else if (key.equals("J1") || key.equals("J2") || key.equals("J3") || key.equals("J4")) {//监理方
                if (map.get(key) != null){
                    try {
                        UserSingleton.getSupervisorList().put(key, map.get(key));
                    }catch (Exception e){
                        LogUtils.i(e.toString());
                    }

                }

            } else {//施工方
                if (map.get(key) != null){
                    try{
                        UserSingleton.getConstructionList().put(key, map.get(key));
                    }catch (Exception e){
                        LogUtils.i(e.toString());
                    }
                }

            }
        }
    }

    /*获取当前用户是哪个类型的角色*/
    public static String getSubmitter(int rodeId) {
        String submitter = "";
        switch (rodeId) {
            case 17:
                submitter = "业主方";
                break;
            case 19:
                submitter = "监理方";
                break;
            case 20:
                submitter = "施工方";
                break;
            default:break;
        }
        return submitter;
    }

    /*获取关系列表中所有成员名单*/
    public static String resolveRelationshipList() {
        String allUserList = "";
        if (UserSingleton.getOwnerList().size() > 0) {
            for (String user : UserSingleton.getOwnerList()) {
                allUserList += user + "#";
            }
            allUserList = allUserList.substring(0, allUserList.length() - 1);
        }
        if (UserSingleton.getSupervisorList().size() > 0) {
            for (String section : UserSingleton.getSupervisorList().keySet()) {
                for (String user : UserSingleton.getSupervisorList().get(section)) {
                    allUserList += user + "#";
                }
            }
            allUserList = allUserList.substring(0, allUserList.length() - 1);
        }
        if (UserSingleton.getConstructionList().size() > 0) {
            for (String section : UserSingleton.getConstructionList().keySet()) {
                for (String user : UserSingleton.getConstructionList().get(section)) {
                    allUserList += user + "#";
                }
            }
        }
        allUserList = allUserList.substring(0, allUserList.length() - 1);
        return allUserList;
    }


}
