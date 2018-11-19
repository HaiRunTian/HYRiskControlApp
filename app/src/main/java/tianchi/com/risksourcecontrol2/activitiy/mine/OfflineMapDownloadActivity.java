package tianchi.com.risksourcecontrol2.activitiy.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.MyBaseAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;

/** 
 *  @描述 离线地图下载
 *  @作者  Kevin.
 *  @创建日期 2017/11/10  14:08.
 */
public class OfflineMapDownloadActivity extends BaseActivity implements MyBaseAdapter.MyCallBack, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String m_MapNames[] = {"全国地图","广东省","湖南省","湖北省"};
    private List<String> m_list;

    private ListView m_listView;
    private TextView m_tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map_download);

        m_listView = (ListView) findViewById(R.id.lvOffLineMapDownload);
        m_list = new ArrayList<String>();
        for (int i = 0; i <m_MapNames.length ; i++) {
            m_list.add(m_MapNames[i]);
        }
        m_listView.setAdapter(new MyBaseAdapter(this,m_list,this));
        m_listView.setOnItemSelectedListener(this);

        m_tvBack = (TextView) findViewById(R.id.tvOffLineMapDownloadBack);
        m_tvBack.setOnClickListener(this);
    }

    @Override
    public void click(View view) {
//        Toast.makeText(this,"下载！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"下载！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

