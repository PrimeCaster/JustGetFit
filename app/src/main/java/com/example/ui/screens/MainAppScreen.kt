package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.data.FitnessRepository
import com.example.ui.components.bounceClick
import com.example.ui.theme.JustGetFitTheme

enum class AppNavDestination(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    TODAY("Today", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
    WORKOUTS("Workouts", Icons.Default.FitnessCenter, Icons.Outlined.FitnessCenter),
    NUTRITION("Nutrition", Icons.Default.Restaurant, Icons.Outlined.Restaurant),
    PROGRESS("Progress", Icons.Default.AutoGraph, Icons.Outlined.AutoGraph)
}

@Composable
fun MainAppScreen(
    repository: FitnessRepository,
    modifier: Modifier = Modifier
) {
    var currentDestination by remember { mutableStateOf(AppNavDestination.TODAY) }
    var workoutInitialTab by remember { mutableIntStateOf(0) }

    // Back handler: if not on TODAY tab, navigate back to TODAY
    BackHandler(enabled = currentDestination != AppNavDestination.TODAY) {
        currentDestination = AppNavDestination.TODAY
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                windowInsets = WindowInsets.navigationBars,
                tonalElevation = 6.dp
            ) {
                AppNavDestination.values().forEach { destination ->
                    val isSelected = currentDestination == destination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentDestination = destination },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.label
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.bounceClick { currentDestination = destination }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentDestination,
                transitionSpec = {
                    val forward = targetState.ordinal > initialState.ordinal
                    if (forward) {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> (fullWidth * 0.28f).toInt() },
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(260)))
                            .togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -(fullWidth * 0.28f).toInt() },
                                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                ) + fadeOut(animationSpec = tween(180))
                            )
                    } else {
                        (slideInHorizontally(
                            initialOffsetX = { fullWidth -> -(fullWidth * 0.28f).toInt() },
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(260)))
                            .togetherWith(
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> (fullWidth * 0.28f).toInt() },
                                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                ) + fadeOut(animationSpec = tween(180))
                            )
                    }
                },
                label = "tabSharedAxisTransition",
                modifier = Modifier.fillMaxSize()
            ) { targetDestination ->
                when (targetDestination) {
                    AppNavDestination.TODAY -> DashboardScreen(
                        repository = repository,
                        onNavigateToWorkouts = {
                            workoutInitialTab = 0
                            currentDestination = AppNavDestination.WORKOUTS
                        },
                        onNavigateToCardio = {
                            workoutInitialTab = 1
                            currentDestination = AppNavDestination.WORKOUTS
                        },
                        onNavigateToNutrition = { currentDestination = AppNavDestination.NUTRITION }
                    )

                    AppNavDestination.WORKOUTS -> WorkoutScreen(
                        repository = repository,
                        initialTab = workoutInitialTab
                    )

                    AppNavDestination.NUTRITION -> NutritionScreen(
                        repository = repository
                    )

                    AppNavDestination.PROGRESS -> ProgressScreen(
                        repository = repository
                    )
                }
            }
        }
    }
}

@Preview(name = "Main App Dark", showBackground = true)
@Composable
fun MainAppScreenDarkPreview() {
    JustGetFitTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainAppScreen(repository = FitnessRepository())
        }
    }
}

@Preview(name = "Main App Light", showBackground = true)
@Composable
fun MainAppScreenLightPreview() {
    JustGetFitTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainAppScreen(repository = FitnessRepository())
        }
    }
}
