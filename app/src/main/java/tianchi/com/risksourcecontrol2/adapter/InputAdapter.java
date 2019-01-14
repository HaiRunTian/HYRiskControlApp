package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tianchi.com.risksourcecontrol2.R;

/**
 * Created by Kevin on 2018-12-27.
 */

public class InputAdapter extends BaseAdapter {
    private Context m_context;
    private LayoutInflater m_inflater;
    private String[] m_string;
    private OnItemClickListener mOnItemClickListener;

    public InputAdapter(Context context, String[] m_string)
    {
        this.m_context = context;
        this.m_inflater = LayoutInflater.from(context);
        this.m_string = m_string;
    }
    @Override
    public int getCount() {
        return m_string.length;
    }

    @Override
    public Object getItem(int position) {
        String _s = null;
        if (m_string!=null){
             _s = m_string[position];
        }
        return _s;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null) {
            convertView = m_inflater.inflate(R.layout.input_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }
        viewHolder.tvTitle.setText(m_string[position].toString());


        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClicked(position);
            }
        });
        return convertView;
    }
    private static class ViewHolder
    {
        TextView tvTitle;
        ViewHolder(View view){
            tvTitle = ((TextView) view.findViewById(R.id.tvTitle));
        }

    }
    /**
     * 选择条目点击监听器接口
     *
     * @author Jone
     */
    public interface OnItemClickListener
    {
        public void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }
}
