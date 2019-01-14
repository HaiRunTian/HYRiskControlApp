package tianchi.com.risksourcecontrol2.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import static android.R.attr.value;

/*
* 本地数据持久化工具类
* */
public class SharedPreferencesUtil {
	/**
	 * 是否被初始化
	 */
	private static boolean sInited = false;
	
	private static SharedPreferencesUtil sInstace = null;
	
	/**
	 * 系统内置的SharedPreferences
	 */
	private SharedPreferences mSharedPreferences = null;
	
	private boolean isXMLOpened = false;
	
	private static WeakReference<Context> sContext = null;
	
	private SharedPreferencesUtil()
	{
	}
	
	public static void init(Context context)
	{
		if(!sInited){
			sContext = new WeakReference<Context>(context);
			sInstace = new SharedPreferencesUtil();
			sInited = true;
		}
	}


	public static SharedPreferencesUtil getInstance()
	{
		if(!sInited){
			throw new IllegalArgumentException("Please call init() firstly");
		}
		
		return sInstace;
	}

	
	/**
	 * 打开指定存储的文件,不存在则新建一个
	 * @param xml
	 */
	public void open(String xml){
		mSharedPreferences = sContext.get().getSharedPreferences(xml, Context.MODE_PRIVATE);
		isXMLOpened = true;
    }
	
	public void put(String key, int value){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().putInt(key, value);
			mSharedPreferences.edit().commit();
		}
	}

	public void put(String key, Boolean value){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().putBoolean(key, value);
			mSharedPreferences.edit().commit();
		}
	}
	public void put(String key, String value){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().putString(key, value);
			mSharedPreferences.edit().commit();
		}
	}
	public void put(String key, float value){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().putFloat(key, value);
			mSharedPreferences.edit().commit();
		}
	}
	public void put(String key, long value){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().putLong(key, value);
			mSharedPreferences.edit().commit();
		}
	}
	public boolean getBoolean(String key){
		if(isXMLOpened)
		{
			return mSharedPreferences.getBoolean(key, false);
		}
		return false;
	}
	public int getInt(String key){
		if(isXMLOpened)
		{
			return mSharedPreferences.getInt(key, 0);
		}
		return 0;
	}
	public float getFloat(String key){
		if(isXMLOpened)
		{
			return mSharedPreferences.getFloat(key, 0);
		}
		return 0;
	}
	public long getLong(String key){
		if(isXMLOpened)
		{
			return mSharedPreferences.getLong(key, 0);
		}
		return 0;
	}
	public String getString(String key){
		if(isXMLOpened)
		{
			return mSharedPreferences.getString(key, "");
		}
		return "";
	}

	public void remove(String key) {
		if (isXMLOpened) {
			mSharedPreferences.edit().remove(key);
			mSharedPreferences.edit().commit();
		}
	}
	public void clear(){
		if(isXMLOpened)
		{
			mSharedPreferences.edit().clear();
			mSharedPreferences.edit().commit();
		}
	}
}
