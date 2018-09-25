package tianchi.com.risksourcecontrol.model;

import java.util.List;

/**
 * 风险源查询接口
 * Created by Kevin on 2017/12/19.
 */

public interface OnRiskQueryListener {

    public void onQuerySucceed(List<String> list);

    public void onQueryFailed();
}
