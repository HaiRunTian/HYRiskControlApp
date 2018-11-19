package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.ReceviceNoticeAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;

/**
 * Created by hairun.tian on 2018/6/19 0019.
 * <p>
 * 整改通知单列表
 */

public class RectifyNotifyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<RectifyNotifyInfo> m_list;
    private ProgressDialog m_progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rectify_noticfy_list_activiaty);
        initView();
    }

    private void initView() {
        TextView _textView = $(R.id.tvTitles);
        _textView.setText("通知单列表");
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        m_progressDialog = new ProgressDialog(RectifyNotifyListActivity.this);
        m_progressDialog.setMessage("整改通知单正在加载中，请稍等…");
        m_progressDialog.setCancelable(true);
        //依据接收者名字
        Bundle _bundle = getIntent().getExtras();
        String string = _bundle.getString("data");

        m_list = GsonUtils.jsonToArrayBeans(string, "data", RectifyNotifyInfo.class);

        MyToast.showMyToast(RectifyNotifyListActivity.this,"共有"+m_list.size()+"条通知", Toast.LENGTH_SHORT);
//        LogUtils.i("照片名字"+s);
        m_lvReceive.setAdapter(new ReceviceNoticeAdapter(RectifyNotifyListActivity.this, m_list));

        m_progressDialog.setMessage("加载完成");
        m_progressDialog.dismiss();
        m_lvReceive.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //点击item,携带数据跳转到通知详情界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();
        RectifyNotifyInfo _rectifyNotifyInfo = m_list.get(position);
//        String _s =  readData.getDataTime();
        _bundle.putSerializable("data", _rectifyNotifyInfo);
        Intent _intent = new Intent(RectifyNotifyListActivity.this, ReadRectifyNotifyInfoActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
