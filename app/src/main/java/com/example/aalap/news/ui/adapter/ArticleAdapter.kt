package com.example.aalap.news.ui.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aalap.news.Pref
import com.example.aalap.news.R
import com.example.aalap.news.Utils
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.ui.activities.Webview
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ArticleAdapter(var context: Context, var list: MutableList<Article>, var screenWidth: Int) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>(), AnkoLogger {

    private var picasso: Picasso = Picasso.get()
    var pref = Pref(context.applicationContext)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ArticleHolder {

        val layout = if (pref.isLayoutCompact())
            R.layout.news_item_compact
        else
            R.layout.news_item_grid

        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        view.findViewById<CardView>(R.id.news_card).setCardBackgroundColor(ColorStateList.valueOf(
                if (pref.isDarkTheme())
                    ContextCompat.getColor(context, R.color.cardBG_dark)
                else
                    ContextCompat.getColor(context, R.color.cardBG_light)
        ))

        return ArticleHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {

        val article = list[position]

        info { "Article: $article" }

        holder.title.text = article.title

        if (pref.isLayoutCompact()) {
            holder.image.layoutParams.width = screenWidth / 4
            holder.image.layoutParams.height = screenWidth / 4
        } else {
            holder.image.layoutParams.height = screenWidth / 2
        }

        if (!TextUtils.isEmpty(article.author)) {
            holder.author.visibility = View.VISIBLE
            holder.author.text = article.author
        } else {
            holder.author.visibility = View.GONE
        }

        holder.date.text = Utils().getLocaleTime(article.publishedAt)

        if (TextUtils.isEmpty(article.urlToImage))
            holder.image.setImageResource(R.drawable.gradient_1)
        else
            picasso.load(article.urlToImage)
                    .error(R.drawable.gradient_1)
                    .fit()
                    .placeholder(R.drawable.gradient_1)
                    .into(holder.image)

        holder.itemView.setOnClickListener { holder.bindClicks(article) }
    }

    fun setNewData(articles: MutableList<Article>) {
        list.clear()
        list.addAll(articles)
        notifyDataSetChanged()
    }

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.article_title)
        var author: TextView = itemView.findViewById(R.id.article_author)
        var date: TextView = itemView.findViewById(R.id.article_published_date)
        var image: ImageView = itemView.findViewById(R.id.article_image)

        fun bindClicks(article: Article) {
            val intent = Intent(context, Webview::class.java)
            intent.putExtra("url", article.url)
            intent.putExtra("title", article.title)
            context.startActivity(intent)
        }
    }
}