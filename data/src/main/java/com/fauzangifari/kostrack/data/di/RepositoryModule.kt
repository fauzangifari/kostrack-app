package com.fauzangifari.kostrack.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fauzangifari.kostrack.data.repository.AuthRepositoryImpl
import com.fauzangifari.kostrack.data.repository.PaymentRepositoryImpl
import com.fauzangifari.kostrack.data.repository.PropertyRepositoryImpl
import com.fauzangifari.kostrack.data.repository.RoomRepositoryImpl
import com.fauzangifari.kostrack.data.repository.SessionRepositoryImpl
import com.fauzangifari.kostrack.data.repository.TenantRepositoryImpl
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.PaymentRepository
import com.fauzangifari.kostrack.domain.repository.PropertyRepository
import com.fauzangifari.kostrack.domain.repository.RoomRepository
import com.fauzangifari.kostrack.domain.repository.SessionRepository
import com.fauzangifari.kostrack.domain.repository.TenantRepository
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

val repositoryModule = module {
    single<SessionRepository> { SessionRepositoryImpl(get<Context>().dataStore) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<PropertyRepository> { PropertyRepositoryImpl(get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single<TenantRepository> { TenantRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }
}