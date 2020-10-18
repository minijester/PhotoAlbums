package com.miharu.imagelist.data.remote

import com.miharu.imagelist.data.model.Photo
import com.miharu.imagelist.data.model.Resource
import com.miharu.imagelist.data.remote.Endpoints.BASE_URL
import com.miharu.imagelist.data.remote.Endpoints.PHOTOALUMBS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import java.util.logging.Level

interface ApiService {

    @GET("$PHOTOALUMBS/{page}/photos")
    suspend fun getPhotoAlbums(
        @Path("page") page : Int
    ) : List<Photo>

    companion object{

        private const val HTTP_READ_TIMEOUT = 10_000L
        private const val HTTP_CONNECT_TIMEOUT = 6_000L

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level =
                HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .method(original.method, original.body)
                        .build()

                    return@addInterceptor chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}