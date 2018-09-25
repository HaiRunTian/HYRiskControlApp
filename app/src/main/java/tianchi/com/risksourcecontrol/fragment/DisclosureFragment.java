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
import tianchi.com.risksourcecontrol.activitiy.QueryDisclosureActivity;
import tianchi.com.risksourcecontrol.adapter.MyExpandableListAdapter;

/**
 * 技术交底
 */

public class DisclosureFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    //二级菜单控件
    private ExpandableListView m_expandableListView;

    //二级菜单控件数据适配器
    private MyExpandableListAdapter m_adapter;

    private String[] m_groups = {"安全交底文件"};

    private String[][] m_childs = {{"交底资料查询"}};

    private int[][] m_imgIds = {{R.mipmap.ic_safety_manufacture_broswe_32px}};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        m_expandableListView = (ExpandableListView) getView().findViewById(R.id.expandableListView);
        m_adapter = new MyExpandableListAdapter(getActivity(),m_groups,m_childs,m_imgIds);
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
        return inflater.inflate(R.layout.fragment_disclosure,container,false);
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
            case "交底资料查询":
//               MyToast.showMyToast(getActivity(),"暂代开发", Toast.LENGTH_SHORT);
                intent = new Intent().setClass(getActivity(), QueryDisclosureActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
