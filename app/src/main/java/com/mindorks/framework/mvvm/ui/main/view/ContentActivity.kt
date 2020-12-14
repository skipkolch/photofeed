package com.mindorks.framework.mvvm.ui.main.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mindorks.framework.mvvm.R
import com.mindorks.framework.mvvm.utils.NetworkHelper
import kotlinx.android.synthetic.main.content_layout.*
import org.koin.android.ext.android.inject

class ContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_layout)

        val networkHelper : NetworkHelper by inject()
        if(networkHelper.isNetworkConnected()) {
            Glide.with(this)
                .load(intent.getStringExtra("image"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(full_image)
            progress_bar.visibility = View.GONE
            full_image.visibility = View.VISIBLE
        }

    }
}