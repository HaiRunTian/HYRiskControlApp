package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 */

public class BaseRiskInfo {
    public int SmID;//唯一标示
    public String serialNum;//序号
    public String stakeNum;//起讫桩号
    public String section;//标段
    public double grade;//评分
    public String levelOfProb;//发生可能性等级
    public String levelOfConseq;//后果严重性等级
    public double colorIdentity;//颜色标识
    public String riskIdentity;//风险源标识

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
