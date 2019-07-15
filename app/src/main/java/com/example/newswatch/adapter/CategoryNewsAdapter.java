package com.example.newswatch.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newswatch.R;
import com.example.newswatch.models.categorynews.ArticlesItem;
import com.example.newswatch.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryNewsAdapter extends RecyclerView.Adapter<CategoryNewsAdapter.MyViewHolder>
{

    private List<ArticlesItem> articlesItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int lastPosition = -1;

    public CategoryNewsAdapter(List<ArticlesItem> articlesItems, Context context)
    {
        this.articlesItems = articlesItems;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.category_news_layout, viewGroup, false);

        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
        final MyViewHolder holder = myViewHolder;
        ArticlesItem model = articlesItems.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);


        holder.title.setText(model.getTitle());
        holder.author.setText("By "+ model.getAuthor());
        holder.time.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()));

    }

    @Override
    public int getItemCount()
    {
        return articlesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title, desc, author, time;
        ImageView imageView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.category_news_title);
            time = itemView.findViewById(R.id.category_news_date);
            author = itemView.findViewById(R.id.category_author);
            imageView = itemView.findViewById(R.id.category_news_img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);
            cardView = itemView.findViewById(R.id.container);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v)
        {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int adapterPosition);
    }
}