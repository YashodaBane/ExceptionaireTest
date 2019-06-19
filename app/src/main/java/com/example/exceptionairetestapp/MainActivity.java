package com.example.exceptionairetestapp;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exceptionairetestapp.databinding.ActivityMainBinding;
import com.example.exceptionairetestapp.fragments.DetailFragment;
import com.example.exceptionairetestapp.fragments.TabFragment1;
import com.example.exceptionairetestapp.fragments.TabFragment2;
import com.example.exceptionairetestapp.networking.APIService;
import com.example.exceptionairetestapp.networking.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TabFragment1.SendDataListener {
    private static final String TAG = "MainActivity";
    private APIService apiService;
    private ArrayList<TicketModel> list = new ArrayList<>();
    private PagerAdapter adapter;
    private TabFragment1 tabFragment1;
    private TabFragment2 tabFragment2;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding == null)
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            tabFragment1 = new TabFragment1();
            tabFragment2 = new TabFragment2();

            loadFragments();
        } else {
            tabFragment2 = (TabFragment2) getSupportFragmentManager().getFragment(savedInstanceState, "tabFragment2");
        }

        setupTabs();

    }

    private void setupTabs() {
        int tabPosition = 0;

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Movie List"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Movie Details"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new PagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount(), list);
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.viewpager.setCurrentItem(tabPosition);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void loadFragments() {
        DetailFragment detailFragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_detail, detailFragment).commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "tabFragment2", tabFragment2);
    }

    @Override
    public void sendData(TicketModel ticketModel, int position) {
        binding.viewpager.setCurrentItem(1);
        binding.tabLayout.getTabAt(1).select();
        if (tabFragment2 != null) {
            tabFragment2.displayReceivedData(ticketModel);
        } else {
            tabFragment2 = (TabFragment2) adapter.getItem(1);
            if (tabFragment2 != null) {
                tabFragment2.displayReceivedData(ticketModel);
            }
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        private final ArrayList<TicketModel> list;
        int mNumOfTabs;

        PagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<TicketModel> list) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    tabFragment1.hitTicketDataApi();
                    return tabFragment1;
                case 1:
                    return tabFragment2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }

}
