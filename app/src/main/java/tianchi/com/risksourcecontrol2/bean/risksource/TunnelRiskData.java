package tianchi.com.risksourcecontrol2.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/20 0020.
 * 隧道详情页
 */

public class TunnelRiskData {
    public Integer smId;
    public Float 	x;			// x坐标
    public Float 	y;			// y坐标
    public String  riskId;      //序号
    public String  tunnelName;		// 隧道名;
    public String 	pileNo;			// 起讫桩号;
    public String 	form;			// 形式;
    public String  length;			// 长度;
    public String 	high;			// 高度;
    public String 	width;			// 宽度;
    public String 	speed;			// 速度;
    public String 	scope;			// 评分;
    public String 	occurPro;		// 发生可能性等级;
    public String 	resultGrade;	 // 后果严重性等级;
    public Float 	colorNote;		// 颜色标示;
    public String 	riskNote;		// 风险源标示;
    public String 	section;		 // 标段;


    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
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

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getPileNo() {
        return pileNo;
    }

    public void setPileNo(String pileNo) {
        this.pileNo = pileNo;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
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
