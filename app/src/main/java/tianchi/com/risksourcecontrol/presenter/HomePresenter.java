package tianchi.com.risksourcecontrol.presenter;

import android.os.Handler;

import java.util.List;

import tianchi.com.risksourcecontrol.model.HomeBiz;
import tianchi.com.risksourcecontrol.model.IHomeBiz;
import tianchi.com.risksourcecontrol.model.OnRiskQueryListener;
import tianchi.com.risksourcecontrol.view.IHomeView;

/**
 * 主界面控制器
 * Created by Kevin on 2017/12/19.
 */

public class HomePresenter {
    IHomeView m_homeView;
    IHomeBiz m_homeBiz;
    Handler m_handler = new Handler();

    public HomePresenter(IHomeView homeView) {
        this.m_homeView = homeView;
        m_homeBiz = new HomeBiz();//←←←业务逻辑入口
    }

    /*
    * 签到
    * */
    public void signIn() {
        //        m_homeBiz.signIn();
        m_homeView.toSignInWindow();
    }


    /*
    * 风险源查询
    * */
    public void riskQuery() {
        m_homeView.showQuerying();
        m_homeBiz.riskQuery(m_homeView.getRiskType(), m_homeView.getRiskStakeNum(),m_homeView.getSection(),new OnRiskQueryListener() {

            @Override
            public void onQuerySucceed(List<String> list) {
                m_handler.post(new Runnable() {//查询成功
                    @Override
                    public void run() {
                        m_homeView.hideQuerying();
                        m_homeView.showRiskQuerySucceed(list);
                    }
                });
            }

            @Override
            public void onQueryFailed() {//查询失败
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_homeView.hideQuerying();
                        m_homeView.showRiskQueryFailed();
                    }
                });
            }
        });
    }

    public void openRiskSourceInfo() {
        m_homeBiz.openRiskSourceInfo();
    }



}
