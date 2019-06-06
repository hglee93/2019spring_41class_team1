package com.team.gajimarket.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.gajimarket.R;

import java.util.ArrayList;
import com.team.gajimarket.item.*;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(RecyclerItem item);
    }

    private final ArrayList<RecyclerItem> mItems;
    private final OnItemClickListener listener;

    public RecyclerAdapter(ArrayList<RecyclerItem> items, OnItemClickListener listener){
        this.mItems = items;
        this.listener = listener;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view,parent,false);
        return new ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //holder.tvName.setText(mItems.get(position).getName());
        //holder.tvPrice.setText(mItems.get(position).getPrice());
        //holder.tvSize.setText(mItems.get(position).getSize());
        holder.bind(mItems.get(position), listener);
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩합니다.
    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvSize;
        private RelativeLayout layoutItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvItemPrice);
            tvSize = (TextView) itemView.findViewById(R.id.tvItemSize);
            layoutItem = (RelativeLayout) itemView.findViewById(R.id.itemView);
        }
        public void bind(final RecyclerItem item, final OnItemClickListener listener) {
            tvName.setText(item.getName());
            tvPrice.setText(item.getPrice());
            tvSize.setText(item.getSize());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
