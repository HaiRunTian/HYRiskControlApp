package tianchi.com.risksourcecontrol.presenter;

import android.graphics.Bitmap;

import java.util.Map;

import tianchi.com.risksourcecontrol.model.LoadingLogInfoBiz;
import tianchi.com.risksourcecontrol.model.OnDownloadFileListener;
import tianchi.com.risksourcecontrol.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol.view.ILoadingLogView;

/**
 * Created by Kevin on 2018/2/6.
 */

public class LoadingLogInfoPresenter {

    LoadingLogInfoBiz m_loadingLogInfoBiz;
    ILoadingLogView m_loadingLogView;

    public LoadingLogInfoPresenter(ILoadingLogView loadingLogView) {
        this.m_loadingLogView = loadingLogView;
        m_loadingLogInfoBiz = new LoadingLogInfoBiz();
    }

    public void downloadLogPicture(int amount, int i) {
        m_loadingLogView.showLoadingPiture("加载" + "第" + (i + 1) + "张图片，共" + amount + "张");
        m_loadingLogInfoBiz.downloadLogPicture(m_loadingLogView.getDownLoadURL(),
                m_loadingLogView.getPictureName(), new OnDownloadFileListener() {
                    @Override
                    public void downloadSucceed(Bitmap bitmap) {
                        //                m_loadingLogView.hideLoadingPicture();
                        m_loadingLogView.showLoadingSucceed(bitmap);
                    }

                    @Override
                    public void downloadFailed(String msg) {
                        //                m_loadingLogView.hideLoadingPicture();
                        m_loadingLogView.showLoadingFailed(msg);
                    }
                });
    }

    public void submitModifyLog(String url,Map<String,Object> objectMap) {
        m_loadingLogInfoBiz.submitModifyLog(url,objectMap, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                m_loadingLogView.showSubmitSucceedOrFailed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                m_loadingLogView.showSubmitSucceedOrFailed(msg);
            }
        });
    }
}
