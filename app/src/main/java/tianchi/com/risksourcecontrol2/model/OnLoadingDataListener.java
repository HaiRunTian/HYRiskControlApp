package tianchi.com.risksourcecontrol2.model;

import java.util.List;

/**
 * 初始化标段数据接口
 * Created by Kevin on 2018/1/23.
 */

public interface OnLoadingDataListener {

    void loadSuccess(List<String> sectionList);

    void loadFailed(String msg);

}
