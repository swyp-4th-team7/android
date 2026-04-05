package com.swyp.firsttodo.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Base
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)

// Secondary.Gray
private val Gray25 = Color(0xFFFDFDFC)
private val Gray50 = Color(0xFFF5F5F4)
private val Gray100 = Color(0xFFE7E5E4)
private val Gray200 = Color(0xFFD7D3D0)
private val Gray300 = Color(0xFFA9A29D)
private val Gray400 = Color(0xFF79716B)
private val Gray500 = Color(0xFF57534E)
private val Gray600 = Color(0xFF44403C)
private val Gray700 = Color(0xFF292524)
private val Gray800 = Color(0xFF171412)

// Secondary.Yellow
private val Yellow25 = Color(0xFFFFFCF5)
private val Yellow50 = Color(0xFFFFFAEB)
private val Yellow100 = Color(0xFFFFF5D9)
private val Yellow200 = Color(0xFFFEF0C7)
private val Yellow300 = Color(0xFFFEDF89)
private val Yellow400 = Color(0xFFFEC84B)
private val Yellow500 = Color(0xFFFDB022)
private val Yellow600 = Color(0xFFF79009)

// Primary.Orange
private val Orange25 = Color(0xFFFFF6F2)
private val Orange50 = Color(0xFFFFF2EC)
private val Orange100 = Color(0xFFFFEAE0)
private val Orange200 = Color(0xFFFFD9C9)
private val Orange300 = Color(0xFFFFB39B)
private val Orange400 = Color(0xFFFF8153)
private val Orange500 = Color(0xFFFF6A3A)
private val Orange600 = Color(0xFFD94920)
private val Orange700 = Color(0xFFB73A19)
private val Orange800 = Color(0xFF7D240C)

// Secondary.Green
private val Green25 = Color(0xFFFAFDF7)
private val Green50 = Color(0xFFF1F9E8)
private val Green100 = Color(0xFFE8F5D8)
private val Green200 = Color(0xFFCFECA7)
private val Green300 = Color(0xFFB7E07F)
private val Green400 = Color(0xFF8FC756)
private val Green500 = Color(0xFF739F3F)
private val Green600 = Color(0xFF4F7A21)
private val Green700 = Color(0xFF3F621A)
private val Green800 = Color(0xFF335015)

// Semantic
private val SemanticYellow = Color(0xFFFFD738)
private val SemanticRed = Color(0xFFE7534B)
private val SemanticGreen = Color(0xFF66B30A)
private val SemanticBlue = Color(0xFF4C85F2)

// Etc
private val bottomSheet = Color(0xFF79747E)

// Label colors
private val LabelRedBackground = Color(0xFFFFE5E5)
private val LabelRedText = Color(0xFFE32529)

@Immutable
data class HaebomColors(
    // Base
    val white: Color,
    val black: Color,
    // Secondary.Gray
    val gray25: Color,
    val gray50: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray500: Color,
    val gray600: Color,
    val gray700: Color,
    val gray800: Color,
    // Secondary.Yellow
    val yellow25: Color,
    val yellow50: Color,
    val yellow100: Color,
    val yellow200: Color,
    val yellow300: Color,
    val yellow400: Color,
    val yellow500: Color,
    val yellow600: Color,
    // Primary.Orange
    val orange25: Color,
    val orange50: Color,
    val orange100: Color,
    val orange200: Color,
    val orange300: Color,
    val orange400: Color,
    val orange500: Color,
    val orange600: Color,
    val orange700: Color,
    val orange800: Color,
    // Secondary.Green
    val green25: Color,
    val green50: Color,
    val green100: Color,
    val green200: Color,
    val green300: Color,
    val green400: Color,
    val green500: Color,
    val green600: Color,
    val green700: Color,
    val green800: Color,
    // Semantic
    val semanticYellow: Color,
    val semanticRed: Color,
    val semanticGreen: Color,
    val semanticBlue: Color,
    // Etc
    val bottomSheet: Color,
    // Label
    val labelRedBackground: Color,
    val labelRedText: Color,
)

val defaultHaebomColors = HaebomColors(
    // Base
    white = White,
    black = Black,
    // Secondary.Gray
    gray25 = Gray25,
    gray50 = Gray50,
    gray100 = Gray100,
    gray200 = Gray200,
    gray300 = Gray300,
    gray400 = Gray400,
    gray500 = Gray500,
    gray600 = Gray600,
    gray700 = Gray700,
    gray800 = Gray800,
    // Secondary.Yellow
    yellow25 = Yellow25,
    yellow50 = Yellow50,
    yellow100 = Yellow100,
    yellow200 = Yellow200,
    yellow300 = Yellow300,
    yellow400 = Yellow400,
    yellow500 = Yellow500,
    yellow600 = Yellow600,
    // Primary.Orange
    orange25 = Orange25,
    orange50 = Orange50,
    orange100 = Orange100,
    orange200 = Orange200,
    orange300 = Orange300,
    orange400 = Orange400,
    orange500 = Orange500,
    orange600 = Orange600,
    orange700 = Orange700,
    orange800 = Orange800,
    // Secondary.Green
    green25 = Green25,
    green50 = Green50,
    green100 = Green100,
    green200 = Green200,
    green300 = Green300,
    green400 = Green400,
    green500 = Green500,
    green600 = Green600,
    green700 = Green700,
    green800 = Green800,
    // Semantic
    semanticYellow = SemanticYellow,
    semanticRed = SemanticRed,
    semanticGreen = SemanticGreen,
    semanticBlue = SemanticBlue,
    // Etc
    bottomSheet = bottomSheet,
    // Label
    labelRedBackground = LabelRedBackground,
    labelRedText = LabelRedText,
)

