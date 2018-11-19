package tianchi.com.risksourcecontrol2.view;

import org.json.JSONObject;

/**
 * Created by hairun.tian on 2018-06-25.
 */

public interface IQueryNotifyView {

    /**
     *
     * @return 获取提交人
     */
    String getSubmiter();
    /**
     *
     * @return 获取开始时间
     */
    String getStartDate();
    /**
     *
     * @return 获取终止时间
     */
    String getEndDate();
    /**
     *
     * @return 获取通知状态
     */
    int getNotifyState();

    /**
     *
     * @return 获取json
     */
    JSONObject getJSon();
}
