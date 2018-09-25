package tianchi.com.risksourcecontrol.model;

import java.util.List;

/**
 * Created by hairun.tian on 2018-06-29.
 */

public interface OnQueryRiskListener {

    public void onQuerySucceed(String string);

    public void onQueryFailed();
}
