package com.example.woofsearch;

import android.annotation.SuppressLint;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://woofsearch-fb69e-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText nameInput = findViewById(R.id.nameInput);
        final EditText phoneInput = findViewById(R.id.phoneInput);
        final EditText emailInput = findViewById(R.id.emailInput);
        final EditText passwdInput = findViewById(R.id.passwdInput);
        final Button signupButton = findViewById(R.id.signupButton2);
        final Button signupButton2 = findViewById(R.id.signupButton2);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameInput.getText().toString();
                final String phone = phoneInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String passwd = passwdInput.getText().toString();

                if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || passwd.isEmpty()) {
                    Toast.makeText(Signup.this, "Vui lòng điền đầy đủ các trường thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone)) {
                                Toast.makeText(Signup.this, "Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(phone).child("nameInput").setValue(name);
                                databaseReference.child("users").child(phone).child("emailInput").setValue(email);
                                databaseReference.child("users").child(phone).child("passwdInput").setValue(passwd);

                                Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("users").child(phone).child("nameInput").setValue(name);
                    databaseReference.child("users").child(phone).child("emailInput").setValue(email);
                    databaseReference.child("users").child(phone).child("passwdInput").setValue(passwd);

                    Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

//        signupButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
}