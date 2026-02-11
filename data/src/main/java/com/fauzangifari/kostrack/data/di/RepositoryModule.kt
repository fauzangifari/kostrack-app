package com.fauzangifari.kostrack.data.di

import com.fauzangifari.kostrack.data.repository.AuthRepositoryImpl
import com.fauzangifari.kostrack.data.repository.SessionRepositoryImpl
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import com.fauzangifari.kostrack.domain.repository.SessionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<SessionRepository> { SessionRepositoryImpl(
        get()
    ) }
}