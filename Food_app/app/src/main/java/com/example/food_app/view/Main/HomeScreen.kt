package com.example.food_app.view.Main

//import com.example.food_app.ui.theme.BackgroundCategoryColor
//import com.example.food_app.ui.theme.BottomNavigationIconColor
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.NormalTextColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.TextBlackColor
//import com.example.food_app.ui.theme.TitleTextColor
//import com.example.food_app.ui.theme.UnClickColor
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.BASE_URL_API_CATEGORY
import com.example.food_app.BASE_URL_API_PRODUCT
import com.example.food_app.R
import com.example.food_app.data.Repository.CategoryRepository
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.Repository.DiscountRepository
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.categoryApiService
import com.example.food_app.data.api.RetrofitClient.locationApiService
import com.example.food_app.data.api.RetrofitClient.orderApiService
import com.example.food_app.data.api.RetrofitClient.productApiService
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Category.Category
import com.example.food_app.data.model.Category.CategoryResponse
import com.example.food_app.data.model.Discount.DiscountResponse
import com.example.food_app.data.model.Orders.OrderNotifiResponseItem
import com.example.food_app.data.model.Product.Image
import com.example.food_app.data.model.Product.ProductResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.AvatarButton
import com.example.food_app.view.ButtonLoginOther
import com.example.food_app.view.DiscountDialog
import com.example.food_app.view.FavoriteButton
import com.example.food_app.view.LabelCategory
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.NormalItem
import com.example.food_app.view.RateComponent
import com.example.food_app.view.RecommendItem
import com.example.food_app.view.SearchFieldNormal
import com.example.food_app.view.TagItem
import com.example.food_app.view.TextHeader
import com.example.food_app.view.TextWithIcon
import com.example.food_app.view.TittleTextMedium
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.CategoryViewModel
import com.example.food_app.viewmodel.ChatViewModel
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.DiscountViewModel
import com.example.food_app.viewmodel.FavoriteViewModel
import com.example.food_app.viewmodel.LocationViewModel
import com.example.food_app.viewmodel.OrderViewModel
import com.example.food_app.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

