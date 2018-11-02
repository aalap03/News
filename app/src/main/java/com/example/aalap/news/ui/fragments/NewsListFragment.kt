package layout

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.presenter.Presenter
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.MainView
import kotlinx.android.synthetic.main.news_list_frag.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

const val CATEGORY = "category"

class NewsListFragment : Fragment(), MainView, AnkoLogger {

    private lateinit var presenter: Presenter
    private lateinit var adapter: ArticleAdapter

    companion object {

        fun newInstance(category: String): Fragment {
            val fragment = NewsListFragment()
            val args = Bundle()
            args.putString(CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.news_list_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString(CATEGORY)
        presenter = Presenter(this, NewsModel())
        new_recycler.layoutManager = LinearLayoutManager(requireContext())

        info { "Fragment Category: $category" }


        if (!TextUtils.isEmpty(category))
            category?.let { presenter.getAllHeadlinesByCategory(it) }
        else
            showError("No categories found")
    }

    override fun loading(isVisible: Boolean) {
        refresh_layout.isRefreshing = true
    }

    override fun showError(errorMsg: String?) {
        refresh_layout.isRefreshing = false
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT)
                .show()
    }

    override fun displayArticles(articles: List<Article>) {
        adapter = ArticleAdapter(requireContext(), articles)
        new_recycler.adapter = adapter
        refresh_layout.isRefreshing = false
    }
}