package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailsActivity";
    Post post;
    TextView tvUsername;
    ImageView ivImage;
    TextView tvCaption;
    TextView tvTimestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(this.getString(R.string.extra_post)));
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivImage);
        }
        tvCaption.setText(post.getCaption());

        Date createdAt = post.getCreatedAt();
        tvTimestamp.setText(Post.calculateTimeAgo(createdAt));

    }
}