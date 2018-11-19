package tianchi.com.risksourcecontrol2.fragment.UserFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.OwnerListviewAdatper;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;

/**
 * Created by HaiRun on 2018-09-13 19:47.
 * 业主方名单
 */

public class OwnerListFragment2 extends Fragment{
    private ListView m_lvOwner;
    private List<String> m_list;
    private OwnerListviewAdatper m_adatper;
    private TextView m_tvSelectAll;
    private TextView m_tvUnSelectAll;
    private TextView m_tvTotalSelections;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_listview_owner, container, false);
        m_lvOwner = (ListView) _view.findViewById(R.id.lvOwner);
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
        initData();
        // 绑定listView的监听器
        m_lvOwner.setAdapter(m_adatper);
        m_lvOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {//选中
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                OwnerListviewAdatper.ViewHolder _holder = (OwnerListviewAdatper.ViewHolder) view.getTag();
                _holder.cbItem.toggle();//反选checbox
                OwnerListviewAdatper.getIsSelected().put(position, _holder.cbItem.isChecked());//同步列表项选中情况
                if (_holder.cbItem.isChecked()) {
                    UsersList.addUserToList(m_list.get(position));//放入名单总列表
                } else {
                    UsersList.removeUserFromList(m_list.get(position));//从名单总列表移除
                }
                notifyChanged();
            }
        });
        m_tvSelectAll.setOnClickListener(new View.OnClickListener() {//全选
            @Override
            public void onClick(View v) {
                for (int i = 0; i < m_list.size(); i++) {
                    OwnerListviewAdatper.getIsSelected().put(i, true);
                    if (!UsersList.getList().contains(m_list.get(i))) {
                        UsersList.addUserToList(m_list.get(i));
                    }
                }
                notifyChanged();
            }
        });
        m_tvUnSelectAll.setOnClickListener(new View.OnClickListener() {//全取消
            @Override
            public void onClick(View v) {
                for (int i = 0; i < m_list.size(); i++) {
                    OwnerListviewAdatper.getIsSelected().put(i, false);
                    if (UsersList.getList().contains(m_list.get(i))) {
                        UsersList.removeUserFromList(m_list.get(i));
                    }
                }
                notifyChanged();
            }
        });
    }

    private void initData() {
//        m_list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            m_list.add("第" + (i + 1) + "个业主");
//        }
        m_list = UserSingleton.getOwnerList();
        m_adatper = new OwnerListviewAdatper(m_list, OwnerListFragment2.this.getActivity());
        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
    }

    // 刷新listview和TextView的显示
    private void notifyChanged() {
        // 通知listView刷新
        m_adatper.notifyDataSetChanged();
        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
    }
}
