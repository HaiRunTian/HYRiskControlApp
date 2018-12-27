package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;

/**
 * listview项适配器
 * Created by Kevin on 2018/3/24.
 */

public class ListviewItemBaseAdapter extends BaseAdapter {
    private LayoutInflater m_inflater;
    private List<String> m_list;
    private int position = 0;
    public ListviewItemBaseAdapter(Context context, List<String> list) {
        m_list = list;
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_list.size();
    }
    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_item_listview_textview, null);
            _holder.m_textView = (TextView) convertView.findViewById(R.id.tvItemText);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.m_textView.setText((CharSequence) m_list.get(position));
        return convertView;
    }
    public void setSelectItem(int position) {
        this.position = position;
    }

    public int getSelectItem() {
        return position;
    }

    class ViewHolder {
        TextView m_textView;
    }
}
