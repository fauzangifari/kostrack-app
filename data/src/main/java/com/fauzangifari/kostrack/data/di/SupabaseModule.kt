package com.fauzangifari.kostrack.data.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.dsl.module

fun supabaseModule(url: String, key: String) = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = key
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
