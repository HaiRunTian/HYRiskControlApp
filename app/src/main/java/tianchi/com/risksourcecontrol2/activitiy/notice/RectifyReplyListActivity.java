package tianchi.com.risksourcecontrol2.activitiy.notice;

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
import tianchi.com.risksourcecontrol2.adapter.RectifyReplyAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;

/**
 * Created by hairun.tian on 2018/6/19 0019.
 * <p>
 * 整改回复通单列表
 */

public class RectifyReplyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<RectifyReplyInfo> m_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rectify_noticfy_list_activiaty);
        initView();
    }

    private void initView() {
        TextView _textView = $(R.id.tvTitles);
        _textView.setText("回复通知单列表");
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        //依据接收者名字
        Bundle _bundle = getIntent().getExtras();
        String string = _bundle.getString("data");
//        String jsonString = GsonUtils.getNodeJsonString(string,"data");
//
//
//        JSONArray _jsonArray = null;
//        try {
//            _jsonArray = new JSONArray(jsonString);
//
//            for (int i = 0; i <_jsonArray.length(); i++) {
//                JSONObject beanObject = _jsonArray.getJSONObject(i);
//                RectifyNotifyInfo _rectifyNotifyInfo = GsonUtils.parseJson2Bean(beanObject, RectifyNotifyInfo.class);
//                    m_rectifyNotifyInfoList.add(_rectifyNotifyInfo);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        m_list = GsonUtils.jsonToArrayBeans(string, "data", RectifyReplyInfo.class);

        MyToast.showMyToast(RectifyReplyListActivity.this,"共有"+m_list.size()+"条通知", Toast.LENGTH_SHORT);
//       String s =  m_list.get(0).getImages();
//        LogUtils.i("照片名字"+s);
        m_lvReceive.setAdapter(new RectifyReplyAdapter(RectifyReplyListActivity.this, m_list));
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
        RectifyReplyInfo _rectifyNotifyInfo = m_list.get(position);
//        String _s =  readData.getDataTime();
        _bundle.putSerializable("data", _rectifyNotifyInfo);
        Intent _intent = new Intent(RectifyReplyListActivity.this, SeeReplyInfoActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
