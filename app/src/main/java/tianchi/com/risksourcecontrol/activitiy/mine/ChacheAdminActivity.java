package tianchi.com.risksourcecontrol.activitiy.mine;

import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;

public class ChacheAdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chache_admin);
        setWindowsSize();
    }

    private void setWindowsSize() {
        //固定窗口大小
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.85);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        getWindow().setAttributes(p);
    }
}
