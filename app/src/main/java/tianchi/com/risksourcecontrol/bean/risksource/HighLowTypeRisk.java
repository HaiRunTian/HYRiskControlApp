package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * 4个属性一致的风险源（高边坡，高填方,一般陡坡,低填浅挖）
 * Created by Kevin on 2018/1/11.
 */

public class HighLowTypeRisk extends BaseRiskInfo {

    private String slopeType;//边坡类型
    private double slopeMaxHeight;//边坡最大高度
    private double slopeLength;//边坡长度
    private double series;//级数
    private String protectedMethod;//防护方式

    public String getSlopeType() {
        return slopeType;
    }

    public void setSlopeType(String slopeType) {
        this.slopeType = slopeType;
    }

    public double getSlopeMaxHeight() {
        return slopeMaxHeight;
    }

    public void setSlopeMaxHeight(double slopeMaxHeight) {
        this.slopeMaxHeight = slopeMaxHeight;
    }

    public double getSlopeLength() {
        return slopeLength;
    }

    public void setSlopeLength(double slopeLength) {
        this.slopeLength = slopeLength;
    }

    public double getSeries() {
        return series;
    }

    public void setSeries(double series) {
        this.series = series;
    }

    public String getProtectedMethod() {
        return protectedMethod;
    }

    public void setProtectedMethod(String protectedMethod) {
        this.protectedMethod = protectedMethod;
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
