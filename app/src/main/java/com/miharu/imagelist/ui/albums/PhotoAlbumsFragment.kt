package com.miharu.imagelist.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miharu.imagelist.R
import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.data.model.Resource
import com.miharu.imagelist.data.model.Status
import com.miharu.imagelist.data.remote.ApiService
import com.miharu.imagelist.data.repository.AlbumsRepository
import com.miharu.imagelist.databinding.FragmentPhotoAlbumsBinding
import com.miharu.imagelist.ui.core.FragmentBase

class PhotoAlbumsFragment : FragmentBase() {

    private var _binding: FragmentPhotoAlbumsBinding? = null
    private val binding get() = _binding!!

    private lateinit var albumsAdapter : AlbumsListAdapter

    private val albumsViewModel by viewModels<AlbumsViewModel>{
        AlbumsViewModelFactory(AlbumsRepository.getInstance(ApiService.create()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoAlbumsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setupContent() {
        (activity as PhotoAlbumsActivity).supportActionBar?.title = getString(R.string.photo_title)
        (activity as PhotoAlbumsActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        albumsAdapter = AlbumsListAdapter(mutableListOf())
        binding.albumsList.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = albumsAdapter
        }
        loadAlbums()

        albumsViewModel.albumsList.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Status.SUCCESS -> {
                    addData(response.data)
                }

                Status.LOADING -> {
                    showLoading()
                }

                Status.ERROR -> {
                    showError(response.msg)
                }
            }
        }
    }

    private fun addData(data: List<Photo>?) {
        binding.photoShimmerLayout.hideShimmer()
        binding.photoShimmerLayout.visibility = View.GONE
        data?.let {
            val position = albumsAdapter.albumsList.lastIndex
            albumsAdapter.albumsList.addAll(data)
            albumsAdapter.notifyItemInserted(position)
        }
    }

    private fun showError(errorMessage : String?) {
        binding.photoShimmerLayout.hideShimmer()
        binding.photoShimmerLayout.visibility = View.GONE
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.albums_titles))
                .setMessage(errorMessage)
                .setPositiveButton(getString(R.string.ok_button),null)
                .show()
    }

    private fun showLoading() {
        binding.photoShimmerLayout.visibility = View.VISIBLE
        binding.photoShimmerLayout.showShimmer(true)
    }

    private fun loadAlbums(){
        albumsViewModel.loadPhotoAlbums()
    }
}