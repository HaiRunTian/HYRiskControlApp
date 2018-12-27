package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.bean.HistoryInfo;

/**
 * Created by Kevin on 2018-12-13.
 */

public class SelectListAdapter extends BaseAdapter {
    private ArrayList<HistoryInfo> mAccounts ;
    private LayoutInflater mInflater;

    private Context m_context;

    public SelectListAdapter(Context context, ArrayList<HistoryInfo> items)
    {
        this.m_context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAccounts = items;
    }


    @Override
    public int getCount() {
        return mAccounts.size();
    }

    @Override
    public Object getItem(int position) {
        if (mAccounts!=null){
            mAccounts.get(position);
        }
        return mAccounts;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = mInflater.inflate(R.layout.down_accont_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=((ViewHolder) convertView.getTag());
        }

        viewHolder.accountText.setText(mAccounts.get(position).getName());

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClicked(position);

            }
        });

        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDelBtnClickListener.onDelBtnClicked(position);
            }
        });
        return convertView;
    }
    private static class ViewHolder
    {
        private final RelativeLayout item;
        ImageView deleteImg;
        TextView accountText;

        ViewHolder(View view){
            item = ((RelativeLayout) view.findViewById(R.id.item));
            accountText = ((TextView) view.findViewById(R.id.accountText));
            deleteImg = ((ImageView) view.findViewById(R.id.deleteImg));
        }

    }

    /**
     * 选择条目点击监听器接口
     *
     * @author Jone
     */
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        public void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 删除按钮点击监听器接口
     *
     * @author Jone
     */
    private OnDelBtnClickListener mOnDelBtnClickListener;

    public interface OnDelBtnClickListener
    {
        public void onDelBtnClicked(int position);
    }

    public void setOnDelBtnClickListener(OnDelBtnClickListener onDeleteBtnClickListener)
    {
        mOnDelBtnClickListener = onDeleteBtnClickListener;
    }



}
