package liu.brandon.workouts.Adapters;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import liu.brandon.workouts.Activities.MainActivity;
import liu.brandon.workouts.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{
    private String mDataSet[];

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recylerview_card, viewGroup, false);
        ViewHolder mViewHolder = new ViewHolder(v);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTitleView.setText(mDataSet[i]);
        viewHolder.mDateView.setText(mDataSet[i]);
        viewHolder.mDescriptionView.setText(mDataSet[i]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitleView;
        public TextView mDateView;
        public TextView mDescriptionView;
        public ViewHolder(View v){
            super(v);
            this.mTitleView = (TextView) v.findViewById(R.id.TitleView);
            this.mDateView = (TextView) v.findViewById(R.id.DateView);
            this.mDescriptionView = (TextView) v.findViewById(R.id.DescriptionView);

        }


    }

}
