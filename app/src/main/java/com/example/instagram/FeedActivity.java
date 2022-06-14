package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private static final String TAG = "FeedActivity";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    private BottomNavigationView bottomNavigationView;
    private PostsAdapter adapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();
        queryPosts();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // do something here
                        return true;
                    case R.id.action_post:
                        // do something here
                        return true;
                    case R.id.action_profile:
                        // do something here
                        return true;
                    default: return true;
                }
            }
        });
    }

    private void initViews() {
        rvPosts = findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        adapter = new PostsAdapter(this, posts);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class); // specify what type of data we want to query - Post.class on parstagram database
        query.include(Post.KEY_USER); // include data referred by current user
        query.setLimit(20); // only want last 20 photos
        query.addDescendingOrder("createdAt"); // get the newer photos first so sort by createdAt column
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                // no error
                // logs all the posts returned
                for (Post post: queryPosts) {
                    Log.i(TAG, "caption: " + post.getCaption() + "username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                posts.addAll(queryPosts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}