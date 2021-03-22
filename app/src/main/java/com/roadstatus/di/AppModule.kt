package com.roadstatus.di

import com.google.gson.Gson
import com.roadstatus.BuildConfig
import com.roadstatus.mapper.RoadStatusErrorMapper
import com.roadstatus.mapper.RoadStatusSuccessMapper
import com.roadstatus.network.RoadStatusService
import com.roadstatus.repository.RoadStatusRepository
import com.roadstatus.repository.RoadStatusRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(
    ActivityRetainedComponent::class
)
class AppModule {

    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideRoadStatusService(): RoadStatusService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()

        val timeout = 20L
        httpClient.connectTimeout(timeout, TimeUnit.SECONDS)
        httpClient.readTimeout(timeout, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.APP_BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RoadStatusService::class.java)
    }

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideRoadStatusRepository(
        roadStatusService: RoadStatusService,
        roadStatusSuccessMapper: RoadStatusSuccessMapper,
        roadStatusErrorMapper: RoadStatusErrorMapper,
        gson: Gson
    ): RoadStatusRepository = RoadStatusRepositoryImpl(
        roadStatusService,
        roadStatusSuccessMapper,
        roadStatusErrorMapper,
        gson
    )
}