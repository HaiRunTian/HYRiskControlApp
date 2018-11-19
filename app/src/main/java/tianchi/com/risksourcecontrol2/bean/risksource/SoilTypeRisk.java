package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * 软土，高液限土
 * Created by Kevin on 2018/1/11.
 */

public class SoilTypeRisk extends BaseRiskInfo {
    public String type;//类型
    public String roadSection;//路段
    public double handleLength;//处理长度
    public double handleWidth;//处理宽度
    public String handleProgram;//处理方案

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoadSection() {
        return roadSection;
    }

    public void setRoadSection(String roadSection) {
        this.roadSection = roadSection;
    }

    public double getHandleLength() {
        return handleLength;
    }

    public void setHandleLength(double handleLength) {
        this.handleLength = handleLength;
    }

    public double getHandleWidth() {
        return handleWidth;
    }

    public void setHandleWidth(double handleWidth) {
        this.handleWidth = handleWidth;
    }

    public String getHandleProgram() {
        return handleProgram;
    }

    public void setHandleProgram(String handleProgram) {
        this.handleProgram = handleProgram;
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
