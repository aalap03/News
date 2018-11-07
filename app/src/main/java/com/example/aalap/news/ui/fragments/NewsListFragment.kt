package layout

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
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
import com.example.aalap.news.ui.activities.CategoryTabActivity
import com.example.aalap.news.ui.activities.SendQuery
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.NewsFragmentView
import kotlinx.android.synthetic.main.news_list_frag.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.windowManager

const val CATEGORY = "category"

class NewsListFragment : Fragment(), NewsFragmentView, AnkoLogger, SendQuery {

    override fun sendQuery(query: String) {
        info { "Everything: $query" }
        presenter.getEverythingArticle(query, 1, 100)
    }

    private lateinit var presenter: Presenter
    private lateinit var adapter: ArticleAdapter
    private var widthScreen: Int = 0
    var category: String = "Anything"

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

        news_toolbar.visibility = View.GONE
        category = arguments?.getString(CATEGORY) ?: "Anything"
        presenter = Presenter(this, NewsModel())
        new_recycler.layoutManager = LinearLayoutManager(requireContext())

        info { "Fragment Category: $category" }
        val displayMetrics = DisplayMetrics()
        context?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        widthScreen = displayMetrics.widthPixels

        if (!TextUtils.isEmpty(category))
            category?.let { presenter.getAllHeadlinesByCategory(it) }
        else
            showError("No categories found")

        refresh_layout.setOnRefreshListener {
            category?.let { presenter.getAllHeadlinesByCategory(it) }
        }
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
        adapter = ArticleAdapter(requireContext(), articles, widthScreen)
        info { "Category: $category" }
        new_recycler?.adapter = adapter
        refresh_layout?.isRefreshing = false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (context as CategoryTabActivity).sendQuery = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.dispose()
    }
}