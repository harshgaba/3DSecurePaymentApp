package com.example.a3dsecurepaymentapp.di

import com.example.a3dsecurepaymentapp.common.Constants
import com.example.a3dsecurepaymentapp.data.remote.PaymentApi
import com.example.a3dsecurepaymentapp.data.repository.PaymentRepositoryImpl
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(logger)
        return client.build()
    }

    @Provides
    @Singleton
    fun providePaymentApi(okHttpClient: OkHttpClient): PaymentApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(api: PaymentApi): PaymentRepository {
        return PaymentRepositoryImpl(api)
    }
}