package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * 桥梁
 * Created by Kevin on 2018/1/11.
 */

public class Bridge extends BaseRiskInfo {
    public String bridgeName;//桥梁名
    public String holeNum;//孔数孔径
    public String bridgeLength;//桥梁全长
    public String maxPierHeight;//最大墩高
    public String topSizeStru;//上部结构
    public String bottomSizeStru;//下部结构

    public String getBridgeName() {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }

    public String getHoleNum() {
        return holeNum;
    }

    public void setHoleNum(String holeNum) {
        this.holeNum = holeNum;
    }

    public String getBridgeLength() {
        return bridgeLength;
    }

    public void setBridgeLength(String bridgeLength) {
        this.bridgeLength = bridgeLength;
    }

    public String getMaxPierHeight() {
        return maxPierHeight;
    }

    public void setMaxPierHeight(String maxPierHeight) {
        this.maxPierHeight = maxPierHeight;
    }

    public String getTopSizeStru() {
        return topSizeStru;
    }

    public void setTopSizeStru(String topSizeStru) {
        this.topSizeStru = topSizeStru;
    }

    public String getBottomSizeStru() {
        return bottomSizeStru;
    }

    public void setBottomSizeStru(String bottomSizeStru) {
        this.bottomSizeStru = bottomSizeStru;
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
