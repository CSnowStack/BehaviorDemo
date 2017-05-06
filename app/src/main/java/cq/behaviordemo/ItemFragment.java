package cq.behaviordemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cq.airbnb.IsChildRequestScrollListener;
import cq.behaviordemo.adapter.FriendInfoAdapter;

/**
 * 列表Fragment
 */

public class ItemFragment extends Fragment implements IsChildRequestScrollListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ItemFragment newInstance() {

        Bundle args = new Bundle();

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        initView();
        return v;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new FriendInfoAdapter());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }


    @Override
    public boolean requestScroll(boolean up,boolean shouldNotRefresh) {
        //向上滑动,并且 mRecyclerView 可以上滑动
        return (up && ViewCompat.canScrollVertically(mRecyclerView, 1)) ||
                //向下滑动,且可以下滑或者(可以刷新,且不在初始位置,不在顶部)
                (!up && (ViewCompat.canScrollVertically(mRecyclerView, -1) ||
                        (!shouldNotRefresh&&((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)));

    }

}
