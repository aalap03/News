package com.example.aalap.news

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aalap.news.models.Article
import com.squareup.picasso.Picasso

class ArticleAdapter(var context: Context, var list: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    var picasso: Picasso = Picasso.get()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleHolder {
        return ArticleHolder(LayoutInflater.from(context).inflate(R.layout.news_item, p0, false))
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

        holder.date.text = article.publishedAt

        Log.d(TAG, "Height: ${holder.image.layoutParams.height}")
        Log.d(TAG, "width: ${holder.image.measuredWidth}")
        picasso
                .load(article.urlToImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.image)
    }

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.article_title)
        var author = itemView.findViewById<TextView>(R.id.article_author)
        var date = itemView.findViewById<TextView>(R.id.article_published_date)
        var image = itemView.findViewById<ImageView>(R.id.article_image)
    }
}