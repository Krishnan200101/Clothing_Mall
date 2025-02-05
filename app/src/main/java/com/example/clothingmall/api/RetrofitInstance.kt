package com.example.clothingmall.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object RetrofitInstance {
    private const val BASE_URL = "https://petlandshop.store/"
    private const val EXTERNAL_BASE_URL = "https://raw.githubusercontent.com/"

    private val unsafeOkHttpClient: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unsafeOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val externalRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(EXTERNAL_BASE_URL)
            .client(unsafeOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val reviewApi: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }

    val externalApi: ExternalApi by lazy {
        externalRetrofit.create(ExternalApi::class.java)
    }
} 