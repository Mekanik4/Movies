package com.example.movies.module

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.movies.utils.Constants.Companion.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {


    @Singleton
    @Provides
    @SharedPreferencesAnn
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    @EncryptedSharedPreferencesAnn
    fun provideEncryptedSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        val masterKey: MasterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // If an exception is thrown here, rename the filename as a temporary solution.
        return EncryptedSharedPreferences.create(
            context,
            "shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedPreferencesAnn

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedSharedPreferencesAnn

fun SharedPreferences.get(string: String): String? {
    return this.getString(string, "")
}

fun SharedPreferences.put(string: String, value: String) {
    this.edit().putString(string, value).commit()
}