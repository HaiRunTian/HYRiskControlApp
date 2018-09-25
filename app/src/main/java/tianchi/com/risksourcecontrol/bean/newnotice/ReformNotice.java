package tianchi.com.risksourcecontrol.bean.newnotice;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by hairun.tian on 2018/6/8 0008.
 */

public class ReformNotice {

    public int id; //整改通知ID
    public String title;//通知单编号
    public String insperctUnit;//检查单位
    public String beCheckedUnit;//受检单位
    public String checkTime; //检查时间
    public String inspectContent; //检查内容
    public String question; //发现问题
    public String request; //整改措施要求
    public String inspectorSign; //检查人签名
    public String retifyPeriod; //整改期限
    public Date rectifyPeriod; //受检单位签名
    public Bitmap m_bitmap; //现场照片
    public String logState; //日志状态
    public String section; //所属标段
    public String receiveMans; //整改单接收人




}
