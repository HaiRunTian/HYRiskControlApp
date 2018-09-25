package tianchi.com.risksourcecontrol.model;

/**
 * 加载人员列表接口
 * Created by Kevin on 2018/3/22.
 */

public interface LoadingRelationshipListener {
    void loadingSucceed();

    void loadingFailed(String msg);
}
