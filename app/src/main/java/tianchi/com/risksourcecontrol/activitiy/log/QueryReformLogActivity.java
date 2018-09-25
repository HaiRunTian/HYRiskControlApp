package tianchi.com.risksourcecontrol.activitiy.log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol.custom.MyDatePicker;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.presenter.QueryReformLogPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.view.IQueryLogView;


/**
 * @描述 整改日志浏览
 * @作者 Kevin.
 * @创建日期 2017/11/7  10:40.
 */
public class QueryReformLogActivity extends BaseActivity implements IQueryLogView, View.OnClickListener{

    private TextView tvStartDate;//起始日期
    private TextView tvEndDate;//结束日期
    private TextView tvBack;//返回

    private EditText edtRiskID;//风险源编号
    private EditText edtLogID;//日志编号
    private EditText edtRecorder;//记录人

    private Spinner spRiskType;//风险源类型
    private EditText edtStakeNum;//桩号

    private Button btnQuery;//查询
    private Button btnQueryAll;//查询全部
    private ProgressDialog m_progressDialog;
    //    private ListView m_lvResult;//查询结果listview
    //    private LogListiviewAdapter m_adapter;
    private static List<ReformLogInfo> m_resultLists;
    //查询整改日志控制器
    QueryReformLogPresenter m_queryReformLogPresenter = new QueryReformLogPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_reform_log);
        initView();
        initValue();
    }

    private void initValue() {
//        tvStartDate.setText(DateTimeUtils.setCurrentTime());
//        tvEndDate.setText(DateTimeUtils.setCurrentTime());
//        setLogId();
        setRecorder();
    }

    private void initView() {
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvBack = (TextView) findViewById(R.id.tvQueryReformBack);

        edtLogID = (EditText) findViewById(R.id.edtLogID);
        edtRiskID = (EditText) findViewById(R.id.edtRiskID);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);

        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        btnQueryAll = (Button) findViewById(R.id.btnQueryAll);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("查询中...");
        m_progressDialog.setCancelable(true);
        //        m_lvResult = (ListView) findViewById(R.id.lvResult);
        //        m_lvResult.setOnItemClickListener(this);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnQueryAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvStartDate:
                MyDatePicker.ShowDatePicker(QueryReformLogActivity.this, tvStartDate);
                break;

            case R.id.tvEndDate:
                MyDatePicker.ShowDatePicker(QueryReformLogActivity.this, tvEndDate);
                break;

            case R.id.tvQueryReformBack:
                finish();
                break;
            //查询
            case R.id.btnQuery:
                m_queryReformLogPresenter.query();
                break;
            //查询全部
            case R.id.btnQueryAll:
                m_queryReformLogPresenter.queryAll();
                break;
            default:
                break;
        }
    }

    @Override
    public void setLogId() {
        edtLogID.setText(String.valueOf(System.currentTimeMillis()).substring(5));
    }

    @Override
    public String getSection() {
        return "";
    }

    @Override
    public void setSection() {

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
            return DateTimeUtils.stringToDate(tvStartDate.getText().toString()+
                    " 00:00:00", DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Date getEndDate() {
        try {
            return DateTimeUtils.stringToDate(tvEndDate.getText().toString()+
                    " 23:59:59",DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRecorder() {
        if (edtRecorder.getText().toString().equals("")) {
            return UserSingleton.getUserInfo().getRealName();
        } else {
            return edtRecorder.getText().toString();
        }
    }

    @Override
    public String getProjectRole() {
        return null;
    }

    @Override
    public String getRiskId() {
        return edtRiskID.getText().toString();
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
            m_resultLists = (List<ReformLogInfo>) logList;
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
        MyToast.showMyToast(this,msg, Toast.LENGTH_SHORT);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ReformLogInfo item = m_resultLists.get(position);
//        Bundle _bundle = new Bundle();
//        _bundle.putString("LogID", item.getLogId());
//        _bundle.putString("StakeNum", item.getStakeNum());
//        _bundle.putString("RiskType", item.getRiskType());
//        _bundle.putString("recorder", item.getRecorder());
//        _bundle.putString("account", item.getAccount());
//        _bundle.putString("title", item.getNoticeTitle());
//        _bundle.putString("date", DateTimeUtils.dateToString(item.getSaveTime(), "yyyy年MM月dd日"));
//        //        _bundle.putByteArray("picture", item.getPicture());
//        _bundle.putString("detail", item.getDetails());
//        startActivity(new Intent(QueryReformLogActivity.this, ReformLogInfoActivity.class).putExtras(_bundle));
//    }
}
