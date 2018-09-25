package tianchi.com.risksourcecontrol.custom;

import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

/**
 *  @描述 自定义控件显示
 *  @作者  kevin蔡跃.
 *  @创建日期 2017/11/4  13:21.
 */

public class CustomClearButton  {

    //编辑框右侧清除按钮
    public static void setClearButtonVisible(final EditText editText, final ImageView imageView) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText != null && imageView != null) {
                    if (editText.getText().length() > 0) {
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        imageView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //密码明文或密文显示
    public static void setPasswordVisible(final EditText editText, final CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (editText != null && checkBox != null) {
                    if (isChecked) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        editText.setSelection(editText.getText().toString().length());
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        editText.setSelection(editText.getText().toString().length());
                    }
                }
            }
        });
    }

}
