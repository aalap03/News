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
import com.example.aalap.news.ui.activities.Webview
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ArticleAdapter(var context: Context, var list: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>(), AnkoLogger {

    var picasso: Picasso = Picasso.get()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleHolder {
        return ArticleHolder(LayoutInflater.from(context).inflate(R.layout.news_item_grid, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val article = list[position]
        holder.title.text = article.title

        if (!TextUtils.isEmpty(article.author)) {
            holder.author.visibility = View.VISIBLE
            holder.author.text = article.author
        } else {
            holder.author.visibility = View.GONE
        }

        holder.date.text = Utils().getLocaleTime(article.publishedAt)

        if (TextUtils.isEmpty(article.urlToImage))
            holder.image.setImageResource(R.mipmap.ic_launcher_round)
        else
            picasso.load(article.urlToImage).error(R.mipmap.ic_launcher_round).resize(Utils().dpToPx(70), Utils().dpToPx(70)).centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.image)

        holder.itemView.setOnClickListener { holder.bindClicks(article) }
    }

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.article_title)
        var author: TextView = itemView.findViewById(R.id.article_author)
        var date: TextView = itemView.findViewById(R.id.article_published_date)
        var image: ImageView = itemView.findViewById(R.id.article_image)

        fun bindClicks(article: Article) {
            info { "Opening..." }
            val intent = Intent(context, Webview::class.java)
            intent.putExtra("url", article.url)
            intent.putExtra("title", article.title)
            context.startActivity(intent)
        }
    }
}