package tianchi.com.risksourcecontrol2.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.notice.CheckedListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.DraftNotifyListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.MyselfRectifyNotifyListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.NewRectifyNotifyInfoActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.QueryRectifyNotiyActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.UnreadMsgListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.WaitCheckListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.myself.MyselfSendNotifyListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.myself.MyselfSendReplyListActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.DataState;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by hairun.tian on 2018/9/11.
 */

public class MessageFragment extends Fragment implements View.OnClickListener {

    private View m_rootView;
    private TextView tvMsgRefy, tvMsgNew, tvMsgDraft, tvMsgRefyNum, tvMsgReply, tvMsgWaitCheck, tvMsgChecked, tvMsgUnRead, tvMsgQuery, tvMsgRefresh,
            tvMsgSendNum, tvMsgSend;
    private View layoutMsg, layoutMsg2;
    private ProgressDialog m_progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initMessages() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.fragment_message, container, false);

        initView();
        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //请求网络数据
        queryDraftNotify();


    }

    private void queryDraftNotify() {
        m_progressDialog.setMessage("正在刷新中…");
        JSONObject _jsonObj = new JSONObject();
        try {
            _jsonObj.put("realName", UserSingleton.getUserInfo().getRealName());

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        LogUtils.i("json=realName", _jsonObj.toString());
        OkHttpUtils.postAsync(ServerConfig.URL_REFRESH_NOTIFY_DATA, _jsonObj.toString(), new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                m_progressDialog.setMessage("刷新失败");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {

//                LogUtils.i("查询成功", jsonString);
                DataState _dataState = GsonUtils.jsonToBean(jsonString, DataState.class);
                if (_dataState.getStatus() == -1) {
                    MyToast.showMyToast(getActivity(), _dataState.getMsg(), 2);
                } else if (_dataState.getStatus() == 0) {
                    MyToast.showMyToast(getActivity(), _dataState.getMsg(), 2);
                } else {
                    //消息数量显示
                    tvMsgDraft.setText("个人草稿(" + _dataState.getDraft() + ")");
                    tvMsgRefyNum.setText("个人收到(" + _dataState.getRecify() + ")");
                    tvMsgWaitCheck.setText("待审核(" + _dataState.getUnChecked() + ")");
                    tvMsgChecked.setText("已审核(" + _dataState.getChecked() + ")");
                    tvMsgUnRead.setText("未读消息(" + _dataState.getUnReadMsg() + ")");
                    tvMsgSendNum.setText("个人下达");
                    tvMsgSend.setText("个人回复");

                    m_progressDialog.setMessage("刷新完成");
                    m_progressDialog.dismiss();
                }
            }
        });
    }

    private void initView() {
        //tvMsgRefy,tvMsgNew,tvMsgDraft,tvMsgRefyNum,tvMsgReply,tvMsgWaitCheck,tvMsgChecked,tvMsgQuery,tvMsgRefresh;
        tvMsgRefy = (TextView) m_rootView.findViewById(R.id.tvMsgRefy);
        tvMsgNew = (TextView) m_rootView.findViewById(R.id.tvMsgNew);
        tvMsgDraft = (TextView) m_rootView.findViewById(R.id.tvMsgDraft);
        tvMsgRefyNum = (TextView) m_rootView.findViewById(R.id.tvMsgRefyNum);
        tvMsgReply = (TextView) m_rootView.findViewById(R.id.tvMsgReply);
        tvMsgWaitCheck = (TextView) m_rootView.findViewById(R.id.tvMsgWaitCheck);
        tvMsgChecked = (TextView) m_rootView.findViewById(R.id.tvMsgChecked);
        tvMsgQuery = (TextView) m_rootView.findViewById(R.id.tvMsgQuery);
        tvMsgRefresh = (TextView) m_rootView.findViewById(R.id.tvMsgRefresh);
        tvMsgUnRead = (TextView) m_rootView.findViewById(R.id.tvMsgUnread);

        tvMsgSendNum = (TextView) m_rootView.findViewById(R.id.tvMsgSendNum);
        tvMsgSend = (TextView) m_rootView.findViewById(R.id.tvMsgSend);

        Drawable _drawable = getResources().getDrawable(R.drawable.ic_tv_close);
        _drawable.setBounds(0, 0, _drawable.getMinimumWidth(), _drawable.getMinimumHeight());
        tvMsgRefy.setCompoundDrawables(null, null, _drawable, null);
        Drawable _drawable2 = getResources().getDrawable(R.drawable.ic_tv_close);
        _drawable.setBounds(0, 0, _drawable2.getMinimumWidth(), _drawable2.getMinimumHeight());
        tvMsgReply.setCompoundDrawables(null, null, _drawable, null);
        layoutMsg = m_rootView.findViewById(R.id.layoutMsg);
        layoutMsg2 = m_rootView.findViewById(R.id.layoutMsg2);

        //整改通知
        tvMsgRefy.setOnClickListener(this);
        tvMsgNew.setOnClickListener(this);
        tvMsgDraft.setOnClickListener(this);
        tvMsgRefyNum.setOnClickListener(this);
        tvMsgSendNum.setOnClickListener(this);
        //回复通知
        tvMsgReply.setOnClickListener(this);
        tvMsgWaitCheck.setOnClickListener(this);
        tvMsgChecked.setOnClickListener(this);
        tvMsgSend.setOnClickListener(this);
        tvMsgQuery.setOnClickListener(this);
        tvMsgRefresh.setOnClickListener(this);
        tvMsgUnRead.setOnClickListener(this);

        m_progressDialog = new ProgressDialog(getActivity());
        m_progressDialog.setCancelable(true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //是否显示整改通知单容器
            case R.id.tvMsgRefy:
                if (layoutMsg.getVisibility() != View.VISIBLE) {
                    layoutMsg.setVisibility(View.VISIBLE);
                    Drawable _drawable = getResources().getDrawable(R.drawable.ic_tv_open);
                    _drawable.setBounds(0, 0, _drawable.getMinimumWidth(), _drawable.getMinimumHeight());
                    tvMsgRefy.setCompoundDrawables(null, null, _drawable, null);

                } else {
                    layoutMsg.setVisibility(View.GONE);
                    Drawable _drawable = getResources().getDrawable(R.drawable.ic_tv_close);
                    _drawable.setBounds(0, 0, _drawable.getMinimumWidth(), _drawable.getMinimumHeight());
                    tvMsgRefy.setCompoundDrawables(null, null, _drawable, null);
                }
                break;
            //新建
            case R.id.tvMsgNew:
                startActivity(new Intent(getActivity(), NewRectifyNotifyInfoActivity.class));
                break;
            //草稿 列表
            case R.id.tvMsgDraft:
                startActivity(new Intent(getActivity(), DraftNotifyListActivity.class));
                break;
            //未读通知数量
            case R.id.tvMsgRefyNum:
                startActivity(new Intent(getActivity(), MyselfRectifyNotifyListActivity.class));
                break;


            case R.id.tvMsgReply:
                if (layoutMsg2.getVisibility() != View.VISIBLE) {
                    layoutMsg2.setVisibility(View.VISIBLE);
                    Drawable _drawable = getResources().getDrawable(R.drawable.ic_tv_open);
                    _drawable.setBounds(0, 0, _drawable.getMinimumWidth(), _drawable.getMinimumHeight());
                    tvMsgReply.setCompoundDrawables(null, null, _drawable, null);
                } else {
                    layoutMsg2.setVisibility(View.GONE);
                    Drawable _drawable = getResources().getDrawable(R.drawable.ic_tv_close);
                    _drawable.setBounds(0, 0, _drawable.getMinimumWidth(), _drawable.getMinimumHeight());
                    tvMsgReply.setCompoundDrawables(null, null, _drawable, null);
                }
                break;
            //未审核 跳转到未审核通知列表
            case R.id.tvMsgWaitCheck:
                startActivity(new Intent(getActivity(), WaitCheckListActivity.class));
                break;
            //已经审核 已审核通知列表
            case R.id.tvMsgChecked:
                startActivity(new Intent(getActivity(), CheckedListActivity.class));
                break;
            //未读消息列表
            case R.id.tvMsgUnread:
                startActivity(new Intent(getActivity(), UnreadMsgListActivity.class));
                break;
            //查询
            case R.id.tvMsgQuery:
                startActivity(new Intent(getActivity(), QueryRectifyNotiyActivity.class));
                break;
            //刷新
            case R.id.tvMsgRefresh:

//                List<Integer> _list = new ArrayList<>();
//                _list.add(194);
//                _list.add(195);
//                _list.add(196);
//                JSONObject _jsonObject = new JSONObject();
//                try {
//                    _jsonObject.putOpt("pids", _list);
//                    _jsonObject.put("RealName", UserSingleton.getUserInfo().getRealName());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                LogUtils.i("jsonString", _jsonObject.toString());
//                OkHttpUtils.postAsync(ServerConfig.URL_SET_MSG_READ, _jsonObject.toString(), new OkHttpUtils.InsertDataCallBack() {
//                    @Override
//                    public void requestFailure(Request request, IOException e) {
//                        LogUtils.i("result=", "失败了");
//                    }
//
//                    @Override
//                    public void requestSuccess(String result) throws Exception {
//                        LogUtils.i("result=", result);
//
//                    }
//                });

                m_progressDialog.show();
                queryDraftNotify();
//                startActivity(new Intent(getActivity(), UsersListActivity.class));
                break;

            //我发送的整改通知单
            case R.id.tvMsgSendNum:
                startActivity(new Intent(getActivity(), MyselfSendNotifyListActivity.class));

                break;
            //我发起的回复通知单
            case R.id.tvMsgSend:
                startActivity(new Intent(getActivity(), MyselfSendReplyListActivity.class));

                break;
            default:
                break;
        }

    }
}
