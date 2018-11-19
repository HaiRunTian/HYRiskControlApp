package tianchi.com.risksourcecontrol2.bean;//

import java.util.Date;//

/**
 * 巡查路线
 * Created by Kevin on 2017/12/22.
 */

public class PatrolRouteBean {
    String account;//账号
    Date date;//日期
    Date patrolTime;//巡查时间
    String inspector;//巡查人员
    String workLoad;//工作量
    String patrolDetail;//巡查详情
    String patrolInstruct;//巡查下达指令
    String patrolNotification;//巡查下达通知

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getPatrolTime() {
        return patrolTime;
    }

    public void setPatrolTime(Date patrolTime) {
        this.patrolTime = patrolTime;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(String workLoad) {
        this.workLoad = workLoad;
    }

    public String getPatrolDetail() {
        return patrolDetail;
    }

    public void setPatrolDetail(String patrolDetail) {
        this.patrolDetail = patrolDetail;
    }

    public String getPatrolInstruct() {
        return patrolInstruct;
    }

    public void setPatrolInstruct(String patrolInstruct) {
        this.patrolInstruct = patrolInstruct;
    }

    public String getPatrolNotification() {
        return patrolNotification;
    }

    public void setPatrolNotification(String patrolNotification) {
        this.patrolNotification = patrolNotification;
    }
}
