package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * 取弃土场
 * Created by Kevin on 2018/1/11.
 */

public class TakingSoilField extends BaseRiskInfo {
    private String placeName;//地名
    private String type;//类型
    private String soilCount;//取土数

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoilCount() {
        return soilCount;
    }

    public void setSoilCount(String soilCount) {
        this.soilCount = soilCount;
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
