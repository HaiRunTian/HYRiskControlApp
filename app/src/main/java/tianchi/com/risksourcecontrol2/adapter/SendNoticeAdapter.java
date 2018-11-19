package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.bean.notice.SendNotice;

/**
 * Created by hairun.tian on 2018/4/1 0001.
 */

public class SendNoticeAdapter extends BaseAdapter {

    private Context m_context;
    private List<SendNotice> m_readDataList;

    public SendNoticeAdapter(Context context, List<SendNotice> readDataList) {
        m_context = context;
        m_readDataList = readDataList;
    }

    @Override
    public int getCount() {
        return m_readDataList.size();
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
            convertView.setTag(_viewHolder);

        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }
        _viewHolder.m_tvAuther.setText("接收人：" + m_readDataList.get(position).getReceiveMans());
        _viewHolder.m_tvTitle.setText(m_readDataList.get(position).getTitleName());
        if (m_readDataList.get(position).getDataTime().length() > 10) {
            _viewHolder.m_tvTime.setText(m_readDataList.get(position).getDataTime().substring(0, 10));
        }

        return convertView;
    }


    class ViewHolder {
        TextView m_tvTitle;
        TextView m_tvAuther;
        TextView m_tvTime;
    }
}
