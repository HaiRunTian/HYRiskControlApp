package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/19 0019.
 */

public class SoilTypeRiskData {


    private Integer smId;
    private Float 	x;			    // x坐标
    private Float 	y;			    // y坐标
    private String    riskId;		// 序号;
    private String 	pileNo;			// 起讫桩号;
    private String 	soilType;		// 软土类型;
    private String 	road;			// 路段;
    private Float 	length;			// 处理长度;
    private Float 	width;			// 处理宽度;
    private String 	processScheme;	// 处理方案;
    private Float 	scope;			// 评分;
    private String 	occurPro;		// 发生可能性等级;
    private String 	resultGrade;	// 后果严重性等级;
    private Float 	colorNote;		// 颜色标示;
    private String 	riskNote;		// 风险源标示;
    private String 	section;		// 标段;

    public String getProcessScheme() {
        return processScheme;
    }

    public void setProcessScheme(String processScheme) {
        this.processScheme = processScheme;
    }

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

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
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
