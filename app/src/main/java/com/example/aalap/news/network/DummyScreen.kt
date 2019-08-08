package com.example.aalap.news.network

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import com.example.aalap.news.R
import com.example.aalap.news.simpleToast
import com.example.aalap.news.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.dummy_layout.*
import kotlinx.android.synthetic.main.toolbar_template.*
import org.jetbrains.anko.info

class DummyScreen : BaseActivity(), LoginView {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = LoginPresenter(this, LoginModel())

        edit_text.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val domainName = edit_text.text.toString()

                presenter.requestRegionFromDomain(domainName)
            }
            false
        }
    }

    override fun domainSuccess() {
        simpleToast("Success")
    }

    override fun domainFailed(errorMsg: String) {
        info { "Error: $errorMsg" }
        simpleToast(errorMsg)
    }

    override fun layoutResID(): Int {
        return R.layout.dummy_layout
    }

    override fun getToolbar(): Toolbar {
        return news_toolbar
    }

    override fun getToolbarTitle(): String {
        return "Dummy Screen"
    }

}