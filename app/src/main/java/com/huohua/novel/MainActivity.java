package com.huohua.novel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends BaseActivity {
    private final int MAIN_DISPLAY_LENGTH = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        int selectedTab = 0;
        String from = "normal";
        String pathName = "";
        if (extras != null) {
            selectedTab = extras.getInt("selectedTab");
            from = extras.getString("from", "normal");
            pathName = extras.getString("pathName", "normal");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_category, R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if (selectedTab != 0) {
            navController.navigate(selectedTab);
        }

        if (TextUtils.equals(from, "third")) {
            final String finalPathName = pathName;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Log.e("Main - Detail", finalPathName);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pathName", finalPathName);
                    intent.putExtra("from", "notification");

                    startActivity(intent);
                }
            }, MAIN_DISPLAY_LENGTH);
        }

        checkPrivacyAgreement();
    }

    private SharedPreferences getSharedPrefs() {
        return this.getSharedPreferences("com.huohua.novel", Context.MODE_PRIVATE);
    }

    private void checkPrivacyAgreement() {
        long hasAgreementChecked = this.getSharedPrefs().getInt("has_agreement_checked_1.0", 0);

        if (hasAgreementChecked == 0) {
            this.showAgreementDialog();
        }
    }

    private void setPrivacyAgreementChecked() {
        this.getSharedPrefs().edit().putInt("has_agreement_checked_1.0", 1).apply();
    }

    private void showAgreementDialog() {
        TextView textView = new TextView(this);
        textView.setText(R.string.dialogContent);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // this is important to make the links clickable

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin_middle);
        params.leftMargin = margin;
        params.topMargin = margin;
        params.rightMargin = margin;
        params.bottomMargin = margin;
        textView.setLayoutParams(params);
        container.addView(textView);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("欢迎使用")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPrivacyAgreementChecked();
                        dialog.dismiss();
                    }
                })
                .setView(container)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
