package beacons.leto.com.letoibeacons.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import beacons.leto.com.letoibeacons.R;
import beacons.leto.com.letoibeacons.utils.MyUtils;

/**
 * Created by Renzo on 27/04/16.
 */
public class ProjectLayout extends LinearLayout {

    Context globalContext;
    String projectName;
    Integer projectDuration;
    Integer projectColor;


    public ProjectLayout(Context context, String name, Integer duration, Integer color) {
        super(context);
        globalContext=context;
        projectName=name;
        projectColor=color;
        projectDuration=duration;
        init();
    }

    public ProjectLayout(Context context) {
        super(context);
        globalContext=context;
        init();
    }


    public ProjectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        globalContext=context;
        init();
    }

    public ProjectLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        globalContext=context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.project_layout, this);
        TextView projNameTV = (TextView) findViewById(R.id.nameTV);
        TextView projDurationTV = (TextView) findViewById(R.id.durationTV);
        if (projectName!=null){
            projNameTV.setText(projectName);
            projNameTV.setBackgroundColor(projectColor);
            projDurationTV.setText(MyUtils.splitToComponentTimes(projectDuration));
            MyUtils.setupDurationTV(projDurationTV);
        }
        MyUtils.applyRoundedCorners(projNameTV);
    }

}
