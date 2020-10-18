package com.miharu.imagelist.ui.albums

import android.view.View
import com.miharu.imagelist.R
import com.miharu.imagelist.databinding.ActivityPhotoAblumsBinding
import com.miharu.imagelist.ui.core.ActivityBase

class PhotoAlbumsActivity : ActivityBase() {

    private lateinit var binding: ActivityPhotoAblumsBinding

    override fun getLayoutView(): View {
        binding = ActivityPhotoAblumsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setupContent() {
        setSupportActionBar(binding.toolbar)

    }
}