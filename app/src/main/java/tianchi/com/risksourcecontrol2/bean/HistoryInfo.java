package tianchi.com.risksourcecontrol2.bean;

/**
 * Created by Kevin on 2018-12-13.
 */

public class HistoryInfo {
    String name;
    String password;
    public HistoryInfo(String name,String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
