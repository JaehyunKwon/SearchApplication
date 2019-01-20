package com.example.searchapplication.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.searchapplication.R;

import com.example.searchapplication.Model.SearchInfo;
import com.example.searchapplication.util.PreferenceUtil;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {
    private String argDate;
    private String argThumbnail;

    private RecyclerView myAddList;

    private ArrayList<SearchInfo> addMyItems = new ArrayList<>();

    // You can modify the parameters to pass in whatever you want
    static MyFragment newInstance(String argDate, String argThumbnail) {
        MyFragment f = new MyFragment();
        Bundle args = new Bundle();
        args.putString("getDate", argDate);
        args.putString("getThumbnail", argThumbnail);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            argDate = getArguments().getString("getDate");
            argThumbnail = getArguments().getString("getThumbnail");

            Log.w("MyFragment", "######### Date :::: ==> " + argDate);
            Log.d("MyFragment", "######### Thumbnail ::: ==> " + argThumbnail);
        }

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        myAddList = view.findViewById(R.id.my_recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myAddList.setLayoutManager(lm);

        argDate = PreferenceUtil.getArgDate(getContext());
        argThumbnail = PreferenceUtil.getArgThumbnailImg(getContext());

        Log.w("MyFragment", "@@@@@@@ Date :::: ==> " + argDate);
        Log.i("MyFragment", "@@@@@@@ Thumbnail ::: ==> " + argThumbnail);

        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setDatetime(argDate);
        searchInfo.setThumbnail(argThumbnail);

        addMyItems.add(searchInfo);

        myAddList.setAdapter(new AddMyAdapter(getActivity(), addMyItems));
        Log.i("MyFragment", "######### onCreateView ######### ");

        return view;
    }

    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {

            for(String key : savedInstanceState.keySet()) {
                Log.w("MyFragment", "savedInstanceState key : " + key);

            }

        }


    }
}
