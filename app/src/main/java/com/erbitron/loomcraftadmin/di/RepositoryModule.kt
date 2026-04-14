package com.erbitron.loomcraftadmin.di

import com.erbitron.loomcraftadmin.data.api.AuthApi
import com.erbitron.loomcraftadmin.data.api.OrderApi
import com.erbitron.loomcraftadmin.data.local.TokenManager
import com.erbitron.loomcraftadmin.data.repository.AuthRepository
import com.erbitron.loomcraftadmin.data.repository.OrderRepository
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
