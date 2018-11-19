package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.bean.notice.CheckFile;
import tianchi.com.risksourcecontrol2.util.LogUtils;

/**
 * Created by hairun.tian on 2018/3/24 0024.
 */

public class CheckFileAdapter extends BaseAdapter {
    private Context m_context;
    private List<CheckFile> m_list;
//    private int temp = -1;
    private String m_end;

    public CheckFileAdapter(Context context , List<CheckFile> list) {
        this.m_context = context;
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
        ViewHolder _viewHolder;
        if (convertView == null){
            _viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(m_context).inflate(R.layout.list_view_item_check_file,null);
            _viewHolder.m_checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            _viewHolder.m_textView = (TextView) convertView.findViewById(R.id.text);
            _viewHolder.m_imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(_viewHolder);
        }else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }

        String fileName = m_list.get(position).getFileName();
        int leng = m_list.size();

//        LogUtils.i("集合长度", String.valueOf(leng));
//        LogUtils.i("文件长度",fileName);
        if (fileName.contains(".")) {
            m_end = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        if (m_end.endsWith("doc") || m_end.endsWith("docx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_word_72px);
        } else if (m_end.endsWith("xls") || m_end.endsWith("xlsx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_excel_72px);
        } else if (m_end.endsWith("pdf")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_pdf_72px);
        } else if (m_end.endsWith("txt")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_txt_72px);
        } else if (m_end.endsWith("ppt") || m_end.endsWith("pptx")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_ppt_72px);
        } else if (m_end.endsWith("zip") || m_end.endsWith("rar")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_zip_72px);
        } else if (m_end.endsWith("jpg") || m_end.endsWith("png") || m_end.endsWith("jepg") || m_end.endsWith("bmp")) {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_picture_72x);
        } else {
            _viewHolder.m_imageView.setImageResource(R.mipmap.ic_unknown_72px);
        }
        _viewHolder.m_textView.setText(m_list.get(position).getFileName());

        if (m_list.get(position).isCheck()){
            _viewHolder.m_checkBox.setChecked(true);
        }else {
            _viewHolder.m_checkBox.setChecked(false);
        }
//        _viewHolder.m_imageView.setImageResource();
        return convertView;
    }


   public class ViewHolder{
        TextView m_textView;
        ImageView m_imageView;
        CheckBox m_checkBox;
    }
}
