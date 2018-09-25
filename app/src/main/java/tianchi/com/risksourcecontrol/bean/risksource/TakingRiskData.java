package tianchi.com.risksourcecontrol.bean.risksource;

/**
 * Created by hairun.tian on 2018/1/20 0020.
 * 废弃土场
 */

public class TakingRiskData {
    private Integer smId;
    private Float 	x;			// x坐标
    private Float 	y;			// y坐标
    private String  riskId;			// 序号;
    private String 	pileNo;			// 桩号;
    private String 	soilType;		// 类型;
    private String  place;			// 地名;
    private String 	soilQuantity;	 // 取土数;
    private Float 	colorNote;		// 颜色标示;
    private String 	riskNote;		// 风险源标示;
    private String 	section;		// 标段;


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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSoilQuantity() {
        return soilQuantity;
    }

    public void setSoilQuantity(String soilQuantity) {
        this.soilQuantity = soilQuantity;
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
