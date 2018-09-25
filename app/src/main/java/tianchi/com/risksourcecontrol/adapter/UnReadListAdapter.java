package tianchi.com.risksourcecontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.newnotice.NotifyMessagesInfo;

/**
 * Created by HaiRun on 2018-09-15 17:11.
 */

public class UnReadListAdapter extends BaseAdapter {
    private Context m_context;
    private List<NotifyMessagesInfo> m_list;

    public UnReadListAdapter(Context context, List<NotifyMessagesInfo> list) {
        m_context = context;
        m_list = list;
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
        if (convertView == null) {
            _viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(m_context).inflate(R.layout.list_view_item_notice, null);
            _viewHolder.m_tvAuther = (TextView) convertView.findViewById(R.id.tvAuther);
            _viewHolder.m_tvTime = (TextView) convertView.findViewById(R.id.tvtime);
            _viewHolder.m_tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            _viewHolder.m_tvIsRead = (TextView) convertView.findViewById(R.id.tv_is_read);

            convertView.setTag(_viewHolder);
        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }

        _viewHolder.m_tvAuther.setText("");
        _viewHolder.m_tvTitle.setText(m_list.get(position).getRemark());
//        String _s = m_readDataList.get(position).getCheckedTime();

        if (m_list.get(position).getSubmitTime().length() > 10) {
            _viewHolder.m_tvTime.setText(m_list.get(position).getSubmitTime().substring(0, 10));
        } else {
            _viewHolder.m_tvTime.setText(m_list.get(position).getSubmitTime()+"");
        }
        return convertView;
    }

    class ViewHolder {
        TextView m_tvTitle;
        TextView m_tvAuther;
        TextView m_tvTime;
        TextView m_tvIsRead;
    }
}
