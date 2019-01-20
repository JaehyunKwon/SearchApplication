package com.example.searchapplication.View;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.searchapplication.R;

import com.example.searchapplication.Model.SearchInfo;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class SearchListFragment extends Fragment {
    private RecyclerView completedList;

    private ArrayList<SearchInfo> searchItems;

    public SearchListFragment() {

    }

    public SearchListFragment(ArrayList<SearchInfo> mItems) {
        searchItems = mItems;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        completedList = view.findViewById(R.id.list_recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        completedList.setLayoutManager(lm);

        completedList.setAdapter(new ThumbnailAdapter(getActivity(), searchItems));

        return view;
    }
}
