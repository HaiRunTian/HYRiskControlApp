package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.bean.risksource.BaseRiskInfo;

/**
 * 风险源列表适配器
 * Created by Kevin on 2018/1/10.
 */

public class RiskSourceListViewAdapter extends BaseAdapter {
    List<BaseRiskInfo> m_resultLists;
    LayoutInflater m_inflater;

    public RiskSourceListViewAdapter(Context context, List<BaseRiskInfo> m_resultLists) {
        this.m_resultLists = m_resultLists;
        m_inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.listview_item_risk_result, null);
            _holder.tvSerialNum = (TextView) convertView.findViewById(R.id.tvSerialNum);
            _holder.tvStakeNum = (TextView) convertView.findViewById(R.id.tvStakeNum);
            _holder.tvSeriousLevel = (TextView) convertView.findViewById(R.id.tvSeriousLevel);
            _holder.tvGrade = (TextView) convertView.findViewById(R.id.tvGrade);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        BaseRiskInfo item = m_resultLists.get(position);
//        _holder.tvSerialNum.setText(item.());
//        _holder.tvStakeNum.setText(item.getStakeNum());
//        _holder.tvSeriousLevel.setText(item.getSeriousLevel());
//        _holder.tvGrade.setText(item.getGrade());
        return convertView;
    }

    public class ViewHolder {
        public TextView tvSerialNum;
        public TextView tvStakeNum;
        public TextView tvSeriousLevel;
        public TextView tvGrade;
    }
}
