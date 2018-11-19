package tianchi.com.risksourcecontrol2.presenter;

import android.graphics.Bitmap;

import tianchi.com.risksourcecontrol2.model.LoadingNotifyInfoBiz;
import tianchi.com.risksourcecontrol2.model.OnDownloadFileListener;
import tianchi.com.risksourcecontrol2.view.ILoadingNotifyView;

/**
 * Created by hairun on 2018/6/26.
 * 通知图片下载
 */

public class LoadingNotifyInfoPresenter {

    LoadingNotifyInfoBiz m_loadingNotifyInfoBiz;
    ILoadingNotifyView m_loadingLogView;

    public LoadingNotifyInfoPresenter(ILoadingNotifyView loadingLogView) {
        this.m_loadingLogView = loadingLogView;
        m_loadingNotifyInfoBiz = new LoadingNotifyInfoBiz();
    }

    public void downloadLogPicture(int amount, int i) {
        m_loadingLogView.showLoadingPiture("加载" + "第" + (i + 1) + "张图片，共" + amount + "张");
        m_loadingNotifyInfoBiz.downloadLogPicture(m_loadingLogView.getDownLoadURL(),
                m_loadingLogView.getPictureName(), new OnDownloadFileListener() {
                    @Override
                    public void downloadSucceed(Bitmap bitmap) {
                        //                m_loadingLogView.hideLoadingPicture();
                        m_loadingLogView.showLoadingSucceed(bitmap);
                    }

                    @Override
                    public void downloadFailed(String msg) {
                        //                m_loadingLogView.hideLoadingPicture();
                        m_loadingLogView.showLoadingFailed(msg+"");
                    }
                });
    }


}
