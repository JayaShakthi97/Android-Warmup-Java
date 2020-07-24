package com.example.warmupappjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArticleListPage extends AppCompatActivity {
    private static final String TAG = "Article Page";
    private ArrayList<NewsArticle> articleList = new ArrayList<>();
    private RecyclerView rv;
    private TextView infoText;
    private TextView emptyView;
    private Spinner spinner;
    private ArticleAdapter articleAdapter;
    boolean isLoading = false;
    private EndlessRecyclerViewScrollListener listener;
    private int page = 1;
    private int pageCount = 10;
    private NewsApi newsApi;
    private String selectedCountry = "us";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list_page);
        infoText = findViewById(R.id.info_text);
        emptyView = findViewById(R.id.empty_text);
        spinner = findViewById(R.id.country_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Constants.countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        rv = findViewById(R.id.rv_articles);
        articleAdapter = new ArticleAdapter(articleList);
        LinearLayoutManager articleLayoutManager
                = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(articleLayoutManager);
        rv.setAdapter(articleAdapter);
        listener = new EndlessRecyclerViewScrollListener(articleLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                if (!isLoading) {
                    Log.i("TAG", "page: " + page);
                    getData();
                    isLoading = true;
                }
            }
        };
        rv.addOnScrollListener(listener);

        Retrofit retrofit = RetrofitHelper.get();
        newsApi = retrofit.create(NewsApi.class);
        getData();
        isLoading = true;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("tag", "selectedddd");
                switch (Constants.countries[i]) {
                    case "United State":
                        selectedCountry = "us";
                        break;
                    case "South Africa":
                        selectedCountry = "sa";
                        break;
                    case "Argentina":
                        selectedCountry = "at";
                        break;
                    case "Belgium":
                        selectedCountry = "bg";
                        break;
                    default:
                        selectedCountry = "ca";
                        break;
                }
                if (!isLoading) {
                    articleList.clear();
                    articleAdapter.notifyDataSetChanged();
                    page = 1;
                    listener.resetState();
                    getData();
                    isLoading = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getData() {
        articleList.add(null);
        articleAdapter.notifyItemInserted(articleList.size() - 1);

        Call<NewsResponse> call
                = newsApi.getTopStories(selectedCountry, Constants.API_KEY, page, pageCount);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                articleList.remove(articleList.size() - 1);
                articleAdapter.notifyItemRemoved(articleList.size());

                if (!response.isSuccessful()) {
                    Log.v(TAG, "onResponse: " + response.message());
                    isLoading = false;
                    return;
                }
                NewsResponse newsResponse = response.body();

                if (newsResponse.getStatus().equals("ok")) {
                    infoText.setVisibility(View.GONE);
                    List<NewsArticle> newsArticles = newsResponse.getNewsArticleList();
                    articleList.addAll(newsArticles);
                    //toggleEmpty();
                    if (!(newsArticles.isEmpty())) {
                        page++;
                    }
                    articleAdapter.notifyDataSetChanged();
                } else {
                    infoText.setText(newsResponse.getStatus());
                    infoText.setVisibility(View.VISIBLE);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                articleList.remove(articleList.size() - 1);
                articleAdapter.notifyItemRemoved(articleList.size());

                infoText.setText("Err" + t.getMessage());
                infoText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void toggleEmpty() {
        if (articleList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }
}