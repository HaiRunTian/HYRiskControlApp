package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 */

public class BaseRiskInfo {
    private int SmID;//唯一标示
    private String serialNum;//序号
    private String stakeNum;//起讫桩号
    private String section;//标段
    private double grade;//评分
    private String levelOfProb;//发生可能性等级
    private String levelOfConseq;//后果严重性等级
    private double colorIdentity;//颜色标识
    private String riskIdentity;//风险源标识

    public int getSmID() {
        return SmID;
    }

    public void setSmID(int smID) {
        SmID = smID;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getLevelOfProb() {
        return levelOfProb;
    }

    public void setLevelOfProb(String levelOfProb) {
        this.levelOfProb = levelOfProb;
    }

    public String getLevelOfConseq() {
        return levelOfConseq;
    }

    public void setLevelOfConseq(String levelOfConseq) {
        this.levelOfConseq = levelOfConseq;
    }

    public double getColorIdentity() {
        return colorIdentity;
    }

    public void setColorIdentity(double colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public String getRiskIdentity() {
        return riskIdentity;
    }

    public void setRiskIdentity(String riskIdentity) {
        this.riskIdentity = riskIdentity;
    }
}
