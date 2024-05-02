package com.example.woofsearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ai.codia.x.java.demo.R;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.sign_up);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dien_dan); // Thay đổi thành sign_up.xml

        // Thiết lập trình nghe sự kiện cho View gốc của sign_up.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            registerReceiver(airplaneModeReceiver, intentFilter);


            if (connect()) {
//            callApi();
            } else {
                Toast.makeText(MainActivity.this, "Không có kết nối mạng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }

    }


    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (connect()) {
//                callApi();
            }
        }
    };

    private BroadcastReceiver airplaneModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
                String message = isAirplaneModeOn ? "Airplane mode vừa Bật" : "Airplane mode vừa Tắt";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(airplaneModeReceiver);
    }

    private boolean connect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private int dp2px(double dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}