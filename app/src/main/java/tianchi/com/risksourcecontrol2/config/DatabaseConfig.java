package tianchi.com.risksourcecontrol2.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库配置文件
 * Created by Kevin on 2017/12/25.
 */

public class DatabaseConfig {
    private static List<String> tableSqlsList;//创建表sql列表
    //    public static int s_currentUserId;//当前用户id(唯一标识)
    public static final String DB_NAME = "RiskCtrl.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_USER = "User";
    public static final String TABLE_SAFETYLOG = "SafetyLogInfo";
    public static final String TABLE_REFORMLOG = "ReformLogInfo";
    public static final String TABLE_PATROLLOG = "PatrolLogInfo";
    public static final String TABLE_NOTIFICATION = "Notification";
    public static List<String> getTableSqlsList() {
        if (tableSqlsList == null) {
            tableSqlsList = new ArrayList<String>();
        }
        tableSqlsList.add(sqlCreateTable_User);
        tableSqlsList.add(sqlCreateTable_SafetyLog);
        tableSqlsList.add(sqlCreateTable_ReformLog);
        tableSqlsList.add(sqlCreateTable_PatrolLog);
        tableSqlsList.add(sqlCreateTable_Notification);
        return tableSqlsList;
    }

    //创建用户表语句
    private static final String sqlCreateTable_User = "create table User(id integer primary key autoincrement," +
            "userName text," +
            "passWord text," +
            "realName text," +
            "phoneNum text," +
            "headPic blob," +
            "role text," +
            "department text," +
            "chargeMan text)";

    //创建安全日志表语句
    private static final String sqlCreateTable_SafetyLog = "create table SafetyLogInfo(id integer primary key autoincrement," +
            "logID text," +
            "stakeNum text," +
            "saveTime DATETIME," +
            "account text," +
            "section text," +
            "emergency text," +
            "leader text," +
            "riskType text," +
            "weather text," +
            "details text," +
            "picture blob)";

    //创建风险源整改日志表语句
    private static final String sqlCreateTable_ReformLog = "create table ReformLogInfo(id integer primary key autoincrement," +
            "logID text," +
            "stakeNum text," +
            "saveTime DATETIME," +
            "account text," +
            "riskType text," +
            "details text," +
            "title text," +
            "picture blob)";

    //创建安全巡查日志表语句
    private static final String sqlCreateTable_PatrolLog = "create table PatrolLogInfo(id integer primary key autoincrement," +
            "logID text," +
            "stakeNum text," +
            "saveTime DATETIME," +
            "account text," +
            "riskType text," +
            "weather text," +
            "section text," +
            "emergency text," +
            "leader text," +
            "mDetails text," +
            "tDetails text," +
            "picture blob)";

    //创建通知表语句
    private static final String sqlCreateTable_Notification = "create table Notification(id integer primary key autoincrement," +
            "notificationID text," +
            "sendAccount text," +
            "recvAccount text," +
            "pushTime DATETIME," +
            "title text," +
            "type text," +
            "content text)";
}
