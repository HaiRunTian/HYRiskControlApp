package tianchi.com.risksourcecontrol2.model;

/**
 * 主页抽象业务逻辑层
 * Created by Kevin on 2017/12/19.
 */

public interface IHomeBiz {

    public void signIn();//签到


    public void riskQuery(String riskType,String stakeNum,String section,OnRiskQueryListener riskQueryListener);//查询风险源


    public void openRiskSourceInfo();


}
