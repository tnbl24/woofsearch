package com.example.woofsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://woofsearch-fb69e-default-rtdb.firebaseio.com/");
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập từ SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // Nếu đã đăng nhập, chuyển hướng đến MainActivity và kết thúc LoginActivity
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
            return; // Dừng thực thi onCreate() của LoginActivity
        }

        // Nếu chưa đăng nhập, tiếp tục hiển thị giao diện đăng nhập
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText phoneInput = findViewById(R.id.phoneInput);
        final EditText passwdInput = findViewById(R.id.passwdInput);
        final Button loginButton = findViewById(R.id.loginButton2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneInput.getText().toString();
                final String passwd = passwdInput.getText().toString();

                if (phone.isEmpty() || passwd.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập số điện thoại hoặc mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phone)) {
                                final String getPassword = snapshot.child(phone).child("passwdInput").getValue(String.class);
                                if (getPassword.equals(passwd)) {
                                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    // Lưu trạng thái đăng nhập vào SharedPreferences
                                    saveLoginStatus(phone);

                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish(); // Kết thúc activity Login
                                } else {
                                    Toast.makeText(Login.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    // Lưu trạng thái đăng nhập
    private void saveLoginStatus(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }
}