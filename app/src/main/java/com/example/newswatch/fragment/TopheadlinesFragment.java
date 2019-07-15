package com.example.newswatch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newswatch.MainActivity;
import com.example.newswatch.NewsDetailActivity;
import com.example.newswatch.R;
import com.example.newswatch.adapter.TopheadlineAdapter;
import com.example.newswatch.api.ApiClient;
import com.example.newswatch.api.ApiInterface;
import com.example.newswatch.models.topheadline.ArticlesItem;
import com.example.newswatch.models.topheadline.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopheadlinesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    public static final String API_KEY = "c227cbd759164c75979e47249b3b93c4";
    private RecyclerView recyclerView;
    private List<ArticlesItem> topheadlinearticlesItems = new ArrayList<>();
    private TopheadlineAdapter topheadlineAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage, topheadline_title;
    private View topheadline_divider;
    private Activity activity;
    private Button btnRetry;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_topheadline,container,false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // Recycler View
        recyclerView = view.findViewById(R.id.topheadlines_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        topheadline_title = view.findViewById(R.id.topheadlines_title);
        topheadline_divider = view.findViewById(R.id.topheadlines_start_divider);

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btn_retry);

        activity = getActivity();

        TopheadlineOnLoadinSwipeRefresh();

        return view;
    }

    private void TopheadlineOnLoadinSwipeRefresh()
    {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadTopheadline();
            }
        });
    }

    public void LoadTopheadline()
    {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String sources = "us";

        Call<News> call;
        call = apiInterface.getNews(sources, API_KEY);


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response)
            {
                if (response.isSuccessful() && response.body().getArticles() != null)
                {
                    if (!topheadlinearticlesItems.isEmpty())
                    {
                        topheadlinearticlesItems.clear();
                    }

                    topheadlinearticlesItems = response.body().getArticles();
                    topheadlineAdapter = new TopheadlineAdapter(topheadlinearticlesItems,getActivity());


                    recyclerView.setAdapter(topheadlineAdapter);
                    topheadlineAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

                    topheadline_title.setVisibility(View.VISIBLE);
                    topheadline_divider.setVisibility(View.VISIBLE);

                    initListener();

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);

                topheadline_title.setVisibility(View.INVISIBLE);
                topheadline_divider.setVisibility(View.INVISIBLE);

                String errorCode;

                switch (call.hashCode())
                {
                    case 404:
                        errorCode ="404 Not Found";
                        break;


                    case 500:
                        errorCode = "500 Server Broken";
                        break;

                    default:
                        errorCode = "Unknown Error";
                        break;
                }

                showErrorMessage(
                        R.drawable.nosearchresult,
                        "No Result",
                        "Please Try again! \n" + errorCode
                );


            }
        });

    }

    private void initListener()
    {
        topheadlineAdapter.setOnItemClickListener(new TopheadlineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                ArticlesItem articlesItem = topheadlinearticlesItems.get(position);

                intent.putExtra("url",articlesItem.getUrl());
                intent.putExtra("title",articlesItem.getTitle());
                intent.putExtra("img",articlesItem.getUrlToImage());
                intent.putExtra("date",articlesItem.getPublishedAt());
                intent.putExtra("source",articlesItem.getSource().getName());
                intent.putExtra("author",articlesItem.getAuthor());

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("News Watch");
    }

    @Override
    public void onRefresh()
    {
        LoadTopheadline();
    }

    private void showErrorMessage(int ImageView, String title, String message)
    {
        if (errorLayout.getVisibility() == View.GONE)
        {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(ImageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopheadlineOnLoadinSwipeRefresh();
            }
        });
    }
}
