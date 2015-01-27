package liu.brandon.workouts.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import liu.brandon.workouts.R;

/**
 * Created by Brandon on 1/24/15.
 */
public class IconAdapter extends ArrayAdapter<String> {

    private final String TAG = "IconAdapter";

    private String[] items;

    private int mResource;

    private Context mContext;

    public IconAdapter(Context context, int resource) {
        super(context, resource);
    }

    public IconAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.items = objects;
        this.mResource = resource;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View mView = inflater.inflate(mResource, parent, false);
        TextView label = (TextView)mView.findViewById(R.id.drawer_item_text);
        ImageView image = (ImageView)mView.findViewById(R.id.drawer_item_image);

        label.setText(getItem(position));
        switch (position){
            case 0: image.setImageResource(R.drawable.ic_action_home);
                break;
            case 1: image.setImageResource(R.drawable.ic_action_schedule);
                break;
            case 2: image.setImageResource(R.drawable.ic_maps_directions_walk);
                break;
            case 3: image.setImageResource(R.drawable.ic_action_settings);
                break;
            case 4: image.setImageResource(R.drawable.ic_action_info_outline);

        }
        return mView;
    }

    public String getItem(int position){
        return items[position];
    }


}
