package cq.behaviordemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cq.behaviordemo.adapter.FriendInfoAdapter;

/**
 * Created by cqll on 2016/9/30.
 */

public class ItemFragment extends Fragment implements IsChildRequestScrollListener{

    private RecyclerView mRecyclerView;

    public static ItemFragment newInstance() {

        Bundle args = new Bundle();

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_item,container,false);
        mRecyclerView=(RecyclerView)v.findViewById(R.id.recycler_view);
        initView();
        return v;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new FriendInfoAdapter());
    }


    @Override
    public boolean requestScroll() {
        //不是在顶部
        return !(mRecyclerView!=null&&
                mRecyclerView.getAdapter()!=null&&
                mRecyclerView.getAdapter().getItemCount()>0&&
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0);
    }
}
