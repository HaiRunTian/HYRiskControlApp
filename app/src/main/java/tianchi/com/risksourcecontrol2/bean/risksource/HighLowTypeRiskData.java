package tianchi.com.risksourcecontrol2.bean.risksource;



/**
 * Created by hairun.tian on 2018/1/18 0018.
 */

public class HighLowTypeRiskData {

    public Integer smId;
    public Float x;
    public Float y;
    public String riskId;// 序号;
    public String pileNo;// 起讫桩号;
    public String slopeType;// 边坡类型;
    public Float slopeHighMax;// 边坡最大高度;
    public Float slopeLength;// 边坡长度;
    public Float step;// 级数;
    public String protectWay;// 防护方式;
    public Float scope;// 评分;
    public String occurPro;// 发生可能性等级;
    public String resultGrade;// 后果严重性等级;
    public Float colorNote;// 颜色标示;
    public String riskNote;// 风险源标示;
    public String section;// 标段;


    public Integer getSmId() {
        return smId;
    }

    public void setSmId(Integer smId) {
        this.smId = smId;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public String getPileNo() {
        return pileNo;
    }

    public void setPileNo(String pileNo) {
        this.pileNo = pileNo;
    }

    public String getSlopeType() {
        return slopeType;
    }

    public void setSlopeType(String slopeType) {
        this.slopeType = slopeType;
    }

    public Float getSlopeHighMax() {
        return slopeHighMax;
    }

    public void setSlopeHighMax(Float slopeHighMax) {
        this.slopeHighMax = slopeHighMax;
    }

    public Float getSlopeLength() {
        return slopeLength;
    }

    public void setSlopeLength(Float slopeLength) {
        this.slopeLength = slopeLength;
    }

    public Float getStep() {
        return step;
    }

    public void setStep(Float step) {
        this.step = step;
    }

    public String getProtectWay() {
        return protectWay;
    }

    public void setProtectWay(String protectWay) {
        this.protectWay = protectWay;
    }

    public Float getScope() {
        return scope;
    }

    public void setScope(Float scope) {
        this.scope = scope;
    }

    public String getOccurPro() {
        return occurPro;
    }

    public void setOccurPro(String occurPro) {
        this.occurPro = occurPro;
    }

    public String getResultGrade() {
        return resultGrade;
    }

    public void setResultGrade(String resultGrade) {
        this.resultGrade = resultGrade;
    }

    public Float getColorNote() {
        return colorNote;
    }

    public void setColorNote(Float colorNote) {
        this.colorNote = colorNote;
    }

    public String getRiskNote() {
        return riskNote;
    }

    public void setRiskNote(String riskNote) {
        this.riskNote = riskNote;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
