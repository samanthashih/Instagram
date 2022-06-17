package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;
    private int numLikes = 0;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate view
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false); // pass it a “blueprint” of the view (reference to XML layout file)
        return new ViewHolder(view); // wrap view in viewholder
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
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

    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername;
        TextView tvUsername2;
        ImageView ivProfilePic;
        ImageView ivImage;
        TextView tvCaption;
        ImageView ivLike;
        TextView tvLikeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            ivLike.setOnClickListener(this);
            ivImage.setOnClickListener(this);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            getNumLikes(post);
            System.out.println(post.getCaption()+ " likes!!: " + String.valueOf(numLikes));
            tvLikeCount.setText(String.valueOf(numLikes));
            ParseFile profilePic = post.getProfilePic();
            Log.i(TAG, post.getCaption());
            if (profilePic != null) {
                Glide.with(context)
                        .load(profilePic.getUrl())
                        .transform(new RoundedCorners(100))
                        .into(ivProfilePic);
            }
            else {
                Glide.with(context)
                        .load(R.drawable.default_pfp)
                        .transform(new RoundedCorners(100))
                        .into(ivProfilePic);
            }

            tvCaption.setText(post.getCaption());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivImage);
            }
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Post post = posts.get(position);
                ParseUser currentUser = ParseUser.getCurrentUser();
                if (v.getId() == R.id.ivLike){
                    Log.i(TAG,"like position: " + position);
                    Like like = new Like();
                    like.setPost(post);
                    like.setUser(currentUser);
                    like.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "error while saving like", e);
                            }
                            Log.i(TAG, "like save success");
                        }
                    });
                    ivLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                    getNumLikes(post);
                    tvLikeCount.setText(String.valueOf(numLikes));
                } else {
                    Log.i(TAG,"detail position: " + position);
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra(context.getString(R.string.extra_post), Parcels.wrap(post));
                    context.startActivity(intent);
                }

            }
        }

    }
}
