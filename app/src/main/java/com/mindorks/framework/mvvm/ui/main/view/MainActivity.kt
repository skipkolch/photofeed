package com.mindorks.framework.mvvm.ui.main.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mindorks.framework.mvvm.BuildConfig
import com.mindorks.framework.mvvm.R
import com.mindorks.framework.mvvm.ui.main.adapter.MainAdapter
import com.mindorks.framework.mvvm.ui.main.viewmodel.MainViewModel
import com.mindorks.framework.mvvm.utils.Status
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val mapper = jacksonObjectMapper()

    private lateinit var adapter: MainAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var pref: SharedPreferences
    private var page: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.pref = getSharedPreferences(BuildConfig.STORAGE_TABLE, Context.MODE_PRIVATE)
        this.recyclerView = findViewById(R.id.recycler_view)
        this.progressBar = findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter

        findViewById<NestedScrollView>(R.id.scroll_view).setOnScrollChangeListener { view: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            scrollEnd(view, scrollY)
        }

        mainViewModel.photos.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.also { photos -> adapter.update(hashSetOf(), photos) }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun scrollEnd(view: NestedScrollView?, scrollY: Int) {
        val child = view?.getChildAt(0)
        val lastItem = child?.measuredHeight?.minus(view.measuredHeight) ?: 0
        if (scrollY >= lastItem) {
            progressBar.visibility = View.VISIBLE
            mainViewModel.fetch(page++)
        }
    }

    override fun onStart() {
        super.onStart()
        pref.getString(BuildConfig.STORAGE_KEY, null)?.also { it ->
            adapter.update(mapper.readValue(it), arrayListOf())
        }
    }

    override fun onPause() {
        super.onPause()
        val edit = pref.edit()
        edit?.putString(BuildConfig.STORAGE_KEY, mapper.writeValueAsString(adapter.favorites))
        edit?.apply()
    }

}
