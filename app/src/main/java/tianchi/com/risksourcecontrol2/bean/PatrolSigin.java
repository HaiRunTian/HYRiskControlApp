package tianchi.com.risksourcecontrol2.bean;//

/**
 * Created by Kevin on 2018/3/24.
 */

public class PatrolSigin {
    private int id;//ID
    private String account;//签到者账号
    private String realName;//签到者真实姓名
    private double x;//签到时的X坐标
    private double y;//签到时的y坐标
    private String section;//要签到的标段
    private String risk;//要签到的风险源

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
