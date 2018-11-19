package tianchi.com.risksourcecontrol2.bean.log;

import java.util.Date;

/**
 * 安全巡查日志
 * Created by Kevin on 2017/12/20.
 */

public class PatrolLogInfo extends BaseLogInfo {

    //    public int id;//id
    //    public String logID;//日志编号
    //    public String stakeNum;//桩号
    //    public String section;//标段
    //    public Date saveTime;//保存时间
    //    public String recorder;//记录员
    //    private String riskType;//风险源类型
    //    private byte[] picture;//照片
    public int id;//id
    public String logId;//日志编号
    public String stakeNum;//桩号
    public String section;//标段
    public Date saveTime;//保存时间
    public String recorder;//记录员
    public String projectRole;//提交者
    public String riskType;//风险源类型
    public String weather;//天气
    public String emergency;//突发状况
    public String chargeBuilder;//施工单位负责人
    public String buildDetails;//生产详情
    public String technoDetails;//技术详情
    public String picture;//照片
    public String riskIndex;//风险源编号
    public String expireTime;//整改期限



    public PatrolLogInfo() {

    }

    public String getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(String riskIndex) {
        this.riskIndex = riskIndex;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getChargeBuilder() {
        return chargeBuilder;
    }

    public void setChargeBuilder(String chargeBuilder) {
        this.chargeBuilder = chargeBuilder;
    }

    public String getBuildDetails() {
        return buildDetails;
    }

    public void setBuildDetails(String buildDetails) {
        this.buildDetails = buildDetails;
    }

    public String getTechnoDetails() {
        return technoDetails;
    }

    public void setTechnoDetails(String technoDetails) {
        this.technoDetails = technoDetails;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
}
