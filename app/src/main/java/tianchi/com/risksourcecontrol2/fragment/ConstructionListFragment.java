package tianchi.com.risksourcecontrol2.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.ConstructionAdapter;
import tianchi.com.risksourcecontrol2.adapter.ListviewItemBaseAdapter;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;

/**
 * 施工方名单Fragment
 * Created by Kevin on 2018/3/21.
 */

public class ConstructionListFragment extends Fragment {
    private ListView m_lvConstructionLeft;//左侧listview
    private ListView m_lvConstructionRight;//右侧listview
    private List<String> m_list_left;//左侧填充的文本list
    private List<String> m_list_right;//右侧填充的文本list
    private Map<String, List<String>> m_listHashMap;//数据容器,string对应左侧项，arraylist对应右侧子项列表
    private ConstructionAdapter m_rightAdapter;//右侧子项适配器
//    private ArrayAdapter m_leftAdapter;//左侧列表适配器
    private ListviewItemBaseAdapter m_leftAdapter;//左侧列表适配器
    private TextView m_tvSelectAll;
    private TextView m_tvUnSelectAll;
    private TextView m_tvTotalSelections;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_listview_construction, container, false);
        m_lvConstructionLeft = (ListView) _view.findViewById(R.id.lvConstructionLeft);
        m_lvConstructionRight = (ListView) _view.findViewById(R.id.lvConstructionRight);
        m_tvSelectAll = (TextView) _view.findViewById(R.id.tvSelectAll);
        m_tvUnSelectAll = (TextView) _view.findViewById(R.id.tvUnSelectAll);
        m_tvTotalSelections = (TextView) _view.findViewById(R.id.tvTotalSelections);
        return _view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
    }

    @Override
    public void onStart() {
        super.onStart();
        // 绑定listView的监听器
        initData();
        m_lvConstructionLeft.setAdapter(m_leftAdapter);
        m_lvConstructionLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                setLeftSelectionBackground(position);//左侧列表选中后效果
                setRightSelectionLocation(position);//同步右侧列表对应位置
            }
        });

        m_lvConstructionRight.setAdapter(m_rightAdapter);
        m_lvConstructionRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ConstructionAdapter.ViewHolder _holder = (ConstructionAdapter.ViewHolder) view.getTag();
                _holder.cbItem.toggle();//反选checbox
                ConstructionAdapter.getIsSelected().put(position, _holder.cbItem.isChecked());//同步列表项选中情况
                if (_holder.cbItem.isChecked()) {
                    UsersList.addUserToList(m_list_right.get(position));//放入名单总列表
                } else {
                    UsersList.removeUserFromList(m_list_right.get(position));//从名单总列表移除
                }
                notifyChanged();
            }
        });
        m_tvSelectAll.setOnClickListener(new View.OnClickListener() {//全选
            @Override
            public void onClick(View v) {
                for (int i = 0; i < m_list_right.size(); i++) {
                    ConstructionAdapter.getIsSelected().put(i, true);
                    if (!UsersList.getList().contains(m_list_right.get(i))) {
                        UsersList.addUserToList(m_list_right.get(i));
                    }
                }
                notifyChanged();
            }
        });
        m_tvUnSelectAll.setOnClickListener(new View.OnClickListener() {//全不选
            @Override
            public void onClick(View v) {
                for (int i = 0; i < m_list_right.size(); i++) {
                    ConstructionAdapter.getIsSelected().put(i, false);
                    if (UsersList.getList().contains(m_list_right.get(i))) {
                        UsersList.removeUserFromList(m_list_right.get(i));
                    }
                }
                notifyChanged();
            }
        });
    }

    // 刷新listview和TextView的显示
    private void notifyChanged() {
        // 通知listView刷新
        m_rightAdapter.notifyDataSetChanged();
        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
    }

    //定位右侧列表位置
    private void setRightSelectionLocation(int position) {
        if (position == 0) {
            m_lvConstructionRight.setSelection(0);
        } else {
            int selection = 0;
            //selection是获取position前所有选项的子项总数
            for (int i = 0; i < position; i++) {
                //计算当前选中项之前所有子项的总数
                selection += m_listHashMap.get(m_list_left.get(i)).size();
            }
            m_lvConstructionRight.setSelection(selection);
        }
    }

    //左侧列表选中后效果
    private void setLeftSelectionBackground(int position) {
        for (int j = 0; j < m_lvConstructionLeft.getChildCount(); j++) {
            m_lvConstructionLeft.getChildAt(j).setBackgroundColor(
                    Color.TRANSPARENT);
        }
        for (int i = 0; i < m_lvConstructionLeft.getChildCount(); i++) {
            if (i == position) {//将选中那项的背景设为灰色
                m_lvConstructionLeft.getChildAt(i).setBackgroundColor(
                        Color.LTGRAY);
            } else {//将其他项的背景设为跟随父视图
                m_lvConstructionLeft.getChildAt(i).setBackgroundColor(
                        Color.TRANSPARENT);
            }
        }
    }

    private void initData() {
        m_list_left = new ArrayList<>();
        m_list_right = new ArrayList<>();
        //        m_listHashMap = new HashMap<>();
        //        /*测试数据*/
        //        for (int i = 1; i <= 3; i++) {
        //            m_list_left.add("施工方" + i);
        //            ArrayList<String> list = new ArrayList<>();
        //            for (int j = 1; j <= 10; j++) {
        //                list.add("施工方" + i + "子项" + j);
        //                m_list_right.add("施工方" + i + "子项" + j);
        //            }
        //            m_listHashMap.put("施工方" + i, list);
        //        }
        m_listHashMap = UserSingleton.getConstructionList();
        for (String key : m_listHashMap.keySet()) {
            m_list_left.add(key);
            for (String name : m_listHashMap.get(key)) {
                m_list_right.add(name);
            }
        }
//        m_leftAdapter = new ArrayAdapter(ConstructionListFragment.this.getActivity(), android.R.layout.simple_list_item_1, m_list_left);
        m_leftAdapter = new ListviewItemBaseAdapter(ConstructionListFragment.this
                .getActivity(), m_list_left);
        m_rightAdapter = new ConstructionAdapter(m_list_right, ConstructionListFragment.this
                .getActivity());
        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
    }
}
