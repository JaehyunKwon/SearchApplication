package com.example.searchapplication.View;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.searchapplication.Model.SearchVO;
import com.example.searchapplication.Network.API;
import com.example.searchapplication.Network.NetworkConnections;
import com.example.searchapplication.R;
import com.example.searchapplication.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ActivityMainBinding mainBinding;

    private EditText m_keyword_et;
    private Button m_search_btn;

    private RecyclerView.Adapter adapter;

    private ArrayList<SearchVO> mItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        m_keyword_et = findViewById(R.id.et_keyword);
        m_search_btn = findViewById(R.id.btn_search);

        setRecyclerView();
        setRefresh();
    }

    private void setRecyclerView() {
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mainBinding.recyclerView.setHasFixedSize(true);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new ThumbnailAdapter(this, mItems);
        mainBinding.recyclerView.setAdapter(adapter);

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        m_search_btn.setOnClickListener(this);

    }

    /***
     * API 호출된 데이터 리스트 세팅
     */
    private void callDataList() {
        try {
            NetworkConnections networkConService = NetworkConnections.retrofit.create(NetworkConnections.class);
            Call<JsonObject> imageCall = networkConService.repoImage(API.APP_KEY, m_keyword_et.getText().toString());

            imageCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Gson gson = new Gson();
                    String jsonString = null;

                    if (response.body() != null) {

                        jsonString = response.body().getAsJsonArray("documents").toString();
                        SearchVO[] array = gson.fromJson(jsonString, SearchVO[].class);
                        mItems.addAll(Arrays.asList(array));

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("MainActivity", "Throwable ::: " + t);
                }
            });

            Call<JsonObject> videoCall = networkConService.repoVclip(API.APP_KEY, m_keyword_et.getText().toString());

            videoCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Gson gson = new Gson();
                    String jsonString = null;

                    if (response.body() != null) {

                        jsonString = response.body().getAsJsonArray("documents").toString();
                        SearchVO[] array = gson.fromJson(jsonString, SearchVO[].class);
                        mItems.addAll(Arrays.asList(array));

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("MainActivity", "Throwable ::: " + t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Exception ::: " + e);
        }

        setData();
    }

    private void setData() {

        // 리스트 초기화
        mItems.clear();
        int num = 0;
        Log.w("MainActivity", "########### setData ############ " );
        Log.i("MainActivity", "@@@@@@@@@@@@ ==> " + mItems.size() );
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(SearchVO searchVO : mItems){
            Log.e("MainActivity", "@@@@@@@@@@ for ############ " );
            Comparator<SearchVO> noDesc = new Comparator<SearchVO>() {
                @Override
                public int compare(SearchVO o1, SearchVO o2) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                    String o1_Date = sdf2.format(o1.getDatetime());
                    String o2_Date = sdf2.format(o2.getDatetime());
                    Log.d("MainActivity", "o1_Date ==> " + o1_Date);
                    Log.w("MainActivity", "o2_Date ==> " + o2_Date);
                    return o2_Date.compareTo(o1_Date);
                }

            };
            Collections.sort(mItems, noDesc) ;

            searchVO.setThumbnail(mItems.get(num).getThumbnail());
            searchVO.setThumbnail_url(mItems.get(num).getThumbnail_url());
            searchVO.setDatetime(mItems.get(num).getDatetime());

            num++;
        }
        adapter.notifyDataSetChanged();
    }

    private void setRefresh(){
        mainBinding.swipeRefreshLo.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mainBinding.recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mainBinding.recyclerView,"Refresh Success", Snackbar.LENGTH_SHORT).show();
                mainBinding.swipeRefreshLo.setRefreshing(false);
            }
        },500);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}


    /***
     * 클릭 이벤트
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search :
                callDataList();
                break;
        }
    }
}
