package com.fauzangifari.kostrack.di

import com.fauzangifari.kostrack.ui.screen.dashboard.DashboardViewModel
import com.fauzangifari.kostrack.ui.screen.login.LoginViewModel
import com.fauzangifari.kostrack.ui.screen.profile.ProfileViewModel
import com.fauzangifari.kostrack.ui.screen.properties.PropertyDetailViewModel
import com.fauzangifari.kostrack.ui.screen.properties.PropertyEditorViewModel
import com.fauzangifari.kostrack.ui.screen.properties.PropertyListViewModel
import com.fauzangifari.kostrack.ui.screen.rooms.RoomEditorViewModel
import com.fauzangifari.kostrack.ui.screen.signup.SignupViewModel
import com.fauzangifari.kostrack.ui.screen.splash.SplashViewModel
import com.fauzangifari.kostrack.ui.screen.tenants.TenantDetailViewModel
import com.fauzangifari.kostrack.ui.screen.tenants.TenantEditorViewModel
import com.fauzangifari.kostrack.ui.screen.transactions.TransactionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::PropertyListViewModel)
    viewModelOf(::PropertyEditorViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::PropertyDetailViewModel)
    viewModelOf(::RoomEditorViewModel)
    viewModelOf(::TransactionsViewModel)
    viewModelOf(::TenantEditorViewModel)
    viewModelOf(::TenantDetailViewModel)
}