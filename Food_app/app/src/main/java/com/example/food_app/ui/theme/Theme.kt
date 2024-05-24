package com.example.food_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    )

/* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/

data class MyColors(
    val mainColor: Color = Color(0xffFE724C),
    val titleSmallColor: Color = Color(0xff9796A1),
    val unClickColor: Color = Color(0xffEEEEEE),
    val plateHolderColor: Color = Color(0xffC4C4C4),
    val textBlackColor: Color = Color(0xff111719),
    val labelColor: Color = Color(0xff9796A1),
    val circleColor: Color = Color(0xffFFECE7),
    val titleTextColor: Color = Color(0xff323643),
    val starColor: Color = Color(0xffFFC529),
    val normalTextColor: Color = Color(0xff8A8E9B),
    val backgroundCategoryColor: Color = Color(0xffF6F6F6),
    val bottomNavigationIconColor: Color = Color(0xff656565),
    val iconColor: Color = Color(0xff111719),
    val backgroundItem: Color = Color(0xffffffff),
    val textReviewColor :Color = Color(0xff7F7D92),
    val backgroundAddress :Color = Color(0xffF0F5FA),
    val addressColor:Color = Color (0xff2790C3),
    val menuColor:Color = Color (0xffffffff),
    val bankColor:Color = Color (0xffF0F5FA),
    val darkBackground:Color = Color (0xff1B1B1F),
    val iconDarkColor:Color = Color(0xFF182022)



)

var myColors:MyColors = MyColors()

val LocalMyColors = staticCompositionLocalOf {
    myColors
}

@Composable
fun Food_appTheme(
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    myColors = if (darkTheme) {
        MyColors(
            mainColor = Color(0xffFE724C),
            titleSmallColor = Color(0xff9796A1),
            unClickColor = Color(0xffEEEEEE),
            plateHolderColor = Color(0xffC4C4C4),
            textBlackColor = Color(0xffffffff),
            labelColor = Color(0xff9796A1),
            circleColor = Color(0xffFFECE7),
            titleTextColor = Color(0xff9796A1),
            starColor = Color(0xffFFC529),
            normalTextColor = Color(0xff8A8E9B),
            backgroundCategoryColor = Color(0xffF6F6F6),
            bottomNavigationIconColor = Color(0xff656565),
            iconColor = Color(0xff111719),
            backgroundItem = Color(0xffffffff),
            textReviewColor = Color(0xff7F7D92),
            backgroundAddress = Color(0xffF0F5FA),
            addressColor = Color(0xff2790C3),
            menuColor = Color(0xffffffff),
            bankColor = Color(0xffF0F5FA),
            darkBackground = Color(0xff1B1B1F),
            iconDarkColor = Color(0xff656565),

        )
    }else{
        MyColors(
            mainColor = Color(0xffFE724C),
            titleSmallColor = Color(0xff9796A1),
            unClickColor = Color(0xffEEEEEE),
            plateHolderColor = Color(0xffC4C4C4),
            textBlackColor = Color(0xff111719),
            labelColor = Color(0xff9796A1),
            circleColor = Color(0xffFFECE7),
            titleTextColor = Color(0xff323643),
            starColor = Color(0xffFFC529),
            normalTextColor = Color(0xff8A8E9B),
            backgroundCategoryColor = Color(0xffF6F6F6),
            bottomNavigationIconColor = Color(0xff656565),
            iconColor = Color(0xff111719),
            backgroundItem = Color(0xffffffff),
            textReviewColor = Color(0xff7F7D92),
            backgroundAddress = Color(0xffF0F5FA),
            addressColor = Color(0xff2790C3),
            menuColor = Color(0xffffffff),
            bankColor = Color(0xffF0F5FA),
            darkBackground = Color(0xffffffff),
            iconDarkColor = Color(0xFF182022),
        )
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }


    CompositionLocalProvider(LocalMyColors provides myColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )

    }

}
object Food_appTheme{
    val myColors :MyColors
        @Composable
        get() = LocalMyColors.current
}