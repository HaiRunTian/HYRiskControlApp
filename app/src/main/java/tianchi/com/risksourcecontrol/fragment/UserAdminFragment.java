package tianchi.com.risksourcecontrol.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.LoginActivity;
import tianchi.com.risksourcecontrol.activitiy.mine.SettingActivity;
import tianchi.com.risksourcecontrol.activitiy.user.UserProfileActivity;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.CameraUtils;

/**
 * 用户管理
 */

public class UserAdminFragment extends Fragment implements View.OnClickListener{

    private TextView m_tvLogOut;
    private TextView m_tvSettings;
    private TextView m_tvUserName;
    private LinearLayout m_llUserProfilePInfo;
    private LinearLayout m_llUserProfileUpdate;
    private ImageView m_imgvUserHead;
    private String currentUser;
    private AlertDialog m_dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    void initView() {
        m_tvUserName = (TextView) getView().findViewById(R.id.tvUserName);
        m_tvLogOut = (TextView) getView().findViewById(R.id.tvLogOut);
        m_tvSettings = (TextView) getView().findViewById(R.id.tvSettings);
        m_llUserProfilePInfo = (LinearLayout) getView().findViewById(R.id.linearUserProfilePInfo);
        m_llUserProfileUpdate = (LinearLayout) getView().findViewById(R.id.linearUserProfileUpdate);
        m_imgvUserHead = (ImageView) getView().findViewById(R.id.imgvUserProfileHead);
        m_tvLogOut.setOnClickListener(this);
        m_tvSettings.setOnClickListener(this);
        m_llUserProfilePInfo.setOnClickListener(this);
        m_llUserProfileUpdate.setOnClickListener(this);
    }

    private void initValues() {
        m_tvUserName.setText(UserSingleton.getUserInfo().getRealName());
        String picBase64String = UserSingleton.getUserInfo().getPicture();
        if (!picBase64String.equals(""))
            m_imgvUserHead.setImageDrawable(CameraUtils.convertBytes2Drawable(CameraUtils.base642Bytes(picBase64String)));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        initValues();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_useradmin,container,false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogOut:
                m_dialog =  MyAlertDialog.showAlertDialog(getActivity(), "提示", "" +
                        "退出当前账号？", R.mipmap.ic_question, "确定", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent().setClass(getActivity(), LoginActivity.class);
                        intent.putExtra("isFromLogOut", true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_dialog!=null)
                            m_dialog.dismiss();
                    }
                });
                break;
            case R.id.linearUserProfilePInfo:
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                break;
            case R.id.tvSettings:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

}
