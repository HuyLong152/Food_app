package com.example.food_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.food_app.R

// Set of Material typography styles to start with
val defaultFont = R.font.sofia_pro_bold
val thinFont = R.font.sofiapro_light
val Typography = Typography(
    //text
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = TextBlackColor,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        color = TextBlackColor,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = TextBlackColor,
    ),

    //header
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.41.sp,
        color = TextBlackColor,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        color = TextBlackColor,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = TitleSmallColor,
    ),
    //label
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        color = LableColor,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = Color.White,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = TextBlackColor,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Thin,
        fontSize = 8.5.sp,
        color = TextBlackColor,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(thinFont)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = TextBlackColor,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = TextBlackColor,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(thinFont)),
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = TextBlackColor,
    ),
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(defaultFont)),
        fontWeight = FontWeight.Bold,
        fontSize = 60.sp,
        color = TextBlackColor,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(thinFont)),
        fontWeight = FontWeight.Thin,
        fontSize = 10.5.sp,
        color = TextBlackColor,
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)