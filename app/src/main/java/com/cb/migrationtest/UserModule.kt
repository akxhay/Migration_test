package com.cb.migrationtest

import android.content.Context
import androidx.room.Room
import com.cb.migrationtest.UserDatabase.Companion.DATABASE_NAME
import com.cb.migrationtest.UserRoomDatabase.Companion.MIGRATION_1_2
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
    fun providesDao(userDatabase: UserRoomDatabase): UserDao =
        userDatabase.userDao()

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): UserRoomDatabase =
        Room.databaseBuilder(context, UserRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()


    @Provides
    fun providesRepository(userDao: UserDao): UserRepository =
        UserRepositoryImpl(userDao)
}