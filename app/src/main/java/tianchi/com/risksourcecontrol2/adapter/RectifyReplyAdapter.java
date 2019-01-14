package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;

/**
 * Created by hairun.tian on 2018/4/1 0001.
 */

public class RectifyReplyAdapter extends BaseAdapter {

    private Context m_context;
    private List<RectifyReplyInfo> m_readDataList;

    @Override
    public int getCount() {
        return m_readDataList.size();
    }

    public RectifyReplyAdapter(Context context, List<RectifyReplyInfo> readDataList) {
        m_context = context;
        m_readDataList = readDataList;
    }

    @Override
    public Object getItem(int position) {
        return m_readDataList.get(position);
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
        if (m_readDataList.get(position).isRead() !=1) {
//            _viewHolder.m_tvIsRead.setText("已读");
        } else {
//            _viewHolder.m_tvIsRead.setText("已审核");
        }
        _viewHolder.m_tvAuther.setText("发送人:" + m_readDataList.get(position).getRectifyManSign());
        _viewHolder.m_tvTitle.setText(m_readDataList.get(position).getLogId());

        String _s = m_readDataList.get(position).getCheckedTime();

        if (m_readDataList.get(position).getCheckedTime().length()>10) {
        _viewHolder.m_tvTime.setText(m_readDataList.get(position).getCheckedTime().substring(0,10));
        }else {
            _viewHolder.m_tvTime.setText(m_readDataList.get(position).getCheckedTime());
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
