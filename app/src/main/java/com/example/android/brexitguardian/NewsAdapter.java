package com.example.android.brexitguardian;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private NewsActivity articleContext;
    private ArrayList<News> articleNews;
    private OnItemClickListener articleListener;

    NewsAdapter(NewsActivity context, ArrayList<News> news, OnItemClickListener listener) {
        articleContext = context;
        articleNews = news;
        articleListener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(News news);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImageView;
        TextView articleTitleTextView;
        TextView articleAuthorTextView;
        TextView articleSectionTextView;
        TextView articleDateTextView;

        ViewHolder(View itemView) {
            super(itemView);

            articleImageView = (ImageView) itemView.findViewById(R.id.article_image);
            articleTitleTextView = (TextView) itemView.findViewById(R.id.article_title);
            articleAuthorTextView = (TextView) itemView.findViewById(R.id.article_author);
            articleSectionTextView = (TextView) itemView.findViewById(R.id.article_section);
            articleDateTextView = (TextView) itemView.findViewById(R.id.article_date);
        }

        void bind(final News news, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(news);
                }
            });
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {

        News news = articleNews.get(position);

        holder.articleTitleTextView.setText(news.getTitle());
        holder.articleAuthorTextView.setText(news.getAuthor());
        holder.articleSectionTextView.setText(news.getSection());
        holder.articleDateTextView.setText(news.getDate());

        //Picasso Library to convert the url from JSONObject imageLinks to an image(@thumbnail)
        Picasso.with(articleContext).load(news.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.articleImageView);

        holder.bind(articleNews.get(position), articleListener);
    }

    @Override
    public int getItemCount() {
        return articleNews.size();
    }

    void clear() {
        articleNews.clear();
        notifyDataSetChanged();
    }

    void addAll(List<News> news) {
        articleNews.addAll(news);
        notifyDataSetChanged();
    }
}
