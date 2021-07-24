package apk.zafar.wonderpic.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VKClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagesClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VKRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagesRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VKApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagesApi