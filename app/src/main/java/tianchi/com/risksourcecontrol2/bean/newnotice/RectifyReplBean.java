package tianchi.com.risksourcecontrol2.bean.newnotice;

import java.util.List;

/**
 * Created by Kevin on 2019-01-03.
 */

public class RectifyReplBean {

    /**
     * status : 1
     * msg : 通知单回复单查询成功
     * rectifyNotify : {"beCheckedUnit":"路基工班","beCheckedUnitSign":"J3总监办","checkedTime":"2018-12-13 18:16:21","id":113,"imageInfos":"检查内容 现场临时用电，临边安全防护，特种设备等等。#","images":"15446955221496733.jpg#369debc97fc1a23e754fa0d3c3fab113.jpg","infos":[],"inspectContent":"发现问题\r\n1、利水口大桥架桥机桁架U型卡螺栓未上紧，存在安全隐患。\r\n2、已架设好的第九跨梁面湿接缝未及时防护，存在安全隐患","inspectUnit":"J3总监办","inspectorSign":"测试员1","inspectorSigns":"","logId":"HYGS-TJ01-2018-113","logState":2,"question":"在安全巡查中发现金口电站大桥右幅3#墩基坑，深度超过2米，没有进行防护，存在安全隐患","receiverMans":"施工方1#施工方1","rectifyPeriod":"2018-12-13 17:12:03","request":"整改要求与方法\r\n1、立即按要求将利水口大桥架桥机桁架U型卡螺栓上紧。\r\n2、立即对已架设好的第九跨梁面湿接缝及时进行防护。","section":"TJ01","submitTime":"2018-12-13 18:16:21"}
     * rectifyReply : {"beCheckedUnit":"路基工班","checkedTime":"2018-12-13 18:09:21","id":383,"imageInfos":"检查内容 现场临时用电，临边安全防护，特种设备等等。/已按要求整改完毕。","images":"15446956137895569.jpg","inspectUnit":"J3总监办","logId":"HYGS-TJ01-2018-113HF","logState":3,"notifyType":17,"ownerMans":"测试员1","receiverMans":"","reciversInfos":[],"reciversOwnerInfos":[],"rectifyLogID":"HYGS-TJ01-2018-113","rectifyManSign":"施工方1","rectifySituation":"已按要求全部整改完成。","reviewSituation":"已按要求整改完毕。","reviewerSign":"张元","submitTime":"2018-12-13 18:09:21","supervisionInfos":[],"supervisorMans":"监理测试员#监理测试员"}
     */

    private int status;
    private String msg;
    private RectifyNotifyBean rectifyNotify;
    private RectifyReplyBean rectifyReply;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RectifyNotifyBean getRectifyNotify() {
        return rectifyNotify;
    }

    public void setRectifyNotify(RectifyNotifyBean rectifyNotify) {
        this.rectifyNotify = rectifyNotify;
    }

    public RectifyReplyBean getRectifyReply() {
        return rectifyReply;
    }

    public void setRectifyReply(RectifyReplyBean rectifyReply) {
        this.rectifyReply = rectifyReply;
    }

    public static class RectifyNotifyBean {
        /**
         * beCheckedUnit : 路基工班
         * beCheckedUnitSign : J3总监办
         * checkedTime : 2018-12-13 18:16:21
         * id : 113
         * imageInfos : 检查内容 现场临时用电，临边安全防护，特种设备等等。#
         * images : 15446955221496733.jpg#369debc97fc1a23e754fa0d3c3fab113.jpg
         * infos : []
         * inspectContent : 发现问题
         1、利水口大桥架桥机桁架U型卡螺栓未上紧，存在安全隐患。
         2、已架设好的第九跨梁面湿接缝未及时防护，存在安全隐患
         * inspectUnit : J3总监办
         * inspectorSign : 测试员1
         * inspectorSigns :
         * logId : HYGS-TJ01-2018-113
         * logState : 2
         * question : 在安全巡查中发现金口电站大桥右幅3#墩基坑，深度超过2米，没有进行防护，存在安全隐患
         * receiverMans : 施工方1#施工方1
         * rectifyPeriod : 2018-12-13 17:12:03
         * request : 整改要求与方法
         1、立即按要求将利水口大桥架桥机桁架U型卡螺栓上紧。
         2、立即对已架设好的第九跨梁面湿接缝及时进行防护。
         * section : TJ01
         * submitTime : 2018-12-13 18:16:21
         */

        private String beCheckedUnit;
        private String beCheckedUnitSign;
        private String checkedTime;
        private int id;
        private String imageInfos;
        private String images;
        private String inspectContent;
        private String inspectUnit;
        private String inspectorSign;
        private String inspectorSigns;
        private String logId;
        private int logState;
        private String question;
        private String receiverMans;
        private String rectifyPeriod;
        private String request;
        private String section;
        private String submitTime;
        private List<?> infos;

        public String getBeCheckedUnit() {
            return beCheckedUnit;
        }

        public void setBeCheckedUnit(String beCheckedUnit) {
            this.beCheckedUnit = beCheckedUnit;
        }

        public String getBeCheckedUnitSign() {
            return beCheckedUnitSign;
        }

        public void setBeCheckedUnitSign(String beCheckedUnitSign) {
            this.beCheckedUnitSign = beCheckedUnitSign;
        }

        public String getCheckedTime() {
            return checkedTime;
        }

        public void setCheckedTime(String checkedTime) {
            this.checkedTime = checkedTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageInfos() {
            return imageInfos;
        }

        public void setImageInfos(String imageInfos) {
            this.imageInfos = imageInfos;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getInspectContent() {
            return inspectContent;
        }

        public void setInspectContent(String inspectContent) {
            this.inspectContent = inspectContent;
        }

        public String getInspectUnit() {
            return inspectUnit;
        }

        public void setInspectUnit(String inspectUnit) {
            this.inspectUnit = inspectUnit;
        }

        public String getInspectorSign() {
            return inspectorSign;
        }

        public void setInspectorSign(String inspectorSign) {
            this.inspectorSign = inspectorSign;
        }

        public String getInspectorSigns() {
            return inspectorSigns;
        }

        public void setInspectorSigns(String inspectorSigns) {
            this.inspectorSigns = inspectorSigns;
        }

        public String getLogId() {
            return logId;
        }

        public void setLogId(String logId) {
            this.logId = logId;
        }

        public int getLogState() {
            return logState;
        }

        public void setLogState(int logState) {
            this.logState = logState;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getReceiverMans() {
            return receiverMans;
        }

        public void setReceiverMans(String receiverMans) {
            this.receiverMans = receiverMans;
        }

        public String getRectifyPeriod() {
            return rectifyPeriod;
        }

        public void setRectifyPeriod(String rectifyPeriod) {
            this.rectifyPeriod = rectifyPeriod;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(String submitTime) {
            this.submitTime = submitTime;
        }

        public List<?> getInfos() {
            return infos;
        }

        public void setInfos(List<?> infos) {
            this.infos = infos;
        }
    }

    public static class RectifyReplyBean {
        /**
         * beCheckedUnit : 路基工班
         * checkedTime : 2018-12-13 18:09:21
         * id : 383
         * imageInfos : 检查内容 现场临时用电，临边安全防护，特种设备等等。/已按要求整改完毕。
         * images : 15446956137895569.jpg
         * inspectUnit : J3总监办
         * logId : HYGS-TJ01-2018-113HF
         * logState : 3
         * notifyType : 17
         * ownerMans : 测试员1
         * receiverMans :
         * reciversInfos : []
         * reciversOwnerInfos : []
         * rectifyLogID : HYGS-TJ01-2018-113
         * rectifyManSign : 施工方1
         * rectifySituation : 已按要求全部整改完成。
         * reviewSituation : 已按要求整改完毕。
         * reviewerSign : 张元
         * submitTime : 2018-12-13 18:09:21
         * supervisionInfos : []
         * supervisorMans : 监理测试员#监理测试员
         */
        private int id;
        private String logId;
        private String inspectUnit;
        private String beCheckedUnit;
        private String checkedTime;
        private String rectifySituation;
        private String reviewSituation;
        private String rectifyManSign;
        private String images;
        private int logState;
        private String receiverMans;
        private String rectifyLogID;
        private String submitTime;
        private String imageInfos;
        private String supervisorMans;
        private String ownerMans;
        private int notifyType;

        private String reviewerSign;
        private List<?> reciversInfos;
        private List<?> reciversOwnerInfos;
        private List<?> supervisionInfos;

        public String getBeCheckedUnit() {
            return beCheckedUnit;
        }

        public void setBeCheckedUnit(String beCheckedUnit) {
            this.beCheckedUnit = beCheckedUnit;
        }

        public String getCheckedTime() {
            return checkedTime;
        }

        public void setCheckedTime(String checkedTime) {
            this.checkedTime = checkedTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageInfos() {
            return imageInfos;
        }

        public void setImageInfos(String imageInfos) {
            this.imageInfos = imageInfos;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getInspectUnit() {
            return inspectUnit;
        }

        public void setInspectUnit(String inspectUnit) {
            this.inspectUnit = inspectUnit;
        }

        public String getLogId() {
            return logId;
        }

        public void setLogId(String logId) {
            this.logId = logId;
        }

        public int getLogState() {
            return logState;
        }

        public void setLogState(int logState) {
            this.logState = logState;
        }

        public int getNotifyType() {
            return notifyType;
        }

        public void setNotifyType(int notifyType) {
            this.notifyType = notifyType;
        }

        public String getOwnerMans() {
            return ownerMans;
        }

        public void setOwnerMans(String ownerMans) {
            this.ownerMans = ownerMans;
        }

        public String getReceiverMans() {
            return receiverMans;
        }

        public void setReceiverMans(String receiverMans) {
            this.receiverMans = receiverMans;
        }

        public String getRectifyLogID() {
            return rectifyLogID;
        }

        public void setRectifyLogID(String rectifyLogID) {
            this.rectifyLogID = rectifyLogID;
        }

        public String getRectifyManSign() {
            return rectifyManSign;
        }

        public void setRectifyManSign(String rectifyManSign) {
            this.rectifyManSign = rectifyManSign;
        }

        public String getRectifySituation() {
            return rectifySituation;
        }

        public void setRectifySituation(String rectifySituation) {
            this.rectifySituation = rectifySituation;
        }

        public String getReviewSituation() {
            return reviewSituation;
        }

        public void setReviewSituation(String reviewSituation) {
            this.reviewSituation = reviewSituation;
        }

        public String getReviewerSign() {
            return reviewerSign;
        }

        public void setReviewerSign(String reviewerSign) {
            this.reviewerSign = reviewerSign;
        }

        public String getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(String submitTime) {
            this.submitTime = submitTime;
        }

        public String getSupervisorMans() {
            return supervisorMans;
        }

        public void setSupervisorMans(String supervisorMans) {
            this.supervisorMans = supervisorMans;
        }

        public List<?> getReciversInfos() {
            return reciversInfos;
        }

        public void setReciversInfos(List<?> reciversInfos) {
            this.reciversInfos = reciversInfos;
        }

        public List<?> getReciversOwnerInfos() {
            return reciversOwnerInfos;
        }

        public void setReciversOwnerInfos(List<?> reciversOwnerInfos) {
            this.reciversOwnerInfos = reciversOwnerInfos;
        }

        public List<?> getSupervisionInfos() {
            return supervisionInfos;
        }

        public void setSupervisionInfos(List<?> supervisionInfos) {
            this.supervisionInfos = supervisionInfos;
        }
    }
}
