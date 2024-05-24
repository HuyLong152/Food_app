package com.example.food_app.view.Product

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_PRODUCT
import com.example.food_app.R
import com.example.food_app.data.Notification.FoodNotificationService
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.Repository.DiscountRepository
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.discountApiService
import com.example.food_app.data.api.RetrofitClient.orderApiService
import com.example.food_app.data.model.Cart.CartResponseItem
import com.example.food_app.data.model.Discount.DiscountResponse
import com.example.food_app.data.model.Location.LocationResponseItem
import com.example.food_app.data.model.Product.Image
import com.example.food_app.data.model.Product.ProductResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.Account.formatPrice
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigationBar
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.DiscountViewModel
import com.example.food_app.viewmodel.LocationViewModel
import com.example.food_app.viewmodel.OrderViewModel
import com.example.food_app.viewmodel.ProductViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PaymentScreen(
    product_id: List<Int>,
    navigationBack: () -> Unit,
    customer_id: Int,
    totalPrice: Float,
    discountCode: String,
    openHomeScreen: () -> Unit,
    quantity: Int,
    openLocationScreen: () -> Unit,
    openBankingScreen: (List<Int>, Int, String, String,Int,Float, Int,Int) -> Unit,
    locationViewModel: LocationViewModel,
    cartViewModel: CartViewModel
) {
    BackHandler {
        GlobalVariables.clearData()
        navigationBack()
    }
    var noteFinal by remember {
        mutableStateOf("")
    }
    var totalFinal by remember {
        mutableStateOf(0.0f)
    }
    var isActive by remember {
        mutableStateOf(0)
    }
//    Log.d(
//        "abc",
//        product_id.toString() + customer_id + totalPrice + discountCode + quantity.toString()
//    )
    val context = LocalContext.current

    val orderRepository = OrderRepository(orderApiService)
    val orderViewModel: OrderViewModel = remember {
        OrderViewModel(orderRepository = orderRepository)
    }
    val discountRepository = DiscountRepository(discountApiService)
    val discountViewModel: DiscountViewModel = remember {
        DiscountViewModel(discountRepository = discountRepository)
    }
    var location_id by remember {
        mutableStateOf(-1)
    }
    var showThanksOrder by remember {
        mutableStateOf(false)
    }
    if (showThanksOrder) {
        CountDownDialog(
            onClose = { showThanksOrder = false; openHomeScreen() },
            icon = R.drawable.check,
            size = 70
        )
    }
    var showDialog by remember {
        mutableStateOf(false)
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
    if (discountCode != "no value") {
        discountViewModel.isExistDiscount(discountCode) { result, discount ->
            if (result) {
                discountValue = discount!!
            }
        }
    }

    if (discountValue.discount_percent != 0) {
        totalFinal = totalPrice - (totalPrice * discountValue.discount_percent / 100)
    } else {
        totalFinal = totalPrice
    }
//    val cash = stringResource(id = R.string.cash)
//    val bank = stringResource(id = R.string.bank)
    val error = stringResource(id = R.string.error)
    val posNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val foodNotificationService = FoodNotificationService(context)

//    val notifiTitle1 = stringResource(id = R.string.notifiTitle1)
//    val notifiTitle2 = stringResource(id = R.string.notifiTitle2)
//    val notifiTitle3 = stringResource(id = R.string.notifiTitle3)
//    val notifiContent1 = stringResource(id = R.string.notifiContent1)
//    val notifiContent2 = stringResource(id = R.string.notifiContent2)
//    val notifiContent3 = stringResource(id = R.string.notifiContent3)
//    val notifiContent4 = stringResource(id = R.string.notifiContent4)


    val randomIndex = (0..2).random()
    val notifiTitle = when (randomIndex) {
        0 -> stringResource(id = R.string.notifiTitle1)
        1 -> stringResource(id = R.string.notifiTitle2)
        else -> stringResource(id = R.string.notifiTitle3)
    }

    val notifiContent = when (randomIndex) {
        0 -> stringResource(id = R.string.notifiContent1)
        1 -> stringResource(id = R.string.notifiContent2)
        2 -> stringResource(id = R.string.notifiContent3)
        else -> stringResource(id = R.string.notifiContent4)
    }

    val randomIcon = when ((0..2).random()) {
        0 -> R.drawable.happy
        1 -> R.drawable.happy1
        else -> R.drawable.happy2
    }

    LaunchedEffect(key1 = true) {
        if (!posNotificationPermission.status.isGranted) {
            posNotificationPermission.launchPermissionRequest()
        }
    }

    if (showDialog) {

        ConfirmationDialog(
            onConfirm = {
                if (isActive == 1) {
                    if (quantity == -1) {
                        openBankingScreen(
                            product_id,
                            customer_id,
                            if(noteFinal == "") "no value" else noteFinal,
                            if (isActive == 0) "cash" else "banking",
                            if (discountValue.id != 0) discountValue.id else -1,
                            totalFinal,
                            -1,
                            location_id
                        )
                    } else {
                        openBankingScreen(
                            product_id,
                            customer_id,
                            if(noteFinal == "") "no value" else noteFinal,
                            if (isActive == 0) "cash" else "banking",
                            if (discountValue.id != 0) discountValue.id else -1,
                            totalFinal,
                            quantity,
                            location_id
                        )
                    }
                } else {
                    if (quantity == -1) {
                        orderViewModel.createOrders(
                            product_id,
                            customer_id,
                            noteFinal,
                            if (isActive == 0) "cash" else "banking",
                            if (discountValue.id != 0) discountValue.id else -1,
                            location_id
                        ) { result, category ->
                            if (result) {
                                GlobalVariables.clearData()
                                showThanksOrder = true
                                foodNotificationService.showExpandableNotification(
                                    randomIcon,
                                    notifiTitle,
                                    notifiContent
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    error,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        orderViewModel.createAOrders(
                            product_id[0],
                            quantity,
                            customer_id,
                            noteFinal,
                            if (isActive == 0) "cash" else "banking",
                            if (discountValue.id != 0) discountValue.id else -1,
                            location_id
                        ) { result, category ->
                            if (result) {
                                GlobalVariables.clearData()
                                showThanksOrder = true
                                foodNotificationService.showExpandableNotification(
                                    randomIcon,
                                    notifiTitle,
                                    notifiContent
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    error,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }

                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

//    val locationRepository = LocationReponsitory(RetrofitClient.locationApiService)
//    val locationViewModel: LocationViewModel = remember {
//        LocationViewModel(locationRepository = locationRepository)
//    }
//    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//    val cartViewModel: CartViewModel = remember {
//        CartViewModel(cartRepository = cartRepository)
//    }


    var locationDefault by remember {
        mutableStateOf(
            LocationResponseItem(
                address = "",
                created_time = "",
                customer_id = 0,
                id = 0,
                is_default = 0,
                name = "",
                phone_number = "",
                updated_time = ""
            )
        )
    }
    var listLocation by remember {
        mutableStateOf(listOf<LocationResponseItem>())
    }
    if (locationViewModel.listAllLocationLiveData.value == null) {
        locationViewModel.getAllAddress(customer_id) { result, location ->
            if (result) {
                listLocation = location!!
            }
        }
    } else {
        listLocation = locationViewModel.listAllLocationLiveData.value!!
    }
    var textName by remember {
        mutableStateOf("")
    }
    var textPhone by remember {
        mutableStateOf("")
    }
    var textAddress by remember {
        mutableStateOf("")
    }
    locationViewModel.getDefaultAddress(customer_id) { result, location ->
        if (result) {
            locationDefault = location!!
            if(location_id == -1){
                location_id = locationDefault.id
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 27.dp, horizontal = 26.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        NavigationBar(
            isShowAvt = false,
            text = stringResource(id = R.string.payment),
            style = Typography.bodyLarge,
            size = 38,
            avtImg = R.drawable.avatar,
            image_url = "",
            navigationBack = {
                navigationBack()
                GlobalVariables.clearData()
            }
        )
        var isShowAllAddress by remember {
            mutableStateOf(false)
        }
        LocationTransfer(
            styleName = Typography.bodyLarge,
            styleAddress = Typography.headlineSmall,
            colorName = Food_appTheme.myColors.textBlackColor,
            colorAddress = Food_appTheme.myColors.labelColor,
            iconColor = Food_appTheme.myColors.iconDarkColor,
            icon = R.drawable.location,
            iconSize = 35,
            textName = if(textName != "") textName else locationDefault.name,
            textPhone = if(textPhone != "") textPhone else locationDefault.phone_number,
            textAddress = if(textAddress != "") textAddress else locationDefault.address,
            modifier = Modifier.clickable {
                isShowAllAddress = !isShowAllAddress
            }
        )
        if(isShowAllAddress){
            LazyColumn(){
                itemsIndexed(listLocation){ index , location ->
                    LocationTransfer(
                        styleName = Typography.bodyLarge,
                        styleAddress = Typography.headlineSmall,
                        colorName = Food_appTheme.myColors.textBlackColor,
                        colorAddress = Food_appTheme.myColors.labelColor,
                        iconColor = Food_appTheme.myColors.iconDarkColor,
                        icon = R.drawable.location,
                        iconSize = 35,
                        textName = location.name,
                        textPhone = location.phone_number,
                        textAddress = location.address,
                        modifier = Modifier.clickable {
                            location_id = location.id
                            textName = location.name
                            textPhone = location.phone_number
                            textAddress = location.address
                            isShowAllAddress = !isShowAllAddress
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        var listProduct by remember {
            mutableStateOf(listOf<CartResponseItem>())
        }
        var aProduct by remember {
            mutableStateOf(
                ProductResponseItem(
                    average_rate = 0.0,
                    calo = 0,
                    categorie = emptyList(),
                    created_time = "",
                    description = "",
                    discount = 0,
                    id = product_id[0],
                    image = emptyList(),
                    ingredient = "",
                    name = "",
                    price = "0.0",
                    quantity = 0,
                    review = emptyList(),
                    sale_number = 0,
                    updated_time = ""
                )
            )
        }
        if (quantity == -1) {
            cartViewModel.getProductsCart(customer_id, product_id) { result, product ->
                if (result) {
                    listProduct = product!!
                }
            }
        } else {
            val productRepository = ProductRepository(RetrofitClient.productApiService)
            val productViewModel: ProductViewModel = remember {
                ProductViewModel(productRepository = productRepository)
            }
            productViewModel.getProductById(product_id[0]) { result, product ->
                if (result) {
                    aProduct = product!!
                } else {
                }
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {

            items(if (quantity == -1) listProduct.size else 1) {
                if (quantity == -1) {
                    PaymentItem(
                        image = R.drawable.no_image,
                        sizeW = 122,
                        sizeH = 84,
                        text = listProduct[it].name,
                        icon = R.drawable.sort_star,
                        numberRate = listProduct[it].average_rate,
                        numberOrder = listProduct[it].cart_quantity,
                        style = Typography.titleMedium,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        iconColor = Food_appTheme.myColors.starColor,
                        rateColor = Food_appTheme.myColors.mainColor,
                        iconSize = 15,
                        corner = 10,
                        image_url = listProduct[it].image
                    )
                } else {
                    PaymentItem(
                        image = R.drawable.no_image,
                        sizeW = 122,
                        sizeH = 84,
                        text = aProduct.name,
                        icon = R.drawable.sort_star,
                        numberRate = aProduct.average_rate,
                        numberOrder = quantity,
                        style = Typography.titleMedium,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        iconColor = Food_appTheme.myColors.starColor,
                        rateColor = Food_appTheme.myColors.mainColor,
                        iconSize = 15,
                        corner = 10,
                        image_url = aProduct.image
                    )
                }
            }
        }

        TextFieldNormal(
            plateHolder = stringResource(id = R.string.noteForChef),
            textLabel = "",
            activeColor = Food_appTheme.myColors.mainColor,
            nonactiveColor = Food_appTheme.myColors.unClickColor,
            plateHolderColor = Food_appTheme.myColors.plateHolderColor,
            style = Typography.bodyLarge,
            labelStyle = Typography.labelMedium,
            isPassField = 0,
            sizeHeight = 80,
            text = noteFinal,
            onValueChange = { noteFinal = it }
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                PaymentMethod(
                    sizeW = 85,
                    sizeH = 72,
                    image = R.drawable.cash,
                    corner = 15,
                    text = stringResource(id = R.string.cash),
                    backgroundColor = Food_appTheme.myColors.bankColor,
                    activeColor = Food_appTheme.myColors.mainColor,
                    textColor = Food_appTheme.myColors.textBlackColor,
                    style = Typography.headlineSmall,
                    imageSize = 30,
                    isActive = isActive == 0,
                    onClick = { isActive = 0 }
                )
            }
            item {
                PaymentMethod(
                    sizeW = 95,
                    sizeH = 72,
                    image = R.drawable.bank,
                    corner = 15,
                    text = stringResource(id = R.string.bank),
                    backgroundColor = Food_appTheme.myColors.bankColor,
                    activeColor = Food_appTheme.myColors.mainColor,
                    textColor = Food_appTheme.myColors.textBlackColor,
                    style = Typography.headlineSmall,
                    imageSize = 30,
                    isActive = isActive == 1,
                    onClick = {
                        isActive = 1
                    }
                )
            }
        }
        Text(
            text = stringResource(id = R.string.totalPayment) + " : ${formatPrice(totalFinal.toDouble().toInt())} ₫",
            style = Typography.headlineMedium.copy(Food_appTheme.myColors.mainColor)
        )
        ButtonMain(
            width = 1500,
            height = 53,
            text = stringResource(id = R.string.payAndConfirm),
            color = Food_appTheme.myColors.mainColor,
            style = Typography.labelMedium,
            clickButton = {
                showDialog = true
            }
        )
    }
}

@Composable
fun CountDownDialog(
    onClose: () -> Unit,
    icon: Int,
    size: Int,
) {
    var timeLeft by remember { mutableStateOf(3) } // Thời gian còn lại tính bằng giây
    var isCounting by remember { mutableStateOf(true) } // Biến state để kiểm soát việc đếm ngược

    // Coroutine đếm ngược
    LaunchedEffect(Unit) {
        repeat(3) { // Lặp lại 3 lần
            delay(1000) // Chờ 1 giây
            if (!isCounting) return@LaunchedEffect // Nếu biến isCounting là false, dừng coroutine
            timeLeft-- // Giảm thời gian còn lại đi 1 giây
        }
        onClose() // Sau khi đếm ngược kết thúc, đóng dialog
    }

    // Hiển thị dialog
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = stringResource(id = R.string.thankOrder),
                color = Food_appTheme.myColors.textBlackColor
            )
        },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = icon),
                    contentDescription = "",
                    tint = Food_appTheme.myColors.mainColor,
                    modifier = Modifier.size(size.dp)
                )
                Text(
                    text = stringResource(id = R.string.goHome) + " $timeLeft s",
                    color = Food_appTheme.myColors.textBlackColor
                )
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.confirmation),
                style = Typography.titleMedium.copy(Food_appTheme.myColors.textBlackColor),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirmaPlace),
                style = Typography.headlineSmall.copy(Food_appTheme.myColors.textBlackColor)
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
//                colors = ButtonDefaults.buttonColors(Food_appTheme.myColors.mainColor)
            ) {
                Text(text = stringResource(id = R.string.no), color = Color.White)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.yes), color = Color.White)
            }
        },
        modifier = Modifier.width(300.dp)
    )
}

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    image: Int,
    sizeW: Int,
    sizeH: Int,
    text: String,
    icon: Int,
    numberRate: Double,
    numberOrder: Int,
    style: TextStyle,
    textColor: Color,
    iconColor: Color,
    rateColor: Color,
    iconSize: Int,
    corner: Int,
    image_url: List<Image>,
) {
    Row(
        modifier = modifier
            .height(sizeH.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (!image_url.isEmpty()) {
            AsyncImage(
                model = BASE_URL_API_PRODUCT + image_url[0].imgurl,
                contentDescription = "",
                modifier = Modifier
                    .width(sizeW.dp)
                    .height(sizeH.dp)
                    .clip(RoundedCornerShape(corner.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painterResource(id = image),
                contentDescription = "",
                modifier = Modifier
                    .width(sizeW.dp)
                    .height(sizeH.dp)
                    .clip(RoundedCornerShape(corner.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = text, style = style.copy(textColor), modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis, maxLines = 3)
                Text(text = "x$numberOrder", style = style.copy(rateColor))
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = icon),
                    contentDescription = "",
                    tint = iconColor,
                    modifier = Modifier.size(iconSize.dp)
                )
                Text(text = numberRate.toString(), style = style.copy(textColor))
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Divider()
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun PaymentMethod(
    modifier: Modifier = Modifier,
    sizeW: Int,
    sizeH: Int,
    image: Int,
    corner: Int,
    text: String,
    backgroundColor: Color,
    activeColor: Color,
    textColor: Color,
    style: TextStyle,
    imageSize: Int,
    isActive: Boolean = false,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(sizeW.dp)
                .height(sizeH.dp)
                .clip(RoundedCornerShape(corner.dp))
                .background(backgroundColor)
                .border(
                    width = 2.dp, // Độ dày của viền
                    color = if (isActive) activeColor else Color.Transparent, // Màu của viền
                    shape = RoundedCornerShape(corner.dp) // Hình dạng của viền
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(id = image),
                contentDescription = "",
                modifier = Modifier.size(imageSize.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = text, style = style.copy(color = textColor))
    }
}

@Composable
fun LocationTransfer(
    styleName: TextStyle,
    styleAddress: TextStyle,
    colorName: Color,
    colorAddress: Color,
    iconColor: Color,
    icon: Int,
    iconSize: Int,
    modifier: Modifier = Modifier,
    textName: String,
    textPhone: String,
    textAddress: String,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(iconSize.dp),
                tint = iconColor
            )
            Text(text = "$textName (+84)$textPhone", style = styleName.copy(colorName))
//            Text(text = "", style = styleName.copy(colorName))
        }
        Text(text = textAddress, style = styleAddress.copy(colorAddress))
    }
}