object GlobalDiscount {
    var isShowedDiscount by mutableStateOf(false)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    customer_id: Int,
    openLoginScreen: () -> Unit,
    openSearchScreen: (String, Int, Int, Int) -> Unit,
    openDetailScreen: (Int, Int) -> Unit,
    openFavoriteScreen: () -> Unit,
    openCartScreen: () -> Unit,
    openOrdersScreen: () -> Unit,
    openLocationScreen: () -> Unit,
    openDetailLocationScreen: (Int) -> Unit,
    openProfileScreen: () -> Unit,
    openSettingScreen: () -> Unit,
    openNotificationScreen: () -> Unit,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    customerViewModel: CustomerViewModel,
    favoriteViewModel: FavoriteViewModel,
    chatViewModel: ChatViewModel
) {
//    val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
//    val customerViewModel: CustomerViewModel = remember {
//        CustomerViewModel(customerRepository)
//    }

//    LaunchedEffect(customer_id){
//        cartViewModel.listAllCartLiveData.value = null
//        locationViewModel.listAllLocationLiveData.value = null
//        favoriteViewModel.listAllFavoriteLiveData.value = null
//        productViewModel.listAllProductLiveData.value = null
//        categoryViewModel.listAllCategoryLiveData.value = null
//        customerViewModel.customerInfoLiveData.value = null
//        Log.d("abc","efergt")
//    }

    //restart server socket
//    LaunchedEffect(customer_id){
//        Log.d("abc","change")
//        chatViewModel.restart(customer_id)
//    }
    var isShowDrawer by remember {
        mutableStateOf(false)
    }
    if (favoriteViewModel.listAllFavoriteLiveData.value != null) {

    } else {
        favoriteViewModel.getAllFavorite(customer_id) { result, listFavorite ->
            if (result) {
                favoriteViewModel.listAllFavoriteLiveData.value = listFavorite!!
            } else {
            }
        }
    }
    var customerInfo by remember {
        mutableStateOf(
            customerResponse(
                created_time = "",
                email = "abc@gmail.com",
                full_name = "Customer Name",
                id = 0,
                image_url = "",
                phone_number = "",
                social_link = "",
                updated_time = ""
            )
        )
    }
    val context = LocalContext.current

    if (customerViewModel.customerInfoLiveData.value == null) {

        customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
            if (result) {
                customerInfo = customerResponse!!
            } else {

            }
        }
    } else {
        customerInfo = customerViewModel.customerInfoLiveData.value!!
    }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldSate = rememberScaffoldState()
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldSate,
        drawerContent = {
            if (isShowDrawer) {
                coroutineScope.launch {
                    scaffoldSate.drawerState.open() //đóng drawer
                    isShowDrawer = !isShowDrawer
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Food_appTheme.myColors.darkBackground)
                    .padding(vertical = 36.dp, horizontal = 26.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.8f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {


                    Column {
//                        Image(
//                            painterResource(
//                                id = R.drawable.avatar
//                            ),
//                            contentDescription = "",
//                            modifier = Modifier
//                            .size(120.dp)
//                            .clip(CircleShape))
                        if (customerInfo.image_url != null) {
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .shadow(3.dp, CircleShape)
                            ) {
                                AsyncImage(
                                    model =
                                    if (customerInfo.image_url.startsWith("https")) {
                                        customerInfo.image_url
                                    } else {
                                        BASE_URL_API_AVATAR + customerInfo.image_url
                                    },
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop

                                )
                            }
                        } else {
                            Image(
                                painterResource(
                                    id = R.drawable.avatar
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .shadow(3.dp, RoundedCornerShape(120.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = customerInfo.full_name,
                            style = Typography.headlineMedium.copy(color = Food_appTheme.myColors.textBlackColor)
                        )
                        Text(
                            text = customerInfo.email,
                            style = Typography.titleMedium,
                            color = Food_appTheme.myColors.normalTextColor
                        )

                    }
                    stringResource(id = R.string.myOrders)
                    IconWithText(
                        icon = R.drawable.order,
                        text = stringResource(id = R.string.myOrders),
                        iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                        iconSize = 23,
                        style = Typography.titleSmall,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openOrdersScreen()
                            },
                        position = true
                    )
                    IconWithText(
                        icon = R.drawable.profile,
                        text = stringResource(id = R.string.myProfile),
                        iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                        iconSize = 23,
                        style = Typography.titleSmall,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openProfileScreen()
                            },
                        position = true
                    )
                    IconWithText(
                        icon = R.drawable.location,
                        text = stringResource(id = R.string.deliveryAddress),
                        iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                        iconSize = 23,
                        style = Typography.titleSmall,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openLocationScreen()
                            },
                        position = true
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openNotificationScreen()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val orderRepository = OrderRepository(orderApiService)
                        val orderViewModel: OrderViewModel = remember {
                            OrderViewModel(orderRepository = orderRepository)
                        }
//                        var numberNotifi by remember {
//                            mutableStateOf(0)
//                        }
                        var numberNotifi = 0
                        var listNotiffi by remember {
                            mutableStateOf(listOf<OrderNotifiResponseItem>())
                        }

                        orderViewModel.getOrderInFiveDays(customer_id) { result, order ->
                            if (result) {
                                listNotiffi = order!!
                            } else {
                            }
                        }
                        listNotiffi.forEach { aNotifi ->
                            if (aNotifi.status == "completed" || aNotifi.status == "rating") {
                                numberNotifi += 1
                            }
                        }
//                        numberNotifi =

                        IconWithText(
                            icon = R.drawable.bell,
                            text = stringResource(id = R.string.notification),
                            iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                            iconSize = 23,
                            style = Typography.titleSmall,
                            textColor = Food_appTheme.myColors.textBlackColor,
                            modifier = Modifier,
                            position = true
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Food_appTheme.myColors.mainColor),
                            contentAlignment = Alignment.Center,

                            ) {
                            Text(
                                text = (numberNotifi + listNotiffi.size).toString(),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    IconWithText(
                        icon = R.drawable.setting,
                        text = stringResource(id = R.string.settings),
                        iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                        iconSize = 23,
                        style = Typography.titleSmall,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openSettingScreen()
                            },
                        position = true
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.2f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ButtonLoginOther(
                        width = 117,
                        height = 43,
                        color = Food_appTheme.myColors.mainColor,
                        astyle = Typography.titleSmall,
                        image = R.drawable.power,
                        imgSize = 16,
                        text = stringResource(id = R.string.logOut),
                        circleSize = 26,
                        circleColor = Color.White,
                        textColor = Color.White,
                        isImage = false,
                        openScreen = {
//                            Log.d("abc", customerViewModel.customerInfoLiveData.value.toString())
//                            cartViewModel.listAllCartLiveData.value = null
//                            locationViewModel.listAllLocationLiveData.value = null
//                            favoriteViewModel.listAllFavoriteLiveData.value = null
//                            productViewModel.listAllProductLiveData.value = null
//                            categoryViewModel.listAllCategoryLiveData.value = null
//                            customerViewModel.customerInfoLiveData.value = null
//                            Log.d("abc", customerViewModel.customerInfoLiveData.value.toString())
                            openLoginScreen()
                        }
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(60.dp)
                    .shadow(((3).dp), RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                    .background(Food_appTheme.myColors.mainColor),
                containerColor = Food_appTheme.myColors.menuColor
            ) {
                var selectedItem by remember { mutableStateOf(0) }
                BottomNavigationItem(
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    icon = {
                        IconBottomNavigation(
                            iconColor = Food_appTheme.myColors.bottomNavigationIconColor.copy(
                                0.7f
                            ),
                            size = 28,
                            icon = R.drawable.home,
                            sizeBox = 45,
                            activeIcon = Color.White,
                            activeBackground = Food_appTheme.myColors.mainColor,
                            isSelected = selectedItem == 0
                        )
                    })
                BottomNavigationItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        openCartScreen()
                    },
                    icon = {
                        IconBottomNavigation(
                            iconColor = Food_appTheme.myColors.bottomNavigationIconColor,
                            size = 28,
                            icon = R.drawable.cart,
                            sizeBox = 45,
                            activeIcon = Color.White,
                            activeBackground = Food_appTheme.myColors.mainColor,
                            isSelected = selectedItem == 1
                        )
                    })
                BottomNavigationItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        openFavoriteScreen()
                    },
                    icon = {
                        IconBottomNavigation(
                            iconColor = Food_appTheme.myColors.bottomNavigationIconColor.copy(
                                0.7f
                            ),
                            size = 28,
                            icon = R.drawable.heart_light,
                            sizeBox = 45,
                            activeIcon = Color.White,
                            activeBackground = Food_appTheme.myColors.mainColor,
                            isSelected = selectedItem == 2
                        )
                    })
                BottomNavigationItem(
                    selected = selectedItem == 3,
                    onClick = {
                        selectedItem = 3
                        openProfileScreen()
                    },
                    icon = {
                        IconBottomNavigation(
                            iconColor = Food_appTheme.myColors.bottomNavigationIconColor.copy(
                                0.7f
                            ),
                            size = 28,
                            icon = R.drawable.user,
                            sizeBox = 45,
                            activeIcon = Color.White,
                            activeBackground = Food_appTheme.myColors.mainColor,
                            isSelected = selectedItem == 3
                        )
                    })
            }
        },
    ) { paddingvalue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // đẩy nội dung dưới cùng lên
                .background(Food_appTheme.myColors.darkBackground)
        ) {
            var isShowDiscount by remember {
                mutableStateOf(false)
            }

            val discountRepository = DiscountRepository(RetrofitClient.discountApiService)
            val discountViewModel: DiscountViewModel = remember {
                DiscountViewModel(discountRepository = discountRepository)
            }
            var discountValue by remember {
                mutableStateOf(
                    DiscountResponse(
                        available = 0,
                        create_time = "",
                        discount_code = "",
                        discount_percent = 0,
                        id = 0,
                        update_time = "",
                        expiration_time = "",
                        number_uses = 0
                    )
                )
            }
            if (!GlobalDiscount.isShowedDiscount) {
                discountViewModel.getNewDiscount() { result, discount ->
                    if (result) {
                        discountValue = discount!!
                        isShowDiscount = true
                        GlobalDiscount.isShowedDiscount = true
                    }
                }
            }
            if (isShowDiscount) {
                DiscountDialog(
                    sizeH = 395,
                    sizeW = 327,
                    startColor = Color(0xffFFEB34),
                    endColor = Color(0xffE76F00),
                    title = stringResource(id = R.string.hurryOffer),
                    discountCode = discountValue.discount_code,
                    discountPercent = discountValue.discount_percent,
                    acceptText = stringResource(id = R.string.gotIt),
                    icon = R.drawable.remove,
                    iconSize = 25,
                    onCancel = { isShowDiscount = false },
                    onConfirm = { isShowDiscount = false }
                )
            }


//            if(isShowHome){
            ContentHomeScreen(
                navigationFun = { isShowDrawer = !isShowDrawer },
                customer_id = customer_id,
                image_url = if (customerInfo.image_url != null) {
                    if (customerInfo.image_url.startsWith("https")) {
                        customerInfo.image_url
                    } else {
                        BASE_URL_API_AVATAR + customerInfo.image_url
                    }
                } else {
                    null
                },
                openSearchScreen = openSearchScreen,
                openDetailScreen = openDetailScreen,
                openDetailLocationScreen = openDetailLocationScreen,
                categoryViewModel = categoryViewModel,
                productViewModel = productViewModel,
                favoriteViewModel = favoriteViewModel
            )
//            }
        }

    }
}

