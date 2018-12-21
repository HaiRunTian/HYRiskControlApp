package tianchi.com.risksourcecontrol2.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import tianchi.com.risksourcecontrol2.bean.UserBean;

/**
 * Created by idea on 2016/10/8.
 */
public class JsonUtil {

    public static List<UserBean> parseUser(String json) {
        List<UserBean> data = null;
        try {
            data = new Gson().fromJson(json,new TypeToken<List<UserBean>>(){}.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return data;
    }

}
