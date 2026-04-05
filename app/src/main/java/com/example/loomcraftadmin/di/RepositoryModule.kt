package com.example.loomcraftadmin.di

import com.example.loomcraftadmin.data.api.AuthApi
import com.example.loomcraftadmin.data.api.OrderApi
import com.example.loomcraftadmin.data.local.TokenManager
import com.example.loomcraftadmin.data.repository.AuthRepository
import com.example.loomcraftadmin.data.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, tokenManager: TokenManager): AuthRepository {
        return AuthRepository(authApi, tokenManager)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderApi: OrderApi): OrderRepository {
        return OrderRepository(orderApi)
    }
}