@Composable
fun IconBottomNavigation(
    iconColor: Color,
    size: Int,
    modifier: Modifier = Modifier,
    icon: Int,
    sizeBox: Int,
    activeIcon: Color,
    activeBackground: Color,
    isSelected: Boolean,
) {
    val color = if (isSelected) activeIcon else iconColor
    val backgroundColor = if (isSelected) activeBackground else Color.Transparent
    Box(
        modifier = Modifier
            .size(sizeBox.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "",
            tint = color,
            modifier = modifier.size(size.dp)
        )
    }
}

@Composable
fun ContentHomeScreen(
    navigationFun: () -> Unit,
    customer_id: Int,
    image_url: String?,
    openSearchScreen: (String, Int, Int, Int) -> Unit,
    openDetailScreen: (Int, Int) -> Unit,
    openDetailLocationScreen: (Int) -> Unit,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    favoriteViewModel: FavoriteViewModel,
) {
    val context = LocalContext.current

    val locationRepository = LocationReponsitory(locationApiService)
    val locationViewModel: LocationViewModel = remember {
        LocationViewModel(locationRepository = locationRepository)
    }
    var locationDefault by remember {
        mutableStateOf("Việt Nam")
    }
    var locationDefaultId by remember {
        mutableStateOf(-1)
    }
    var notExistLocation by remember {
        mutableStateOf(true)
    }
    locationViewModel.getDefaultAddress(customer_id) { result, location ->
        if (result) {
            locationDefault = location!!.address
            locationDefaultId = location!!.id
            if (location.address == "Việt Nam") notExistLocation = false
        }
    }

    if (!notExistLocation) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = stringResource(id = R.string.firstAddress),
                    style = Typography.bodyLarge.copy(Food_appTheme.myColors.mainColor)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.addYourfirst),
                    style = Typography.labelMedium.copy(Food_appTheme.myColors.textBlackColor)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDetailLocationScreen(locationDefaultId)
                        notExistLocation = true
                    },
                    colors = ButtonDefaults.buttonColors(Food_appTheme.myColors.mainColor)
                ) {
                    Text(
                        stringResource(id = R.string.accept),
                        style = Typography.bodyLarge.copy(Food_appTheme.myColors.textBlackColor)
                    )
                }
            },
        )
    }
    var listPopular by remember {
        mutableStateOf(listOf<ProductResponseItem>())
    }
