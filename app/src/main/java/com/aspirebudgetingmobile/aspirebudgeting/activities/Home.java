package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.ViewPagerAdapter_Home;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.AccountBalance;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.Dashboard;
import com.aspirebudgetingmobile.aspirebudgeting.fragments.AddTransactionFragment;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class Home extends AppCompatActivity {

    // VIEWS
    ViewPagerAdapter_Home pagerAdapterHome;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView settingButton_Dashboard;
    ExtendedFloatingActionButton addTransactionFAB;

    // BOTTOM SHEET
    MaterialCardView settingsBottomSheet;
    BottomSheetBehavior settingsSheetBehaviour;
    TextView versionName;
    MaterialButton signOut;

    // SINGLETON AND CLASS OBJECTS
    ObjectFactory objectFactory = ObjectFactory.getInstance();
    UserManager userManager;

    // Fragments
    Dashboard dashboard;
    AddTransactionFragment transaction;
    AccountBalance accountBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        setupViewPager();
        getVersionName();
        onClickListeners();
    }

    private void initView() {

        dashboard = new Dashboard();
        dashboard.shareData(Home.this);
        transaction = new AddTransactionFragment();
        transaction.shareData(Home.this);
        accountBalance = new AccountBalance();

        viewPager = findViewById(R.id.viewPager_Home);
        tabLayout = findViewById(R.id.homeTabLayout);
        settingButton_Dashboard = findViewById(R.id.settingButton_Dashboard);
        settingsBottomSheet = findViewById(R.id.settingsBottomSheet);
        addTransactionFAB = findViewById(R.id.addTransactionsFAB_home);
        versionName = findViewById(R.id.appVersionTextView);
        signOut = findViewById(R.id.signOutButton);
        userManager = objectFactory.getUserManager();

        settingsSheetBehaviour = BottomSheetBehavior.from(settingsBottomSheet);
        settingsSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setupViewPager() {
        pagerAdapterHome = new ViewPagerAdapter_Home(getSupportFragmentManager(), 1);
        pagerAdapterHome.addFragment(dashboard, "Dashboard");
        pagerAdapterHome.addFragment(accountBalance, "Account Balance");
        viewPager.setAdapter(pagerAdapterHome);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

        addTransactionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.show(getSupportFragmentManager(), "Transactions");
            }
        });
    }

    public void reloadCards(){
        dashboard.reloadCards();
    }

    public void reloadAccounts(){
        accountBalance.fetchAccountBalance();
    }

    public void dataLoaded(){
        new CountDownTimer(1000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                addTransactionFAB.shrink();
            }
        }.start();

    }
}
