package tianchi.com.risksourcecontrol2.util;

import org.json.JSONArray;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;


/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class MyJsonUtills {

    /*
      example:
      public class Student {
        public int id ;
        public String name;
        public Integer age;

         public String toString(){
            return this.id + ", " + this.name + ", " + this.age;
        }
      }

     String json = "{id:1001,name:'张三',age:22,sex:'男'}";
     JSONObject obj = new JSONObject(json);
     Student stu =  parseJson2Bean(obj, Student.class);
     Logs.i("student:" + stu.toString());
     */


    //用变量名来反射
    /**
     * 将json格式的字符串抽取为对象
     * @param jsonString ：要转为bean的字符串(json格式)
     * @param clazz bean的对象类型
     * @param <T>
     * @return 转换成功的集合
     * @throws Exception
     */
    public static <T> T parseJson2Bean(String jsonString, Class<T> clazz) throws Exception {
        JSONObject object = new JSONObject(jsonString);
        T newInstance = null;
        Field[] fields = clazz.getDeclaredFields();
        //只要保证clazz有一个无参的public构造方法就不可能发生异常
        try {
            newInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtils.i("field count:"+fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            // Logs.i("field "+i +" name:"+ name);
            //不可能发生的异常
            try {
                String _type = fields[i].getGenericType().toString();
                // class java.lang
                // String jsonName = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                //Logs.i("type:" + _type+", Name "+name);
                if (!object.has(name)) continue;//jsonobject 没有包含这个属性名，则跳过
                switch (_type) {
                    case "class java.lang.String":{
                        fields[i].set(newInstance, object.getString(name));
                    }break;
                    case "int":
                    case "class java.lang.Integer":{
                        fields[i].set(newInstance, object.getInt(name));
                    }break;
                    case "class java.lang.Double":{
                        fields[i].set(newInstance, object.getDouble(name));
                    }break;
                    case "class java.lang.Boolean":{
                        fields[i].set(newInstance, object.getBoolean(name));
                    }break;
                    case "class java.util.Date":{
                        //fields[i].set(newInstance, object.get(jsonName));
                    }break;
                    default:break;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return newInstance;
    }

    //用变量名来反射
    /**
     * 将JSONObject对象串抽取为对象
     * @param object ：要转为bean的JSONObject对象
     * @param clazz bean的对象类型
     * @param <T>
     * @return 转换成功的集合
     * @throws Exception
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
//        LogUtils.i("field count:"+fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
           // Logs.i("field "+i +" name:"+ name);
            //不可能发生的异常
            try {
                String _type = fields[i].getGenericType().toString();
                // class java.lang
                // String jsonName = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//                LogUtils.i("type:" + _type+", Name "+name);
                if (!object.has(name)) continue;//jsonobject 没有包含这个属性名，则跳过
                switch (_type) {
                    case "class java.lang.String":{
                        fields[i].set(newInstance, object.getString(name));
                    }break;
                    case "int":
                    case "class java.lang.Integer":{
                        fields[i].set(newInstance, object.getInt(name));
                    }break;
                    case "class java.lang.Double":{
                        fields[i].set(newInstance, object.getDouble(name));
                    }break;
                    case "class java.lang.Boolean":{
                        fields[i].set(newInstance, object.getBoolean(name));
                    }break;
                    case "class java.util.Date":{
                        //fields[i].set(newInstance, object.get(jsonName));
                    }break;
                    default:break;

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return newInstance;
    }

    /**
     * 将json格式的字符串抽取为对象集合
     * @param jsonString ：要转为bean的字符串(json格式)
     * @param param： 存bean对象string的key值
     * @param clazz   bean的对象类型
     * @param <T>
     * @return 转换成功的集合
     */
    public static <T> boolean parseJson2Beans(String jsonString, String param, Class<T> clazz,  List<T> results) {
        try {
            //List<T> _results = new List<T>();
            JSONObject _object = new JSONObject(jsonString);
            JSONArray _objs = _object.getJSONArray(param);

            for(int i=0;i<_objs.length(); i++) {
                JSONObject _obj = _objs.getJSONObject(i);
                T _result = parseJson2Bean(_obj,clazz);
                if(_result == null) continue;
                results.add(_result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !results.isEmpty();
    }

    /**
     * 将json格式的字符串抽取为对象集合
     * @param jsonString ：要转为bean的字符串(json格式)
     * @param param： 存bean对象string的key值
     * @param clazz   bean的对象类型
     * @param <T>
     * @return 转换成功的集合
     */
    public static <T> List<T> parseJson2Beans(String jsonString, String param, Class<T> clazz) {
        List<T> _results = new ArrayList<T>();
        try {
            //List<T> _results = new List<T>();
            JSONObject _object = new JSONObject(jsonString);
            JSONArray _objs = _object.getJSONArray(param);

            for(int i=0;i<_objs.length(); i++) {
                JSONObject _obj = _objs.getJSONObject(i);
                T _result = parseJson2Bean(_obj,clazz);
                if(_result == null) continue;
                _results.add(_result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _results;
    }

    //用函数来反射..待实现
    public static <T> T parseJson2Bean2(JSONObject object, Class<T> clazz) throws Exception {
       /*FileInputStream fis = new FileInputStream("/mnt/sdcard/json.txt");
        int len = fis.available();
        byte[] buffer = new byte[len];
        fis.read(buffer);*/
        String json = "{id:1001,name:'张三',age:22,sex:'男'}";
        object = new JSONObject(json);
        T newInstance = null;
        Field[] fields = clazz.getDeclaredFields();
        //只要保证clazz有一个无参的public构造方法就不可能发生异常
        try {
            newInstance = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtils.i("field count:"+fields.length);
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
//            LogUtils.i("field "+i +" name:"+ name);
            //不可能发生的异常

            try {
                String _type = fields[i].getGenericType().toString();
                // class java.lang
                // String jsonName = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//                LogUtils.i("type:" + _type+", Name "+name);
                if (!object.has(name)) continue;//jsonobject 没有包含这个属性名，则跳过
                switch (_type) {
                    case "class java.lang.String":{
                        fields[i].set(newInstance, object.getString(name));
                    }break;
                    case "int":
                    case "class java.lang.Integer":{
                        fields[i].set(newInstance, object.getInt(name));
                    }break;
                    case "class java.lang.Double":{
                        fields[i].set(newInstance, object.getDouble(name));
                    }break;
                    case "class java.lang.Boolean":{
                        fields[i].set(newInstance, object.getBoolean(name));
                    }break;
                    case "class java.util.Date":{
                        //fields[i].set(newInstance, object.get(jsonName));
                    }break;
                    default:break;

                }
                // Logs.i("name", name + "****");
             /*   if (object.has(name)) {
                    if (name.equals("ResultCode")) {
                        fields[i].set(newInstance, object.getInt(name));
                    } else {
                        fields[i].set(newInstance, object.getString(name));
                    }
                }*/
                //Log.i("name", jsonName+":"+object.has(jsonName)+object.getString(jsonName));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return newInstance;
    }
}
