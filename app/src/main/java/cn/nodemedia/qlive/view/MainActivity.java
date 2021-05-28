package cn.nodemedia.qlive.view;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import cn.nodemedia.qlive.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.main_navigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_play, R.id.navigation_push, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.main_content);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


//    private NoScrollViewPager mainContent;
//    private BottomNavigationView navigation;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_play:
//                    changeFooterState(0);
//                    return true;
//                case R.id.navigation_push:
//                    changeFooterState(1);
//                    return true;
////                case R.id.navigation_conv:
////                    changeFooterState(2);
////                    return true;
//                case R.id.navigation_about:
//                    changeFooterState(3);
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    private int currentItem;
//    private int selectIndex = -1;
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        currentItem = intent.getIntExtra("p0", 0);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_main;
//    }
//
//    @Override
//    public void initView(Bundle savedInstanceState) {
//        super.initView(savedInstanceState);
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }
//        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this,
//                new PermissionsResultAction() {
//                    @Override
//                    public void onGranted() {
//
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
//                        Toast.makeText(getContext(),"onDenied permission:"+permission,Toast.LENGTH_LONG);
//                    }
//                });
//        hasBack(View.GONE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        assignViews();
//        if (savedInstanceState == null) {
//            currentItem = getIntent().getIntExtra("p0", 0);
//        } else {
//            currentItem = savedInstanceState.getInt("p0", 0);
//        }
//        initData();
//    }
//
//    /**
//     * 实例化视图控件
//     */
//    private void assignViews() {
//        mainContent = getView(R.id.main_content);
//        navigation = getView(R.id.main_navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(navigation);
//    }
//
//    /**
//     * 初始化数据
//     */
//    public void initData() {
//        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new PlayFragment());
//        fragmentList.add(new PushFragment());
//
////        fragmentList.add(new ConvFragment());
//        fragmentList.add(new AboutFragment());
//        BasePagerFragmentAdapter fragmentAdapter = new BasePagerFragmentAdapter(getFragmentManager(), fragmentList);
//        mainContent.setAdapter(fragmentAdapter);
//        mainContent.setOffscreenPageLimit(fragmentList.size());
//        mainContent.setCurrentItem(currentItem);
//        changeFooterState(currentItem);
//    }
//
//    /**
//     * 改变选项卡状态
//     */
//    private void changeFooterState(int position) {
//        if (position == selectIndex) {
//            return;
//        }
//
//        switch (position) {
//            case 0:
//                setTitle(R.string.title_play);
//                break;
//            case 1:
//                setTitle(R.string.title_push);
//                break;
//            case 2:
//                setTitle(R.string.title_conv);
//                break;
//            case 3:
//                setTitle(R.string.title_about);
//                break;
//        }
//
//        mainContent.setCurrentItem(position, false);
//        selectIndex = position;
//    }
//
//    @Override
//    public void initPresenter() {
//        mPresenter.initPresenter(this);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("p0", selectIndex);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void exit() {
//        long newClickTime = System.currentTimeMillis();
//        if (newClickTime - oldClickTime < 1000) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//        } else {
//            oldClickTime = newClickTime;
//            ToastUtils.show(mActivity, R.string.app_exit);
//        }
//    }
//
//    @Override
//    public boolean hasLightMode() {
//        return true;
//    }
//
//    @Override
//    public boolean hasSwipeFinish() {
//        return false;
//    }




}
