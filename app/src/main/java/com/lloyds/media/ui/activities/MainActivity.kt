package com.lloyds.media.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lloyds.media.ui.components.details.MediaDetailsScreen
import com.lloyds.media.ui.components.home.FavouritesScreen
import com.lloyds.media.ui.components.home.TrendingScreen
import com.lloyds.media.ui.components.home.models.HomeScreen
import com.lloyds.media.utils.FontUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var appBarTitle by remember { mutableStateOf(HomeScreen.Trending.title) }
            Scaffold(topBar = {
                Surface(shadowElevation = 3.dp) {
                    TopAppBar(
                        title = {
                            Text(
                                text = appBarTitle,
                                fontFamily = FontUtils.robotoFamily,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }, colors = TopAppBarColors(
                            containerColor = Color.White,
                            scrolledContainerColor = Color.Transparent,
                            navigationIconContentColor = Color.Black,
                            titleContentColor = Color.Black,
                            actionIconContentColor = Color.Black
                        )
                    )
                }
            }, bottomBar = {
                BottomNavigationBarUI(navController)
            }) {
                BottomNavGraph(navController = navController,
                    modifier = Modifier.padding(it),
                    updateTitle = { appBarTitle = it })
            }
        }
    }


    @Composable
    fun BottomNavGraph(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        updateTitle: (title: String) -> Unit
    ) {
        NavHost(
            navController, startDestination = HomeScreen.Trending.route, modifier = modifier
        ) {
            composable(HomeScreen.Trending.route) {
                updateTitle(HomeScreen.Trending.title)
                TrendingScreen { mediaId, mediaType, mediaTitle ->
                    navController.navigate(
                        HomeScreen.MediaDetails.route(
                            mediaType, mediaId, mediaTitle
                        )
                    )
                }
            }
            composable(HomeScreen.Favourites.route) {
                updateTitle(HomeScreen.Favourites.title)

                val status = it.savedStateHandle.get<Boolean?>("status")
                it.savedStateHandle.remove<Boolean?>("status")
                FavouritesScreen(refresh = status == true) { mediaId, mediaType, mediaTitle ->
                    navController.navigate(
                        HomeScreen.MediaDetails.route(
                            mediaType, mediaId, mediaTitle
                        )
                    )
                }
            }
            composable(
                route = HomeScreen.MediaDetails.route, arguments = listOf(
                    navArgument("mediaId") { type = NavType.IntType },
                    navArgument("mediaType") { type = NavType.StringType },
                    navArgument("mediaTitle") { type = NavType.StringType },
                )
            ) {
                updateTitle(it.arguments?.getString("mediaTitle") ?: "Media Details")
                MediaDetailsScreen { mediaId, status ->
                    if (navController.previousBackStackEntry?.destination?.route == HomeScreen.Favourites.route) {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "itemId",
                            mediaId
                        )
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            "status",
                            status
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBarUI(navController: NavHostController) {
        val items = listOf(
            HomeScreen.Trending, HomeScreen.Favourites
        )
        NavigationBar {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            items.forEach { screen ->
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = screen.drawable),
                        contentDescription = screen.title
                    )
                }, label = {
                    Text(
                        text = screen.title,
                        fontSize = 12.sp,
                        fontFamily = FontUtils.robotoFamily,
                        fontWeight = FontWeight.Normal
                    )
                }, selected = currentRoute == screen.route, onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                })
            }

        }
    }
}

