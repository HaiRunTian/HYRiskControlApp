package tianchi.com.risksourcecontrol.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务器配置
 * Created by Kevin on 2018/1/18.
 */

public class ServerConfig {

    public static final String PORT_DATA = "8080";
    public static final String PORT_MAP = "8090";
    //内网ip=192.168.0.21
    //外网ip=120.77.41.216
//    public static final String IP = "192.168.0.21";
    public static final String IP = "120.77.41.216";
    public static final String URL = "http://120.77.41.216" + ":" + PORT_DATA;//外网服务器URL
    public static final String URLMAP = "http://120.77.41.216" + ":" + PORT_MAP;//外网地图

//    public static final String URL = "http://192.168.0.21" + ":" + PORT_DATA;//内网服务器URL
//    public static final String URLMAP = "http://192.168.0.21" + ":" + PORT_MAP;//内网地图  弃用

    public static String URL_GET_RISKINFO = URL + "/risk/riskmanager/getCheckInfo.json"; //获取风险源信息
    //        public static String URL_GET_RISKINFO = URL + "/risk/riskmanager/getRiskInfo.json"; //获取风险源信息
    public static String URL_CHECK_LOGIN = URL + "/risk/login/checkUserInfo.json";//登录接口
    public static String URL_MODIFY_USERINFO = URL + "/risk/admin/user/modifyUserInfo.json";//用户信息修改接口
    public static String URL_GET_USER_RELATIONSHIP = URL + "/risk/admin/user/getUserRelationShip.json";//获取用户关系表接口
    public static String URL_GET_USERINFO = URL + "/risk/admin/user/getUserInfoByRealName.json";//获取用户实体接口
    public static String URL_GET_SECTION_LIST = URL + "/risk/log/getSectionList.json";//获取标段列表接口
    public static String URL_SUBMIT_PRO_SAFETY_LOG = URL + "/risk/highwaylog/submitProSafetyLog.json";//提交生产安全日志接口
    public static String URL_SUBMIT_MODIFY_PRO_SAFETY_LOG = URL + "/risk/highwaylog/modifyProSafetyLog.json";//提交修改生产安全日志接口
    public static String URL_SUBMIT_RISK_REFORM_LOG = URL + "/risk/highwaylog/submitRiskReformLog.json";//提交风险源整改日志接口
    public static String URL_SUBMIT_SAFETY_PATROL_LOG = URL + "/risk/highwaylog/submitSafetyPatrolLog.json";//提交安全巡查日志接口
    public static String URL_SUBMIT_MODIFY_SAFETY_PATROL_LOG = URL + "/risk/highwaylog/modifySafetyPatrolLog.json";//提交修改安全巡查日志接口
    public static String URL_QUERY_PRO_SAFETY_LOG = URL + "/risk/highwaylog/queryProSafetyLogByCondition.json";//查询生产安全日志接口
    public static String URL_QUERY_RISK_REFORM_LOG = URL + "/risk/highwaylog/queryRiskReformLogByCondition.json";//查询风险源整改日志接口
    public static String URL_QUERY_SAFETY_PATROL_LOG = URL + "/risk/highwaylog/querysafetyPatrolLogByCondition.json";//查询安全巡查日志接口
    public static String URL_PATROL_SIGNIN_FOR_RISK = URL + "/risk/patrolSign/PatrolSignForRisk.json";//巡查签到接口
    public static String URL_GET_PATROL_SIGNIN_RISK = URL + "/risk/patrolSign/QueryPatrolSignRisk.json";//获取巡查签到风险源列表
    public static String URL_UPLOAD_FILE = URL + "/risk/filemanage/uploadFile.json";//文件上传接口
    public static String URL_DOWNLOAD_USER_FILE = URL + "/risk/file/user/";//头像下载接口
    public static String URL_DOWNLOAD_PRO_SAFETY_FILE = URL + "/risk/file/safeproduce/";//安全生产文件下载接口
    public static String URL_DOWNLOAD_SAFETY_PATROL_FILE = URL + "/risk/file/safepatrol/";//安全巡查文件下载接口
    public static String URL_DOWNLOAD_REFORM_FILE = URL + "/risk/file/riskreform/";//风险源整改文件下载接口
    public static String URL_DOWNLOAD_PATROL_SIGNIN_FILE = URL + "/risk/file/sign/";//巡查签到文件下载接口
    public static String URL_CHECK_RISKINFO_BY_VAGUE = URL + "/risk/riskmanager/JsonObjecTrans.json";
    public static String URL_QUERY_DISCLOSURE_FROM_SECTION = URL + "/risk/Disclosure/getDisclosureFromSection.json";//技术交底标段查询接口
    public static String URL_QUERY_DISCLOSURE_FROM_PILE = URL + "/risk/Disclosure/getDisclosureFromPile.json";//技术交底桩号查询接口
    public static String URL_CHECK_SECTION = URLMAP + "/iserver/services/map-RiskSource/rest/maps/"; //获取地图
    public static String URL_SEND_NOTICE = URL + "/risk/noticeSend/sendNotice.json"; //发送通知
    public static String URL_RECEIVE_NOTICE = URL + "/risk/noticeSend/receiveNotice.json"; //接收通知
    public static String URL_HAVE_SEND_NOTICE = URL + "/risk/noticeSend/sailGoNotice.json"; //已经发送的通知
    public static String URL_UPDATA_NOTICE_PIC = URL + "/risk/file/notify/"; //上传通知附件
    public static String URL_DOWNLOAD_DIS = URL + "/risk/file/safeinfo/";
    public static String URL_GET_VERSION_CODE = URL + "/risk/version/getVersionCode.json"; //获取版本号信息
    public static String URL_UPDATE_APP = URL + "/risk/file/version/RiskControl.apk";//更新版本

