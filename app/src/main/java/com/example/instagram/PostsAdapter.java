package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

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


    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername;
        ImageView ivImage;
        TextView tvCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
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
            Log.i(TAG,"post detail click" );
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Log.i(TAG,"position: " + position);
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra(context.getString(R.string.extra_post), Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }
}
