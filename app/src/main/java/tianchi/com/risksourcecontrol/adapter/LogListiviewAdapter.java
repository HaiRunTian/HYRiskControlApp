package tianchi.com.risksourcecontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.LogListTitleItems;
import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;

/**
 * 日志查询结果简要列表适配器
 * Created by Kevin on 2018/1/15.
 */

public class LogListiviewAdapter extends BaseAdapter{

    List<? extends BaseLogInfo> m_lists;
    LayoutInflater m_inflater;

    public LogListiviewAdapter(Context context, List<? extends BaseLogInfo> lists) {
        this.m_lists = lists;
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return m_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.listview_item_log_result, null);
            _holder.tvID = (TextView) convertView.findViewById(R.id.tvID);
            _holder.tvLogID = (TextView) convertView.findViewById(R.id.tvLogID);
            _holder.tvRecorder = (TextView) convertView.findViewById(R.id.tvRecorder);
            _holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        LogListTitleItems item = new LogListTitleItems();
        String clsName = m_lists.get(position).getClass().getSimpleName();
        if (clsName.equals(SafetyLogInfo.class.getSimpleName())) {
            SafetyLogInfo _logInfo = (SafetyLogInfo) m_lists.get(position);
            item.setId(_logInfo.getId());
            item.setLogId(_logInfo.getLogId());
            item.setRecorder(_logInfo.getRecorder());
            item.setDate(_logInfo.getSaveTime());
        } else if (clsName.equals(ReformLogInfo.class.getSimpleName())) {
            ReformLogInfo _logInfo = (ReformLogInfo) m_lists.get(position);
            item.setId(_logInfo.getId());
            item.setLogId(_logInfo.getLogId());
            item.setRecorder(_logInfo.getRecorder());
            item.setDate(_logInfo.getSaveTime());
        } else if (clsName.equals(PatrolLogInfo.class.getSimpleName())) {
            PatrolLogInfo _logInfo = (PatrolLogInfo) m_lists.get(position);
            item.setId(_logInfo.getId());
            item.setLogId(_logInfo.getLogId());
            item.setRecorder(_logInfo.getRecorder());
            item.setDate(_logInfo.getSaveTime());
        }
        _holder.tvID.setText(String.valueOf(item.getId()));
        _holder.tvLogID.setText(item.getLogId());
        _holder.tvRecorder.setText(item.getRecorder());
        _holder.tvDate.setText(DateTimeUtils.dateToString(item.getDate(), "yyyy-MM-dd"));
        return convertView;
    }


    public class ViewHolder {
        public TextView tvID;
        public TextView tvLogID;
        public TextView tvRecorder;
        public TextView tvDate;
    }
}
