package com.lloyds.media.infra.network

import com.lloyds.media.infra.local.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author: Gokul Kalagara
 * @Date: 06/06/24
 *
 */
object RetrofitAdapter {
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.Apis.BASE_URL)
            .build()
    }
}