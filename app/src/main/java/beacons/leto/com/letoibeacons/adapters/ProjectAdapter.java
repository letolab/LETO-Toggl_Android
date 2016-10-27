package beacons.leto.com.letoibeacons.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.activities.NewTaskActivity;
import beacons.leto.com.letoibeacons.models.Project;
import beacons.leto.com.letoibeacons.models.TimeEntry;
import beacons.leto.com.letoibeacons.utils.MyUtils;

/**
 * Created by Renzo on 29/02/16.
 */
public class ProjectAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private List<List<Project>> mDataset;
    private List<String> mHeaders;
    AppCompatActivity mParentActivity;

    public ProjectAdapter(List<List<Project>> myDataset, List<String> sections, AppCompatActivity parentActivity) {
        mHeaders = sections;
        mDataset = myDataset;
        mParentActivity = parentActivity;
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
        final Project tmp=mDataset.get(section).get(relativePosition);
        holder.title.setText(tmp.getName());
        holder.title.setBackgroundColor(Color.parseColor(MyUtils.getColorFromIndex(Integer.valueOf(tmp.getColor()))));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = mParentActivity.getIntent();
                resultIntent.putExtra("project", tmp);
                mParentActivity.setResult(mParentActivity.RESULT_OK, resultIntent);
                mParentActivity.finish();
            }
        });
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
                layout = R.layout.simple_text;
                View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                return new HeaderViewHolder(v);
            case VIEW_TYPE_ITEM:
                layout = R.layout.simple_text;
                break;
            default:
                layout = R.layout.simple_text;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ItemViewHolder(v);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ItemViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.titleTV);
            title.setTextColor(Color.WHITE);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleTV);
            title.setTextSize(12);
        }
    }
}
