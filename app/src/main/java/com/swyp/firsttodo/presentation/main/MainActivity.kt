package com.swyp.firsttodo.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.swyp.firsttodo.core.designsystem.theme.HeabomTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel // ViewModel 생성 트리거 (init 블록에서 앱 초기화 수행)
        askNotificationPermission()
        checkGooglePlayServices()

        setContent {
            val navController = rememberNavController()

            HeabomTheme {
                MainScreen(
                    navController = navController,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkGooglePlayServices()
    }

    // FCM 정상 이용 위해 GooglePlayService 필요해, 설치 여부 확인 및 설치 유도
    private fun checkGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS && apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.makeGooglePlayServicesAvailable(this)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) {}

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("User accept notification permission")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
