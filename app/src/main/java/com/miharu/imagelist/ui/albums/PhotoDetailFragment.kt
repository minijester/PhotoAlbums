package com.miharu.imagelist.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.miharu.imagelist.R
import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.databinding.FragmentPhotoDetailBinding
import com.miharu.imagelist.ui.core.FragmentBase

class PhotoDetailFragment : FragmentBase() {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PhotoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setupContent() {
        (activity as PhotoAlbumsActivity).supportActionBar?.title = args.photo?.title
        (activity as PhotoAlbumsActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        context?.let { context ->
            val photoUrl = GlideUrl(args.photo?.url, LazyHeaders.Builder()
                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(context))
                .build())

            Glide.with(context)
                .load(photoUrl)
                .error(R.drawable.ic_photo_placeholder)
                .into(binding.photoImageView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.root.findNavController().navigateUp()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}