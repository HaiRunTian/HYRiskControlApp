package tianchi.com.risksourcecontrol2.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tianchi.com.risksourcecontrol2.R;

/**
 * 自定义二级菜单适配器
 */

public class MyExpandableListAdapter implements ExpandableListAdapter {

    private Context m_context;
    private int[][] m_imgId;
    private String[] m_groups;
    private String[][] m_childs;

    public MyExpandableListAdapter(Context context,String[] groups,String[][] childs,int[][] imgId) {
        m_context = context;
        m_groups = groups;
        m_childs = childs;
        m_imgId = imgId;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return m_groups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return m_childs[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return m_groups[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return m_childs[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(m_context).inflate(R.layout.expandablelistview_item_group,null,false);
        }
        TextView tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
        tvGroup.setText(m_groups[groupPosition]);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(m_context).inflate(R.layout.expandablelistview_item_child,null,false);
        }
        TextView tvChild = (TextView) convertView.findViewById(R.id.tvChild);
        tvChild.setText(m_childs[groupPosition][childPosition]);
        ImageView imgGroup = (ImageView) convertView.findViewById(R.id.imgvGroup);
        imgGroup.setImageResource(m_imgId[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
