package tianchi.com.risksourcecontrol2.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.notice.MyselfRectifyReplyListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.QueryRectifyNotiyActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.NewRectifyNotifyInfoActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.MyselfRectifyNotifyListActivity;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;

/**
 * Created by Kevin on 2018/1/16.
 * 备份  弃用
 */

public class MessageFragment3 extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView lvMessage;//消息列表
    private ArrayAdapter<String> m_adapter;//适配器
    private List<String> m_MessageList;//消息列表容器
    private AlertDialog m_dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        lvMessage = (ListView) getView().findViewById(R.id.lvMessage);
        lvMessage.setOnItemClickListener(this);
        lvMessage.setOnItemLongClickListener(this);
        initMessages();
        m_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, m_MessageList);
        lvMessage.setAdapter(m_adapter);
    }

    private void initMessages() {

        m_MessageList = new ArrayList<String>();
        m_MessageList.add("整改通知");
        m_MessageList.add("回复通知");
        m_MessageList.add("未读消息");
        m_MessageList.add("查询");
        m_MessageList.add("刷新");

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
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        MyToast.showMyToast(getActivity(), "暂代开发", Toast.LENGTH_SHORT);

        switch (position) {
            //新建整改通知
            case 0:

                startActivity(new Intent(getActivity(), NewRectifyNotifyInfoActivity.class));
//                startActivity(new Intent(getActivity(),ReceiveNoticeListActivity.class));
                break;
            //整改通知单已读和未读列表
            case 1:


                startActivity(new Intent(getActivity(), MyselfRectifyNotifyListActivity.class));
//                startActivity(new Intent(getActivity(), SendNoticeListActivity.class));
                break;
            //整改通知回复列表
            case 2:
                startActivity(new Intent(getActivity(),MyselfRectifyReplyListActivity.class));
                break;

            //草稿
            case 3:
                startActivity(new Intent(getActivity(), QueryRectifyNotiyActivity.class));
                break;
            //草稿
            case 4:

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        m_dialog = MyAlertDialog.showAlertDialog(getActivity(), "删除信息", "确定删除？",
                R.mipmap.ic_question, "确定", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyToast.showMyToast(getActivity(), "删除成功", Toast.LENGTH_SHORT);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_dialog != null)
                            m_dialog.dismiss();
                    }
                });
        return true;
    }
}
