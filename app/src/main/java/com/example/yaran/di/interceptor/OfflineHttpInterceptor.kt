package com.example.yaran.di.interceptor


import com.blankj.utilcode.util.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response


class OfflineHttpInterceptor :
    Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val forceCacheResponse = chain.proceed(request)

        if (!NetworkUtils.isConnected()) {

            request = if (forceCacheResponse.code != 504) {
                request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
            } else {
                request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
        }
        return chain.proceed( request )
//
////        val build = request.cacheControl(
////            CacheControl.Builder()
////                .onlyIfCached()
////                .build()
////        )
////            .url("http://publicobject.com/helloworld.txt")
////            .build()


//        return forceCacheResponse


    }
}