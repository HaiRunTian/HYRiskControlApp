package tianchi.com.risksourcecontrol.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.PatrolSignInActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.RiskQueryActivity;

/**
 * Created by Kevin on 2018/3/19.
 */

public class MyPopupWindow extends PopupWindow {
    private View conentView;

    public MyPopupWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_popupwindow_menu, null, false);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        conentView.findViewById(R.id.item_riskQuery).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //do something you need here
                MyPopupWindow.this.dismiss();
                context.startActivity(new Intent(context, RiskQueryActivity.class));
            }
        });
        conentView.findViewById(R.id.item_patrolSignIn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // do something before signing out
                context.startActivity(new Intent(context, PatrolSignInActivity.class));
                MyPopupWindow.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);
        } else {
            this.dismiss();
        }
    }
}
