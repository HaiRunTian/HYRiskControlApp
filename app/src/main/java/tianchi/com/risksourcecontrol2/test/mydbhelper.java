package tianchi.com.risksourcecontrol2.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol2.db.DataBaseOpenHelper;
import tianchi.com.risksourcecontrol2.util.LogUtils;

/**
 * Created by Kevin on 2017/12/27.
 */

public class mydbhelper extends DataBaseOpenHelper {
    List<String> m_list = new ArrayList<String>();

    public mydbhelper(Context context, String dbName, int dbVersion, List<String> tableSqls) {
        super(context, dbName, dbVersion, tableSqls);
        m_list = tableSqls;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String _s : m_list) {
            db.execSQL(_s);
        }
        LogUtils.i("create db Successful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //    public void onCreate(SQLiteDatabase db) {
    //
    //    }
    //
    //    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    //    }
}
