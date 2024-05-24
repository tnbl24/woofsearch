package com.example.woofsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.Length;

import java.sql.Timestamp;

public class AddPost extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://woofsearch-fb69e-default-rtdb.firebaseio.com/");
    private EditText tao_bai_viet_noi_dung;
    private FrameLayout addImage;
    private FrameLayout addVideo;
    private FrameLayout addIcon;
    private ImageView addVideo2, back;
    private Button submitbtn;
    private Uri imageUri;
    private Uri videoUri;
    private Timestamp creatAt;
    private static final int REQUEST_CODE_IMAGE = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tao_bai_viet);

        tao_bai_viet_noi_dung = findViewById(R.id.tao_bai_viet_noi_dung);
        addImage = findViewById(R.id.add_post_image);
        addVideo = findViewById(R.id.add_post_video);
        addIcon = findViewById(R.id.add_post_icon);
        addVideo2 = findViewById(R.id.add_video_icon);
        submitbtn = findViewById(R.id.add_post_btn);
        back = findViewById(R.id.add_post_back);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPost.this,"hello",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddPost.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        addVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoPicker();
            }
        });
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIcon();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noi_dung = tao_bai_viet_noi_dung.getText().toString();
                Toast.makeText(AddPost.this,"hello",Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(noi_dung)) {
                    String currentUserPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                    String postId = databaseReference.child("posts").push().getKey();
                    Post newPost = new Post(postId, currentUserPhoneNumber, noi_dung,imageUri ,videoUri ,creatAt );
                    FirebaseDatabase.getInstance().getReference("posts").child(postId).setValue(newPost);
                    databaseReference.child(postId).setValue(newPost)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddPost.this, "Thêm bài đăng thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddPost.this, "Không thể thêm bài đăng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AddPost.this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        private void openImagePicker() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        }

        private void openVideoPicker() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_VIDEO);
        }

        private void insertIcon() {

            String[] EMOJIS = new String[] {
                    "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😇", "😉",
            };
            LinearLayout emojiPanel = new LinearLayout(this);
            emojiPanel.setOrientation(LinearLayout.HORIZONTAL);
            emojiPanel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            for (String emoji : EMOJIS)
            {
                Button emojiButton = new Button(this);
                emojiButton.setText(emoji);
                emojiButton.setOnClickListener(v -> {
                    int cursorPosition = tao_bai_viet_noi_dung.getSelectionStart();
                    tao_bai_viet_noi_dung.getText().insert(cursorPosition, emoji);
                    tao_bai_viet_noi_dung.setSelection(cursorPosition + emoji.length());
                });
                emojiPanel.addView(emojiButton);
            }

            ViewGroup rootLayout = (ViewGroup)findViewById(R.id.add_post_layout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            rootLayout.addView(emojiPanel, params);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == RESULT_OK && data != null) {
             videoUri = data.getData();
        }
    }
    public void onBackClick(View view) {
        Intent intent = new Intent(AddPost.this, Dien_dan.class);
        startActivity(intent);
        finish();
    }
}