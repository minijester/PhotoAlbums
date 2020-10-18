package com.miharu.imagelist.ui.albums

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.miharu.imagelist.MainCoroutineRule
import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.data.model.Status
import com.miharu.imagelist.data.repository.AlbumsRepository
import com.miharu.imagelist.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AlbumsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var repository: AlbumsRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        repository = mock(AlbumsRepository::class.java)
        albumsViewModel = AlbumsViewModel(repository)
    }


    @Test
    fun testLoading() {
        albumsViewModel.loadPhotoAlbums()
        assertThat(albumsViewModel.albumsList.getOrAwaitValue().status, `is`(Status.LOADING))
    }

    @Test
    fun testGetData() {
        val photo1 = Photo("1","1","title1",
                "url1","thumbUrl1")
        runBlockingTest {
            `when`(repository.getAlbums(albumsViewModel.currentPage))
                    .thenReturn(listOf(photo1))
        }
        albumsViewModel.loadPhotoAlbums()

        runBlockingTest {
            delay(1000)
        }
        assertThat(albumsViewModel.albumsList.getOrAwaitValue().status, `is`(Status.SUCCESS))
        assertThat(albumsViewModel.albumsList.getOrAwaitValue().data, `is`(listOf(photo1)))
    }

}