package com.fauzangifari.kostrack.data.repository

import android.util.Log
import com.fauzangifari.kostrack.data.model.dto.ProfileDto
import com.fauzangifari.kostrack.domain.model.User
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
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            supabaseClient.auth.currentUserOrNull()?.let {
                // Insert into profiles table
                val profile = ProfileDto(
                    id = it.id,
                    fullName = fullName,
                    email = email
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
                    val user = status.session.user
                    val userId = user?.id ?: ""
                    Log.d("AuthRepository", "Authenticated: userId=$userId")
                    
                    // Fetch profile from Supabase
                    val profile = try {
                        val result = supabaseClient.postgrest["profiles"]
                            .select {
                                filter {
                                    eq("id", userId)
                                }
                            }
                            .decodeSingleOrNull<ProfileDto>()
                        Log.d("AuthRepository", "Profile fetch success: $result")
                        result
                    } catch (e: Exception) {
                        Log.e("AuthRepository", "Error fetching profile: ${e.message}", e)
                        null
                    }

                    User(
                        id = userId,
                        email = user?.email ?: "",
                        fullName = profile?.fullName,
                        role = profile?.role
                    ).also { 
                        Log.d("AuthRepository", "Returning User: $it")
                    }
                }
                is SessionStatus.NotAuthenticated -> {
                    Log.d("AuthRepository", "Not Authenticated")
                    null
                }
                else -> {
                    Log.d("AuthRepository", "Session status: $status")
                    null
                }
            }
        }
    }
}