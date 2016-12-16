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
import cq.behaviordemo.listener.IsChildRequestScrollListener;
import cq.behaviordemo.listener.NeedExpandListener;
import cq.behaviordemo.listener.SupportNeedExpendListener;

/**
 * Created by cqll on 2016/9/30.
 */

public class ItemFragment extends Fragment implements IsChildRequestScrollListener ,SupportNeedExpendListener{

    private RecyclerView mRecyclerView;
    private NeedExpandListener mNeedExpandListener;
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0&&((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0){
                    if(mNeedExpandListener!=null)
                        mNeedExpandListener.needExpand();
                }
            }
        });
    }


    @Override
    public boolean requestScroll() {
        if(mRecyclerView!=null&&
                mRecyclerView.getAdapter()!=null&&
                mRecyclerView.getAdapter().getItemCount()>0){
            //不是在顶部
            return !(((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0);
        }
        return false;

    }


    @Override
    public void setNeedExpendListener(NeedExpandListener listener) {
        mNeedExpandListener=listener;
    }

    @Override
    public NeedExpandListener getNeedExpendListener() {
        return mNeedExpandListener;
    }
}
