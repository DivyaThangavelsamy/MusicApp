package com.lastfm.musicapp.di

import android.content.Context
import com.lastfm.musicapp.BuildConfig.API_KEY
import com.lastfm.musicapp.repository.ArtistRepository
import com.lastfm.musicapp.repository.ArtistRepositoryImpl
import com.lastfm.musicapp.service.Constants
import com.lastfm.musicapp.service.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    class AuthTokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val originalRequest = chain.request()
            val originalHttpUrl: HttpUrl = originalRequest.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("format", "json")
                .build()

            val requestBuilder: Request.Builder = originalRequest.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun myHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor(AuthTokenInterceptor())
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(myHttpClient())
            .baseUrl(Constants.BASE_URL).build()
    }

    @Provides
    @Singleton
    fun provideArtists(retrofit: Retrofit): WebService {
        return retrofit.create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideRepositoryImpl(repository: ArtistRepositoryImpl): ArtistRepository {
        return repository
    }
}
