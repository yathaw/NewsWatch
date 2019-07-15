package com.example.newswatch.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.newswatch.models.topheadline.ArticlesItem;
import com.example.newswatch.utils.Utils;

import java.util.List;

public class TopheadlineAdapter extends RecyclerView.Adapter<TopheadlineAdapter.MyViewHolder>
{
    private OnItemClickListener onItemClickListener;
    private List<ArticlesItem> topheadlinearticlesItems;
    private Context context;

    public interface OnItemClickListener
    {
        void onItemClick(View view,int position);
    }

    public TopheadlineAdapter(List<ArticlesItem> topheadlinearticlesItems, Context context)
    {
        this.topheadlinearticlesItems = topheadlinearticlesItems;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topheadlinenews_layout, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final  MyViewHolder myViewHolder = holder;
        ArticlesItem model = topheadlinearticlesItems.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawableColor());
        requestOptions.error(Utils.getRandomDrawableColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(myViewHolder.imageView.getContext())
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        myViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        myViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(myViewHolder.imageView);

        myViewHolder.title.setText(model.getTitle());
        myViewHolder.author.setText(model.getSource().getName());
        myViewHolder.time.setText(Utils.DateToTimeFormat(model.getPublishedAt()));
    }

    @Override
    public int getItemCount() {
        return topheadlinearticlesItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title,author, time;
        ImageView imageView;
        OnItemClickListener onItemClickListener;
        ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            time = itemView.findViewById(R.id.time);

            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v)
        {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
