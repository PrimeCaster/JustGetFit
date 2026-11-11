package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Athletic Brand Themes with complete Dark and Light Material 3 ColorSchemes
 */
enum class AppColorTheme(
    val id: String,
    val displayName: String,
    val emoji: String,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val tertiaryColor: Color,
    val previewColors: List<Color>
) {
    EMERALD_VITALITY(
        id = "emerald_vitality",
        displayName = "Emerald Vitality",
        emoji = "⚡",
        description = "Electric Mint & Active Coral",
        primaryColor = MintPrimary,
        secondaryColor = CoralPrimary,
        tertiaryColor = TrophyGold,
        previewColors = listOf(MintPrimary, CoralPrimary, TrophyGold)
    ),
    CYBER_NEON(
        id = "cyber_neon",
        displayName = "Cyber Neon",
        emoji = "🌌",
        description = "Electric Cyan & Cyber Violet",
        primaryColor = Color(0xFF06B6D4),
        secondaryColor = Color(0xFFA855F7),
        tertiaryColor = Color(0xFF84CC16),
        previewColors = listOf(Color(0xFF06B6D4), Color(0xFFA855F7), Color(0xFF84CC16))
    ),
    SOLAR_FLARE(
        id = "solar_flare",
        displayName = "Solar Flare",
        emoji = "🔥",
        description = "Crimson Blaze & Molten Amber",
        primaryColor = Color(0xFFEF4444),
        secondaryColor = Color(0xFFF97316),
        tertiaryColor = Color(0xFFFBBF24),
        previewColors = listOf(Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFFBBF24))
    ),
    OCEAN_TITAN(
        id = "ocean_titan",
        displayName = "Ocean Titan",
        emoji = "🌊",
        description = "Deep Cobalt & Glacier Teal",
        primaryColor = Color(0xFF3B82F6),
        secondaryColor = Color(0xFF14B8A6),
        tertiaryColor = Color(0xFF38BDF8),
        previewColors = listOf(Color(0xFF3B82F6), Color(0xFF14B8A6), Color(0xFF38BDF8))
    ),
    MIDNIGHT_AMETHYST(
        id = "midnight_amethyst",
        displayName = "Midnight Amethyst",
        emoji = "🔮",
        description = "Imperial Purple & Rose Gold",
        primaryColor = Color(0xFF8B5CF6),
        secondaryColor = Color(0xFFF43F5E),
        tertiaryColor = Color(0xFFF59E0B),
        previewColors = listOf(Color(0xFF8B5CF6), Color(0xFFF43F5E), Color(0xFFF59E0B))
    ),
    NORDIC_FOREST(
        id = "nordic_forest",
        displayName = "Nordic Forest",
        emoji = "🌲",
        description = "Alpine Evergreen & Champagne Amber",
        primaryColor = Color(0xFF22C55E),
        secondaryColor = Color(0xFFEAB308),
        tertiaryColor = Color(0xFF14B8A6),
        previewColors = listOf(Color(0xFF22C55E), Color(0xFFEAB308), Color(0xFF14B8A6))
    );

    fun toDarkColorScheme(): ColorScheme {
        return when (this) {
            EMERALD_VITALITY -> darkColorScheme(
                primary = MintPrimary,
                onPrimary = Obsidian950,
                primaryContainer = Slate800,
                onPrimaryContainer = MintLight,
                secondary = CoralPrimary,
                onSecondary = PureWhite,
                secondaryContainer = Slate800,
                onSecondaryContainer = CoralLight,
                tertiary = TrophyGold,
                onTertiary = Obsidian950,
                tertiaryContainer = Slate700,
                onTertiaryContainer = TrophyGoldLight,
                background = Obsidian900,
                onBackground = PearlWhite,
                surface = Obsidian950,
                onSurface = PearlWhite,
                surfaceVariant = Slate800,
                onSurfaceVariant = PearlMuted,
                surfaceContainerHigh = Slate700,
                outline = Slate600,
                outlineVariant = Slate700
            )
            CYBER_NEON -> darkColorScheme(
                primary = Color(0xFF06B6D4),
                onPrimary = Color(0xFF03151E),
                primaryContainer = Color(0xFF132838),
                onPrimaryContainer = Color(0xFF67E8F9),
                secondary = Color(0xFFA855F7),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFF28183E),
                onSecondaryContainer = Color(0xFFE9D5FF),
                tertiary = Color(0xFF84CC16),
                onTertiary = Color(0xFF132005),
                tertiaryContainer = Color(0xFF233010),
                onTertiaryContainer = Color(0xFFBEF264),
                background = Color(0xFF080C14),
                onBackground = Color(0xFFF1F5F9),
                surface = Color(0xFF05080E),
                onSurface = Color(0xFFF1F5F9),
                surfaceVariant = Color(0xFF131D2D),
                onSurfaceVariant = Color(0xFF94A3B8),
                surfaceContainerHigh = Color(0xFF1E2D44),
                outline = Color(0xFF334B68),
                outlineVariant = Color(0xFF1E2D44)
            )
            SOLAR_FLARE -> darkColorScheme(
                primary = Color(0xFFEF4444),
                onPrimary = Color(0xFF1A0505),
                primaryContainer = Color(0xFF3B1212),
                onPrimaryContainer = Color(0xFFFCA5A5),
                secondary = Color(0xFFF97316),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFF381A08),
                onSecondaryContainer = Color(0xFFFDBA74),
                tertiary = Color(0xFFFBBF24),
                onTertiary = Color(0xFF261802),
                tertiaryContainer = Color(0xFF3D2A08),
                onTertiaryContainer = Color(0xFFFDE68A),
                background = Color(0xFF140808),
                onBackground = Color(0xFFFFF1F2),
                surface = Color(0xFF0D0404),
                onSurface = Color(0xFFFFF1F2),
                surfaceVariant = Color(0xFF261414),
                onSurfaceVariant = Color(0xFFFDA4AF),
                surfaceContainerHigh = Color(0xFF381C1C),
                outline = Color(0xFF572A2A),
                outlineVariant = Color(0xFF381C1C)
            )
            OCEAN_TITAN -> darkColorScheme(
                primary = Color(0xFF3B82F6),
                onPrimary = Color(0xFF03102B),
                primaryContainer = Color(0xFF14244B),
                onPrimaryContainer = Color(0xFF93C5FD),
                secondary = Color(0xFF14B8A6),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFF0D3330),
                onSecondaryContainer = Color(0xFF5EEAD4),
                tertiary = Color(0xFF38BDF8),
                onTertiary = Color(0xFF021B2B),
                tertiaryContainer = Color(0xFF0F3249),
                onTertiaryContainer = Color(0xFF7DD3FC),
                background = Color(0xFF080E1C),
                onBackground = Color(0xFFF0F9FF),
                surface = Color(0xFF050914),
                onSurface = Color(0xFFF0F9FF),
                surfaceVariant = Color(0xFF131D33),
                onSurfaceVariant = Color(0xFF94A3B8),
                surfaceContainerHigh = Color(0xFF1C2C4C),
                outline = Color(0xFF2E4573),
                outlineVariant = Color(0xFF1C2C4C)
            )
            MIDNIGHT_AMETHYST -> darkColorScheme(
                primary = Color(0xFF8B5CF6),
                onPrimary = Color(0xFF160A29),
                primaryContainer = Color(0xFF2B164C),
                onPrimaryContainer = Color(0xFFC4B5FD),
                secondary = Color(0xFFF43F5E),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFF3C0E1C),
                onSecondaryContainer = Color(0xFFFDA4AF),
                tertiary = Color(0xFFF59E0B),
                onTertiary = Color(0xFF281802),
                tertiaryContainer = Color(0xFF3E2808),
                onTertiaryContainer = Color(0xFFFCD34D),
                background = Color(0xFF0C0717),
                onBackground = Color(0xFFFAF5FF),
                surface = Color(0xFF080410),
                onSurface = Color(0xFFFAF5FF),
                surfaceVariant = Color(0xFF1C122F),
                onSurfaceVariant = Color(0xFFA78BFA),
                surfaceContainerHigh = Color(0xFF291B44),
                outline = Color(0xFF4C337B),
                outlineVariant = Color(0xFF291B44)
            )
            NORDIC_FOREST -> darkColorScheme(
                primary = Color(0xFF22C55E),
                onPrimary = Color(0xFF04190B),
                primaryContainer = Color(0xFF0D3318),
                onPrimaryContainer = Color(0xFF86EFAC),
                secondary = Color(0xFFEAB308),
                onSecondary = Color(0xFF1C1402),
                secondaryContainer = Color(0xFF382B08),
                onSecondaryContainer = Color(0xFFFDE047),
                tertiary = Color(0xFF14B8A6),
                onTertiary = Color(0xFF021E1B),
                tertiaryContainer = Color(0xFF0C3833),
                onTertiaryContainer = Color(0xFF5EEAD4),
                background = Color(0xFF07120B),
                onBackground = Color(0xFFF0FDF4),
                surface = Color(0xFF040C07),
                onSurface = Color(0xFFF0FDF4),
                surfaceVariant = Color(0xFF122318),
                onSurfaceVariant = Color(0xFF86EFAC),
                surfaceContainerHigh = Color(0xFF1B3524),
                outline = Color(0xFF2D583C),
                outlineVariant = Color(0xFF1B3524)
            )
        }
    }

    fun toLightColorScheme(): ColorScheme {
        return when (this) {
            EMERALD_VITALITY -> lightColorScheme(
                primary = MintDark,
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFE6F7F0),
                onPrimaryContainer = MintDark,
                secondary = CoralPrimary,
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFFFEDD5),
                onSecondaryContainer = Color(0xFFC2410C),
                tertiary = TrophyAmber,
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFFEF3C7),
                onTertiaryContainer = TrophyAmber,
                background = CeramicLight,
                onBackground = DarkTextPrimary,
                surface = CeramicSurface,
                onSurface = DarkTextPrimary,
                surfaceVariant = CeramicSubtle,
                onSurfaceVariant = DarkTextSecondary,
                surfaceContainerHigh = CeramicBorder,
                outline = CeramicBorder,
                outlineVariant = CeramicSubtle
            )
            CYBER_NEON -> lightColorScheme(
                primary = Color(0xFF0891B2),
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFE0F7FA),
                onPrimaryContainer = Color(0xFF0E7490),
                secondary = Color(0xFF9333EA),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFF3E8FF),
                onSecondaryContainer = Color(0xFF7E22CE),
                tertiary = Color(0xFF65A30D),
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFECFCCB),
                onTertiaryContainer = Color(0xFF4D7C0F),
                background = Color(0xFFF8FAFC),
                onBackground = Color(0xFF0F172A),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0F172A),
                surfaceVariant = Color(0xFFF1F5F9),
                onSurfaceVariant = Color(0xFF64748B),
                surfaceContainerHigh = Color(0xFFE2E8F0),
                outline = Color(0xFFCBD5E1),
                outlineVariant = Color(0xFFF1F5F9)
            )
            SOLAR_FLARE -> lightColorScheme(
                primary = Color(0xFFDC2626),
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFFEE2E2),
                onPrimaryContainer = Color(0xFFB91C1C),
                secondary = Color(0xFFEA580C),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFFFEDD5),
                onSecondaryContainer = Color(0xFFC2410C),
                tertiary = Color(0xFFD97706),
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFFEF3C7),
                onTertiaryContainer = Color(0xFFB45309),
                background = Color(0xFFFFF7ED),
                onBackground = Color(0xFF1C1917),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF1C1917),
                surfaceVariant = Color(0xFFFFF1E6),
                onSurfaceVariant = Color(0xFF78716C),
                surfaceContainerHigh = Color(0xFFFED7AA),
                outline = Color(0xFFFDBA74),
                outlineVariant = Color(0xFFFFF1E6)
            )
            OCEAN_TITAN -> lightColorScheme(
                primary = Color(0xFF2563EB),
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFDBEAFE),
                onPrimaryContainer = Color(0xFF1D4ED8),
                secondary = Color(0xFF0D9488),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFCCFBF1),
                onSecondaryContainer = Color(0xFF0F766E),
                tertiary = Color(0xFF0284C7),
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFE0F2FE),
                onTertiaryContainer = Color(0xFF0369A1),
                background = Color(0xFFF0F9FF),
                onBackground = Color(0xFF0C1A30),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0C1A30),
                surfaceVariant = Color(0xFFE0F2FE),
                onSurfaceVariant = Color(0xFF475569),
                surfaceContainerHigh = Color(0xFFBAE6FD),
                outline = Color(0xFF7DD3FC),
                outlineVariant = Color(0xFFE0F2FE)
            )
            MIDNIGHT_AMETHYST -> lightColorScheme(
                primary = Color(0xFF7C3AED),
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFEDE9FE),
                onPrimaryContainer = Color(0xFF6D28D9),
                secondary = Color(0xFFE11D48),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFFFE4E6),
                onSecondaryContainer = Color(0xFFBE123C),
                tertiary = Color(0xFFD97706),
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFFEF3C7),
                onTertiaryContainer = Color(0xFFB45309),
                background = Color(0xFFFAF5FF),
                onBackground = Color(0xFF1E1035),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF1E1035),
                surfaceVariant = Color(0xFFF3E8FF),
                onSurfaceVariant = Color(0xFF6B7280),
                surfaceContainerHigh = Color(0xFFDDD6FE),
                outline = Color(0xFFC4B5FD),
                outlineVariant = Color(0xFFF3E8FF)
            )
            NORDIC_FOREST -> lightColorScheme(
                primary = Color(0xFF16A34A),
                onPrimary = PureWhite,
                primaryContainer = Color(0xFFDCFCE7),
                onPrimaryContainer = Color(0xFF15803D),
                secondary = Color(0xFFCA8A04),
                onSecondary = PureWhite,
                secondaryContainer = Color(0xFFFEF9C3),
                onSecondaryContainer = Color(0xFFA16207),
                tertiary = Color(0xFF0D9488),
                onTertiary = PureWhite,
                tertiaryContainer = Color(0xFFCCFBF1),
                onTertiaryContainer = Color(0xFF0F766E),
                background = Color(0xFFF2FBF5),
                onBackground = Color(0xFF0B2312),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0B2312),
                surfaceVariant = Color(0xFFE8F7EE),
                onSurfaceVariant = Color(0xFF4B6354),
                surfaceContainerHigh = Color(0xFFBBF7D0),
                outline = Color(0xFF86EFAC),
                outlineVariant = Color(0xFFE8F7EE)
            )
        }
    }
}

@Composable
fun JustGetFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorTheme: AppColorTheme = AppColorTheme.EMERALD_VITALITY,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        colorTheme.toDarkColorScheme()
    } else {
        colorTheme.toLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun ApexFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorTheme: AppColorTheme = AppColorTheme.EMERALD_VITALITY,
    content: @Composable () -> Unit
) {
    JustGetFitTheme(darkTheme = darkTheme, colorTheme = colorTheme, content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ApexFitTheme(darkTheme = darkTheme, content = content)
}
