package tianchi.com.risksourcecontrol2.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 2018/2/1.
 */

public class BeanUtils<T> {
    /**
     * 根据List<Map<String, Object>>数据转换为JavaBean数据
     * @param datas
     * @param beanClass
     * @return
     * @throws CommonException
     */
    public List<T> ListMap2JavaBean(List<Map<String, Object>> datas, Class<T> beanClass) throws CommonException  {
        // 返回数据集合
        List<T> list = null;
        // 对象字段名称
        String fieldname = "";
        // 对象方法名称
        String methodname = "";
        // 对象方法需要赋的值
        Object methodsetvalue = "";
        try {
            list = new ArrayList<T>();
            // 得到对象所有字段
            Field fields[] = beanClass.getDeclaredFields();
            // 遍历数据
            for (Map<String, Object> mapdata : datas) {
                // 创建一个泛型类型实例
                T t = beanClass.newInstance();
                // 遍历所有字段，对应配置好的字段并赋值
                for (Field field : fields) {
                    if(null != field) {
                        // 全部转化为大写
//                        String dbfieldname = field.getName().toUpperCase();
                        // 获取字段名称
                        fieldname = field.getName();
                        // 拼接set方法
                        methodname = "set" + StrUtil.capitalize(fieldname);
                        // 获取data里的对应值
                        methodsetvalue = mapdata.get(fieldname);
                        // 赋值给字段
                        Method m = beanClass.getDeclaredMethod(methodname, field.getType());
                        m.invoke(t, methodsetvalue);
                    }
                }
                // 存入返回列表
                list.add(t);
            }
        } catch (InstantiationException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (IllegalAccessException e) {
            throw new CommonException(e, "创建beanClass实例异常");
        } catch (SecurityException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (NoSuchMethodException e) {
            throw new CommonException(e, "获取[" + fieldname + "] getter setter 方法异常");
        } catch (IllegalArgumentException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        } catch (InvocationTargetException e) {
            throw new CommonException(e, "[" + methodname + "] 方法赋值异常");
        }
        // 返回
        return list;
    }
}

/**
 * 公共异常类
 * 备注：与原异常没有区别，只是多了一个errormsg字段，保存开发人员提供的异常提示信息
 * @author suny
 * @date 2017-7-4
 * <pre>
 *  desc:
 * </pre>
 */
 class CommonException extends Exception {

    // 原始异常
    private Throwable target;

    // 开发提供异常提示内容
    private String errormsg = "";

    public Throwable getTargetException() {
        return target;
    }

    public Throwable getCause() {
        return target;
    }

    protected CommonException() {
        super((Throwable) null);
    }

    public CommonException(Throwable target, String s) {
        super(s, null);
        this.target = target;
        this.errormsg = s;
    }

    public CommonException(Throwable target) {
        super((Throwable) null);
        this.target = target;
    }

    public String getErrormsg() {
        return errormsg;
    }

}

/**
 * 字符串工具类
 * @author suny
 * @date 2017-7-4
 * <pre>
 *  desc:创建
 * </pre>
 */
class StrUtil {

    /**
     * 给第字符串第一个字母大写
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }

    /**
     * 使用StringBuilder拼接字符串
     * @param objects
     * @return
     */
    public static String appendSbl(Object... objects) {
        StringBuilder sbl = new StringBuilder();
        for (Object obj : objects) {
            sbl.append(obj);
        }
        return sbl.toString();
    }

    /**
     * 使用StringBuffer拼接字符串
     * @param objects
     * @return
     */
    public static String appendSbf(Object... objects) {
        StringBuffer sbl = new StringBuffer();
        for (Object obj : objects) {
            sbl.append(obj);
        }
        return sbl.toString();
    }

    /**
     * 根据字符串，获取后缀
     * @param str
     *      若获取不到，返回 null
     */
    public static String getSuffix(String str) {
        if(null != str && str.lastIndexOf(".") > 0) {
            str = str.substring(str.lastIndexOf("."), str.length());
        } else {
            str = null;
        }
        return str;
    }

}
