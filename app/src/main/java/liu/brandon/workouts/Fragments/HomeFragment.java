package liu.brandon.workouts.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import liu.brandon.workouts.Adapters.HomeAdapter;
import liu.brandon.workouts.R;

/**
 * Created by Brandon on 12/13/14.
 */
public class HomeFragment extends Fragment{

    private Context mContext;
    private final static String TAG = "HomeFragment";
    private final static String URL = "http://www.bodybuilding.com/rss/articles";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private HomeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        mContext = getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initSwipeLayout(view);
        initList(view);

        return view;
    }

    private void initSwipeLayout(View view) {

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*Toast.makeText(mContext, "Refreshing", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }, 1000); */

                refreshContent();

            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_dark, R.color.accent, R.color.primary_dark, R.color.accent);

    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getNewTweets());
                //mListView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    private void initList(View view){

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        mAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

}
