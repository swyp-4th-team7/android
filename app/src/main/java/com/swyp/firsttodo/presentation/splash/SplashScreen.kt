package com.swyp.firsttodo.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.navigation.Route
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@Composable
fun SplashRouteScreen(
    resolvedDestination: StateFlow<Route?>,
    onNavigate: (Route) -> Unit,
) {
    LaunchedEffect(Unit) {
        coroutineScope {
            val destinationDeferred = async { resolvedDestination.filterNotNull().first() }
            delay(1_500L)
            onNavigate(destinationDeferred.await())
        }
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    var alpha by remember { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 500),
        label = "SplashAlpha",
    )

    LaunchedEffect(Unit) { alpha = 1f }

    Image(
        painter = painterResource(R.drawable.img_splash),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .alpha(animatedAlpha),
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
