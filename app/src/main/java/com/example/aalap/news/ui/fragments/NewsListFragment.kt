package layout

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.aalap.news.R
import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.pref
import com.example.aalap.news.presenter.NewsPresenter
import com.example.aalap.news.ui.adapter.ArticleAdapter
import com.example.aalap.news.view.NewsListView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.news_list_frag.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.windowManager

const val CATEGORY = "category"

class NewsListFragment : Fragment(), NewsListView, AnkoLogger {

    private lateinit var presenter: NewsPresenter
    private var adapter: ArticleAdapter? = null
    private var widthScreen: Int = 0
    var category: String = "Anything"
    val articles = mutableListOf<Article>()

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
        presenter = NewsPresenter(this, NewsModel())
        var manager = if (pref.isLayoutCompact())
            LinearLayoutManager(requireContext())
        else
            GridLayoutManager(requireContext(), 2)

        new_recycler.layoutManager = manager

        val displayMetrics = DisplayMetrics()
        context?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        widthScreen = displayMetrics.widthPixels

        presenter.requestHeadlinesByCountryAndCategory(category)

        adapter = ArticleAdapter(requireContext(), articles, widthScreen)
        new_recycler?.adapter = adapter

        refresh_layout.setOnRefreshListener {
            category.let { presenter.requestHeadlinesByCountryAndCategory(it) }
        }
    }

    override fun displayArticlesR(articles: MutableList<Article>?) {

        if (articles?.isEmpty() == true) {
            empty_view.visibility = View.VISIBLE
            new_recycler.visibility = View.GONE
        } else {
            empty_view.visibility = View.GONE
            new_recycler.visibility = View.VISIBLE
            articles?.let { adapter?.setNewData(it) }
            refresh_layout?.isRefreshing = false
        }
    }

    override fun loading(isVisible: Boolean) {
        refresh_layout.isRefreshing = true
    }

    override fun showError(errorMsg: String?) {
        refresh_layout.isRefreshing = false
        errorMsg?.let {
            Toasty.error(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
        }
        if (adapter == null || adapter?.itemCount == 0) {
            empty_view.visibility = View.VISIBLE
            new_recycler.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.dispose()
    }
}