package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;

/**
 * 自定义监理listview子项复选框适配器
 * Created by Kevin on 2018/3/22.
 */

public class SupervisorListviewAdapter extends BaseAdapter {
    // 填充数据的list
    private List<String> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater;

    // 构造器
    public SupervisorListviewAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<>();
        initData();
    }

    // 初始化isSelected的数据,全部置为未选
    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item_listview_checkbox, null);
            _holder.tvItem = (TextView) convertView.findViewById(R.id.tvItemText);
            _holder.cbItem = (CheckBox) convertView.findViewById(R.id.cbItemCheckbox);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.tvItem.setText(list.get(position));
        _holder.cbItem.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SupervisorListviewAdapter.isSelected = isSelected;
    }

    public class ViewHolder {
        public TextView tvItem;//选项左侧文本
        public CheckBox cbItem;//选项右侧复选框
    }
}
