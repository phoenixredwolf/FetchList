package com.phoenixredwolf.fetchlist

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

/**
 * A composable that displays a splash screen with animated logos and text.
 *
 * This composable presents a splash screen with two logos that slide in from opposite sides of the screen,
 * followed by a fade-in text "&". After a delay, it navigates to the "home" screen.
 *
 * @param navController The [NavHostController] used for navigation.
 */
@Composable
fun SplashPage(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var logo1Offset by remember { mutableStateOf(screenWidth) }
    var logo2Offset by remember { mutableStateOf(-screenWidth) }
    var fadeText by remember { mutableStateOf(0f) }

    val animatedLogo1Offset by animateDpAsState(
        targetValue = logo1Offset,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
    )
    val animatedLogo2Offset by animateDpAsState(
        targetValue = logo2Offset,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
    )
    val animatedTextAlpha by animateFloatAsState(
        targetValue = fadeText,
        animationSpec = tween(durationMillis = 600, delayMillis = 500)
    )

    // Start animations and navigate to HomeScreen after delay
    LaunchedEffect(Unit) {
        delay(500)
        logo1Offset = 0.dp
        logo2Offset = 0.dp
        delay(700)
        fadeText = 1f
        delay(1500) // Keep splash screen visible for a moment
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Removes splash from back stack
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.fetch),
                contentDescription = "Logo 1",
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = animatedLogo1Offset)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "&",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(animatedTextAlpha)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.kclogo),
                contentDescription = "Logo 2",
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = animatedLogo2Offset)
            )
        }
    }
}
