package tianchi.com.risksourcecontrol.work;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import tianchi.com.risksourcecontrol.util.DateTimeUtils;

/**
 * 查询日志的一些后台工作
 * Created by Kevin on 2018/1/24.
 */

public class QueryLogWork {
    public static  String parseObjectMap2Json(Map<String,Object> objectMap) {
        JSONObject _jsonObject = new JSONObject();
        if (objectMap.size() > 0) {
            try {
                if (objectMap.containsKey("riskId"))
                    _jsonObject.put("riskId", objectMap.get("riskId"));
                if (objectMap.containsKey("section"))
                    _jsonObject.put("section", objectMap.get("section"));
                _jsonObject.put("logId", objectMap.get("logId"));
                _jsonObject.put("section", objectMap.get("section"));
                _jsonObject.put("riskType", objectMap.get("riskType"));
                _jsonObject.put("stakeNum", objectMap.get("stakeNum"));
                if (objectMap.get("startDate") != null) {
                    _jsonObject.put("startDate", DateTimeUtils.dateToString((Date)
                            objectMap.get("startDate"), DateTimeUtils.FULL_DATE_TIME_FORMAT));
                } else {
                    _jsonObject.put("startDate","");
                }
                if (objectMap.get("endDate") != null) {
                    _jsonObject.put("endDate", DateTimeUtils.dateToString((Date)
                            objectMap.get("endDate"), DateTimeUtils.FULL_DATE_TIME_FORMAT));
                } else {
                    _jsonObject.put("endDate","");
                }
                _jsonObject.put("recorders", objectMap.get("recorders"));
                _jsonObject.put("projectRole", objectMap.get("projectRole"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return _jsonObject.toString();
    }
}
