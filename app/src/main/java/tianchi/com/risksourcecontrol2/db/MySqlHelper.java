package tianchi.com.risksourcecontrol2.db;

import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import tianchi.com.risksourcecontrol2.config.PropertiesManager;

/**
 * mysql工具类
 * Created by Kevin on 2017/12/27.
 */

public class MySqlHelper {
    private static boolean isInited = false;
    private static Connection s_connection;
    private static Context context;

    public MySqlHelper() {

    }

    public void init(Context context) throws Exception {
        this.context = context;
        //加载assets中properties文件
        Properties _properties = PropertiesManager.loadProperties(context, "jdbc.properties");
        // 1. 准备获取连接的 4 个字符串: user, password, url, jdbcDriver
        String user = _properties.getProperty("user");
        String password = _properties.getProperty("password");
        String url = _properties.getProperty("url");
        String driver = _properties.getProperty("Driver");
        //2. 加载驱动: Class.forName(driverClass)
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Class.forName(driver);
                    // 3.获取数据库连接
                    s_connection = DriverManager.getConnection(url, user, password);
                    isInited = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @return Connection mysql连接对象
     * @datetime 2017/12/27  13:53.
     */
    public static Connection getConnection() throws Exception {
        if (isInited) {
            return s_connection;
        }
        return null;
    }

    /*
    * 释放数据库
    * */
    public static void releaseDB(ResultSet resultSet, Statement statement,
                                 Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新
     *
     * @param sql  sql语句
     * @param args 字段值数组
     * @datetime 2017/12/29  9:20.
     */
    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = MySqlHelper.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySqlHelper.releaseDB(null, preparedStatement, connection);
        }
        return 0;
    }

    /**
     * 查询
     *
     * @param sql sql语句
     * @datetime 2017/12/29  9:20.
     */
    public static ResultSet query(String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = MySqlHelper.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySqlHelper.releaseDB(null, preparedStatement, connection);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param sql sql语句
     * @param id  表主键id
     * @datetime 2017/12/29  9:20.
     */
    public static ResultSet query(String sql, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySqlHelper.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySqlHelper.releaseDB(null, preparedStatement, connection);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param sql  sql语句
     * @param args 字段值数组
     * @datetime 2017/12/29  9:20.
     */
    public static ResultSet query(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = MySqlHelper.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            MySqlHelper.releaseDB(null, preparedStatement, connection);
        }
        return null;
    }

    /**
     * 插入
     *
     * @param sql  sql语句
     * @param args 字段值数组
     * @datetime 2017/12/29  9:20.
     */
    public static int insert(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = MySqlHelper.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            MySqlHelper.releaseDB(null, preparedStatement, connection);
        }
        return 0;
    }

}
