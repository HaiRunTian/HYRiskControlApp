package tianchi.com.risksourcecontrol2.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;


/**
 * Created by hairun.tian on 2018/1/16 0016.
 * 技术交底列表适配器
 */

public class TecDisFromSDAdapter extends BaseAdapter {


    ViewHolder _viewHolder = null;
    List<String> m_list;
    private Context m_context;
    private String end;

    public TecDisFromSDAdapter(List<String> list) {
        this.m_list = list;

//        this.m_callBack =callBack;
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
        if (convertView == null) {
            _viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_download, null);
            _viewHolder.m_imageView = (ImageView) convertView.findViewById(R.id.imgHead);
            _viewHolder.m_textView = (TextView) convertView.findViewById(R.id.tvTieleName);
            _viewHolder.mTvFrom = (TextView) convertView.findViewById(R.id.tvformName);
            _viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tvtime);
            convertView.setTag(_viewHolder);
        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }

        String fileName = m_list.get(position);
        _viewHolder.m_textView.setText(fileName);


        if (fileName.contains(".")) {
            end = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
//        String end = m_list.get(position).toLowerCase();


        if (end.endsWith("doc") || end.endsWith("docx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_word_72px);
        } else if (end.endsWith("xls") || end.endsWith("xlsx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_excel_72px);
        } else if (end.endsWith("pdf")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_pdf_72px);
        } else if (end.endsWith("txt")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_txt_72px);
        } else if (end.endsWith("ppt") || end.endsWith("pptx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_ppt_72px);
        } else if (end.endsWith("zip") || end.endsWith("rar")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_zip_72px);

        } else if (end.endsWith("jpg") || end.endsWith("png") || end.endsWith("jepg") || end.endsWith("bmp")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_picture_72x);
        } else {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_unknown_72px);
        }

        return convertView;
    }

    public class ViewHolder {
        TextView m_textView;
        ImageView m_imageView;
        TextView mtvFileSize;
        TextView mTvFrom;
        TextView mTvTime;
    }


}
