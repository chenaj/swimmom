package swim.swimmom;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Smooth on 2/25/2015.
 */
public class SwimmerAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> swimmersCategory;
    private List<String> eventList;
    public ArrayList<ArrayList<Integer>> check_states = new ArrayList<ArrayList<Integer>>();

    public SwimmerAdapter(Context ctx, HashMap<String, List<String>> swimmersCategory,
                          List<String> eventList)
    {
        this.ctx = ctx;
        this.swimmersCategory = swimmersCategory;
        this.eventList = eventList;
    }

    @Override
    public int getGroupCount() {
        return eventList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return swimmersCategory.get(eventList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return eventList.get(groupPosition);
    }

    @Override
    public Object getChild(int parent, int child) {

        return swimmersCategory.get(eventList.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int parent, int child) {

        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {

        String groupTitle = getGroup(parent).toString();
        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.parent_layout, parentView, false);
        }

        TextView parentText = (TextView) convertView.findViewById(R.id.parent_txt);
        parentText.setTypeface(null, Typeface.BOLD);
        parentText.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int parent, int child, boolean lastChild, View convertView, ViewGroup parentView) {

        String childTitle = (String) getChild(parent, child);
        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.child_layout, parentView, false);
        }
        CheckedTextView childTextView = (CheckedTextView) convertView.findViewById(R.id.childTextView);
        childTextView.setText(childTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {


        return true;
    }
}