//    val productRepository = ProductRepository(productApiService)
//    val productViewModel: ProductViewModel = remember {
//        ProductViewModel(productRepository = productRepository)
//    }
    if (productViewModel.listAllProductPopularLiveData.value == null) {
        productViewModel.getPopularProducts() { result, products ->
            if (result) {
                listPopular = products!!

            } else {
            }
        }
    } else {
        listPopular = productViewModel.listAllProductPopularLiveData.value!!

    }

    var listAll by remember {
        mutableStateOf(listOf<ProductResponseItem>())
    }
    if (productViewModel.listAllProductLiveData.value == null) {
        productViewModel.getAllProducts() { result, products ->
            if (result) {
                listAll = products!!

            } else {
            }
        }
    } else {
        listAll = productViewModel.listAllProductLiveData.value!!
    }
    Column(
        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 26.dp, bottom = 25.dp, end = 26.dp, top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            HeaderScreen(navigationFun, locationDefault, image_url)
            TextHeader(
                style = Typography.headlineMedium.copy(Food_appTheme.myColors.textBlackColor),
                text = stringResource(id = R.string.whatWouldYouLike)
            )
            SearchAndSort(
                openSearchScreen = openSearchScreen,
                customer_id
            )
        }
        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 15.dp)
        ) {
            //category
            item {
                var listCategory by remember {
                    mutableStateOf(listOf<Category>())
                }
//                val categoryRepository = CategoryRepository(categoryApiService)
//                val categoryViewModel: CategoryViewModel = remember {
//                    CategoryViewModel(categoryRepository = categoryRepository)
//                }
                if (categoryViewModel.listAllCategoryLiveData.value == null) {
                    categoryViewModel.getAllCategory { result, category ->
                        if (result) {
                            listCategory = category!!.categories
                        } else {
                        }
                    }
                } else {
                    listCategory = categoryViewModel.listAllCategoryLiveData.value!!.categories
                }
                DisplayListCategory(
                    openSearchScreen = openSearchScreen,
                    customer_id = customer_id,
                    listCategory = listCategory
                )
            }
            //title hot search
//            item {
//                TittleTextMedium(
//                    text = stringResource(id = R.string.topSearch),
//                    style = Typography.bodyLarge,
//                    color = Food_appTheme.myColors.titleTextColor,
//                    modifier = Modifier.padding(start = 26.dp)
//                )
//
//            }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.Bottom
                ) {
                    TittleTextMedium(
                        text = stringResource(id = R.string.topSearch),
                        style = Typography.bodyLarge,
                        color = Food_appTheme.myColors.titleTextColor
                    )
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        TextWithIcon(
                            text = stringResource(id = R.string.viewAll),
                            color = Food_appTheme.myColors.mainColor,
                            iconColor = Food_appTheme.myColors.mainColor,
                            iconSize = 10,
                            style = Typography.labelSmall,
                            icon = R.drawable.right_icon,
                            modifier = Modifier.clickable {
                                openSearchScreen(
                                    "",
                                    6,
                                    -1,
                                    customer_id
                                )
                            }
                        )
                    }
                }
            }
            //hot search

            item {
                var listProduct by remember {
                    mutableStateOf(listOf<ProductResponseItem>())
                }
                if (productViewModel.listAllProductByHotSearchLiveData.value != null) {

                    listProduct = productViewModel.listAllProductByHotSearchLiveData.value!!
                } else {

                    productViewModel.getAllProductsByHotSearch { result, listAllPNew ->
                        if (result) {
                            listProduct = listAllPNew!!
                        } else {
                        }
                    }
                }
                DisplayListTopSearch(
                    openDetailScreen,
                    customer_id,
                    listProduct,
                    favoriteViewModel
                )
            }
            //title recommend
