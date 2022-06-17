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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;
import org.w3c.dom.Text;

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
    TextView tvLikeCount;
    int numLikes = 0;


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
        tvLikeCount = findViewById(R.id.tvLikeCount);
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

        getNumLikes(post);
        tvLikeCount.setText(String.valueOf(numLikes));
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

    protected void getNumLikes(Post post) {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class); // specify what type of data we want to query - Post.class
        query.whereEqualTo(Like.KEY_POST, post);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_POST);
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likes, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting likes", e);
                    return;
                }
                numLikes = likes.size();
            }
        });
        System.out.println(post.getCaption()+ " likes: " + numLikes);
    }
}