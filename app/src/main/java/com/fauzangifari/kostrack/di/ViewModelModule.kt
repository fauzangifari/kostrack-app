package com.fauzangifari.kostrack.di

import com.fauzangifari.kostrack.ui.screen.dashboard.DashboardViewModel
import com.fauzangifari.kostrack.ui.screen.login.LoginViewModel
import com.fauzangifari.kostrack.ui.screen.signup.SignupViewModel
import com.fauzangifari.kostrack.ui.screen.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::DashboardViewModel)
}