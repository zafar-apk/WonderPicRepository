package apk.zafar.wonderpic.di

import apk.zafar.wonderpic.BuildConfig
import apk.zafar.wonderpic.IMAGES_BASE_URL
import apk.zafar.wonderpic.model.network.images.ImageApi
import apk.zafar.wonderpic.model.network.images.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.file.attribute.AclEntry.newBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImagesNetworkModule {

    @ImagesClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", BuildConfig.IMAGES_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @ImagesRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(
        @ImagesClient client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(IMAGES_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideImageApi(
        @ImagesRetrofit retrofit: Retrofit
    ): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImagesRepository(
        api: ImageApi
    ): ImagesRepository {
        return ImagesRepository(api)
    }

}