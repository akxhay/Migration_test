package com.cb.migrationtest

import android.content.Context
import androidx.room.Room
import com.cb.cb_test.application.data.database.UserDatabase
import com.cb.cb_test.application.data.database.UserDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SenderModule {


    @Provides
    fun providesDao(userDatabase: UserDatabase): UserDao =
        userDatabase.userDao()

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME)
            .build()


    @Provides
    fun providesRepository(userDao: UserDao): UserRepository =
        UserRepositoryImpl(userDao)
}