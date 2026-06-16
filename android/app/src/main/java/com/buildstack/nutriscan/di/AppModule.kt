package com.buildstack.nutriscan.di

import android.content.Context
import com.buildstack.nutriscan.data.remote.AuthApi
import com.buildstack.nutriscan.data.repository.AuthRepositoryImpl
import com.buildstack.nutriscan.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val BASE_URL = com.buildstack.nutriscan.BuildConfig.API_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: com.buildstack.nutriscan.data.local.prefs.TokenManager): okhttp3.OkHttpClient {
        return okhttp3.OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = kotlinx.coroutines.runBlocking {
                    tokenManager.token.firstOrNull()
                }
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: okhttp3.OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMotivationApiService(retrofit: Retrofit): com.buildstack.nutriscan.data.remote.MotivationApiService {
        return retrofit.create(com.buildstack.nutriscan.data.remote.MotivationApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScanApiService(retrofit: Retrofit): com.buildstack.nutriscan.data.remote.ScanApiService {
        return retrofit.create(com.buildstack.nutriscan.data.remote.ScanApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideScanDatabase(@ApplicationContext context: Context): com.buildstack.nutriscan.data.local.ScanDatabase {
        return androidx.room.Room.databaseBuilder(
            context,
            com.buildstack.nutriscan.data.local.ScanDatabase::class.java,
            "scan_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideScanDao(database: com.buildstack.nutriscan.data.local.ScanDatabase): com.buildstack.nutriscan.data.local.ScanDao {
        return database.scanDao
    }

    @Provides
    @Singleton
    fun provideGson(): com.google.gson.Gson {
        return com.google.gson.Gson()
    }
    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): com.buildstack.nutriscan.data.remote.ProfileApi {
        return retrofit.create(com.buildstack.nutriscan.data.remote.ProfileApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: com.buildstack.nutriscan.data.repository.AuthRepositoryImpl
    ): com.buildstack.nutriscan.domain.repository.AuthRepository

    @Binds
    @Singleton
    abstract fun bindScanRepository(
        scanRepositoryImpl: com.buildstack.nutriscan.data.repository.ScanRepositoryImpl
    ): com.buildstack.nutriscan.domain.repository.ScanRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: com.buildstack.nutriscan.data.repository.ProfileRepositoryImpl
    ): com.buildstack.nutriscan.domain.repository.ProfileRepository
}
