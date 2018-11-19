package tianchi.com.risksourcecontrol2.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;

/**
 * 自定义弹窗
 */

public class MyAlertDialog extends AlertDialog {

    protected MyAlertDialog(Context context) {
        super(context);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, int iconId, String btYesText,
                                              String btCancelText,boolean cancelable, OnClickListener onOkClickListener,
                                              OnClickListener onCancelClickListener) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(iconId)
                .setMessage(message)
                .setCancelable(cancelable)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYesText,onOkClickListener)
                .setNegativeButton(btCancelText,onCancelClickListener)
                .create();
        _dialog.show();
        return _dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, String btYesText,
                                              String btCancelText,boolean cancelable ,OnClickListener onClickListener, OnClickListener cancelListener) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.ic_question)
                .setMessage(message)
                .setCancelable(cancelable)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYesText,onClickListener)
                .setNegativeButton(btCancelText,cancelListener)
                .create();
        _dialog.show();
        return _dialog;
    }
    public static AlertDialog showAlertDialog(Context context, String title, String message, String btYesText,
                                              String btCancelText, OnClickListener onClickListener ) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.mipmap.ic_question)
                .setMessage(message)
                .setCancelable(false)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYesText,onClickListener)
                .setNegativeButton(btCancelText,null)
                .create();
        _dialog.show();
        return _dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setInverseBackgroundForced(true)
                .create();
        _dialog.show();
        return _dialog;
    }

    public static AlertDialog editTextDialog(Context context, String title, String message,List<String> list,
                                                 int iconId, View.OnClickListener okClickListener,
                                                 View.OnClickListener cancelClickListener) {
        View view = View.inflate(context, R.layout.layout_push_log, null);
        AutoCompleteTextView autoTvAccount = (AutoCompleteTextView) view.findViewById(R.id.autoTvAccount);
        autoTvAccount.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list));
        Button btnPushLog = (Button) view.findViewById(R.id.btnPushLog);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnPushLog.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);

        AlertDialog.Builder builder = new MyAlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(iconId)
                .setMessage(message)
                .setCancelable(true)
                .setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String message, int iconId, String btYesText,String btnYesTest2,
                                              String btCancelText,boolean cancelable, OnClickListener onOkClickListener,OnClickListener onOkClickListener2,
                                              OnClickListener onCancelClickListener) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(iconId)
                .setMessage(message)
                .setCancelable(cancelable)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYesText,onOkClickListener)
                .setNeutralButton(btnYesTest2,onOkClickListener2)
                .setNegativeButton(btCancelText,onCancelClickListener)
                .create();
        _dialog.show();
        return _dialog;
    }
    public static AlertDialog showAlertDialog(Context context, String title, String btYseText, String btnCancelText,
                                              boolean cancelable, View view,OnClickListener onOkClickListener, OnClickListener onClickListener2){
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(cancelable)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYseText,onOkClickListener)
                .setNegativeButton(btnCancelText,onClickListener2)
                .setView(view)
                .create();
        _dialog.show();
        return _dialog;
    }


    public static AlertDialog showAlertDialog(Context context, String title, String btYseText, String btnCancelText,
                                              boolean cancelable, View view, String[] array, OnClickListener onClickListener, OnClickListener onOkClickListener, OnClickListener onClickListener2) {
        AlertDialog _dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(cancelable)
                .setInverseBackgroundForced(true)
                .setPositiveButton(btYseText, onOkClickListener)
                .setNegativeButton(btnCancelText, onClickListener2)
                .setSingleChoiceItems(array,0,onClickListener)
                .setView(view)
                .create();
        _dialog.show();
        return _dialog;
    }




}
