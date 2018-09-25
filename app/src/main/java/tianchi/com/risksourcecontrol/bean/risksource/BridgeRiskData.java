package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/19 0019.
 */

public class BridgeRiskData {

    private Integer smId;
    private Float 	x;			// x坐标
    private Float 	y;			// y坐标
    private String  riskId;			// 序号;
    private String 	bridgeName;		// 桥梁名;
    private String 	pileNo;			// 中心桩号;
    private String  holeNum;		// 孔数孔径;
    private String 	bridgeLength;	 // 桥梁全长;
    private String 	bridgeHigh;		// 最大敦高;
    private String 	topStruct;		// 上部结构;
    private String 	downStruct;		// 下部结构;
    private Float 	scope;			// 评分;
    private String 	occurPro;		// 发生可能性等级;
    private String 	resultGrade;	             // 后果严重性等级;
    private Float 	colorNote;		// 颜色标示;
    private String 	riskNote;		// 风险源标示;
    private String 	section;		              // 标段;


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

    public String getBridgeName() {
        return bridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.bridgeName = bridgeName;
    }

    public String getPileNo() {
        return pileNo;
    }

    public void setPileNo(String pileNo) {
        this.pileNo = pileNo;
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

    public String getBridgeHigh() {
        return bridgeHigh;
    }

    public void setBridgeHigh(String bridgeHigh) {
        this.bridgeHigh = bridgeHigh;
    }

    public String getTopStruct() {
        return topStruct;
    }

    public void setTopStruct(String topStruct) {
        this.topStruct = topStruct;
    }

    public String getDownStruct() {
        return downStruct;
    }

    public void setDownStruct(String downStruct) {
        this.downStruct = downStruct;
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
