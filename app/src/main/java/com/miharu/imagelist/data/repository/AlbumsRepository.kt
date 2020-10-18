package com.miharu.imagelist.data.repository

import com.miharu.imagelist.data.remote.ApiService

class AlbumsRepository(private val service : ApiService) {

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AlbumsRepository? = null

        fun getInstance(service: ApiService) =
                instance ?: synchronized(this) {
                    instance ?: AlbumsRepository(service).also { instance = it }
                }
    }

    suspend fun getAlbums(page : Int) = service.getPhotoAlbums(page)

}