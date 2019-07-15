package com.example.newswatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newswatch.adapter.CategoryNewsAdapter;
import com.example.newswatch.api.ApiClient;
import com.example.newswatch.api.ApiInterface;
import com.example.newswatch.models.categorynews.ArticlesItem;
import com.example.newswatch.models.categorynews.Categorynews;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryNewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private String appbarTitle;
    public static final String API_KEY = "c227cbd759164c75979e47249b3b93c4";
    private TextView categoryName, errorTitle, errorMessage;
    private Button btnRetry;
    private ImageView errorImage;

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorLayout;

    private CategoryNewsAdapter categoryNewsAdapter;
    private List<ArticlesItem> articlesItems = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_news);

        Bundle bundle = getIntent().getExtras();
        appbarTitle = bundle.getString("Category");

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(appbarTitle);
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.category_recyclerView);
        layoutManager = new LinearLayoutManager(CategoryNewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        errorLayout = findViewById(R.id.errorLayout);
        errorImage = findViewById(R.id.errorImage);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        btnRetry = findViewById(R.id.btn_retry);

        onLoadingCategorySwipeRefresh();


    }

    public void onLoadingCategorySwipeRefresh()
    {
        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country ="us";

        Call<Categorynews> call;

        if (appbarTitle.matches("Business"))
        {
            call = apiInterface.getCategory(country, "business",API_KEY);
        }

        else if (appbarTitle.matches("Entertainment"))
        {
            call = apiInterface.getCategory(country, "entertainment",API_KEY);
        }

        else if (appbarTitle.matches("Health"))
        {
            call = apiInterface.getCategory(country, "health",API_KEY);
        }

        else if (appbarTitle.matches("Science"))
        {
            call = apiInterface.getCategory(country, "science",API_KEY);
        }

        else if (appbarTitle.matches("Sports"))
        {
            call = apiInterface.getCategory(country, "sports",API_KEY);
        }

        else
        {
            call = apiInterface.getCategory(country, "technology",API_KEY);
        }

        call.enqueue(new Callback<Categorynews>() {
            @Override
            public void onResponse(Call<Categorynews> call, Response<Categorynews> response)
            {
                if (response.isSuccessful() && response.body().getArticles() != null)
                {
                    if (!articlesItems.isEmpty())
                    {
                        articlesItems.clear();
                    }

                    articlesItems = response.body().getArticles();
                    categoryNewsAdapter = new CategoryNewsAdapter(articlesItems, CategoryNewsActivity.this);

                    recyclerView.setAdapter(categoryNewsAdapter);
                    categoryNewsAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

                    initListener();
                }

                else
                {
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 Not found";
                            break;

                        case 500:
                            errorCode = "500 server broken";
                            break;

                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.nosearchresult,
                            "No Result",
                            "Please try again!\n"+
                                    errorCode
                    );
                }
            }

            @Override
            public void onFailure(Call<Categorynews> call, Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.error,
                        "Oops....",
                        "Network failure, Please try again\n"+
                                t.toString()
                );
            }
        });

    }

    private void initListener() {
        categoryNewsAdapter.setOnItemClickListener(new CategoryNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(CategoryNewsActivity.this, NewsDetailActivity.class);

                ArticlesItem article = articlesItems.get(position);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                startActivity(intent);

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public void onRefresh()
    {
        onLoadingCategorySwipeRefresh();
    }

    private void showErrorMessage(int imageView, String title, String message) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onLoadingCategorySwipeRefresh();
            }
        });
    }
}
