package com.miharu.imagelist.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.miharu.imagelist.R
import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.databinding.ItemPhotoAlbumsBinding

class AlbumsListAdapter(val albumsList : MutableList<Photo>) :
        RecyclerView.Adapter<AlbumsListAdapter.AlbumsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsItemViewHolder {
        val binding = ItemPhotoAlbumsBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
        return AlbumsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumsItemViewHolder, position: Int) {
        holder.setupData(albumsList[position])
    }

    override fun getItemCount(): Int = albumsList.size

    inner class AlbumsItemViewHolder(private val binding: ItemPhotoAlbumsBinding):
            RecyclerView.ViewHolder(binding.root){

        private var photo : Photo? = null

        init {
            binding.root.setOnClickListener {
                val direction = PhotoAlbumsFragmentDirections
                        .actionAlbumsFragmentToPhotoDetailFragment(photo)
                binding.root.findNavController().navigate(direction)
            }
        }

        fun setupData(photo: Photo){
            this.photo = photo

            val url = GlideUrl(photo.thumbnailUrl, LazyHeaders.Builder()
                    .addHeader("User-Agent",WebSettings.getDefaultUserAgent(binding.root.context))
                    .build())
            Glide.with(binding.root.context)
                    .load(url)
                    .error(R.drawable.ic_photo_placeholder)
                    .into(binding.photoImageView)

            binding.titleTextView.text = photo.title
        }
    }
}