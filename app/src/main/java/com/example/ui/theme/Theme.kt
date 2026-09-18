package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = BankBlueAccent,
    onPrimary = Color.White,
    primaryContainer = BankNavyPrimary,
    onPrimaryContainer = Color.White,
    secondary = BankSkyBlue,
    onSecondary = Color.White,
    secondaryContainer = BankNavyDark,
    onSecondaryContainer = BankIceBlue,
    tertiary = BankSuccessGreen,
    background = Color(0xFF0A1120),
    surface = Color(0xFF0F172A),
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155),
    error = BankErrorRed,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BankNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = BankIceBlue,
    onPrimaryContainer = BankNavyDark,
    secondary = BankBlueAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAFE),
    onSecondaryContainer = BankNavyPrimary,
    tertiary = BankSuccessGreen,
    background = BankBackgroundLight,
    surface = BankSurfaceLight,
    onBackground = BankTextPrimary,
    onSurface = BankTextPrimary,
    surfaceVariant = BankSurfaceVariantLight,
    onSurfaceVariant = BankTextSecondary,
    outline = BankBorderLight,
    error = BankErrorRed,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For standard banking app presentation, allow dynamic color on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val useDynamic = dynamicColor && Build.VERSION.SDK_INT >= 31
  val colorScheme = when {
    useDynamic && darkTheme -> androidx.compose.material3.dynamicDarkColorScheme(androidx.compose.ui.platform.LocalContext.current)
    useDynamic && !darkTheme -> androidx.compose.material3.dynamicLightColorScheme(androidx.compose.ui.platform.LocalContext.current)
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, shapes = Shapes, content = content)
}
