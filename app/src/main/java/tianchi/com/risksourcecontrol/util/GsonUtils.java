package tianchi.com.risksourcecontrol.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 * Created by Kevin on 2018/1/17.
 */

public class GsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
//            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        }
    }

    private GsonUtils() {
    }

    /**
     * 将object对象转成json字符串
     *
     * @param object bean对象
     * @return 返回的是json字符串
     */
    public static String objectToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 将GsonString转成泛型bean
     *
     * @param gsonString gson字符串
     * @param cls        bean类
     * @return bean
     */
    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 将GsonString转成beanlist
     *
     * @param gsonString gson字符串
     * @return bean列表
     */
    public static <T> List<T> jsonToList(String gsonString) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }


    /**
     * GsonString转成list中有map的
     *
     * @param gsonString gson字符串
     * @return maplist
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    /**
     * gsonString转成map的
     *
     * @param gsonString gson字符串
     * @return map
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }


    /**
     * 按章节点得到相应的内容
     *
     * @param jsonString json字符串
     * @param node       节点
     * @return 整型节点对应的内容
     */
    public static int getIntNoteJsonString(String jsonString, String node) {
        if (TextUtils.isEmpty(jsonString)) {
            throw new RuntimeException("json字符串");
        }
        if (TextUtils.isEmpty(node)) {
            throw new RuntimeException("note标签不能为空");
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return element.getAsJsonObject().get(node).getAsInt();
    }

    /**
     * 按章节点得到相应的内容
     *
     * @param jsonString json字符串
     * @param node       节点
     * @return string节点对应的内容
     */
    public static String getStringNodeJsonString(String jsonString, String node) {
        if (TextUtils.isEmpty(jsonString)) {
            throw new RuntimeException("json字符串");
        }
        if (TextUtils.isEmpty(node)) {
            throw new RuntimeException("note标签不能为空");
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return element.getAsJsonObject().get(node).toString();
    }

    /**
     * 按章节点得到相应的内容
     *
     * @param jsonString json字符串
     * @param node       节点
     * @return string节点对应的内容
     */
    public static String getNodeJsonString(String jsonString, String node) {
        if (TextUtils.isEmpty(jsonString)) {
            throw new RuntimeException("json字符串");
        }
        if (TextUtils.isEmpty(node)) {
            throw new RuntimeException("note标签不能为空");
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if (element.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return element.getAsJsonObject().get(node).toString();
    }

    /**
     * 按照节点得到节点内容，然后传化为相对应的bean数组
     *
     * @param jsonString 原json字符串
     * @param node       节点标签
     * @param beanClazz  要转化成的bean class
     * @return 返回bean的数组
     */
    public static <T> List<T> jsonToArrayBeans(String jsonString, String node, Class<T> beanClazz) {
        String noteJsonString = getStringNodeJsonString(jsonString, node);
        return parserJsonToArrayBeans(noteJsonString, beanClazz);
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param beanClazz  集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public static <T> List<T> parserJsonToArrayBeans(String jsonString, Class<T> beanClazz) {
        if (TextUtils.isEmpty(jsonString)) {
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        if (!jsonElement.isJsonArray()) {
            throw new RuntimeException("json字符不是一个数组对象集合");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> beans = new ArrayList<T>();
        for (JsonElement jsonElement2 : jsonArray) {
            JsonObject _js = jsonElement2.getAsJsonObject();


            /*JSONObject _jsonObject = jsonElement2.getAsJsonObject();
            LogUtils.i("jsonElement2",jsonElement2.toString());
            parseJson2Bean(jsonElement2,beanClazz);
            //T bean = new Gson().fromJson(jsonElement2, beanClazz);*/
            T bean = null;
            try {
                bean = parseJson2Bean2(_js, beanClazz);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.i("jsonObject error", e.toString());
                LogUtils.i("jsonObject error", _js.toString());
            }
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 把相对应节点的内容封装为对象
     *
     * @param jsonString json字符串
     * @param clazzBean  要封装成的目标对象
     * @return 目标对象
     */
    public static <T> T parserJsonToArrayBean(String jsonString, Class<T> clazzBean) {
        if (TextUtils.isEmpty(jsonString)) {
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("json字符串为空");
        }
        if (!jsonElement.isJsonObject()) {
            throw new RuntimeException("json不是一个对象");
        }
        return new Gson().fromJson(jsonElement, clazzBean);
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param node       json节点
     * @param clazzBean  集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public static <T> T jsonToArrayBean(String jsonString, String node, Class<T> clazzBean) {
        String noteJsonString = (String) getStringNodeJsonString(jsonString, node);
        return parserJsonToArrayBean(noteJsonString, clazzBean);
    }

    /**
     * @param object JSONObject
     * @param clazz  bean类
     * @return bean类对象   包括时间解析
     * @datetime 2018/1/20  10:52.
     */
    public static <T> T parseJson2Bean2(JsonObject object, Class<T> clazz) throws Exception {
      /*  String json = "{id:1001,name:'张三',age:22,sex:'男'}";
        object = new JSONObject(json);*/
        T newInstance = null;
        Field[] fields = clazz.getDeclaredFields();
        //只要保证clazz有一个无参的public构造方法就不可能发生异常
        try {
            newInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i("field count:" + fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            // LogUtils.i("field "+i +" name:"+ name);
            //不可能发生的异常
            try {
                String _type = fields[i].getGenericType().toString();
                // class java.lang
                // String jsonName = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                // LogUtils.i("type:" + _type + ", Name " + name);
                if (!object.has(name)) {//jsonobject 没有包含这个属性名，则跳过
                    LogUtils.i("gson", "jsonstring lost the field: " + name);
                    continue;
                } else {
//                    LogUtils.i("gson", "type:" + _type + ", Name " + name + ", value: " + object.get(name).getAsString());
                    if (_type.equals("class java.util.Date")) {
                        String time = object.get(name).getAsString();
                        fields[i].set(newInstance, DateTimeUtils.stringToDate(object.get(name).getAsString(), "yyyy-MM-dd"));
                        LogUtils.i("json对应键值对时间 = ", name + "-----------" + DateTimeUtils.stringToDate(object.get(name).getAsString(), "yyyy-MM-dd"));
                    } else {
                        LogUtils.i("json对应键值对 = ", name + "-----------" + object.get(name));
//                        fields[i].set(newInstance, object.getAsJsonPrimitive(name));
//                        fields[i].set(newInstance, object.get(name).getAsString());
                        switch (_type) {
                            case "class java.lang.String": {
                                fields[i].set(newInstance, object.get(name).getAsString());
                            }
                            break;
                            case "int":
                            case "class java.lang.Integer": {

                                LogUtils.i("int类型", name + "==" + String.valueOf(object.get(name).getAsInt()));
                                fields[i].set(newInstance, object.get(name).getAsInt());
                                LogUtils.i("int类型", name + "=====" + String.valueOf(object.get(name).getAsBigInteger()));
                            }
                            break;
                            case "class java.lang.Double": {
                                fields[i].set(newInstance, object.get(name).getAsDouble());
                            }
                            break;
                            case "class java.lang.Boolean": {
                                fields[i].set(newInstance, object.get(name).getAsBoolean());
                            }
                            break;
                            case "class java.lang.Float": {
                                fields[i].set(newInstance, object.get(name).getAsFloat());
                            }
                            break;
                            case "java.util.List<tianchi.com.risksourcecontrol.bean.newnotice.ReplySupervisorInfo>": {
                                fields[i].set(newInstance, object.get(name));
//                                fields[i].set(newInstance, object.get(name).getAsJsonObject());
                            }
                            break;
                            default:
                                break;
                        }
                        LogUtils.i("json对应键值对 = ", name + "-----------" + object.getAsJsonPrimitive(name));
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogUtils.i("typeError:" + e.getMessage().toString());
            }
        }

        return newInstance;
    }

    /**
     * @param object JSONObject
     * @param clazz  bean类
     * @return bean类对象   包括时间解析
     * @datetime 2018/1/20  10:52.
     */

    public static <T> T parseJson2Bean(JSONObject object, Class<T> clazz) throws Exception {
      /*  String json = "{id:1001,name:'张三',age:22,sex:'男'}";
        object = new JSONObject(json);*/
        T newInstance = null;
        Field[] fields = clazz.getDeclaredFields();
        //只要保证clazz有一个无参的public构造方法就不可能发生异常
        try {
            newInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i("field count:" + fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            // Logs.i("field "+i +" name:"+ name);
            //不可能发生的异常
            try {
                String _type = fields[i].getGenericType().toString();
                // class java.lang
                // String jsonName = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                LogUtils.i("type:" + _type + ", Name " + name);
                if (!object.has(name)) {//jsonobject 没有包含这个属性名，则跳过
                    continue;
                } else {
                    if (_type.equals("class java.util.Date")) {
                        fields[i].set(newInstance, DateTimeUtils.stringToDate((String) object.get(name), "yyyy-MM-dd"));
                    } else {
//                        fields[i].set(newInstance, object.get(name));
                        switch (_type) {
                            case "class java.lang.String": {
                                fields[i].set(newInstance, object.getString(name));
                            }
                            break;
                            case "int":
                            case "class java.lang.Integer": {
                                fields[i].set(newInstance, object.getInt(name));
                            }
                            break;
                            case "class java.lang.Double": {
                                fields[i].set(newInstance, object.getDouble(name));
                            }
                            break;
                            case "class java.lang.Boolean": {
                                fields[i].set(newInstance, object.getBoolean(name));
                            }
                            break;

//                    case "class java.util.Date":{
//                        fields[i].set(newInstance, (Date)object.get(name));
//                    }break;
                            default:
                                break;
                        }
//                }
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogUtils.i("typeError: parseJson2Bean" + e.getMessage().toString());
            }
        }

        return newInstance;
    }


}
