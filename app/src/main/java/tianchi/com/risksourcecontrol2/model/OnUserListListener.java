package tianchi.com.risksourcecontrol2.model;

import tianchi.com.risksourcecontrol2.bean.login.UserInfo;

/**
 * Created by Kevin on 2018/5/3.
 */

public interface OnUserListListener extends IListener{

    public void onQuerySucceed(UserInfo userInfoList);

    public void onQueryFailed(String msg);
}
