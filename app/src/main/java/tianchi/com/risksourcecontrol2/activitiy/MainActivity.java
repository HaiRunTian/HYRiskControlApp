package tianchi.com.risksourcecontrol2.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.ViewPagerAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
public class  MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{
    private ViewPager m_viewPager;//滑动页面控件
    private int index = 0;
    final int WHAT = 1;
    private Timer m_timer;
    private TimerTask m_task;
    private List<View> m_views;//装图片容器
    private int[] picsId = new int[]{R.drawable.img_banner1, R.drawable.img_banner2,
            R.drawable.img_banner3, R.drawable.img_banner4};//图片id数组
    private ViewPagerAdapter m_adapter;//滑动页面控件适配器
    private RadioButton radioButton, rdbtnHome,rdbtnMap;
    private RadioGroup radioGroup, radioGroupBottom;//radiobutton组
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    m_viewPager.setCurrentItem(index % 4);
                    index++;
                    break;
            }
        }
    };
    private long intervalTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rdbtnHome.setChecked(true);
    }

    @Override
    public void onBackPressed() {
//        Map<String, Object> _map = MyApplication.quitPrompt(this, intervalTime);
//        if (_map.get(MyApplication.KEY_IS_QUIT).equals(true)) {
//            super.onBackPressed();
//        } else {
//            intervalTime = (long) _map.get(MyApplication.KEY_INTERVAL);
//        }
    }

    private void initData() {
        m_views = new ArrayList<>();
        LinearLayout.LayoutParams _layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int picId : picsId) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(_layoutParams);
            iv.setImageResource(picId);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            m_views.add(iv);
        }
        m_adapter = new ViewPagerAdapter(m_views);
        m_viewPager.setAdapter(m_adapter);
        m_task = new TimerTask() {//定时任务
            @Override
            public void run() {
                Message _message = new Message();
                _message.what = WHAT;
                m_handler.sendMessage(_message);
            }
        };
        m_timer = new Timer();//定时器
        // 参数：
        // 1000，延时1秒后执行。
        // 2000，每隔2秒执行1次task。
        m_timer.schedule(m_task, 1000, 2500);
    }

    private void initView() {
        m_viewPager = (ViewPager) findViewById(R.id.viewPager);
        m_viewPager.setOnPageChangeListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroupBottom = (RadioGroup) findViewById(R.id.radioGroupBottom);
        radioGroupBottom.setOnCheckedChangeListener(this);
        rdbtnHome = (RadioButton) findViewById(R.id.rdbtHome);
        rdbtnMap = (RadioButton) findViewById(R.id.rdbtMap);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        radioButton = (RadioButton) radioGroup.getChildAt(position);
        radioButton.setChecked(true);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                m_viewPager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                m_viewPager.setCurrentItem(1);
                break;
            case R.id.radioButton3:
                m_viewPager.setCurrentItem(2);
                break;
            case R.id.radioButton4:
                m_viewPager.setCurrentItem(3);
                break;
            case R.id.rdbtMap:
                startActivity(new Intent(this, DrawerActivity.class));
                break;
            default:
                break;
        }
    }
}
