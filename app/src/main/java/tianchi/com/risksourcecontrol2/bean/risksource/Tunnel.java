package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * 隧道
 * Created by Kevin on 2018/1/11.
 */

public class Tunnel extends BaseRiskInfo {
    public String tunnelName;//隧道名
    public String form;//形式
    public String tunnelLength;//隧道长度
    public String clearHeight;//净高
    public String clearWidth;//净宽
    public String designSpeed;//设计速度

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getTunnelLength() {
        return tunnelLength;
    }

    public void setTunnelLength(String tunnelLength) {
        this.tunnelLength = tunnelLength;
    }

    public String getClearHeight() {
        return clearHeight;
    }

    public void setClearHeight(String clearHeight) {
        this.clearHeight = clearHeight;
    }

    public String getClearWidth() {
        return clearWidth;
    }

    public void setClearWidth(String clearWidth) {
        this.clearWidth = clearWidth;
    }

    public String getDesignSpeed() {
        return designSpeed;
    }

    public void setDesignSpeed(String designSpeed) {
        this.designSpeed = designSpeed;
    }

    @Override
    public int getSmID() {
        return super.getSmID();
    }

    @Override
    public void setSmID(int smID) {
        super.setSmID(smID);
    }

    @Override
    public String getSerialNum() {
        return super.getSerialNum();
    }

    @Override
    public void setSerialNum(String serialNum) {
        super.setSerialNum(serialNum);
    }

    @Override
    public String getStakeNum() {
        return super.getStakeNum();
    }

    @Override
    public void setStakeNum(String stakeNum) {
        super.setStakeNum(stakeNum);
    }

    @Override
    public String getSection() {
        return super.getSection();
    }

    @Override
    public void setSection(String section) {
        super.setSection(section);
    }

    @Override
    public double getGrade() {
        return super.getGrade();
    }

    @Override
    public void setGrade(double grade) {
        super.setGrade(grade);
    }

    @Override
    public String getLevelOfProb() {
        return super.getLevelOfProb();
    }

    @Override
    public void setLevelOfProb(String levelOfProb) {
        super.setLevelOfProb(levelOfProb);
    }

    @Override
    public String getLevelOfConseq() {
        return super.getLevelOfConseq();
    }

    @Override
    public void setLevelOfConseq(String levelOfConseq) {
        super.setLevelOfConseq(levelOfConseq);
    }

    @Override
    public double getColorIdentity() {
        return super.getColorIdentity();
    }

    @Override
    public void setColorIdentity(double colorIdentity) {
        super.setColorIdentity(colorIdentity);
    }

    @Override
    public String getRiskIdentity() {
        return super.getRiskIdentity();
    }

    @Override
    public void setRiskIdentity(String riskIdentity) {
        super.setRiskIdentity(riskIdentity);
    }
}
