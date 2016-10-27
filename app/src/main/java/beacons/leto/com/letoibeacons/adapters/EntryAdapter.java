package beacons.leto.com.letoibeacons.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.utils.MyUtils;

/**
 * Created by Renzo on 29/02/16.
 */
public class EntryAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private List<List<TimeEntry>> mDataset;
    private List<String> mHeaders;

    public EntryAdapter(List<List<TimeEntry>> myDataset, List<String> sections) {
        mHeaders = sections;
        mDataset = myDataset;
    }


    @Override
    public int getSectionCount() {
        return mDataset.size();
    }

    @Override
    public int getItemCount(int section) {
        return mDataset.get(section).size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int section) {
        HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        holder.title.setText(mHeaders.get(section));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int section, int relativePosition, int absolutePosition) {
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        TimeEntry tmp=mDataset.get(section).get(relativePosition);
        holder.description.setText(tmp.getDescription());
        if (tmp.getProjectObj()!=null){
            String projectName;
            if (tmp.getProjectObj()!=null){
                projectName=tmp.getProjectObj().getName();
            }else{
                projectName="-";
            }
            holder.project.setText(projectName);
            holder.project.setBackgroundColor(Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(tmp.getProjectObj().getColor()))));
            MyUtils.applyRoundedCorners(holder.project);
        }
        holder.duration.setText(tmp.getDurationString());
        MyUtils.setupDurationTV(holder.duration);
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1)
            return 0; // VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1. You can return 0 or greater.
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.header_test;
                View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                return new HeaderViewHolder(v);
            case VIEW_TYPE_ITEM:
                layout = R.layout.time_entry_row;
                break;
            default:
                layout = R.layout.time_entry_row;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ItemViewHolder(v);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView project;
        public TextView duration;


        public ItemViewHolder(View v) {
            super(v);
            description = (TextView) v.findViewById(R.id.descriptionTV);
            project = (TextView) v.findViewById(R.id.projectTV);
            duration = (TextView) v.findViewById(R.id.durationTV);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
