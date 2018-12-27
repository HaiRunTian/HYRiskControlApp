package tianchi.com.risksourcecontrol2.activitiy.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.fragment.ConstructionListFragment;
import tianchi.com.risksourcecontrol2.fragment.OwnerListFragment;
import tianchi.com.risksourcecontrol2.fragment.RelationshipListFragment;
import tianchi.com.risksourcecontrol2.fragment.SupervisorListFragment;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.LogUtils;

/**
 * 用户列表Activity,加载业主、监理、施工方Fragment
 * 在此设置权限管理：
 * 1.重大风险源日志查看记录   a.业主查看全部日志 b.监理查看监理、施工方  c.施工方查看施工方
 * 2.生产安全日志   a.业主查看全部日志 b.监理查看监理、施工方  c.施工方查看施工方
 * 3.整改通知单下达           a.业主查看全部日志 b.监理查看监理、施工方  c.施工方查看施工方
 * 4.整改通知回复单审核       a.业主无需回复     b.监理查看业主、监理     c.施工方查看监理、施工方
 */
public class RelationshipListActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup m_radioGroup;
    private Fragment m_fragmentRaltionShip;//默认提示的fragment
    private Fragment m_fragmentOwner;//业主方fragment
    private Fragment m_fragmentSupervisor;//监理方fragment
    private Fragment m_fragmentConstruction;//施工方fragment
    private FragmentManager m_manager;
    private FragmentTransaction m_transaction;
    private TextView m_tvBack;
    private TextView m_tvSubmit;
    private int m_roid;
    private int m_permission = -1;
//    private DisplayMetrics dm;
//    private ListView m_lvSupervisorLeft;
//    private ListView m_lvSupervisorRight;
//    private ListviewItemBaseAdapter m_LeftAdapter;
//    private SupervisorListviewAdapter m_rightAdapter;
//    private TextView m_tvTotalSelections;
//
//    private Map<String, List<String>> m_listHashMap;
//    private List<String> m_list_left;//左侧填充的文本list
//    private List<String> m_list_right;//右侧填充的文本list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_list);
        init();
        initView();
//        dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        initModle();// 添加数据
    }



    @Override
    protected void onStop() {
        UsersList.clearList();
        super.onStop();
    }

    private void init() {
        m_permission = getIntent().getIntExtra("Type", 0);
        m_manager = getSupportFragmentManager();
        LogUtils.i("m_permission = ",m_permission+"");
    }

    private void initView() {
//                m_fragmentRaltionShip = new RelationshipListFragment();
        m_roid = UserSingleton.getUserInfo().getRoleId();
//        switchFragment(-1);
//
//        if (m_roid == 17) {
//            switchFragment(0);
//        } else if (m_roid == 19) {
//            switchFragment(1);
//        } else
//            switchFragment(2);
        m_tvBack = (TextView) findViewById(R.id.tvBack);
        m_tvBack.setOnClickListener(this);
        m_tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        m_tvSubmit.setOnClickListener(this);
        m_radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        m_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rdbOwner://业主方
                        if (UserPermission.OWNER_ALL == m_permission) { //业主单位
                            switchFragment(0);
                        } else if (m_roid == 19 && m_permission == UserPermission.SUPERVISON_SECOND) { //监理
                            switchFragment(0);
                        } else {
                            //施工方
                            MyToast.showMyToast(RelationshipListActivity.this, "您没有权限查看业主方人员名单", Toast.LENGTH_SHORT);
                        }
                        break;
                    case R.id.rdbSupervisor://监理方

                        if (m_roid == 17 && m_permission != UserPermission.CONSTRU_SECOND) {
                            switchFragment(1);
                        } else if (m_roid == 19 && UserPermission.SUPERVISON_FIRST == m_permission) {
                            switchFragment(1);
                        } else if (m_roid == 20 && UserPermission.CONSTRU_FIRST == m_permission) {
                            switchFragment(1);
                        } else if (m_roid == 17 && m_permission == UserPermission.SUPERVISON_THREE) {
                            switchFragment(1);
                        } else if (m_permission == UserPermission.OWNER_ALL){
                            switchFragment(1);
                        }else  MyToast.showMyToast(RelationshipListActivity.this, "您没有权限查看监理人员名单", Toast.LENGTH_SHORT);
                        break;
                    case R.id.rdbConstruction://施工方
                        if (m_roid == 19 && m_permission == UserPermission.SUPERVISON_SECOND) {

                            MyToast.showMyToast(RelationshipListActivity.this, "您没有权限查看施工方人员名单", Toast.LENGTH_SHORT);
                        } else if (m_roid == 17 && m_permission == UserPermission.SUPERVISON_THREE) {
                            MyToast.showMyToast(RelationshipListActivity.this, "您没有权限查看施工方人员名单", Toast.LENGTH_SHORT);
                        } else {
                            switchFragment(2);
                        }
                        break;
                }
            }
        });
    }

    private void switchFragment(int index) {
        if (m_manager == null)
            m_manager = getSupportFragmentManager();
        m_transaction = m_manager.beginTransaction();
        hideFragment(m_transaction);
        switch (index) {
            case -1: //默认
                if (m_fragmentRaltionShip == null) {//切换默认fragment
                    m_fragmentRaltionShip = new RelationshipListFragment();
                    m_transaction.add(R.id.fragment, m_fragmentRaltionShip);
                }
                m_transaction.show(m_fragmentRaltionShip);
                break;
            case 0:
                if (m_fragmentOwner == null) {//切换业主fragment
                    m_fragmentOwner = new OwnerListFragment();
                    m_transaction.add(R.id.fragment, m_fragmentOwner);
                }
                m_transaction.show(m_fragmentOwner);
                break;
            case 1:
                if (m_fragmentSupervisor == null) {//切换监理fragment
                    m_fragmentSupervisor = new SupervisorListFragment();
                    m_transaction.add(R.id.fragment, m_fragmentSupervisor);
                }
                m_transaction.show(m_fragmentSupervisor);
                break;
            case 2:
                if (m_fragmentConstruction == null) {//切换施工方fragment
                    m_fragmentConstruction = new ConstructionListFragment();
                    m_transaction.add(R.id.fragment, m_fragmentConstruction);
                }
                m_transaction.show(m_fragmentConstruction);
                break;
            default:
                break;
        }
        m_transaction.commit();
    }

