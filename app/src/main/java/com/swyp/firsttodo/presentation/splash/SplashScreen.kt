package com.swyp.firsttodo.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
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

    Box(
        modifier = Modifier
            .alpha(animatedAlpha)
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        LabelColor.YELLOW.completedBackground,
                        HaebomTheme.colors.white,
                    ),
                ),
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.img_splash_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = screenHeightDp(104.dp))
                .width(224.dp),
        )

        Image(
            painter = painterResource(R.drawable.img_splash_floor),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentSize(unbounded = true)
                .height(354.dp)
                .offset(y = 94.dp),
        )

        Image(
            painter = painterResource(R.drawable.img_splash_characters),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(334.dp)
                .offset(y = (-64).dp),
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
