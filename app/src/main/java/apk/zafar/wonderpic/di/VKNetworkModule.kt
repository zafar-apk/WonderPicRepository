package apk.zafar.wonderpic.di

import apk.zafar.wonderpic.BASE_URL
import apk.zafar.wonderpic.model.network.vk.VKRepository
import apk.zafar.wonderpic.model.network.vk.VKApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VKNetworkModule {

    @VKClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(BODY)
            )
            .build()
    }

    @VKRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(
        @VKClient client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVkApi(
        @VKRetrofit retrofit: Retrofit
    ): VKApi {
        return retrofit.create(VKApi::class.java)
    }


    @Provides
    @Singleton
    fun provideVkRepository(
        vkApi: VKApi
    ): VKRepository {
        return VKRepository(vkApi)
    }

}