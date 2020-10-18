package com.miharu.imagelist.ui.albums

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.data.model.Resource
import com.miharu.imagelist.data.model.Status
import com.miharu.imagelist.data.repository.AlbumsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumsViewModel(private val albumsRepository : AlbumsRepository)
    : ViewModel() {

    private val _albumsList = MutableLiveData<Resource<List<Photo>>>()
    val albumsList : LiveData<Resource<List<Photo>>> = _albumsList

    @VisibleForTesting
    var currentPage = 1

    fun loadPhotoAlbums(page : Int = currentPage){
        _albumsList.value = Resource.loading()
        var albums = listOf<Photo>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                albums = albumsRepository.getAlbums(page)
            }
            try {
                _albumsList.value = Resource.success(albums)
            } catch (e : Exception){
                _albumsList.value = Resource.error(e.message ?: "Error occur.")
            }
        }
    }

    fun resetPage(){
        currentPage = 1
    }
}

@Suppress("UNCHECKED_CAST")
class AlbumsViewModelFactory (private val albumsRepository: AlbumsRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (AlbumsViewModel(albumsRepository) as T)
}