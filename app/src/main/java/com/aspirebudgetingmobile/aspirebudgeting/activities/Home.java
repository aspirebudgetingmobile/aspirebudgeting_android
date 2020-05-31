package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.ViewPagerAdapter_Home;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.Dashboard;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.AddTransactionFragment;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

public class Home extends AppCompatActivity {

    // VIEWS
    ViewPagerAdapter_Home pagerAdapterHome;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView settingButton_Dashboard;

    // BOTTOM SHEET
    MaterialCardView settingsBottomSheet;
    BottomSheetBehavior settingsSheetBehaviour;
    TextView versionName;
    MaterialButton signOut;

    // SINGLETON AND CLASS OBJECTS
    ObjectFactory objectFactory = ObjectFactory.getInstance();
    UserManager userManager;

    // Fragments
    Dashboard dashboard = new Dashboard();
    AddTransactionFragment transaction = new AddTransactionFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager_Home);
        tabLayout = findViewById(R.id.homeTabLayout);
        settingButton_Dashboard = findViewById(R.id.settingButton_Dashboard);
        settingsBottomSheet = findViewById(R.id.settingsBottomSheet);
        versionName = findViewById(R.id.appVersionTextView);
        signOut = findViewById(R.id.signOutButton);

        userManager = objectFactory.getUserManager();

        settingsSheetBehaviour = BottomSheetBehavior.from(settingsBottomSheet);
        settingsSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

        pagerAdapterHome = new ViewPagerAdapter_Home(getSupportFragmentManager(), 1);
        pagerAdapterHome.addFragment(dashboard, "Dashboard");
        pagerAdapterHome.addFragment(transaction, "Transaction");
        viewPager.setAdapter(pagerAdapterHome);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    transaction.initValues();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getVersionName();

        onClickListeners();
    }

    private void getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionName.setText(String.format("App version : v%s", version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onClickListeners() {

        settingButton_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsSheetBehaviour.getState() == BottomSheetBehavior.STATE_HIDDEN)
                    settingsSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                else
                    settingsSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.initializeGoogleSignIn(Home.this);
                userManager.signOutUser(Home.this);
            }
        });
    }

    public void reloadCards(){
        dashboard.reloadCards();
    }

}
