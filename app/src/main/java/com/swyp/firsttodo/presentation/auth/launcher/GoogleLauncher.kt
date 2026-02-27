package com.swyp.firsttodo.presentation.auth.launcher

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.swyp.firsttodo.BuildConfig
import kotlinx.coroutines.delay
import java.security.SecureRandom
import java.util.Base64

class GoogleLauncher(
    private val activity: Activity,
) {
    private val credentialManager = CredentialManager.create(activity)
    private val webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID

    suspend fun startGoogleLogin(): GoogleLoginResult {
        val result = signInWithExistingAccount()

        if (result is GoogleLoginResult.NoCredential) {
            return signInWithNewAccount()
        }

        return result
    }

    private suspend fun signInWithExistingAccount(): GoogleLoginResult {
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(webClientId)
            .setNonce(generateSecureRandomNonce())
            .build()

        val result = getCredential(option)
        return result
    }

    private suspend fun signInWithNewAccount(): GoogleLoginResult {
        val option = GetSignInWithGoogleOption.Builder(webClientId)
            .setNonce(generateSecureRandomNonce())
            .build()

        val result = getCredential(option)
        return result
    }

    private suspend fun getCredential(option: CredentialOption): GoogleLoginResult {
        return try {
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()

            delay(250)

            val result = credentialManager.getCredential(
                context = activity,
                request = request,
            )

            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken

            GoogleLoginResult.Success(idToken)
        } catch (_: NoCredentialException) {
            GoogleLoginResult.NoCredential
        } catch (_: GetCredentialCancellationException) {
            GoogleLoginResult.Cancelled
        } catch (e: GoogleIdTokenParsingException) {
            GoogleLoginResult.Error(e.message ?: "Token parsing failed")
        } catch (e: GetCredentialException) {
            GoogleLoginResult.Error(e.message ?: "Credential error")
        }
    }

    private fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom.getInstanceStrong().nextBytes(randomBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
    }
}

sealed interface GoogleLoginResult {
    data class Success(val idToken: String) : GoogleLoginResult

    data object NoCredential : GoogleLoginResult

    data object Cancelled : GoogleLoginResult

    data class Error(val message: String) : GoogleLoginResult
}