//            item {
//                TittleTextMedium(
//                    text = stringResource(id = R.string.recommendItem),
//                    style = Typography.bodyLarge,
//                    color = Food_appTheme.myColors.titleTextColor,
//                    modifier = Modifier.padding(start = 26.dp)
//                )
//
//            }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.Bottom
                ) {
                    TittleTextMedium(
                        text = stringResource(id = R.string.recommendItem),
                        style = Typography.bodyLarge,
                        color = Food_appTheme.myColors.titleTextColor
                    )
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        TextWithIcon(
                            text = stringResource(id = R.string.viewAll),
                            color = Food_appTheme.myColors.mainColor,
                            iconColor = Food_appTheme.myColors.mainColor,
                            iconSize = 10,
                            style = Typography.labelSmall,
                            icon = R.drawable.right_icon,
                            modifier = Modifier.clickable {
                                openSearchScreen(
                                    "",
                                    5,
                                    -1,
                                    customer_id
                                )
                            }
                        )
                    }
                }
            }
            //recommend
            item {
                var listProduct by remember {
                    mutableStateOf(listOf<ProductResponseItem>())
                }
                if (productViewModel.listAllProductRecommendLiveData.value != null) {

                    listProduct = productViewModel.listAllProductRecommendLiveData.value!!
                } else {
                    productViewModel.getProductsRecommend { result, listAllPNew ->
                        if (result) {
                            listProduct = listAllPNew!!
                        }
                    }
                }
                DisplayListRecommend(
                    openDetailScreen,
                    customer_id,
                    listProduct
                )
            }
            //title
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.Bottom
                ) {
                    TittleTextMedium(
                        text = stringResource(id = R.string.newItem),
                        style = Typography.bodyLarge,
                        color = Food_appTheme.myColors.titleTextColor
                    )
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        TextWithIcon(
                            text = stringResource(id = R.string.viewAll),
                            color = Food_appTheme.myColors.mainColor,
                            iconColor = Food_appTheme.myColors.mainColor,
                            iconSize = 10,
                            style = Typography.labelSmall,
                            icon = R.drawable.right_icon,
                            modifier = Modifier.clickable {
                                openSearchScreen(
                                    "",
                                    4,
                                    -1,
                                    customer_id
                                )
                            }
                        )
                    }
                }
            }
            //new item
            item {
                var listProduct by remember {
                    mutableStateOf(listOf<ProductResponseItem>())
                }
                if (productViewModel.listAllProductByTimeLiveData.value != null) {

                    listProduct = productViewModel.listAllProductByTimeLiveData.value!!
                } else {

                    productViewModel.getAllProductsByTime(14) { result, listAllPNew ->
                        if (result) {
                            listProduct = listAllPNew!!
                        } else {
                        }
                    }
                }
                DisplayListNewItem(
                    openDetailScreen,
                    customer_id,
                    listProduct,
                    favoriteViewModel
                )
            }

            //title popular
            item {
                TittleTextMedium(
                    text = stringResource(id = R.string.popularItem),
                    style = Typography.bodyLarge,
                    color = Food_appTheme.myColors.titleTextColor,
                    modifier = Modifier.padding(start = 26.dp)
                )

            }
            //list popular

            items(listPopular.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier

                        .padding(horizontal = 26.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    for (item in rowItems) {
                        NormalItem(
                            sizeW = 154,
                            sizeH = 216,
                            image = R.drawable.food_04,
                            name = item.name,
                            ingredient = item.ingredient,
                            styleText = Typography.labelMedium,
                            styleIngredient = Typography.displayMedium,
                            corner = 15,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { openDetailScreen(item.id, customer_id) },
                            image_list = item.image,
                            rateStar = item.average_rate,
                            numberRate = item.review.size,
                            price = item.price,
                            discount = item.discount,
                            customer_id = customer_id,
                            product_id = item.id,
                            favoriteViewModel = favoriteViewModel
                        )
                    }
                }
            }
            //title all
            item {
                TittleTextMedium(
                    text = stringResource(id = R.string.ortherItems),
                    style = Typography.bodyLarge,
                    color = Food_appTheme.myColors.titleTextColor,
                    modifier = Modifier.padding(start = 26.dp)
                )

            }
            //list all

            items(listAll.chunked(1)) { rowItems ->
                Row(
                    modifier = Modifier

                        .padding(horizontal = 26.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    for (item in rowItems) {

                        NormalItem(
                            sizeW = 154,
                            sizeH = 216,
                            image = R.drawable.food_04,
                            name = item.name,
                            ingredient = item.ingredient,
                            styleText = Typography.labelMedium,
                            styleIngredient = Typography.displayMedium,
                            corner = 15,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { openDetailScreen(item.id, customer_id) },
                            image_list = item.image,
                            rateStar = item.average_rate,
                            numberRate = item.sale_number,
                            price = item.price,
                            discount = item.discount,
                            customer_id = customer_id,
                            product_id = item.id,
                            favoriteViewModel = favoriteViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayListRecommend(
    openDetailScreen: (Int, Int) -> Unit,
    customer_id: Int,
    listProduct: List<ProductResponseItem>,
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        contentPadding = PaddingValues(horizontal = 26.dp)
    ) {
        itemsIndexed(listProduct) { index, it ->
//            var listCategoryName = mutableListOf<String>()
            RecommendItem(
                sizeW = 300,
                sizeH = 130,
                image = R.drawable.food_04,
                corner = 15,
                image_list = it.image,
                modifier = Modifier.clickable {
                    openDetailScreen(it.id, customer_id)
                },
            )
        }
    }
}

@Composable
fun DisplayListTopSearch(
    openDetailScreen: (Int, Int) -> Unit,
    customer_id: Int,
    listProduct: List<ProductResponseItem>,
    favoriteViewModel: FavoriteViewModel,
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        contentPadding = PaddingValues(horizontal = 26.dp)
    ) {
        itemsIndexed(listProduct) { index, it ->
            var listCategoryName by remember {
                mutableStateOf(mutableListOf<String>())
            }
            if(listCategoryName.isEmpty()){
                it.categorie.forEach() { category ->
                    listCategoryName.add(category.name)
                }
            }
            ItemNew(
                image = R.drawable.food_03,
                name = it.name,
                style = Typography.bodyMedium,
                sizeW = 266,
                sizeH = 229,
                corner = 15,
                listCategory = listCategoryName,
                image_url = it.image,
                rateStar = it.average_rate,
                numbeRate = it.review.size,
                modifier = Modifier.clickable {
                    openDetailScreen(it.id, customer_id)
                },
                customer_id = customer_id,
                product_id = it.id,
                valueTag = -1,
                favoriteViewModel = favoriteViewModel
            )
        }
//        items(listProduct.size) {
//            var listCategoryName = mutableListOf<String>()
//            for (i in 0..3){
//                listCategoryName.add("category")
//            }
////            listProduct[it].categorie.forEach(){
////                category ->
////                listCategoryName.add(category.name)
////            }
//            ItemNew(
//                image = R.drawable.food_03,
//                name = listProduct[it].name,
//                style = Typography.bodyMedium,
//                sizeW = 266,
//                sizeH = 229,
//                corner = 15,
//                listCategory = listCategoryName,
//                image_url = listProduct[it].image[0].imgurl
//            )
//        }
    }
}

@Composable
fun DisplayListNewItem(
    openDetailScreen: (Int, Int) -> Unit,
    customer_id: Int,
    listProduct: List<ProductResponseItem>,
    favoriteViewModel: FavoriteViewModel,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        contentPadding = PaddingValues(horizontal = 26.dp)
    ) {
        itemsIndexed(listProduct) { index, it ->
            var listCategoryName by remember {
                mutableStateOf(mutableListOf<String>())
            }
            if(listCategoryName.isEmpty()){
                it.categorie.forEach() { category ->
                    listCategoryName.add(category.name)
                }
            }

            ItemNew(
                image = R.drawable.food_03,
                name = it.name,
                style = Typography.bodyMedium,
                sizeW = 266,
                sizeH = 229,
                corner = 15,
                listCategory = listCategoryName,
                image_url = it.image,
                rateStar = it.average_rate,
                numbeRate = it.review.size,
                modifier = Modifier.clickable {
                    openDetailScreen(it.id, customer_id)
                },
                customer_id = customer_id,
                product_id = it.id,
                valueTag = 0,
                favoriteViewModel = favoriteViewModel
            )
        }
//        items(listProduct.size) {
//            var listCategoryName = mutableListOf<String>()
//            for (i in 0..3){
//                listCategoryName.add("category")
//            }
////            listProduct[it].categorie.forEach(){
////                category ->
////                listCategoryName.add(category.name)
////            }
//            ItemNew(
//                image = R.drawable.food_03,
//                name = listProduct[it].name,
//                style = Typography.bodyMedium,
//                sizeW = 266,
//                sizeH = 229,
//                corner = 15,
//                listCategory = listCategoryName,
//                image_url = listProduct[it].image[0].imgurl
//            )
//        }
    }
}

@Composable
fun DisplayListCategory(
    customer_id: Int,
    openSearchScreen: (String, Int, Int, Int) -> Unit,
    listCategory: List<Category>,
) {

//    var listCategoryDisplay by remember {
//        mutableStateOf(mutableListOf<Category>())
//    }
    var start by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current

//    val categoryRepository = CategoryRepository(categoryApiService)
//    val categoryViewModel: CategoryViewModel = remember {
//        CategoryViewModel(categoryRepository = categoryRepository)
//    }

    var activeIndex by remember {
        mutableStateOf(0)
    }
    //**
//    val batchSize = 10
//
//    var lastIndexVisible by remember { mutableStateOf(batchSize - 1) }
//
//    fun loadMoreItems() {
//        val currentSize = listCategoryStore.size
//        val nextIndex = lastIndexVisible + batchSize
//        for (i in currentSize until currentSize + batchSize) {
//            listCategoryDisplay.add(listCategoryStore[i])
//            Log.d("abc", listCategoryStore[i].toString())
//
//        }
//        lastIndexVisible = nextIndex
//    }
//

    //**


    val scrollState = rememberLazyListState()
    LaunchedEffect(start) {
        scrollState.scrollToItem(0)
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 26.dp),
        state = scrollState,
    ) {
        itemsIndexed(listCategory) { index, it ->
            val active = activeIndex == index
            //**
//
//            if (index == lastIndexVisible && index < 1000) {
//                loadMoreItems()
//            }
            //**


            ItemCategory(
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Color.White,
                categoryName = it.name,
                imgCategory = R.drawable.rice_food,
                widthItem = 59,
                heightItem = 98,
                imgSize = 50,
                borderSize = 100,
                active = active,
                onClick = { activeIndex = index; openSearchScreen("", 0, it.id, customer_id) },
                image_link = it.image_url,
            )
        }
    }
}

//@Composable  // hiển thị list porpular items
//fun DisplayListPopular() {
//
//    LazyColumn(
//        verticalArrangement = Arrangement.spacedBy(25.dp),
//        contentPadding = PaddingValues(26.dp)
//    ) {
//        items(5) {
//            var listCategoryName = mutableListOf<String>()
//            for (i in 0..5) {
//                listCategoryName.add("Burger")
//            }
//            NormalItem(
//                sizeW = 154,
//                sizeH = 216,
//                image = R.drawable.food_04,
//                name = "Salmon Salad",
//                ingredient = "Baked salmon fish",
//                styleText = Typography.labelMedium,
//                styleIngredient = Typography.displayMedium,
//                corner = 15
//            )
//        }
//    }
//}

@Composable
fun ItemNew(
    modifier: Modifier = Modifier,
    image: Int,
    name: String,
    style: androidx.compose.ui.text.TextStyle,
    sizeW: Int,
    sizeH: Int,
    corner: Int,
    listCategory: List<String>,
    image_url: List<Image>,
    rateStar: Double,
    numbeRate: Int,
    customer_id: Int,
    product_id: Int,
    valueTag: Int,
    favoriteViewModel: FavoriteViewModel,
) {
    Box() {
        Column(
            modifier = modifier
                .width(sizeW.dp)
                .height(sizeH.dp)
                .shadow(2.dp, RoundedCornerShape(corner.dp))
                .clip(RoundedCornerShape(corner.dp))
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth(),
            ) {

                if (!image_url.isEmpty()) {
                    AsyncImage(
                        model = BASE_URL_API_PRODUCT + image_url[0].imgurl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight()
                    )
                } else {
                    Image(
                        painterResource(id = image),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 15.dp)
                ) {
                    val df = DecimalFormat("0.0")
                    val roundedNumber: String = df.format(rateStar)
                    RateComponent(
                        rateStar = "$roundedNumber",
                        numberRate = "$numbeRate",
                        colorRate = Food_appTheme.myColors.iconColor,
                        colorNumber = Food_appTheme.myColors.normalTextColor,
                        sizeW = 69,
                        sizeH = 28,
                        icon = R.drawable.star,
                        corner = 100,
                        backgroundColor = Color.White
                    )
                    FavoriteButton(
                        size = 28,
                        icon = R.drawable.heart,
                        activeColor = Food_appTheme.myColors.mainColor,
                        nonactiveColor = Color.White.copy(0.4f),
                        iconColor = Color.White,
                        iconSize = 14,
                        customer_id = customer_id,
                        product_id = product_id,
                        favoriteViewModel = favoriteViewModel
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.Start,
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = name, style = style, color = Food_appTheme.myColors.iconColor)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconWithText(
                        icon = R.drawable.derivery,
                        text = stringResource(id = R.string.freeDelivery),
                        iconColor = Food_appTheme.myColors.mainColor,
                        iconSize = 13,
                        style = Typography.displayMedium,
                        textColor = Food_appTheme.myColors.labelColor
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    IconWithText(
                        icon = R.drawable.time,
                        text = stringResource(id = R.string.mins),
                        iconColor = Food_appTheme.myColors.mainColor,
                        iconSize = 13,
                        style = Typography.displayMedium,
                        textColor = Food_appTheme.myColors.labelColor
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                        items(listCategory) {
                            LabelCategory(
                                text = it,
                                backgroundColor = Food_appTheme.myColors.backgroundCategoryColor,
                                corner = 5,
                                style = Typography.displayMedium,
                                color = Food_appTheme.myColors.normalTextColor,
                                padding = 5
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .offset((5).dp, (-sizeH * 0.2).dp)
        ) {
            TagItem(
                text = stringResource(id = R.string.newValue),
                sizeH = 15,
                sizeW = 40,
                color = Food_appTheme.myColors.mainColor,
                discount = valueTag,
                style = Typography.titleLarge,
                colorDiscount = Food_appTheme.myColors.starColor,
                isShowTag = true
            )
        }
    }
}


@Composable
fun IconWithText(
    icon: Int,
    text: String,
    iconColor: Color,
    iconSize: Int,
    style: androidx.compose.ui.text.TextStyle,
    textColor: Color,
    modifier: Modifier = Modifier,
    position: Boolean = false,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (position) Arrangement.spacedBy(10.dp) else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier.size(iconSize.dp)
        )
        Text(
            text = text,
            style = style,
            color = textColor,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun HeaderScreen(
    navigationFun: () -> Unit,
    locationDefault: String,
    image_url: String?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigateButton(
            size = 38,
            icon = R.drawable.setting_icon,
            iconColor = Food_appTheme.myColors.iconColor,
            buttonColor = Color.White,
            borderButton = 10,
            navigationFun = {
                navigationFun()
            }
        )

        DeliverText(style = Typography.labelMedium, locationText = locationDefault, maxWidth = 150)
        AvatarButton(
            size = 38,
            icon = R.drawable.avatar,
            buttonColor = Color.White,
            borderButton = 10,
            image_url = image_url
        )
    }
}

@Composable
fun DeliverText(
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle,
    locationText: String,
    maxWidth: Int,
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(maxWidth.dp)
    ) {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.deliverTo),
                style = style,
                color = Food_appTheme.myColors.labelColor
            )
            Icon(
                painterResource(id = R.drawable.down_icon),
                contentDescription = "",
                modifier = Modifier.size(15.dp),
                tint = Food_appTheme.myColors.labelColor
            )
        }
        Text(
            text = locationText,
            style = style,
            color = Food_appTheme.myColors.mainColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchAndSort(
    openSearchScreen: (String, Int, Int, Int) -> Unit,
    customer_id: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SearchFieldNormal(
            plateHolder = stringResource(id = R.string.findFood),
            activeColor = Food_appTheme.myColors.mainColor,
            nonactiveColor = Food_appTheme.myColors.unClickColor,
            plateHolderColor = Food_appTheme.myColors.plateHolderColor,
            style = Typography.bodyLarge,
            sizeHeight = 51,
            icon = R.drawable.search,
            modifier = Modifier.weight(1f),
            closeIcon = R.drawable.remove,
            isShowTrailingIcon = true,
            openSearchScreen = openSearchScreen,
            customer_id = customer_id
        )
        Spacer(modifier = Modifier.width(10.dp))
        var expanded by remember {
            mutableStateOf(false)
        }
        Column {
            NavigateButton(
                size = 51,
                icon = R.drawable.sort,
                iconColor = Food_appTheme.myColors.mainColor,
                buttonColor = Color.White,
                borderButton = 14,
                navigationFun = { expanded = !expanded }
            )
            Spacer(modifier = Modifier.height(5.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .shadow(3.dp, RoundedCornerShape(10.dp))

                    .background(Food_appTheme.myColors.menuColor)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                DropdownMenuItem(

                    text = {
                        IconWithText(
                            icon = R.drawable.money,
                            text = stringResource(id = R.string.sortBy),
                            iconColor = Food_appTheme.myColors.iconColor,
                            iconSize = 22,
                            style = Typography.bodyLarge,
                            textColor = Food_appTheme.myColors.iconColor,
                        )
                    },
                    onClick = { openSearchScreen("", 1, -1, customer_id) }
                )
                DropdownMenuItem(
                    text = {
                        IconWithText(
                            icon = R.drawable.sort_star,
                            text = stringResource(id = R.string.sortByRate),
                            iconColor = Food_appTheme.myColors.iconColor,
                            iconSize = 22,
                            style = Typography.bodyLarge,
                            textColor = Food_appTheme.myColors.iconColor,
                        )
                    },
                    onClick = { openSearchScreen("", 2, -1, customer_id) }
                )
                DropdownMenuItem(
                    text = {
                        IconWithText(
                            icon = R.drawable.chart,
                            text = stringResource(id = R.string.sortBySale),
                            iconColor = Food_appTheme.myColors.iconColor,
                            iconSize = 22,
                            style = Typography.bodyLarge,
                            textColor = Food_appTheme.myColors.iconColor,
                        )
                    },
                    onClick = { openSearchScreen("", 3, -1, customer_id) }
                )
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ItemCategory(
    activeColor: Color,
    nonactiveColor: Color,
    categoryName: String,
    imgCategory: Int,
    widthItem: Int,
    heightItem: Int,
    imgSize: Int,
    borderSize: Int,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    image_link: String = "",
) {
    val textColor = if (active) Color.White else Food_appTheme.myColors.iconColor
    val backgroundColor = if (active) activeColor else nonactiveColor

    Scaffold(
        modifier = modifier
            .width(widthItem.dp)
            .height(heightItem.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(borderSize.dp))
            .clip(RoundedCornerShape(borderSize.dp))
            .clickable(onClick = onClick),
        containerColor = backgroundColor,

//        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            if (image_link != "") {
                AsyncImage(
                    model = BASE_URL_API_CATEGORY + image_link,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(imgSize.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painterResource(id = imgCategory),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(imgSize.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = categoryName,
                style = Typography.labelSmall,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
