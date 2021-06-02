package com.longjunhao.wanjetpack.di

import com.longjunhao.wanjetpack.api.WanJetpackApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * .NetWorkModule
 *
 * @author Admitor
 * @date 2021/05/24
 */

@InstallIn(SingletonComponent::class)
@Module
class NetWorkModule {

    @Singleton
    @Provides
    fun provideWanJetpackApi(): WanJetpackApi {
        return WanJetpackApi.create()
    }
}