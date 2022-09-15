package com.example.a3dsecurepaymentapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a3dsecurepaymentapp.presentation.ui.theme.Secure3DAppTheme
import dagger.hilt.android.AndroidEntryPoint
import  com.example.a3dsecurepaymentapp.presentation.card_details.CardDetailsScreen
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Secure3DAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CardDetailScreen.route
                    ) {
                        composable(
                            route = Screen.CardDetailScreen.route
                        ) {
                            CardDetailsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}