//    private void downPopwindow() {
//        View contentView  = LayoutInflater.from(this).inflate(R.layout.pop_down, null);
//        int screenHeight = dm.heightPixels * 2 / 3;
//        PopupWindow _popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
//        _popupWindow.setContentView(contentView);
//        _popupWindow.setFocusable(true);
//        _popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        _popupWindow.setOutsideTouchable(false);
//        m_lvSupervisorLeft = ((ListView) contentView.findViewById(R.id.lvLeft));
//        m_lvSupervisorRight = ((ListView) contentView.findViewById(R.id.lvRight));
//        m_tvTotalSelections = ((TextView) contentView.findViewById(R.id.tvTotalSelections));
//
//
//        m_listHashMap = UserSingleton.getSupervisorList();
//
//        for (String key : m_listHashMap.keySet()){
//            m_list_left.add(key);
//            m_list_right=(m_listHashMap.get(key));
//        }
//
//
//        m_LeftAdapter = new ListviewItemBaseAdapter(this,m_list_left);
//        m_LeftAdapter.setSelectItem(0);
//        m_lvSupervisorLeft.setAdapter(m_LeftAdapter);
//        m_lvSupervisorLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                initAdapter();
//                m_lvSupervisorLeft.setAdapter(m_LeftAdapter);
//                m_LeftAdapter.notifyDataSetChanged();
//            }
//        });
//
//        m_lvSupervisorLeft.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        initAdapter();//适配器
//
//        m_lvSupervisorRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String _s = (String) m_rightAdapter.getItem(position);
//                Toast.makeText(RelationshipListActivity.this, ""+_s, Toast.LENGTH_SHORT).show();
//                m_rightAdapter.setSelectItem(position);
//                m_rightAdapter.notifyDataSetChanged();
//
//                SupervisorListviewAdapter.ViewHolder _holder = (SupervisorListviewAdapter.ViewHolder) view.getTag();
//                _holder.cbItem.toggle();//反选checbox
//                SupervisorListviewAdapter.getIsSelected().put(position, _holder.cbItem.isChecked());//同步列表项选中情况
//                if (_holder.cbItem.isChecked()) {
////                    UsersList.addUserToList(m_list_right.get(position));//放入名单总列表
//                } else {
////                    UsersList.removeUserFromList(m_list_right.get(position));//从名单总列表移除
//                }
//                notifyChanged();
//            }
//        });
//        _popupWindow.showAsDropDown(m_radioGroup);
//
////
////        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
////        SupervisorListviewAdapter.ViewHolder _holder = (SupervisorListviewAdapter.ViewHolder) view.getTag();
////        _holder.cbItem.toggle();//反选checbox
////        SupervisorListviewAdapter.getIsSelected().put(position, _holder.cbItem.isChecked());//同步列表项选中情况
////        if (_holder.cbItem.isChecked()) {
////            UsersList.addUserToList(m_list_right.get(position));//放入名单总列表
////        } else {
////            UsersList.removeUserFromList(m_list_right.get(position));//从名单总列表移除
////        }
////        notifyChanged();
//    }
//    private void initModle() {
//        m_list_left = new ArrayList<>();
//        m_list_right = new ArrayList<>();
//
//    }
//    // 刷新listview和TextView的显示
//    private void notifyChanged() {
//        // 通知listView刷新
//        m_rightAdapter.notifyDataSetChanged();
//        m_tvTotalSelections.setText("一共选择了" + UsersList.getList().size() + "人");
//    }
//    private void initAdapter() {
//        m_rightAdapter = new SupervisorListviewAdapter(m_list_right,this);
//        m_lvSupervisorRight.setAdapter(m_rightAdapter);
//        m_rightAdapter.notifyDataSetChanged();
//    }

    private void hideFragment(FragmentTransaction transaction) {
        if (m_fragmentRaltionShip != null) {
            transaction.hide(m_fragmentRaltionShip);
        }
        if (m_fragmentOwner != null) {
            transaction.hide(m_fragmentOwner);
        }
        if (m_fragmentSupervisor != null) {
            transaction.hide(m_fragmentSupervisor);
        }
        if (m_fragmentConstruction != null) {
            transaction.hide(m_fragmentConstruction);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                Intent _intent = new Intent();
                setResult(RESULT_CANCELED, _intent);
                finish();
                break;
            case R.id.tvSubmit:
                MyAlertDialog.showAlertDialog(this, "提交提示", "一共选择了" + UsersList.getList().size() + "人，" +
                        "确定提交？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent _intent = new Intent();
                        setResult(RESULT_OK, _intent);
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }
}