val LocalHaebomColorsProvider = staticCompositionLocalOf { defaultHaebomColors }

// Label colors
private val LabelPinkBackground = Color(0xFFFFEAF7)
private val LabelPinkText = Color(0xFFD81A60)
private val LabelPinkCompletedBackground = Color(0xFFFFF5FB)
private val LabelPinkCompletedText = Color(0xFFEB8CAF)

private val LabelPurpleBackground = Color(0xFFF8ECFF)
private val LabelPurpleText = Color(0xFF8E24AA)
private val LabelPurpleCompletedBackground = Color(0xFFFBF5FF)
private val LabelPurpleCompletedText = Color(0xFFC791D5)

private val LabelBlueBackground = Color(0xFFDFE9FF)
private val LabelBlueText = Color(0xFF3949AB)
private val LabelBlueCompletedBackground = Color(0xFFEFF4FF)
private val LabelBlueCompletedText = Color(0xFF9CA4D5)

private val LabelSkyBlueBackground = Color(0xFFCCEFFF)
private val LabelSkyBlueText = Color(0xFF0A579B)
private val LabelSkyBlueCompletedBackground = Color(0xFFE5F7FF)
private val LabelSkyBlueCompletedText = Color(0xFF84ABCD)

private val LabelMintBackground = Color(0xFFCBF7F7)
private val LabelMintText = Color(0xFF0D5C51)
private val LabelMintCompletedBackground = Color(0xFFE5FBFB)
private val LabelMintCompletedText = Color(0xFF86ADA8)
private val LabelLimeBackground = Color(0xFFE2FCC7)
private val LabelLimeText = Color(0xFF34681E)
private val LabelLimeCompletedBackground = Color(0xFFF1FDE3)
private val LabelLimeCompletedText = Color(0xFF99B38E)

private val LabelYellowBackground = Color(0xFFFFFAC3)
private val LabelYellowText = Color(0xFFD4430F)
private val LabelYellowCompletedBackground = Color(0xFFFFFDE1)
private val LabelYellowCompletedText = Color(0xFFE9A187)

private val LabelOrangeBackground = Color(0xFFFFECC4)
private val LabelOrangeText = Color(0xFFB53600)
private val LabelOrangeCompletedBackground = Color(0xFFFFF5E1)
private val LabelOrangeCompletedText = Color(0xFFDA9A80)

private val LabelBrownBackground = Color(0xFFF9EBE5)
private val LabelBrownText = Color(0xFF70423A)
private val LabelBrownCompletedBackground = Color(0xFFFCF5F2)
private val LabelBrownCompletedText = Color(0xFFB7A09C)

private val LabelGrayBackground = Color(0xFFE2EBF0)
private val LabelGrayText = Color(0xFF36474F)
private val LabelGrayCompletedBackground = Color(0xFFF1F5F7)
private val LabelGrayCompletedText = Color(0xFF9AA3A7)

enum class LabelColor(
    val background: Color,
    val text: Color,
    val completedBackground: Color,
    val completedText: Color,
) {
    PINK(
        background = LabelPinkBackground,
        text = LabelPinkText,
        completedBackground = LabelPinkCompletedBackground,
        completedText = LabelPinkCompletedText,
    ),
    PURPLE(
        background = LabelPurpleBackground,
        text = LabelPurpleText,
        completedBackground = LabelPurpleCompletedBackground,
        completedText = LabelPurpleCompletedText,
    ),
    BLUE(
        background = LabelBlueBackground,
        text = LabelBlueText,
        completedBackground = LabelBlueCompletedBackground,
        completedText = LabelBlueCompletedText,
    ),
    SKY_BLUE(
        background = LabelSkyBlueBackground,
        text = LabelSkyBlueText,
        completedBackground = LabelSkyBlueCompletedBackground,
        completedText = LabelSkyBlueCompletedText,
    ),
    MINT(
        background = LabelMintBackground,
        text = LabelMintText,
        completedBackground = LabelMintCompletedBackground,
        completedText = LabelMintCompletedText,
    ),
    LIME(
        background = LabelLimeBackground,
        text = LabelLimeText,
        completedBackground = LabelLimeCompletedBackground,
        completedText = LabelLimeCompletedText,
    ),
    YELLOW(
        background = LabelYellowBackground,
        text = LabelYellowText,
        completedBackground = LabelYellowCompletedBackground,
        completedText = LabelYellowCompletedText,
    ),
    ORANGE(
        background = LabelOrangeBackground,
        text = LabelOrangeText,
        completedBackground = LabelOrangeCompletedBackground,
        completedText = LabelOrangeCompletedText,
    ),
    BROWN(
        background = LabelBrownBackground,
        text = LabelBrownText,
        completedBackground = LabelBrownCompletedBackground,
        completedText = LabelBrownCompletedText,
    ),
    GRAY(
        background = LabelGrayBackground,
        text = LabelGrayText,
        completedBackground = LabelGrayCompletedBackground,
        completedText = LabelGrayCompletedText,
    ),
}
