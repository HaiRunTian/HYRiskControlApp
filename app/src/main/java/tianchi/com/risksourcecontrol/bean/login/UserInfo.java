package tianchi.com.risksourcecontrol.bean.login;

import java.util.Date;

/**
 * 用户信息
 * Created by Kevin on 2018/1/18.
 */

public class UserInfo {
    public int userId;//用户ID
    public String loginName;//账号
    public String password;//密码
    public String realName;//真名
    public String address;//地址
    public String tel;//电话
    public String moblie;//手机
    public String email;//邮箱
    public Date birthday;//生日
    public String oicq;//QQ
    public Date regDate;//注册日期
    public String bz;//备注
    public boolean validity;//账号有效性
    public int userType;//用户类型
//    public byte[] picture;//用户头像
    public String picture;//用户头像
    public String sectionList;  //用户管理标段
    public int roleId;//角色 只有17，业主管理人员；19，监理方人员；20，安全员；
    public String projectRole;//项目职务 实际项目中承担的角色
    public String managerSection;//所属标段 管理或从属于的标段，区别于SectionList，本字段主要用于识别人员之间的关系



    public UserInfo() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getOicq() {
        return oicq;
    }

    public void setOicq(String oicq) {
        this.oicq = oicq;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public boolean getValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

//    public byte[] getPicture() {
//        return picture;
//    }
//
//    public void setPicture(byte[] picture) {
//        this.picture = picture;
//    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSectionList() {
        return sectionList;
    }

    public void setSectionList(String sectionList) {
        this.sectionList = sectionList;
    }

    public boolean isValidity() {
        return validity;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    public String getManagerSection() {
        return managerSection;
    }

    public void setManagerSection(String managerSection) {
        this.managerSection = managerSection;
    }
}
