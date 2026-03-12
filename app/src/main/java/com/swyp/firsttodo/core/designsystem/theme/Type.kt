package com.swyp.firsttodo.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.R

private val HaebomBaseTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
    lineHeight = 1.45.em,
    letterSpacing = 0.02.em,
)

private val SuiteExtraBold = FontFamily(Font(R.font.suite_extrabold))
private val SuiteBold = FontFamily(Font(R.font.suite_bold))
private val SuiteSemiBold = FontFamily(Font(R.font.suite_semibold))
private val SuiteMedium = FontFamily(Font(R.font.suite_medium))

val ExtraBoldStyle = HaebomBaseTextStyle.copy(fontFamily = SuiteExtraBold, fontWeight = FontWeight.ExtraBold)
val BoldStyle = HaebomBaseTextStyle.copy(fontFamily = SuiteBold, fontWeight = FontWeight.Bold)
val SemiBoldStyle = HaebomBaseTextStyle.copy(fontFamily = SuiteSemiBold, fontWeight = FontWeight.SemiBold)
val MediumStyle = HaebomBaseTextStyle.copy(fontFamily = SuiteMedium, fontWeight = FontWeight.Medium)

@Immutable
data class HaebomTypography(
    // Title
    val week: TextStyle,
    val hero: TextStyle,
    val screen: TextStyle,
    val card: TextStyle,
    val section: TextStyle,
    // Body
    val subtitle: TextStyle,
    val description: TextStyle,
    // Elements
    val dDay: TextStyle,
    val button: TextStyle,
    val placeholder: TextStyle,
    val caption: TextStyle,
    val helperText: TextStyle,
    val bottomNavbar: TextStyle,
)

val defaultHaebomTypography = HaebomTypography(
    // Title
    week = BoldStyle.copy(fontSize = 24.sp, letterSpacing = 0.em),
    hero = BoldStyle.copy(fontSize = 22.sp, letterSpacing = 0.em),
    screen = BoldStyle.copy(fontSize = 20.sp),
    card = BoldStyle.copy(fontSize = 18.sp),
    section = BoldStyle.copy(fontSize = 16.sp, letterSpacing = 0.em),
    // Body
    subtitle = MediumStyle.copy(fontSize = 16.sp),
    description = MediumStyle.copy(fontSize = 14.sp),
    // Elements
    dDay = ExtraBoldStyle.copy(fontSize = 16.sp, letterSpacing = 0.em),
    button = BoldStyle.copy(fontSize = 16.sp),
    placeholder = MediumStyle.copy(fontSize = 14.sp),
    caption = BoldStyle.copy(fontSize = 12.sp),
    helperText = MediumStyle.copy(fontSize = 12.sp),
    bottomNavbar = SemiBoldStyle.copy(fontSize = 12.sp),
)

val LocalHaebomTypographyProvider = staticCompositionLocalOf { defaultHaebomTypography }
