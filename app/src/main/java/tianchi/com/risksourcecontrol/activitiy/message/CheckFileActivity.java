package tianchi.com.risksourcecontrol.activitiy.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.adapter.CheckFileAdapter;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.notice.CheckFile;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;

/**
 * Created by hairun.tian on 2018/3/24 0024.
 */

public class CheckFileActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView tvClose;
    private ListView lvFile;
    private List<CheckFile> listFile = new ArrayList<>();
    private CheckFileAdapter m_adapter;
    private TextView btnSubmit;
    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_file);
        initView();
    }

    private void initView() {
        tvClose = (TextView) findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
        lvFile = (ListView) findViewById(R.id.lv_file);
        btnSubmit = (TextView) findViewById(R.id.tvSubmit);
        btnSubmit.setOnClickListener(this);
        checkFile();
        if (listFile.size() != 0) {
            m_adapter = new CheckFileAdapter(CheckFileActivity.this, listFile);
            lvFile.setAdapter(m_adapter);
            lvFile.setOnItemClickListener(this);
        } else {
            MyToast.showMyToast(CheckFileActivity.this, "文件夹中没有文件", Toast.LENGTH_SHORT);
        }

    }

    private void checkFile() {
        File _file = new File(FoldersConfig.SD_FILE);
        File[] fileArr = _file.listFiles();
        if (fileArr.length != 0) {
            for (int i = 0; i < fileArr.length; i++) {
                if (!(fileArr[i].getName().isEmpty()) && fileArr[i].getName().contains(".")) {
                    listFile.add(new CheckFile(fileArr[i].getName(), false));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                finish();
                break;



            case R.id.tvSubmit:
                if (fileName.isEmpty()) {
                    MyToast.showMyToast(CheckFileActivity.this, "请选择文件再确认", Toast.LENGTH_SHORT);
                } else {
                    Intent _intent = new Intent();
                    _intent.putExtra("file", fileName);
                    setResult(2, _intent);
                    finish();
                }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //每次点击都设置为false
        for (CheckFile _checkFile : listFile) {
            _checkFile.setCheck(false);
        }
        listFile.get(position).setCheck(true);
        m_adapter.notifyDataSetChanged();
        fileName = listFile.get(position).getFileName();

    }

}
