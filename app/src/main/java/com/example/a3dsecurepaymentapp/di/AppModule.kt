package com.example.a3dsecurepaymentapp.di

import com.example.a3dsecurepaymentapp.common.Constants
import com.example.a3dsecurepaymentapp.data.remote.PaymentApi
import com.example.a3dsecurepaymentapp.data.repository.PaymentRepositoryImpl
import com.example.a3dsecurepaymentapp.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaymentApi(): PaymentApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(api: PaymentApi): PaymentRepository {
        return PaymentRepositoryImpl(api)
    }
}