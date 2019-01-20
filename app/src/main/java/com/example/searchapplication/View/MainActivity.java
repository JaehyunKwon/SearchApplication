package com.example.searchapplication.View;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.searchapplication.Model.SearchInfo;
import com.example.searchapplication.Network.API;
import com.example.searchapplication.Network.NetworkConnections;
import com.example.searchapplication.R;
import com.example.searchapplication.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static okhttp3.internal.Internal.instance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mainBinding;

    List<Fragment> tabFragments;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private EditText m_keyword_et;
    private Button m_search_btn;

    private RecyclerView.Adapter adapter;

    private ArrayList<SearchInfo> mItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        m_keyword_et = findViewById(R.id.et_keyword);
        m_search_btn = findViewById(R.id.btn_search);
        m_search_btn.setOnClickListener(this);

        // 각 탭 이름 지정
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("리스트"));
        tabLayout.addTab(tabLayout.newTab().setText("내보관함"));
        tabLayout.setTabTextColors(Color.LTGRAY, Color.BLUE);
    }

    /**
     * ViewPager 생성
     * */
    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);

        // 프래그먼트 리스트에 추가
        tabFragments = new ArrayList<>();
        tabFragments.add(new SearchListFragment(mItems));
        tabFragments.add(new MyFragment());
        // 탭 어댑터에 리스트 넘겨준 후 뷰페이저 연결
        TabPagerAdapter fragmentPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabFragments);
        viewPager.setAdapter(fragmentPagerAdapter);

        /* 뷰페이저 이동했을때 & 탭 눌렀을때 해당 위치로 이동 */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /***
     * API 호출된 데이터 리스트 세팅
     */
    private void callDataList() {
        // 리스트 초기화
        mItems.clear();

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
                        SearchInfo[] array = gson.fromJson(jsonString, SearchInfo[].class);
                        mItems.addAll(Arrays.asList(array));
                        setData(mItems);
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
                        SearchInfo[] array = gson.fromJson(jsonString, SearchInfo[].class);
                        mItems.addAll(Arrays.asList(array));
                        setData(mItems);
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
    }

    private void setData(ArrayList<SearchInfo> mSearchInfoItems) {

        int num = 0;
        Log.w("MainActivity", "########### setData ############ " );
        Log.i("MainActivity", "@@@@@@@@@@@@ ==> " + mSearchInfoItems.size() );
        // RecyclerView 에 들어갈 데이터를 추가합니다.
        for(SearchInfo searchInfo : mSearchInfoItems){
            Log.e("MainActivity", "@@@@@@@@@@ for ############ " );

            searchInfo.setThumbnail(mSearchInfoItems.get(num).getThumbnail());
            searchInfo.setThumbnail_url(mSearchInfoItems.get(num).getThumbnail_url());
            searchInfo.setDatetime(mSearchInfoItems.get(num).getDatetime());

            num++;
        }

        Comparator<SearchInfo> noDesc = new Comparator<SearchInfo>() {
            @Override
            public int compare(SearchInfo o1, SearchInfo o2) {

                Log.d("MainActivity", "o1_Date ==> " + o1.getDatetime());
                Log.w("MainActivity", "o2_Date ==> " + o2.getDatetime());
                return o2.getDatetime().compareTo(o1.getDatetime());
            }

        };
        Collections.sort(mSearchInfoItems, noDesc);

        initViewPager();
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
