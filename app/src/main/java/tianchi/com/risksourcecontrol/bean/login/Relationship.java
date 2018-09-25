package tianchi.com.risksourcecontrol.bean.login;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用于json转换的关系列表bean
 * Created by Kevin on 2018/3/23.
 */

public class Relationship{

    public HashMap<String,ArrayList<String>> list;

    public HashMap<String, ArrayList<String>> getList() {
        return list;
    }

    public void setList(HashMap<String, ArrayList<String>> list) {
        this.list = list;
    }
}
