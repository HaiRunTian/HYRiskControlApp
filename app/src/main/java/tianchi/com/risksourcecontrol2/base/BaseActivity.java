package tianchi.com.risksourcecontrol2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tianchi.com.risksourcecontrol2.util.ActivityCollector;

/**
 *  @描述 活动基类
 *  @作者  kevin蔡跃.
 *  @创建日期 2017/11/4  12:06.
 */

public  abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Log.d("BaseActivity",getClass().getSimpleName()+"该活动已添加");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.d("BaseActivity",getClass().getSimpleName()+"该活动已移除");
    }

    protected final  <T> T $(int id){
        return (T)findViewById(id);
    }

}
