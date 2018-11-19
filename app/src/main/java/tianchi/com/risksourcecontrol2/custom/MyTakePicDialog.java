package tianchi.com.risksourcecontrol2.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import tianchi.com.risksourcecontrol2.R;

/**
 * 自定义拍照窗口
 * Created by Kevin on 2017/12/21.
 */

public class MyTakePicDialog  implements View.OnClickListener{

    public OnItemClickListener m_itemClickListener;

    public AlertDialog showTakePicDialog(Context context) {
        LayoutInflater _inflater = LayoutInflater.from(context);
        View _view = _inflater.inflate(R.layout.layout_takepicture, null);
        _view.findViewById(R.id.btnTakePicture).setOnClickListener(this);
        _view.findViewById(R.id.btnPickFromAlbum).setOnClickListener(this);
        _view.findViewById(R.id.btnCancel).setOnClickListener(this);
        _view.findViewById(R.id.btnViewPicture).setOnClickListener(this);

        AlertDialog.Builder _builder = new AlertDialog.Builder(context);
        _builder.setView(_view);
        _builder.setCancelable(true);
        AlertDialog _dialog = _builder.create();
        return _dialog;
    }

    @Override
    public void onClick(View v) {
        if (m_itemClickListener != null) {
            m_itemClickListener.setOnItemClick(v);
        }
    }

    public interface OnItemClickListener{
        void setOnItemClick(View v);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.m_itemClickListener = itemClickListener;
    }
}
