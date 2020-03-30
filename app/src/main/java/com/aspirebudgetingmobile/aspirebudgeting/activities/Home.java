package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.ViewPagerAdapter_Home;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.Dashboard;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.Transaction;
import com.google.android.material.tabs.TabLayout;

public class Home extends AppCompatActivity {

    ViewPagerAdapter_Home pagerAdapterHome;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager_Home);
        tabLayout = findViewById(R.id.homeTabLayout);

        pagerAdapterHome = new ViewPagerAdapter_Home(getSupportFragmentManager(), 1);
        pagerAdapterHome.addFragment(new Dashboard(), "Dashboard");
        pagerAdapterHome.addFragment(new Transaction(), "Transaction");
        viewPager.setAdapter(pagerAdapterHome);
        tabLayout.setupWithViewPager(viewPager);
    }
}
