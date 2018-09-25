package tianchi.com.risksourcecontrol.activitiy.log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol.bean.login.UsersList;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyDatePicker;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.presenter.QueryPatrolLogPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.view.IQueryLogView;
import tianchi.com.risksourcecontrol.work.UserLoginWork;

/**
 * @描述 巡查日志浏览
 * @作者 Kevin.
 * @创建日期 2017/11/7  10:39.
 */
public class QueryPatrolLogActivity extends BaseActivity implements IQueryLogView, View.OnClickListener, View.OnLongClickListener {
    private final int GET_RELATIONSHIP = 0;
    private TextView tvStartDate;//起始日期
    private TextView tvEndDate;//终止日期
    private TextView tvBack;//返回

    private Spinner spSection;//标段
    private Spinner spRiskType;//风险类型
    private EditText edtStakeNum;//桩号

    private EditText edtRecorder;//记录人
    private EditText edtLogID;//记录人

    private Button btnQuery;//查询
    private Button btnQueryAll;//查询全部
    private Button btnQuerySelf;//查询自己的日志
    private ProgressDialog m_progressDialog;
    private CheckBox cbOwner, cbSupervisor, cbConstructor;
    //    private RadioGroup m_radioGroup;
    //    private ListView m_lvResult;//查询结果listview
    //    private LogListiviewAdapter m_adapter;
    private static List<PatrolLogInfo> m_resultLists;
    private String projectRole = "";
    //查询安全巡查日志控制器
    QueryPatrolLogPresenter m_queryPatrolLogPresenter = new QueryPatrolLogPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_patrol_log);
        initView();
        initValue();
    }

    private void initValue() {
        //        tvStartDate.setText(DateTimeUtils.setCurrentTime());
        //        tvEndDate.setText(DateTimeUtils.setCurrentTime());
        //        setRecorder();
        setSection();
        //        setLogId();
    }

    private void initView() {
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartDate.setOnClickListener(this);

        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvEndDate.setOnClickListener(this);

        tvBack = (TextView) findViewById(R.id.tvBrowseBack);
        tvBack.setOnClickListener(this);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(this);

        btnQueryAll = (Button) findViewById(R.id.btnQueryAll);
        btnQueryAll.setOnClickListener(this);

        btnQuerySelf = (Button) findViewById(R.id.btnQuerySelf);
        btnQuerySelf.setOnClickListener(this);

        cbOwner = (CheckBox) findViewById(R.id.cbOwner);
        cbSupervisor = (CheckBox) findViewById(R.id.cbSupervisor);
        cbConstructor = (CheckBox) findViewById(R.id.cbConstructor);

//        m_radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        m_radioGroup.setOnCheckedChangeListener(this);

        //        m_lvResult = (ListView) findViewById(R.id.lvResult);
        //        m_lvResult.setOnItemClickListener(this);

        spSection = (Spinner) findViewById(R.id.spSection);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtRecorder.setOnClickListener(this);
        edtRecorder.setOnLongClickListener(this);
        edtLogID = (EditText) findViewById(R.id.edtLogID);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("查询中...");
        m_progressDialog.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edtRecorder:
                int m_roid = UserSingleton.getUserInfo().getRoleId();
                if (m_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.OWNER_ALL), GET_RELATIONSHIP);
                } else if (m_roid == 19) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_RELATIONSHIP);
                } else
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_RELATIONSHIP);

                break;

            case R.id.tvStartDate:
                MyDatePicker.ShowDatePicker(QueryPatrolLogActivity.this, tvStartDate);
                break;

            case R.id.tvEndDate:
                MyDatePicker.ShowDatePicker(QueryPatrolLogActivity.this, tvEndDate);
                break;

            case R.id.tvBrowseBack:
                finish();
                break;

            //查询
            case R.id.btnQuery:
                if (getRecorder().length() == 0) {
                    edtRecorder.setText(UserLoginWork.resolveRelationshipList());
                }
                m_queryPatrolLogPresenter.query();
                resetCheckbox();
                resetEdittext();
                break;

            //查询全部
            case R.id.btnQueryAll:
                m_queryPatrolLogPresenter.queryAll();
                break;

            //查询自己提交的日志
            case R.id.btnQuerySelf:
                resetCheckbox();
                resetEdittext();
                edtRecorder.setText(UserSingleton.getUserInfo().getRealName());
                spSection.setSelection(UserSingleton.getSectionList().size() - 1);
                spRiskType.setSelection(10);
                m_queryPatrolLogPresenter.query();
                resetEdittext();
                break;

            default:
                break;
        }
    }

    private void resetCheckbox() {
        cbOwner.setChecked(false);

        cbConstructor.setChecked(false);
    }

    private void resetEdittext() {
        edtRecorder.setText("");
        projectRole = "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_RELATIONSHIP:
                if (resultCode == RESULT_OK) {
                    String allNameList = "";
                    for (String name : UsersList.getList()) {
                        allNameList += name + "#";
                    }
                    edtRecorder.setText(allNameList.substring(0, allNameList.length() - 1));
                }
                UsersList.clearList();
                break;
        }
    }

    @Override
    public void setLogId() {
        if (UserSingleton.getUserInfo().getRealName() != null)
            edtRecorder.setText(UserSingleton.getUserInfo().getRealName());
    }

    @Override
    public String getSection() {
        if (spSection.getSelectedItem() != null)
            return spSection.getSelectedItem().toString();
        return "";
    }

    @Override
    public void setSection() {
        if (UserSingleton.getSectionList() != null) {
            spSection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                    UserSingleton.getSectionList()));
            spSection.setSelection(UserSingleton.getSectionList().size() - 1);
            spRiskType.setSelection(10);
        }
    }

    @Override
    public String getRiskType() {
        String riskType = spRiskType.getSelectedItem().toString();
        if (riskType != null && riskType.length() > 0) {
            return riskType.substring(2, riskType.length());
        }
        return "";
    }

    @Override
    public String getStakeNum() {
        return edtStakeNum.getText().toString();
    }

    @Override
    public Date getStartDate() {
        try {
            return DateTimeUtils.stringToDate(tvStartDate.getText().toString()
                    + " 00:00:00", DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Date getEndDate() {
        try {
            return DateTimeUtils.stringToDate(tvEndDate.getText().toString()
                    + " 23:59:59", DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRecorder() {
        //        if (edtRecorder.getText().toString().equals("")) {
        //            return UserSingleton.getUserInfo().getRealName();
        //        } else {
        //        }
        return edtRecorder.getText().toString();
    }

    @Override
    public String getProjectRole() {
        if (cbOwner.isChecked()) {
            projectRole += cbOwner.getText().toString() + "#";
        }
        if (cbSupervisor.isChecked()) {
            projectRole += cbSupervisor.getText().toString() + "#";
        }
        if (cbConstructor.isChecked()) {
            projectRole += cbConstructor.getText().toString() + "#";
        }
        if (projectRole.length() > 0) {
            projectRole = projectRole.substring(0, projectRole.length() - 1);
        }
        return projectRole;
    }

    @Override
    public String getRiskId() {//用不到
        return null;
    }

    @Override
    public String getLogId() {
        return edtLogID.getText().toString();
    }

    //    @Override
    //    public String getIdLoginName() {
    //        return UserSingleton.getUserInfo().getUserId()
    //                + UserSingleton.getUserInfo().getLoginName();
    //    }

    @Override
    public void setRecorder() {
        if (UserSingleton.getUserInfo().getRealName() != null)
            edtRecorder.setText(UserSingleton.getUserInfo().getRealName());
    }

    @Override
    public void showQuerying(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage("查询中...");
            m_progressDialog.show();
        }
    }

    @Override
    public void hideQuerying() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
    }

    @Override
    public void showQuerySucceed(List<? extends BaseLogInfo> logList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideQuerying();
            }
        });
        if (logList.size() > 0) {
            m_resultLists = (List<PatrolLogInfo>) logList;
            Intent intent = new Intent(this, LogResultListActivity.class);
            intent.putExtra("logList", (Serializable) m_resultLists);
            startActivity(intent);
        }
    }

    @Override
    public void showQueryFailed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideQuerying();
            }
        });
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.edtRecorder) {
            MyAlertDialog.showAlertDialog(this, "已选人员列表", getRecorder());
        }
        return true;
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        RadioButton _radioButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
//        projectRole = _radioButton.getText().toString();
//    }

    //    @Override
    //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //        PatrolLogInfo item = m_resultLists.get(position);
    //        Bundle _bundle = new Bundle();
    //        _bundle.putString("LogID", item.getLogId());
    //        _bundle.putString("Section", item.getSection());
    //        _bundle.putString("StakeNum", item.getStakeNum());
    //        _bundle.putString("RiskType", item.getRiskType());
    //        _bundle.putString("Weather", item.getWeather());
    //        _bundle.putString("Emergency", item.getEmergency());
    //        _bundle.putString("recorder", item.getRecorder());
    //        _bundle.putString("Leader", item.getLeader());
    //        _bundle.putString("date", DateTimeUtils.dateToString(item.getSaveTime(), "yyyy年MM月dd日"));
    //        //        _bundle.putByteArray("picture", item.getPicture());
    //        _bundle.putString("mdetails", item.getmDetails());
    //        _bundle.putString("tdetails", item.gettDetails());
    //        startActivity(new Intent(QueryPatrolLogActivity.this, PatrolLogInfoActivity.class).putExtras(_bundle));
    //    }
}