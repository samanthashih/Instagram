package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailsActivity";
    Post post;
    TextView tvUsername;
    TextView tvUsername2;
    ImageView ivImage;
    ImageView ivProfilePic;
    TextView tvCaption;
    TextView tvTimestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(this.getString(R.string.extra_post)));
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivImage = findViewById(R.id.ivImage);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ParseFile profilePic = post.getProfilePic();
        if (profilePic != null) {
            Glide.with(this)
                    .load(profilePic.getUrl())
                    .transform(new RoundedCorners(100))
                    .into(ivProfilePic);
        }
        else {
            Glide.with(this)
                    .load(R.drawable.default_pfp)
                    .transform(new RoundedCorners(100))
                    .into(ivProfilePic);
        }

        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());

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