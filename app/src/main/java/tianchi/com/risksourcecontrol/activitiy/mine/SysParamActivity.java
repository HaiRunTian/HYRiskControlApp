package tianchi.com.risksourcecontrol.activitiy.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.custom.MyToast;

public class SysParamActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Switch m_switch;
    private Button m_btnClearChache;
    private Button m_btnClearPicChache;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_param);
        initEvent();
        MyToast.showMyToast(this, "该功能尚未开发!", Toast.LENGTH_SHORT);
    }

    private void initEvent() {
        m_btnClearChache = (Button) findViewById(R.id.btnClearChache);
        m_btnClearChache.setOnClickListener(this);
        m_btnClearPicChache = (Button) findViewById(R.id.btnClearPicChache);
        m_btnClearPicChache.setOnClickListener(this);
        m_switch = (Switch) findViewById(R.id.swtich_MessageAlert);
        m_switch.setOnCheckedChangeListener(this);
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnClearChache:

                break;
            case R.id.btnClearPicChache:

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (m_switch.isChecked()) {
            MyToast.showMyToast(SysParamActivity.this, "消息提示已开", Toast.LENGTH_SHORT);
        } else {
            MyToast.showMyToast(SysParamActivity.this, "消息提示已关", Toast.LENGTH_SHORT);
        }
    }
}
