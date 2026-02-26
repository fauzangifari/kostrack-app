package com.fauzangifari.kostrack.data.repository

import android.util.Log
import com.fauzangifari.kostrack.data.model.dto.ProfileDto
import com.fauzangifari.kostrack.domain.model.User
import com.fauzangifari.kostrack.domain.model.UserRole
import com.fauzangifari.kostrack.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.exception.AuthRestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {
        return try {
            val authResponse = supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            val userId = authResponse?.id ?: supabaseClient.auth.currentUserOrNull()?.id
            
            userId?.let { id ->
                // Insert into profiles table
                // Set default role to OWNER so user can manage properties
                val profile = ProfileDto(
                    id = id,
                    fullName = fullName,
                    email = email,
                    role = "OWNER"
                )
                supabaseClient.postgrest["profiles"].insert(profile)
            }
            
            Result.success(Unit)
        } catch (e: AuthRestException) {
            val friendlyMessage = when (e.error) {
                "over_email_send_rate_limit" -> "Terlalu banyak mencoba. Silakan tunggu beberapa menit lagi."
                "user_already_exists" -> "Email ini sudah terdaftar."
                else -> "Gagal mendaftar: ${e.description ?: e.message}"
            }
            Result.failure(Exception(friendlyMessage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            
            val user = supabaseClient.auth.currentUserOrNull()?.let {
                User(
                    id = it.id,
                    email = it.email ?: "",
                    fullName = null,
                    role = null
                )
            }
            
            if (user != null) Result.success(user) 
            else Result.failure(Exception("User tidak ditemukan setelah login"))
        } catch (e: AuthRestException) {
            val friendlyMessage = when (e.error) {
                "invalid_credentials" -> "Email atau kata sandi salah."
                else -> "Gagal masuk: ${e.description ?: e.message}"
            }
            Result.failure(Exception(friendlyMessage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        return supabaseClient.auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val authUser = status.session.user
                    val userId = authUser?.id ?: return@map null
                    
                    // Fetch profile from Supabase
                    var profile = try {
                        supabaseClient.postgrest["profiles"]
                            .select {
                                filter {
                                    eq("id", userId)
                                }
                            }
                            .decodeSingleOrNull<ProfileDto>()
                    } catch (e: Exception) {
                        Log.e("AuthRepository", "Error fetching profile: ${e.message}")
                        null
                    }

                    if (profile == null && authUser != null) {
                        try {
                            val newProfile = ProfileDto(
                                id = userId,
                                fullName = authUser.userMetadata?.get("full_name")?.toString() ?: "User",
                                email = authUser.email ?: "",
                                role = "OWNER"
                            )
                            supabaseClient.postgrest["profiles"].insert(newProfile)
                            profile = newProfile
                            Log.d("AuthRepository", "Self-healed: Profile created for $userId")
                        } catch (e: Exception) {
                            Log.e("AuthRepository", "Failed self-healing profile: ${e.message}")
                        }
                    }

                    User(
                        id = userId,
                        email = authUser.email ?: "",
                        fullName = profile?.fullName,
                        role = profile?.role?.let {
                            try { UserRole.valueOf(it) } catch (e: Exception) { null }
                        }
                    )
                }
                is SessionStatus.NotAuthenticated -> null
                else -> null
            }
        }
    }
}