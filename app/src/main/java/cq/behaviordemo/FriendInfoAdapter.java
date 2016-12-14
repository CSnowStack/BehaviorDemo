package cq.behaviordemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cqll on 2016/9/30.
 */

public class FriendInfoAdapter extends RecyclerView.Adapter<FriendInfoAdapter.ViewHolder>{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_info,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txt;
        public ViewHolder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.txt);
        }
    }

}
