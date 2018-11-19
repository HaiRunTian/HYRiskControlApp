package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/19 0019.
 */

public class SoilTypeRiskData {


    public Integer smId;
    public Float 	x;			    // x坐标
    public Float 	y;			    // y坐标
    public String    riskId;		// 序号;
    public String 	pileNo;			// 起讫桩号;
    public String 	soilType;		// 软土类型;
    public String 	road;			// 路段;
    public Float 	length;			// 处理长度;
    public Float 	width;			// 处理宽度;
    public String 	processScheme;	// 处理方案;
    public Float 	scope;			// 评分;
    public String 	occurPro;		// 发生可能性等级;
    public String 	resultGrade;	// 后果严重性等级;
    public Float 	colorNote;		// 颜色标示;
    public String 	riskNote;		// 风险源标示;
    public String 	section;		// 标段;

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
