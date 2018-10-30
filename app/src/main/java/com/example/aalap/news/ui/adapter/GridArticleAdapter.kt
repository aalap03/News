package com.example.aalap.news.ui.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aalap.news.R
import com.example.aalap.news.Utils
import com.example.aalap.news.models.Article
import com.example.aalap.news.ui.activities.TAG
import com.example.aalap.news.ui.activities.Webview
import com.squareup.picasso.Picasso

class GridArticleAdapter(var context: Context, var list: List<Article>, var screenWidth: Int) : RecyclerView.Adapter<GridArticleAdapter.ArticleHolder>() {

    var picasso: Picasso = Picasso.get()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleHolder {
        return ArticleHolder(LayoutInflater.from(context).inflate(R.layout.news_item_grid, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.itemView.layoutParams.width = screenWidth / 2
        holder.image.layoutParams.width = screenWidth / 2
        holder.image.layoutParams.height = screenWidth / 2

        val article = list[position]
        holder.title.text = article.title

        if (!TextUtils.isEmpty(article.author))
            holder.author.text = article.author
        else
            holder.author.text = "Author N/A"


        holder.date.text = Utils().getLocaleTime(article.publishedAt)

        if (TextUtils.isEmpty(article.urlToImage))
            picasso.load(R.drawable.ic_news_app_launcher).error(R.drawable.ic_news_app_launcher).placeholder(R.drawable.ic_news_app_launcher).into(holder.image)
        else
            picasso.load(article.urlToImage).error(R.drawable.ic_news_app_launcher).placeholder(R.drawable.ic_news_app_launcher).into(holder.image)

        holder.itemView.setOnClickListener { holder.bindClicks(article) }
    }

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.article_title)
        var author: TextView = itemView.findViewById(R.id.article_author)
        var date: TextView = itemView.findViewById(R.id.article_published_date)
        var image: ImageView = itemView.findViewById(R.id.article_image)

        fun bindClicks(article: Article) {
            Log.d(TAG, "Opening...")
            val intent = Intent(context, Webview::class.java)
            intent.putExtra("url", article.url)
            intent.putExtra("title", article.title)
            context.startActivity(intent)
        }
    }

}