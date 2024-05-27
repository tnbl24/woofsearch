package com.example.woofsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Kiểm tra trạng thái đăng nhập
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // Đã đăng nhập, hiển thị MainActivity
            setContentView(R.layout.tao_bai_viet);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            loadData();
        } else {
            // Chưa đăng nhập, chuyển hướng đến HomeActivity
            startActivity(new Intent(MainActivity.this, Home.class)); // Chuyển đến Home.class
            finish(); // Kết thúc MainActivity để tránh quay lại khi bấm nút Back
        }
    }

    private void loadData(){
        if(AppUtil.checkconnect(this)){
            setContentView(R.layout.tao_bai_viet);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }else {
            Toast.makeText(this,"Network disconnected",Toast.LENGTH_SHORT).show();
        }
    }
}