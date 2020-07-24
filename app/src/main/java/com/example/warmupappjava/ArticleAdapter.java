package com.example.warmupappjava;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsArticle> articles;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ArticleViewHolder(View v) {
            super(v);
            this.view = v;
        }

        public void bind(NewsArticle article) {
            TextView title = itemView.findViewById(R.id.title);
            TextView content = itemView.findViewById(R.id.content);
            ImageView image = itemView.findViewById(R.id.image);
            title.setText(article.getTitle());
            if (article.getContent() != null) {
                content.setText(Html.fromHtml(article.getContent()));
            } else if (article.getDescription() != null) {
                content.setText(Html.fromHtml(article.getDescription()));
            }
            if(article.getUrlToImage() != null){
                Glide.with(view)
                        .load(article.getUrlToImage())
                        .into(image);
            }
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public ArticleAdapter(ArrayList<NewsArticle> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View articleView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.article_card, parent, false);

            return new ArticleViewHolder(articleView);
        } else {
            View loadingView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.loading_item, parent, false);
            return new LoadingViewHolder(loadingView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            bindArticle((ArticleViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            bindLoading((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return articles.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    private void bindArticle(ArticleViewHolder viewHolder, int position) {
        NewsArticle at = articles.get(position);
        viewHolder.bind(at);
    }

    private void bindLoading(LoadingViewHolder viewHolder, int position) {
    }
}
