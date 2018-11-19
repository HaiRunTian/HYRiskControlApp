package tianchi.com.risksourcecontrol2.config;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Kevin on 2017/12/27.
 */

public class PropertiesManager {

    public static Properties loadProperties(Context context,String prtFileName) {
        Properties properties = new Properties();
        try {
            InputStream in = context.getAssets().open(prtFileName);
            properties.load(in);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

}
