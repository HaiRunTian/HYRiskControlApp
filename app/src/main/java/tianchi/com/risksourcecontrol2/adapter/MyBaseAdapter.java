package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;

/**
 * @描述 地图下载列表适配器
 * @作者 Kevin.
 * @创建日期 2017/11/10  15:46.
 */

public class MyBaseAdapter extends BaseAdapter implements View.OnClickListener {
    private List<String> m_list;
    private LayoutInflater m_inflater;
    private MyCallBack m_callBack;

    @Override
    public void onClick(View v) {
        m_callBack.click(v);
    }

    public interface MyCallBack {
        public void click(View view);
    }

    public MyBaseAdapter(Context context, List<String> datas, MyCallBack callBack) {
        m_list = datas;
        m_inflater = LayoutInflater.from(context);
        m_callBack = callBack;
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
        ViewHolder _holder;
        if (convertView == null) {
            _holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.listview_item_offmap, null);
            _holder.tvName = (TextView) convertView.findViewById(R.id.tvMapName);
            _holder.tvDownload = (TextView) convertView.findViewById(R.id.tvMapDownload);
            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.tvName.setText(m_list.get(position));
        _holder.tvDownload.setOnClickListener(this);
        _holder.tvDownload.setTag(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tvName;
        public TextView tvDownload;
    }
}
