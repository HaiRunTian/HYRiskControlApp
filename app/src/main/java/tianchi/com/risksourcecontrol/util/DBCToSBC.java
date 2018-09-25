package tianchi.com.risksourcecontrol.util;

/**
 * Created by hairun.tian on 2018/1/31 0031.
 * 半角转全角
 */

public class DBCToSBC {public static String ToDBC(String input) {
    char[] c = input.toCharArray();
    for (int i = 0; i< c.length; i++) {
        if (c[i] == 12288) {
            c[i] = (char) 32;
            continue;
        }if (c[i]> 65280&& c[i]< 65375)
            c[i] = (char) (c[i] - 65248);
    }
    return new String(c);
}
}
