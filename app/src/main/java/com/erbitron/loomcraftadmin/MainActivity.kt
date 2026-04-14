package com.erbitron.loomcraftadmin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erbitron.loomcraftadmin.data.local.TokenManager
import com.erbitron.loomcraftadmin.ui.features.admin.orders.AdminOrderDetailScreen
import com.erbitron.loomcraftadmin.ui.features.admin.orders.AdminOrderListScreen
import com.erbitron.loomcraftadmin.ui.features.admin.shipping.PrintPreviewScreen
import com.erbitron.loomcraftadmin.ui.features.auth.LoginScreen
import com.erbitron.loomcraftadmin.ui.features.vendor.orders.VendorOrderDetailScreen
import com.erbitron.loomcraftadmin.ui.features.vendor.orders.VendorOrderListScreen
import com.erbitron.loomcraftadmin.ui.theme.LoomCraftAdminTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Handle permission result if needed.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setContent {
            LoomCraftAdminTheme {
                AppNavigation(tokenManager)
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun AppNavigation(tokenManager: TokenManager) {
    val navController = rememberNavController()
    val authToken by tokenManager.authToken.collectAsState(initial = null)
    val userRole by tokenManager.userRole.collectAsState(initial = null)
    val rememberMe by tokenManager.rememberMe.collectAsState(initial = true)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authToken, userRole, rememberMe) {
        val destination = when {
            authToken.isNullOrBlank() || userRole.isNullOrBlank() -> "login"
            rememberMe && userRole == "vendor" -> "vendor_order_list"
            rememberMe -> "admin_order_list"
            else -> {
                tokenManager.clearAuth()
                "login"
            }
        }

        if (currentRoute != destination) {
            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = "bootstrap") {
        composable("bootstrap") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        composable("login") {
            LoginScreen(onLoginSuccess = { })
        }
        composable("vendor_order_list") {
            VendorOrderListScreen(
                onOrderClick = { orderId ->
                    navController.navigate("vendor_order_detail/$orderId")
                },
                onLogout = {
                    coroutineScope.launch {
                        tokenManager.clearAuth()
                    }
                }
            )
        }
        composable(
            route = "vendor_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntryForRoute ->
            val orderId = backStackEntryForRoute.arguments?.getInt("orderId") ?: 0
            VendorOrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("admin_order_list") {
            AdminOrderListScreen(
                onOrderClick = { orderId ->
                    navController.navigate("admin_order_detail/$orderId")
                },
                onLogout = {
                    coroutineScope.launch {
                        tokenManager.clearAuth()
                    }
                }
            )
        }
        composable(
            route = "admin_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntryForRoute ->
            val orderId = backStackEntryForRoute.arguments?.getInt("orderId") ?: 0
            AdminOrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() },
                onPrintLabelClick = { id ->
                    navController.navigate("admin_print_preview/$id")
                }
            )
        }
        composable(
            route = "admin_print_preview/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntryForRoute ->
            val orderId = backStackEntryForRoute.arguments?.getInt("orderId") ?: 0
            PrintPreviewScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
