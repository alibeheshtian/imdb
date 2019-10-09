package com.example.yaran.di

import android.content.Context
import com.blankj.utilcode.util.NetworkUtils
import com.example.yaran.BuildConfig
import com.example.yaran.webService.ApiService
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


private const val MODULE_NAME = "Network Module"
val networkModule = Module(MODULE_NAME, false) {
    bind<OkHttpClient>() with singleton { getOkHttpClient(instance()) }
    bind<Retrofit>() with singleton { getRetrofit(instance()) }
    bind<ApiService>() with singleton { getApiService(instance()) }
}


private fun getOkHttpClient(context: Context): OkHttpClient {

    return OkHttpClient.Builder().apply {

        addNetworkInterceptor(provideOfflineCacheInterceptor())

        addNetworkInterceptor(provideCacheInterceptor())

        cache(provideCache(context))
        addInterceptor(HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })


    }.build()
}


private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        client(okHttpClient).build()
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }.build()
}


private fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


private fun provideOfflineCacheInterceptor(): Interceptor {

    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            if (!NetworkUtils.isConnected()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            return chain.proceed(request)
        }

    }
}

private fun provideCache(context: Context): Cache {
    val httpCacheDirectory = File(context.cacheDir, "http-cache")
    val cacheSize: Long = (10 * 1024 * 1024)

    return Cache(httpCacheDirectory, cacheSize)
}

private fun provideCacheInterceptor(): Interceptor {

    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())

            // re-write response header to force use of cache

            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()
            return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }

    }
}