    public static String URl_DOWNLOAD_LICENSE = URL + "/risk/file/license/";//许可证下载
    //整改下达单
    public static String URL_RECTIFYNOTIFYINFO = URL + "/risk/rectifyNotify/submitRectifyNotify.json"; //提交整改通知单

    public static String URL_QUERYRECTIFYNOTIFY = URL + "/risk/rectifyNotify/queryMyselfRectifyNotfiy.json"; //查询已读和未读整改通知单

    public static String URL_QUERY_RECTIFY_NOTFIY = URL + "/risk/rectifyNotify/queryRectifyNotfiy.json";//查询整改通知单
    public static String URL_QUERY_REPLY_NOTIFY = URL + "/risk/rectifyNotifyReply/queryRectifyReplyNotfiy.json"; //查询回复通知单

    public static String URL_SUBMIT_REPLY_NOTICE = URL + "/risk/rectifyNotifyReply/submitRectifyReplyNotify.json"; //提交回复通知单

    public static String URL_QUERY_MYSELF__REPLY_NOTIFY = URL + "/risk//rectifyNotifyReply/queryMyselfRectifyReplyNotfiy.json"; //查询已读和未读回复通知单
    public static String URL_NOTICEFY_FILE_UPLOAD = URL + "/risk/file/recitynotify/"; //整改通知单文件下载
    public static String URL_QUERY_NEW_PATROL_LOG = URL + "/risk/highwaylog/querySafetyPatrolNewest.json"; //查询最新的安全巡查日志
    public static String URL_QUERY_NEW_PRO_LOG = URL + "/risk/highwaylog/queryProSafetyLogNewest.json"; //查询最新的生产安全日志
    public static String URL_UPDATE_NOTIFY_STATE_NOTIFY = URL + "/risk/rectifyNotifyReply/updateNotifyStateForReceiver.json"; //修改整改通知单的状态，是否已回复，是否已读
    public static String URL_UPDATE_NOTIFY_STATE_REPLY = URL + "/risk/rectifyNotifyReply/updateReplyNotifyStateForReceiver.json"; //修改整改通知单的状态，是否已回复，是否已读


    public static String URL_REFRESH_NOTIFY_DATA = URL + "/risk/rectifyNotifyReply/refreshNotifyData.json";//查询相关业务数据
    public static String URL_QUERY_DRAFT_NOTICE = URL + "/risk/rectifyNotify/queryDraftRectifyNotfiy.json"; //查询个人通知草稿
    public static String URL_SUBMIT_DRAFT_NTIFY = URL + "/risk/rectifyNotify/submitDraftRectifyNotify.json"; //提交个人草稿
    public static String URL_NEW_RECTIFY_REPLY_NOTIFY = URL + "/risk/rectifyNotifyReply/newRectifyReplyNotify.json";//新的提交整改回复单接口
    public static String URL_SUBMIT_SUPERVISOR = URL + "/risk/rectifyNotifyReply/verifyReplyNotify.json";//业主、监理审批
    public static String URL_REPLY_NOTIFY_READ = URL + "/risk/rectifyNotifyReply/setReplyNotifyRead.json";//修改回复通知单的状态
    public static String URL_QUERY_NOTIFY_MSG = URL + "/risk/rectifyNotifyReply/queryNotifyMessages.json";//查询相关的整改消息
    public static String URL_SET_MSG_READ = URL + "/risk/rectifyNotifyReply/setNotifyMessagesReaded.json";//将未读信息设为已读
    public static String URL_GET_MSG_CHECK_ONCHECK2 = URL + "/risk/rectifyNotifyReply/queryMyselfRectifyReplyNotfiy2.json";//获取已审核或者未审核的整改回复单
    public static String URL_GET_MSG_CHECK_ONCHECK = URL + "/risk/rectifyNotifyReply/setNotifyMessagesReaded.json";//未回复的通知单数量
    public static String URL_UPDATE_NOTIFY_TYPE = URL + "/risk/rectifyNotifyReply/updateNotifyStateForReceiver.json";//更新整改通知单阅读
    public static String URL_QUERY_USER_LIST = URL + "/risk/admin/user/getSuperVisorNameList.json";//获取相关用户名单
    public static String URL_QUERY_USER_LIST2 = URL + "/risk/admin/user/getUsersNameList.json";//获取相关用户名单
    public static String URL_QUERY_MSG = URL + "/risk/rectifyNotifyReply/queryNotifyMessages.json";//获取用户已读未读消息
    public static String URL_QUERY_NOTIFY_FOR_ID = URL + "/risk/rectifyNotify/queryNotifyInfo.json";//根据ID查询整改通知单
    public static String URL_QUERY_REPLY_FOR_ID = URL + "/risk/rectifyNotifyReply/queryNotifyInfo.json";//根据ID查询整改回复通知单
    public static String URL_QUERY_REPLY = URL + "/risk/rectifyNotifyReply/queryRectifyReplyNotfiy.json";//查询的回复通知单 可用于查别人关联的通知单


