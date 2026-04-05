package com.example.loomcraftadmin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loomcraftadmin.data.local.TokenManager
import com.example.loomcraftadmin.ui.features.admin.orders.AdminOrderDetailScreen
import com.example.loomcraftadmin.ui.features.admin.orders.AdminOrderListScreen
import com.example.loomcraftadmin.ui.features.admin.shipping.PrintPreviewScreen
import com.example.loomcraftadmin.ui.features.auth.LoginScreen
import com.example.loomcraftadmin.ui.features.vendor.orders.VendorOrderDetailScreen
import com.example.loomcraftadmin.ui.features.vendor.orders.VendorOrderListScreen
import com.example.loomcraftadmin.ui.theme.LoomCraftAdminTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Handle permission result if needed
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
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
    val userRole by tokenManager.userRole.collectAsState(initial = null)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    val destination = if (userRole == "vendor") "vendor_order_list" else "admin_order_list"
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("vendor_order_list") {
            VendorOrderListScreen(
                onOrderClick = { orderId ->
                    navController.navigate("vendor_order_detail/$orderId")
                }
            )
        }
        composable(
            route = "vendor_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            VendorOrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("admin_order_list") {
            AdminOrderListScreen(
                onOrderClick = { orderId ->
                    navController.navigate("admin_order_detail/$orderId")
                }
            )
        }
        composable(
            route = "admin_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
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
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            PrintPreviewScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
