package tianchi.com.risksourcecontrol.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.log.NewPatrolActivity;
import tianchi.com.risksourcecontrol.activitiy.log.QueryPatrolLogActivity;
import tianchi.com.risksourcecontrol.adapter.MyExpandableListAdapter;

/**
 * 日志管理
 */

public class LogFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    //二级菜单控件
    private ExpandableListView m_expandableListView;

    //二级菜单控件数据适配器
    private MyExpandableListAdapter m_adapter;

    //    private String[] m_groups = {"生产日志上报与管理","巡查日志上报与管理","风险源日志上报与管理"};
//    private String[] m_groups = {"生产日志上报与管理", "重大风险源巡查日志"};
    private String[] m_groups = {"重大风险源巡查日志"};

    //    private String[][] m_childs = {{"新建安全日志","安全日志浏览"},
//            {"新建巡查日志","巡查日志浏览"},{"新建整改日志","整改日志浏览"}};
//    private String[][] m_childs = {{"新建安全日志", "安全日志浏览"},
//            {"新建重大风险源日志", "重大风险源日志浏览"}};

    private String[][] m_childs = {{"新建重大风险源日志", "重大风险源日志浏览"}};

    //    private int[][] m_imgIds = {{R.mipmap.ic_safety_manufacture_new_32px,
//            R.mipmap.ic_safety_manufacture_broswe_32px},
//            {R.mipmap.ic_safety_manufacture_new_32px,
//                    R.mipmap.ic_safety_manufacture_broswe_32px},
//            {R.mipmap.ic_safety_manufacture_new_32px,
//                    R.mipmap.ic_safety_manufacture_broswe_32px}};
//    private int[][] m_imgIds = {{R.mipmap.ic_safety_manufacture_new_32px,
//            R.mipmap.ic_safety_manufacture_broswe_32px},
//            {R.mipmap.ic_safety_manufacture_new_32px,
//                    R.mipmap.ic_safety_manufacture_broswe_32px},};
    private int[][] m_imgIds = {
            {R.mipmap.ic_safety_manufacture_new_32px,
                    R.mipmap.ic_safety_manufacture_broswe_32px}};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        m_expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
        m_adapter = new MyExpandableListAdapter(getActivity(), m_groups, m_childs, m_imgIds);
        m_expandableListView.setAdapter(m_adapter);
        m_expandableListView.setOnChildClickListener(this);
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
        return inflater.inflate(R.layout.fragment_safety, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent;
        String itemText = m_childs[groupPosition][childPosition];
        switch (itemText) {
//            case "新建安全日志":
//                intent = new Intent().setClass(getActivity(), NewSafetyLogActivity.class);
//                startActivity(intent);
//                break;
//            case "安全日志浏览":
//                intent = new Intent().setClass(getActivity(), QuerySafetyLogActivity.class);
//                startActivity(intent);
//                break;
            case "新建重大风险源日志":
                intent = new Intent().setClass(getActivity(), NewPatrolActivity.class);
                startActivity(intent);
                break;
            case "重大风险源日志浏览":
                intent = new Intent().setClass(getActivity(), QueryPatrolLogActivity.class);
                startActivity(intent);
                break;

//            case "新建整改日志":
//                intent = new Intent().setClass(getActivity(), NewReformActivity.class);
//                startActivity(intent);
//                break;
//            case "整改日志浏览":
//                intent = new Intent().setClass(getActivity(), QueryReformLogActivity.class);
//                startActivity(intent);
//                break;
//            case "新建通知":
//
//                intent = new Intent().setClass(getActivity(), RectifyNotifyActivity.class);
//                startActivity(intent);
//                break;
//            case "发送的通知":break;
//
//            case "接收的通知":break;
//
//            case "通知的查询":break;

            default:
                break;

        }
        return false;
    }
}