    public static String URL_QUERY_MYSELF_SEND_NOTIFY = URL + "/risk/rectifyNotify/queryRectifyNotifyFromMyself.json";//根据名字查整改通知单
    public static String URL_QUERY_MYSELF_SEND_REPLY = URL + "/risk/rectifyNotifyReply/queryRectifyReplyNotifyFromMyself.json";//根据ID查询整改回复通知单


    public static Map<String, String> s_map = new HashMap<>();
    public static Map<String, String> s_mapMap = new HashMap<>();
    public static Map<String, String> s_mapSection = new HashMap<>();

    public static Map<String, String> getMap() {
        s_map.put("0", "全部标段");
        s_map.put("1", "第1标段");
        s_map.put("2", "第2标段");
        s_map.put("3", "第3标段");
        s_map.put("4", "第4标段");
        s_map.put("5", "第5标段");
        s_map.put("6", "第6标段");
        s_map.put("7", "第7标段");
        s_map.put("8", "第8标段");
        s_map.put("9", "第9标段");
        s_map.put("10", "第10标段");
        s_map.put("11", "第11标段");
        s_map.put("12", "第12标段");
        s_map.put("13", "第13标段");
        s_map.put("14", "第14标段");
        s_map.put("15", "第15标段");
        s_map.put("16", "第16标段");

        return s_map;

    }


    public static Map<String, String> getMapSection() {
        s_mapSection.put("0", "全部标段");
        s_mapSection.put("1", "TJ01");
        s_mapSection.put("2", "TJ02");
        s_mapSection.put("3", "TJ03");
        s_mapSection.put("4", "TJ04");
        s_mapSection.put("5", "TJ05");
        s_mapSection.put("6", "TJ06");
        s_mapSection.put("7", "TJ07");
        s_mapSection.put("8", "TJ08");
        s_mapSection.put("9", "TJ09");
        s_mapSection.put("10", "TJ10");
        s_mapSection.put("11", "TJ11");
        s_mapSection.put("12", "TJ12");
        s_mapSection.put("13", "TJ13");
        s_mapSection.put("14", "TJ14");
        s_mapSection.put("15", "TJ15");
        s_mapSection.put("16", "TJ16");

        return s_mapSection;
    }


    public static Map<String, String> getMapMap() {
        s_mapMap.put("全部标段", "RiskSource0");
        s_mapMap.put("第1标段", "RiskSource1");
        s_mapMap.put("第2标段", "RiskSource2");
        s_mapMap.put("第3标段", "RiskSource3");
        s_mapMap.put("第4标段", "RiskSource4");
        s_mapMap.put("第5标段", "RiskSource5");
        s_mapMap.put("第6标段", "RiskSource6");
        s_mapMap.put("第7标段", "RiskSource7");
        s_mapMap.put("第8标段", "RiskSource8");
        s_mapMap.put("第9标段", "RiskSource9");
        s_mapMap.put("第10标段", "RiskSource10");
        s_mapMap.put("第11标段", "RiskSource11");
        s_mapMap.put("第12标段", "RiskSource12");
        s_mapMap.put("第13标段", "RiskSource13");
        s_mapMap.put("第14标段", "RiskSource14");
        s_mapMap.put("第15标段", "RiskSource15");
        s_mapMap.put("第16标段", "RiskSource16");
        return s_mapMap;

    }


}