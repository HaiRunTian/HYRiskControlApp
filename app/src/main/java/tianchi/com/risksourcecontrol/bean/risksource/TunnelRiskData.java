package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/20 0020.
 * 隧道详情页
 */

public class TunnelRiskData {
    private Integer smId;
    private Float 	x;			// x坐标
    private Float 	y;			// y坐标
    private String  riskId;      //序号
    private String  tunnelName;		// 隧道名;
    private String 	pileNo;			// 起讫桩号;
    private String 	form;			// 形式;
    private String  length;			// 长度;
    private String 	high;			// 高度;
    private String 	width;			// 宽度;
    private String 	speed;			// 速度;
    private String 	scope;			// 评分;
    private String 	occurPro;		// 发生可能性等级;
    private String 	resultGrade;	 // 后果严重性等级;
    private Float 	colorNote;		// 颜色标示;
    private String 	riskNote;		// 风险源标示;
    private String 	section;		 // 标段;


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
