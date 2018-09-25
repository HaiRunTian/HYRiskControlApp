package tianchi.com.risksourcecontrol.activitiy.message;

import android.os.Bundle;
import android.view.View;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;

public class NotificationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViewById(R.id.tvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
