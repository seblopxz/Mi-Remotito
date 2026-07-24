package com.algorithmicsluque.miremotito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.algorithmicsluque.miremotito.ui.home.HomeScreen
import com.algorithmicsluque.miremotito.ui.home.HomeViewModel
import com.algorithmicsluque.miremotito.ui.remote.RemoteScreen
import com.algorithmicsluque.miremotito.ui.remote.RemoteViewModel
import com.algorithmicsluque.miremotito.ui.settings.*
import com.algorithmicsluque.miremotito.ui.setup.AddChoiceSheet
import com.algorithmicsluque.miremotito.ui.setup.SetupFlow
import com.algorithmicsluque.miremotito.ui.theme.MiRemotitoTheme
import com.algorithmicsluque.miremotito.ui.updates.BetaUpdatesScreen
import com.algorithmicsluque.miremotito.ui.updates.UpdateFlow
import com.algorithmicsluque.miremotito.ui.updates.UpdateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiRemotitoTheme {
                RemotitoApp()
            }
        }
    }
}

@Composable
fun RemotitoApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController, 
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            ) + fadeIn(animationSpec = tween(200))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            ) + fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            ) + fadeIn(animationSpec = tween(200))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            ) + fadeOut(animationSpec = tween(200))
        }
    ) {
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel()
            var showAddChoice by remember { mutableStateOf(false) }
            
            HomeScreen(
                viewModel = homeViewModel,
                onDeviceClick = { deviceId ->
                    navController.navigate("remote/$deviceId")
                },
                onProfileClick = {
                    navController.navigate("account")
                },
                onAddClick = {
                    showAddChoice = true
                }
            )

            if (showAddChoice) {
                AddChoiceSheet(
                    onAddDevice = {
                        showAddChoice = false
                        navController.navigate("add_remote")
                    },
                    onAddGroup = { /* TODO */ },
                    onDismiss = { showAddChoice = false }
                )
            }
        }
        
        composable(
            route = "remote/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            val remoteViewModel: RemoteViewModel = viewModel()
            RemoteScreen(
                deviceId = deviceId,
                viewModel = remoteViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable("account") {
            AccountScreen(
                onNavigateToSettings = { navController.navigate("settings") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateToAbout = { navController.navigate("about") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("about") {
            val settingsViewModel: SettingsViewModel = viewModel()
            var showChangelog by remember { mutableStateOf(false) }
            
            AboutScreen(
                viewModel = settingsViewModel,
                onNavigateToChangelog = { showChangelog = true },
                onNavigateToUpdates = { navController.navigate("updates") },
                onBack = { navController.popBackStack() }
            )

            if (showChangelog) {
                ChangelogSheet(onDismiss = { showChangelog = false })
            }
        }

        composable("updates") {
            val updateViewModel: UpdateViewModel = viewModel()
            UpdateFlow(
                viewModel = updateViewModel,
                onNavigateToBeta = { navController.navigate("beta_program") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("beta_program") {
            BetaUpdatesScreen(onBack = { navController.popBackStack() })
        }
        
        composable("add_remote") {
            SetupFlow(
                onFinished = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

    }
}
