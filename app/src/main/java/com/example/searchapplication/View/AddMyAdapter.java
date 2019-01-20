package com.example.searchapplication.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.searchapplication.Model.SearchInfo;
import com.example.searchapplication.R;

import java.util.ArrayList;

public class AddMyAdapter extends RecyclerView.Adapter<AddMyAdapter.ItemViewHolder> {

    Context context;
    ArrayList<SearchInfo> mItems;

    public AddMyAdapter(FragmentActivity activity, ArrayList<SearchInfo> addItems) {
        this.context = activity;
        mItems = addItems;
    }

    // 새로운 뷰 홀더 생성
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_thumbnail_view, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.mNameTv.setText(mItems.get(position).getDatetime());
        itemViewHolder.mNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "눌렀구나~~", Toast.LENGTH_SHORT).show();
            }
        });
        if (mItems.get(position).getThumbnail_url() != null) {
            Glide.with(context).load(mItems.get(position).getThumbnail_url()).into(itemViewHolder.mThumbIv);
        } else {
            Glide.with(context).load(mItems.get(position).getThumbnail()).into(itemViewHolder.mThumbIv);
        }
    }

    // 데이터 사이즈
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mNameTv;
        private ImageView mThumbIv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.itemNameTv);
            mThumbIv = itemView.findViewById(R.id.itemThumbIv);
        }
    }
}
