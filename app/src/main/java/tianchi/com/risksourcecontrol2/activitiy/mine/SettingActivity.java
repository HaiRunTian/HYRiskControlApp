package tianchi.com.risksourcecontrol2.activitiy.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;

/**
 *  @描述 系统设置
 *  @作者  kevin蔡跃.
 *  @创建日期 2017/11/4  12:09.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView m_tvBack;
    private RelativeLayout m_offLineMapDownload;
    private RelativeLayout m_systemConfigSetting;
    private RelativeLayout m_developmentInfo;
    private RelativeLayout m_aboutApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        m_tvBack = (TextView) findViewById(R.id.tvSettingsBack);
        m_offLineMapDownload = (RelativeLayout) findViewById(R.id.offLineMapDownload);
        m_systemConfigSetting = (RelativeLayout) findViewById(R.id.systemConfigSetting);
        m_developmentInfo = (RelativeLayout) findViewById(R.id.developmentInfo);
        m_aboutApp = (RelativeLayout) findViewById(R.id.aboutApp);

        m_tvBack.setOnClickListener(this);
        m_offLineMapDownload.setOnClickListener(this);
        m_systemConfigSetting.setOnClickListener(this);
        m_developmentInfo.setOnClickListener(this);
        m_aboutApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.tvSettingsBack:
                finish();
                break;
            case R.id.offLineMapDownload:
                intent = new Intent().setClass(SettingActivity.this,OfflineMapDownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.systemConfigSetting:
                Toast.makeText(this,"待开发。。。",Toast.LENGTH_SHORT).show();
                break;
            case R.id.developmentInfo:
                intent = new Intent().setClass(SettingActivity.this,DevelopmentInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutApp:
                intent = new Intent().setClass(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
        }
    }
}
