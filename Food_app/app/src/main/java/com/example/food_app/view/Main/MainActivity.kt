package com.example.food_app.view.Main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food_app.composegooglesignincleanarchitecture.presentation.sign_in.GoogleAuthUiClient
import com.example.food_app.composegooglesignincleanarchitecture.presentation.sign_in.SignInViewModel
import com.example.food_app.data.BroadcastReceiver.BroadcastReceiverInternet
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.Repository.CategoryRepository
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.Repository.FavoriteRepository
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.reviewApiService
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Cart.CartResponse
import com.example.food_app.data.model.Cart.CartResponseItem
import com.example.food_app.factory.ReviewViewModelFactory
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.view.Account.AddAddressScreen
import com.example.food_app.view.Account.AllAddressScreen
import com.example.food_app.view.Account.BackgroundAccount
import com.example.food_app.view.Account.ChangePasswordScreen
import com.example.food_app.view.Account.EditProfileScreen
import com.example.food_app.view.Account.InfoScreen
import com.example.food_app.view.Account.LoginOtherWay
import com.example.food_app.view.Account.LoginScreen
import com.example.food_app.view.Account.MessageScreen
import com.example.food_app.view.Account.OtpScreen
import com.example.food_app.view.Account.ResetPassScreen
import com.example.food_app.view.Account.SignInScreen
import com.example.food_app.view.Product.BankingScreen
import com.example.food_app.view.Product.CartScreen
import com.example.food_app.view.Product.CategoryScreen
import com.example.food_app.view.Product.DetailProductScreen
import com.example.food_app.view.Product.FavoriteScreen
import com.example.food_app.view.Product.OrdersScreen
import com.example.food_app.view.Product.PaymentScreen
import com.example.food_app.view.Product.WatchReviewScreen
import com.example.food_app.viewmodel.AuthViewModel
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.CategoryViewModel
import com.example.food_app.viewmodel.ChatModelFactory
import com.example.food_app.viewmodel.ChatViewModel
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.FavoriteViewModel
import com.example.food_app.viewmodel.LocationViewModel
import com.example.food_app.viewmodel.OrderViewModel
import com.example.food_app.viewmodel.ProductViewModel
import com.example.food_app.viewmodel.ReviewViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        var darkMode by mutableStateOf(false)

        setContent {
            Food_appTheme(darkTheme = darkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val br = BroadcastReceiverInternet(context)
                    val connectivityFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    val airplaneModeFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

                    context.registerReceiver(br, connectivityFilter)
                    context.registerReceiver(br, airplaneModeFilter)
                    MainApp(
                        changeDarkMode = { it ->
                            darkMode = it
                        },
                        darkMode = darkMode,
                        googleAuthUiClient = googleAuthUiClient
                    )
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(
    changeDarkMode: (Boolean) -> Unit,
    darkMode: Boolean,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    val context = LocalContext.current
    val navController = rememberNavController() // điều hướng giữa các màn hình
    var defaultRouter = "onBoard"
    val sharedPreferences = context.getSharedPreferences("customer_data", Context.MODE_PRIVATE)
    var customerIdInfo by remember {
        mutableIntStateOf(sharedPreferences.getInt("customer_id", -1))
    }

    val sharedPreferencesDark = context.getSharedPreferences("isDarkMode", Context.MODE_PRIVATE)
    var isDarkMode = sharedPreferencesDark.getBoolean("isDarkMode", darkMode)

    val sharedPreferencesLanguage = context.getSharedPreferences("isEng", Context.MODE_PRIVATE)
    var isEng = sharedPreferencesLanguage.getString("isEng", "en")
    var isEngValue by remember {
        mutableStateOf(isEng)
    }
    changeLocales(context, isEngValue!!)

    if (isDarkMode != darkMode) {
        changeDarkMode(isDarkMode)
    }
    if (customerIdInfo != -1) {
        defaultRouter = "homeScreen"
    }

    val factory = ChatModelFactory(customerIdInfo)
    val chatViewModel: ChatViewModel = viewModel(factory = factory)

    LaunchedEffect(customerIdInfo){
        Log.d("abc",customerIdInfo.toString())
        chatViewModel.restart(customerIdInfo)
    }
//    Food_appTheme(darkTheme = darkMode) {
    val categoryRepository = CategoryRepository(RetrofitClient.categoryApiService)
    val categoryViewModel: CategoryViewModel = remember {
        CategoryViewModel(categoryRepository = categoryRepository)
    }
    val productRepository = ProductRepository(RetrofitClient.productApiService)
    val productViewModel: ProductViewModel = remember {
        ProductViewModel(productRepository = productRepository)
    }
    val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
    val customerViewModel: CustomerViewModel = remember {
        CustomerViewModel(customerRepository)
    }
    val favoriteRepository = FavoriteRepository(RetrofitClient.favoriteApiService)
    val favoriteViewModel: FavoriteViewModel = remember {
        FavoriteViewModel(favoriteRepository = favoriteRepository)
    }
    val locationRepository = LocationReponsitory(RetrofitClient.locationApiService)
    val locationViewModel: LocationViewModel = remember {
        LocationViewModel(locationRepository = locationRepository)
    }
//    val orderRepository = OrderRepository(RetrofitClient.orderApiService)
//    val orderViewModel: OrderViewModel = remember {
//        OrderViewModel(orderRepository = orderRepository)
//    }
    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
    val cartViewModel: CartViewModel = remember {
        CartViewModel(cartRepository = cartRepository)
    }

    NavHost(navController = navController, startDestination = defaultRouter) {
        composable("onBoard") {
            OnBoardingScreen {
                navController.navigate("accountScreen") // mở màn hình đăng nhập
            }
        }
        composable("accountScreen") {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()
            val authRepository = AuthRepository(RetrofitClient.authApiService)
            val authViewModel: AuthViewModel = remember {
                AuthViewModel(authRepository = authRepository)
            }
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    val userData = googleAuthUiClient.getSignedInUser()
                    authViewModel.signUpGoogle(
                        userData!!.username!!,
                        userData!!.email!!,
                        if (userData.phoneNumber == null) "0" else userData!!.phoneNumber!!,
                        userData!!.profilePictureUrl!!,
                        userData.userId
                    ) { result, authSignUp ->
                        if (result) {
                            cartViewModel.listAllCartLiveData.value = null
                            locationViewModel.listAllLocationLiveData.value = null
                            favoriteViewModel.listAllFavoriteLiveData.value = null
                            productViewModel.listAllProductLiveData.value = null
                            categoryViewModel.listAllCategoryLiveData.value = null
                            customerViewModel.customerInfoLiveData.value = null

                            sharedPreferences.edit().putInt("customer_id", authSignUp!!).apply()
                            customerIdInfo = sharedPreferences.getInt("customer_id", -1)
                            navController.popBackStack()
                            navController.navigate("homeScreen")
                            viewModel.resetState()
                        }
                    }
                }
            }
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
//                    val userData = googleAuthUiClient.getSignedInUser()
//                    customerIdInfo  = sharedPreferences.getInt("customer_id",-1)
                    googleAuthUiClient.signOut()
                    navController.navigate("accountScreen")
                }
            }
            var viewModelScope = rememberCoroutineScope()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        viewModelScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )


            BackgroundAccount(
                content = {
                    LoginScreen(
                        openSignUpScreen = { navController.navigate("singUpScreen") }, // mở màn hình đăng ký
                        openResetPassScreen = { navController.navigate("resetPassScreen") }, // mở màn hình đăng kýopenResetPassScreen = { navController.navigate("resetPassScreen") }
                        openHomeScreen = {
                            cartViewModel.listAllCartLiveData.value = null
                            locationViewModel.listAllLocationLiveData.value = null
                            favoriteViewModel.listAllFavoriteLiveData.value = null
                            productViewModel.listAllProductLiveData.value = null
                            categoryViewModel.listAllCategoryLiveData.value = null
                            customerViewModel.customerInfoLiveData.value = null

                            customerIdInfo = sharedPreferences.getInt("customer_id", -1)
                            navController.navigate("homeScreen");
                        },
                        openOtpScreen = { username ->
                            navController.navigate("otpScreen/$username")
                        },
                    )
                },

                secondContent = {
                    LoginOtherWay(
                        state = state,
                        onSignInClick = {
                            viewModelScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                },
                isShowBackButton = true,
                navigationBack = { navController.popBackStack() }
            )
        }
        composable("singUpScreen") {
//            val viewModel = viewModel<SignInViewModel>()
//            val state by viewModel.state.collectAsState()
//
//            LaunchedEffect(key1 = Unit) {
//                if(googleAuthUiClient.getSignedInUser() != null) {
//                    navController.navigate("")
//                }
//            }
//            var viewModelScope = rememberCoroutineScope()
//
//            val launcher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartIntentSenderForResult(),
//                onResult = { result ->
//                    if(result.resultCode == RESULT_OK) {
//                        viewModelScope.launch {
//                            val signInResult = googleAuthUiClient.signInWithIntent(
//                                intent = result.data ?: return@launch
//                            )
//                            viewModel.onSignInResult(signInResult)
//                        }
//                    }
//                }
//            )
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()
            val authRepository = AuthRepository(RetrofitClient.authApiService)
            val authViewModel: AuthViewModel = remember {
                AuthViewModel(authRepository = authRepository)
            }
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    val userData = googleAuthUiClient.getSignedInUser()
                    authViewModel.signUpGoogle(
                        userData!!.username!!,
                        userData!!.email!!,
                        if (userData.phoneNumber == null) "0" else userData!!.phoneNumber!!,
                        userData!!.profilePictureUrl!!,
                        userData.userId
                    ) { result, authSignUp ->
                        if (result) {
                            cartViewModel.listAllCartLiveData.value = null
                            locationViewModel.listAllLocationLiveData.value = null
                            favoriteViewModel.listAllFavoriteLiveData.value = null
                            productViewModel.listAllProductLiveData.value = null
                            categoryViewModel.listAllCategoryLiveData.value = null
                            customerViewModel.customerInfoLiveData.value = null
                            sharedPreferences.edit().putInt("customer_id", authSignUp!!).apply()
                            customerIdInfo = sharedPreferences.getInt("customer_id", -1)
                            navController.popBackStack()
                            navController.navigate("homeScreen")
                            viewModel.resetState()
                        }
                    }
                }
            }
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
//                    val userData = googleAuthUiClient.getSignedInUser()
//                    customerIdInfo  = sharedPreferences.getInt("customer_id",-1)
                    googleAuthUiClient.signOut()
                    navController.navigate("accountScreen")
                }
            }
            var viewModelScope = rememberCoroutineScope()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        viewModelScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            BackgroundAccount(
                content = {
                    SignInScreen(
                        openCollectInfor = { phoneOrEmail, password ->
                            navController.navigate("collectInforScreen/$phoneOrEmail/$password")
                        },
                        openLoginScreen = {
                            navController.popBackStack()
                        }
                    )
                },
                secondContent = {
                    LoginOtherWay(
                        state = state,
                        onSignInClick = {
                            viewModelScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                },
                isShowBackButton = false,
                navigationBack = { navController.popBackStack() }
            )
        }
        composable("resetPassScreen") {
            ResetPassScreen(
                isShowBackButton = true,
                navigationBack = { navController.popBackStack() })
        }
        composable("collectInforScreen/{phoneOrEmail}/{password}",
            arguments = listOf(
                navArgument("phoneOrEmail") {
                    type = NavType.StringType
                },
                navArgument("password") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val phoneOrEmail = backStackEntry.arguments?.getString("phoneOrEmail")
            val password = backStackEntry.arguments?.getString("password")
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate("")
                }
            }
            var viewModelScope = rememberCoroutineScope()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        viewModelScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            BackgroundAccount(
                content = {
                    InfoScreen(
                        phoneOrEmail = phoneOrEmail!!,
                        password = password!!,
                        openLoginScreen = {
                            navController.popBackStack(
                                "accountScreen",
                                inclusive = false,
                                saveState = true
                            )
                        },
                        openOtpScreen = { username ->
                            navController.navigate("otpScreen/$username")
                        }
                    )
                },
                secondContent = {
                    LoginOtherWay(
                        state = state,
                        onSignInClick = {
                            viewModelScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                },
                isShowBackButton = true,
                navigationBack = { navController.popBackStack() }
            )
        }
        composable(
            "homeScreen",
        ) { backStackEntry ->
            var viewModelScope = rememberCoroutineScope()
            var abc by remember {
                mutableStateOf(false)
            }
            HomeScreen(
                customer_id = customerIdInfo!!,
                openLoginScreen = {

//                    LaunchedEffect(customerIdInfo){

//                    }
//                    cartViewModel.listAllCartLiveData.postValue(null)
//                    locationViewModel.listAllLocationLiveData.postValue(null)
//                    favoriteViewModel.listAllFavoriteLiveData.postValue(null)
//                    productViewModel.listAllProductLiveData.postValue(null)
//                    categoryViewModel.listAllCategoryLiveData.postValue(null)
//                    customerViewModel.customerInfoLiveData.postValue(null)
//                    LaunchedEffect(Unit){

//                    }
                    sharedPreferences.edit().putInt("customer_id", -1).apply()
                    navController.popBackStack()
                    navController.navigate("accountScreen")
                    viewModelScope.launch {
                        googleAuthUiClient.signOut()
                    }
                },
                openSearchScreen = { textSearch, value, category_id, customer_id ->
                    navController.navigate("searchScreen?textSearch=$textSearch/$value/$category_id/$customer_id")
                },
                openDetailScreen = { product_id, customer_id ->
                    navController.navigate("detailScreen/$product_id/$customer_id")
                },
                openFavoriteScreen = {
                    navController.navigate("favoriteScreen/$customerIdInfo")
                },
                openCartScreen = {
                    navController.navigate("cartScreen/$customerIdInfo")
                },
                openOrdersScreen = {
                    navController.navigate("ordersScreen/$customerIdInfo")
                },
                openLocationScreen = {
                    navController.navigate("allAddressScreen/$customerIdInfo")
                },
                openDetailLocationScreen = { locationId ->
                    navController.navigate("addAddressScreen/$customerIdInfo/$locationId")
                },
                openProfileScreen = {
                    navController.navigate("profileScreen/$customerIdInfo")
                },
                openSettingScreen = {
                    navController.navigate("settingScreen/$customerIdInfo")
                },
                openNotificationScreen = {
                    navController.navigate("notificationScreen/$customerIdInfo")
                },
                categoryViewModel,
                productViewModel,
                customerViewModel,
                favoriteViewModel,
                chatViewModel
            )
        }
        composable("otpScreen/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments!!.getString("username")
            OtpScreen(
                username = username!!,
                isShowBackButton = true,
                navigationBack = { navController.popBackStack() },
                openLoginScreen = { navController.navigate("accountScreen") }
            )
        }
        composable("resetPassScreen") {
            ResetPassScreen(
                isShowBackButton = true,
                navigationBack = { navController.popBackStack() }
            )
        }
        composable("searchScreen?textSearch={textSearch}/{value}/{category_id}/{customer_id}",
            arguments = listOf(
                navArgument("textSearch") {
                    type = NavType.StringType
                },
                navArgument("value") {
                    type = NavType.IntType
                },
                navArgument("category_id") {
                    type = NavType.IntType
                },
                navArgument("customer_id") {
                    type = NavType.IntType
                }
            )) { backStackEntry ->
            val textSearch = backStackEntry.arguments?.getString("textSearch")
            val value = backStackEntry.arguments?.getInt("value")
            val category_id = backStackEntry.arguments?.getInt("category_id")
            val customer_id = backStackEntry.arguments?.getInt("customer_id")

            CategoryScreen(
                customer_id = customer_id!!,
                text = textSearch,
                value = value,
                category_id = category_id,
                openHomeScreen = { navController.popBackStack() },
                openSearchScreen = { textSearch1, value, category_id, customer_id ->
                    navController.popBackStack()
                    navController.navigate("searchScreen?textSearch=$textSearch1/$value/$category_id/$customer_id")
                },
                openDetailScreen = { product_id, customer_id ->
                    navController.navigate("detailScreen/$product_id/$customer_id")
                },
                favoriteViewModel
            )
        }
        composable(
            "detailScreen/{product_id}/{customer_id}",
            arguments = listOf(
                navArgument("product_id") {
                    type = NavType.IntType
                },
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )

        ) { backStackEntry ->
            val product_id = backStackEntry.arguments?.getInt("product_id")
            val customer_id = backStackEntry.arguments?.getInt("customer_id")
            DetailProductScreen(
                product_id = product_id!!,
                navigationBack = { navController.popBackStack() },
                customer_id = customer_id!!,
                openPaymentScreen = { listProductIds, customer_id, totalPrice, discountCode, quantity ->
                    val listProductIdsString = listProductIds.joinToString(",")
                    navController.navigate("paymentScreen/$listProductIdsString/$customer_id/$totalPrice/$discountCode/$quantity")
                },
                openReviewScreen = {
                    navController.navigate("reviewScreen/$customer_id/$product_id")
                },
                favoriteViewModel,
                cartViewModel
            )
        }

        composable(
            "favoriteScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->
            val customer_id = backStackEntry.arguments?.getInt("customer_id")
            FavoriteScreen(
                customer_id = customer_id!!,
                navigationBack = { navController.popBackStack() },
                openDetailScreen = { product_id, customer_id ->
                    navController.navigate("detailScreen/$product_id/$customer_id")
                },
                customerViewModel,
                favoriteViewModel
            )
        }
        composable(
            "cartScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->
            val customer_id = backStackEntry.arguments?.getInt("customer_id")
            CartScreen(
                customer_id = customer_id!!,
                navigationBack = { navController.popBackStack() },
                openDetailScreen = { product_id, customer_id ->
                    navController.navigate("detailScreen/$product_id/$customer_id")
                },
                openPaymentScreen = { listProductIds, totalPrice, discountCode ->
                    val listProductIdsString = listProductIds.joinToString(",")
                    val quantity = -1
                    navController.navigate("paymentScreen/$listProductIdsString/$customer_id/$totalPrice/$discountCode/$quantity")
                },
                cartViewModel
            )
        }
        composable(
            "paymentScreen/{listProductIds}/{customer_id}/{totalPrice}/{discountCode}/{quantity}",
            arguments = listOf(
                navArgument("listProductIds") {
                    type = NavType.StringType
                },
                navArgument("customer_id") {
                    type = NavType.IntType
                },
                navArgument("totalPrice") {
                    type = NavType.FloatType
                },
                navArgument("discountCode") {
                    type = NavType.StringType
                },
                navArgument("quantity") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val listProductIdsString = backStackEntry.arguments?.getString("listProductIds")
            val listProductIds =
                listProductIdsString?.split(",")?.map { it.toInt() } ?: emptyList()
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            val totalPrice = backStackEntry.arguments?.getFloat("totalPrice") ?: 0f
            val discountCode = backStackEntry.arguments?.getString("discountCode") ?: ""
            val quantity = backStackEntry.arguments?.getInt("quantity")

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PaymentScreen(
                product_id = listProductIds,
                navigationBack = { navController.popBackStack() },
                customer_id = customerId!!,
                totalPrice = totalPrice,
                discountCode = discountCode,
                openHomeScreen = {
                    cartViewModel.getAllCart(customerId!!) { result, product ->
                        if (result) {
                            cartViewModel.listAllCartLiveData.value = product!!
                        }
                    }
                    navController.popBackStack()
                },
                quantity = quantity!!,
                openLocationScreen = {
                    navController.navigate("allAddressScreen/$customerId")
                },
                openBankingScreen = { listProductIds, customer_id, note, payment, discountValue, totalPrice, quantity, location_id ->
                    val listProductIdsString = listProductIds.joinToString(",")
                    navController.navigate("bankingScreen/$listProductIdsString/$customerId/$note/$payment/$discountValue/$totalPrice/$quantity/$location_id")
                },
                locationViewModel,
                cartViewModel
            )
//            }
        }
        composable("reviewScreen/{customer_id}/{product_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
                navArgument("product_id") {
                    type = NavType.IntType
                }
            )) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            val product_id = backStackEntry.arguments?.getInt("product_id")

            WatchReviewScreen(
                navigationBack = { navController.popBackStack() },
                customer_id = customerId!!,
                product_id = product_id!!,
                reloadScreen = { customer_id, product_id ->
                    navController.popBackStack()
                    navController.navigate("reviewScreen/$customer_id/$product_id")
                },
                customerViewModel,
            )
        }
        composable(
            "OrdersScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            OrdersScreen(
                customer_id = customerId!!,
                navigationBack = { navController.popBackStack() },
                reloadScreen = {
                    navController.popBackStack()
                    navController.navigate("OrdersScreen/$customerId")
                },
                openDetailScreen = { product_id ->
                    navController.navigate("detailScreen/$product_id/$customerId")
                },
                openRateScreen = { product_id ->
                    navController.navigate("reviewScreen/$customerId/$product_id")
                },
                customerViewModel,
            )
        }
        composable("allAddressScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                }
            )) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            AllAddressScreen(
                customer_id = customerId!!,
                navigationBack = { navController.popBackStack() },
                reloadScreen = {
                    navController.popBackStack()
                    navController.navigate("allAddressScreen/$customerId")
                },
                openDetailScreen = { locationId ->
                    navController.navigate("addAddressScreen/$customerId/$locationId")
                },
                locationViewModel
            )
        }
        composable("addAddressScreen/{customer_id}/{location_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
                navArgument("location_id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            val location_id = backStackEntry.arguments?.getInt("location_id")
            AddAddressScreen(
                customer_id = customerId!!,
                location_id = location_id!!,
                navigationBack = {
                    navController.popBackStack()
                },
                locationViewModel
            )
        }
        composable(
            "profileScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            EditProfileScreen(
                customer_id = customerId!!,
                navigationBack = {
                    navController.popBackStack()
                },
                customerViewModel
            )
        }
        composable(
            "settingScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            SettingScreen(
                customer_id = customerId!!,
                navigationBack = {
                    navController.popBackStack()
                },
                changeDarkMode = {
                    isDarkMode = !isDarkMode
                    sharedPreferencesDark.edit().putBoolean("isDarkMode", isDarkMode).apply()
                    changeDarkMode(
                        isDarkMode
                    )
                },
                isDarkMode = darkMode,
                openChangePassScreen = {
                    navController.navigate("changePasswordScreen/$customerId")
                },
                isEng = isEngValue!!,
                changeLanguage = {
                    if (isEngValue == "en") isEngValue = "vi" else isEngValue = "en"
                    sharedPreferencesLanguage.edit().putString("isEng", isEngValue).apply()
                    changeLocales(context, isEngValue!!)
                },
                openReport = {
                    navController.navigate("messageScreen/$customerId")
                }
            )
        }
        composable(
            "changePasswordScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            ChangePasswordScreen(
                customer_id = customerId!!,
                navigationBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            "notificationScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            NotificationScreen(
                customer_id = customerId!!,
                openOrderScreen = {
                    navController.navigate("OrdersScreen/$customerId")
                }
            )
        }
        composable(
            "bankingScreen/{listProductIds}/{customer_id}/{note}/{payment}/{discountCode}/{totalPrice}/{quantity}/{location_id}",
            arguments = listOf(
                navArgument("listProductIds") {
                    type = NavType.StringType
                },
                navArgument("customer_id") {
                    type = NavType.IntType
                },
                navArgument("note") {
                    type = NavType.StringType
                },
                navArgument("payment") {
                    type = NavType.StringType
                },
                navArgument("discountCode") {
                    type = NavType.IntType
                },
                navArgument("totalPrice") {
                    type = NavType.FloatType
                },
                navArgument("quantity") {
                    type = NavType.IntType
                },
                navArgument("location_id") {
                    type = NavType.IntType
                },
            )
        ) { backStackEntry ->
            val listProductIdsString = backStackEntry.arguments?.getString("listProductIds")
//            val listProductIds =
//                listProductIdsString?.split(",")?.map { it.toInt() } ?: emptyList()
            val note = backStackEntry.arguments?.getString("note") ?: ""
            val payment = backStackEntry.arguments?.getString("payment") ?: ""
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            val totalPrice = backStackEntry.arguments?.getFloat("totalPrice") ?: 0f
            val discountCode = backStackEntry.arguments?.getInt("discountCode") ?: -1
            val quantity = backStackEntry.arguments?.getInt("quantity")
            val location_id = backStackEntry.arguments?.getInt("location_id")

            BankingScreen(
                product_id = listProductIdsString!!,
                navigationBack = {
                    navController.popBackStack()
                    navController.popBackStack()
                },
                customer_id = customerId!!,
                note = note,
                totalPrice = totalPrice,
                discountCode = discountCode,
                quantity = quantity!!,
                payment = payment,
                location_id = location_id!!,
                cartViewModel = cartViewModel
            )
        }
        composable("messageScreen/{customer_id}",
            arguments = listOf(
                navArgument("customer_id") {
                    type = NavType.IntType
                }
            )) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customer_id")
            MessageScreen(
                chatViewModel = chatViewModel,
                customer_id = customerId!!
            )
        }
    }
//    }
}


fun changeLocales(context: Context, localeString: String) {
    val localeList = LocaleList.forLanguageTags(localeString)
    LocaleList.setDefault(localeList)
    val config = context.resources.configuration
    config.setLocales(localeList)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

@ExperimentalPagerApi
@Preview(showBackground = true, name = "test")
@Composable
fun GreetingPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
//        MainFunction()
    }
}
