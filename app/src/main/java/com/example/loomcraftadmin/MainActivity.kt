package com.example.loomcraftadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loomcraftadmin.ui.features.vendor.orders.VendorOrderDetailScreen
import com.example.loomcraftadmin.ui.features.vendor.orders.VendorOrderDetailViewModel
import com.example.loomcraftadmin.ui.features.vendor.orders.VendorOrderListScreen
import com.example.loomcraftadmin.ui.theme.LoomCraftAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoomCraftAdminTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "vendor_order_list") {
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
            val viewModel = VendorOrderDetailViewModel(orderId)
            VendorOrderDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
