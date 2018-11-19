package tianchi.com.risksourcecontrol2.activitiy.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.LogListiviewAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.ScreenUtils;

/**
 *  日志结果列表窗口
 *  @datetime 2018/1/31  16:03.
 *
 */
public class  LogResultListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView m_lvLogResult;
    private LogListiviewAdapter m_adapter;
    private List<? extends BaseLogInfo> m_list;//装日志数据的list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_result_list);
        initView();
        setWindowsSize();
        getBundleData();
    }

    private void setWindowsSize() {
        //固定窗口大小
        ScreenUtils.setWindowsSize(this,0.85,0.85,1.0f,0.0f);
    }

    private void initView() {
        m_lvLogResult = (ListView) findViewById(R.id.lvLogResult);
        m_lvLogResult.setOnItemClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getBundleData() {
        m_list = (List<? extends BaseLogInfo>) getIntent().getSerializableExtra("logList");
        m_adapter = new LogListiviewAdapter(this, m_list);
        m_lvLogResult.setAdapter(m_adapter);
        MyToast.showMyToast(this,"一共查询到"+m_list.size()+"条日志记录", Toast.LENGTH_SHORT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent _intent;
        String clsName = m_list.get(position).getClass().getSimpleName();
        if (clsName.equals(SafetyLogInfo.class.getSimpleName())) {
            _intent = new Intent(this, SafetyLogInfoActivity.class);
            _intent.putExtra("logInfo", m_list.get(position));
            startActivity(_intent);
        } else if (clsName.equals(ReformLogInfo.class.getSimpleName())) {
            _intent = new Intent(this, ReformLogInfoActivity.class);
            _intent.putExtra("logInfo", m_list.get(position));
            startActivity(_intent);
        } else if (clsName.equals(PatrolLogInfo.class.getSimpleName())) {
            _intent = new Intent(this, PatrolLogInfoActivity.class);
            _intent.putExtra("logInfo", m_list.get(position));
            startActivity(_intent);
        }
    }
}
