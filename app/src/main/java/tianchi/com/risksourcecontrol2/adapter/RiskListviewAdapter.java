package tianchi.com.risksourcecontrol2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import tianchi.com.risksourcecontrol2.R;

/**
 * Created by hairun.tian on 2018/1/24 0024.
 */

public class RiskListviewAdapter extends BaseAdapter {

    private List<String> m_list = new ArrayList<>();

    public RiskListviewAdapter(List<String> list) {
        this.m_list = list;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _viewHolder = null;
        if (convertView == null){
            _viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_risk_source,null);
            _viewHolder.m_textView = (TextView) convertView.findViewById(R.id.tvlistitem);
            convertView.setTag(_viewHolder);
        }else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }
        _viewHolder.m_textView.setText(m_list.get(position));

        return convertView;
    }

    class ViewHolder{
        TextView m_textView;
    }
}
