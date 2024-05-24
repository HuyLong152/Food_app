package com.example.food_app.view.Product

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.BASE_URL_API_PRODUCT
import com.example.food_app.R
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.orderApiService
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Orders.DeliveringOrderResponseItem
import com.example.food_app.data.model.Orders.OrdersResponseItem
import com.example.food_app.data.model.Product.Image
//import com.example.food_app.data.model.Orders.Image
//import com.example.food_app.data.model.Product
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.Account.formatPrice
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.ConfirmDialog
import com.example.food_app.view.NavigationBar
import com.example.food_app.view.TabLayoutComponent
import com.example.food_app.view.TittleTextMedium
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.OrderViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

//@Preview(showBackground = true, name = "orders screen")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    customer_id: Int,
    navigationBack: () -> Unit,
    reloadScreen:() ->Unit,
    openDetailScreen:(Int) ->Unit,
    openRateScreen:(Int) ->Unit,
    customerViewModel: CustomerViewModel
) {

    val orderRepository = OrderRepository(orderApiService)
    val orderViewModel: OrderViewModel = remember {
        OrderViewModel(orderRepository = orderRepository)
    }

    var listDelivering by remember {
        mutableStateOf(listOf<DeliveringOrderResponseItem>())
    }

        orderViewModel.getDeliveringProducts(
            customer_id
        ) { result, product ->
            if (result) {
                listDelivering = product!!
            }
        }

    var listRateProduct by remember {
        mutableStateOf(listOf<OrdersResponseItem>())
    }

    orderViewModel.getRatingProducts(
        customer_id
    ) { result, product ->
        if (result) {
            listRateProduct = product!!
        } else {
        }
    }

    var listHistory by remember {
        mutableStateOf(listOf<OrdersResponseItem>())
    }

    orderViewModel.getHistoryOrders(
        customer_id
    ) { result, product ->
        if (result) {
            listHistory = product!!
        } else {
        }
    }

//    val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
//    val customerViewModel: CustomerViewModel = remember {
//        CustomerViewModel(customerRepository)
//    }

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


    var showDialog by remember {
        mutableStateOf(false)
    }
    var order_id by remember {
        mutableStateOf(-1)
    }
    if (showDialog) {
        ConfirmDialog(
            onConfirm = {
                showDialog = false
                orderViewModel.deleteOrder(
                    order_id
                ) { result, message ->
                    if (result) {
                        reloadScreen()
                    }
                }
            },
            onCancel = { showDialog = false },
            title =  stringResource(id = R.string.confirmCancel),
            text =  stringResource(id = R.string.confirmContent),
            confirmText = stringResource(id = R.string.yes),
            dismissText = stringResource(id = R.string.no)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 22.dp, end = 22.dp, top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        NavigationBar(
            isShowAvt = true,
            text = stringResource(id = R.string.myOrders),
            style = Typography.bodyLarge,
            size = 38,
            avtImg = R.drawable.avatar,
            image_url = if(customerInfo.image_url != null){
                if(customerInfo.image_url.startsWith("https")){
                    customerInfo.image_url
                }else{
                    BASE_URL_API_AVATAR + customerInfo.image_url
                }
            }else{
                null
            },
            navigationBack = navigationBack
        )
        val tabItems = listOf( stringResource(id = R.string.upcoming),  stringResource(id = R.string.history))
        TabLayoutComponent(
            modifier = Modifier.fillMaxSize(),
            uncheckButtonColor = Food_appTheme.myColors.mainColor,
            tabItems = tabItems,
            firstComposable = {
                ItemOrderList(
                    listOrder = listDelivering,
                    showDialog = { it , it1 ->
                        showDialog = it
                        order_id = it1
                    },
                    openDetailScreen = openDetailScreen,
                    customer_id = customer_id
                    )
            }) {
            ItemHistoryList(
                listHistory = listHistory,
                listRateProduct = listRateProduct,
                openRateScreen = openRateScreen,
                openDetailScreen = openDetailScreen
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemHistoryList(
    modifier: Modifier = Modifier,
    listHistory: List<OrdersResponseItem>,
    listRateProduct: List<OrdersResponseItem>,
    openRateScreen: (Int) -> Unit,
    openDetailScreen: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        item{
            TittleTextMedium(
                text = stringResource(id = R.string.needRate),
                style = Typography.titleMedium,
                color = Food_appTheme.myColors.titleTextColor,
                modifier = Modifier.padding(start = 26.dp)
            )
        }
        itemsIndexed(listRateProduct) { index,item ->
            ItemHistory(
                image = R.drawable.food_04,
                imageSize = 65,
                imageCorner = 15,
                quantity = 3,
                size = 168,
                price = item.price.toDouble(),
                timeOrder = item.created_time,
                text = item.name,
                textColor = Food_appTheme.myColors.iconColor,
                labelColor = Food_appTheme.myColors.labelColor,
                idColor = Food_appTheme.myColors.mainColor,
                textStyle = Typography.titleSmall,
                labelStyle = Typography.labelSmall,
                mainStyle = Typography.headlineLarge,
                backgroundColor = Food_appTheme.myColors.backgroundItem,
                corner = 18,
                modifier = Modifier.padding(bottom = 15.dp),
                image_url = item.image,
                canRate = true,
                product_id = item.id,
                openRateScreen = openRateScreen,
                openDetailScreen = openDetailScreen
            )
        }
        item{
            TittleTextMedium(
                text =  stringResource(id = R.string.lastedOrder),
                style = Typography.titleMedium,
                color = Food_appTheme.myColors.titleTextColor,
                modifier = Modifier.padding(start = 26.dp)
            )
        }
        itemsIndexed(listHistory) { index,item ->
            ItemHistory(
                image = R.drawable.food_04,
                imageSize = 65,
                imageCorner = 15,
                quantity = 3,
                size = 168,
                price = item.price.toDouble(),
                timeOrder = item.created_time,
                text = item.name,
                textColor = Food_appTheme.myColors.textBlackColor,
                labelColor = Food_appTheme.myColors.labelColor,
                idColor = Food_appTheme.myColors.mainColor,
                textStyle = Typography.titleSmall,
                labelStyle = Typography.labelSmall,
                mainStyle = Typography.headlineLarge,
                backgroundColor = Food_appTheme.myColors.backgroundItem,
                corner = 18,
                modifier = Modifier.padding(bottom = 15.dp),
                image_url = item.image,
                product_id = item.id,
                openDetailScreen = openDetailScreen
            )
        }
    }
}

@Composable
fun ItemOrderList(
    modifier: Modifier = Modifier,
    listOrder: List<DeliveringOrderResponseItem>,
    showDialog : (Boolean, Int) -> Unit,
    openDetailScreen: (Int) -> Unit,
    customer_id: Int,
) {
    if(listOrder.isEmpty()){
        Image(painterResource(id = R.drawable.no_order), contentDescription = "", modifier = Modifier.fillMaxWidth(),contentScale = ContentScale.Crop)
    }else{
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            itemsIndexed(listOrder) { item,product ->
                ItemOder(
                    image = R.drawable.rice_food,
                    imageSize = 65,
                    imageCorner = 15,
                    quantity = product.quantity,
                    id = product.order_id,
                    size = 238,
                    backgroundColor = Food_appTheme.myColors.backgroundItem,
                    corner = 18,
                    textColor = Food_appTheme.myColors.iconColor,
                    labelColor = Food_appTheme.myColors.labelColor,
                    idColor = Food_appTheme.myColors.mainColor,
                    textStyle = Typography.titleSmall,
                    labelStyle = Typography.labelSmall,
                    mainStyle = Typography.headlineLarge,
                    text = product.name,
                    modifier = Modifier.padding(bottom = 15.dp),
                    img_url = product.image,
                    canCancel = product.status == "initialization",
                    order_id = product.order_id,
                    shoDialog = showDialog,
                    openDetailScreen = openDetailScreen,
                    product_id = product.id,
                    price = formatPrice(product.price.toDouble().toInt()),
                    adddress = product.address
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemHistory(
    image: Int,
    imageSize: Int,
    imageCorner: Int,
    quantity: Int,
    size: Int,
    price: Double,
    timeOrder: String,
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color,
    labelColor: Color,
    idColor: Color,
    textStyle: TextStyle,
    labelStyle: TextStyle,
    mainStyle: TextStyle,
    backgroundColor: Color,
    corner: Int,
    image_url :List<Image>,
    canRate : Boolean = false,
    product_id: Int,
    openRateScreen: (Int) -> Unit = {},
    openDetailScreen: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .height(size.dp)
            .clip(RoundedCornerShape(corner.dp))
            .background(backgroundColor)
            .padding(10.dp)
            .clickable { openDetailScreen(product_id) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageSize.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            if(!image_url.isEmpty()){
                AsyncImage(
                    model = BASE_URL_API_PRODUCT + image_url[0].imgurl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(imageSize.dp)
                        .clip(RoundedCornerShape(imageCorner.dp))
                )
            }else{
                Image(
                    painterResource(id = image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(imageSize.dp)
                        .clip(RoundedCornerShape(imageCorner.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM")

                    val dateTime = LocalDateTime.parse(timeOrder, inputFormatter)
                    val formattedDate = dateTime.format(outputFormatter)
                    Text(text = formattedDate, style = labelStyle.copy(labelColor))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(labelColor)
                    ) { }
                    Text(text = "$quantity " + stringResource(id = R.string.itemsValue), style = labelStyle.copy(labelColor))
                }
                Text(text = text, style = textStyle.copy(textColor), maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(idColor)
                    ) { }
                    Text(text = stringResource(id = R.string.orderDelivered), style = labelStyle.copy(idColor))

                }

            }
            Text(text = "${formatPrice(price.toDouble().toInt())} ₫", style = textStyle.copy(idColor))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if(!canRate) Arrangement.Center else Arrangement.spacedBy(10.dp),
        ) {
            if(canRate){
                ButtonMain(
                    width = 120,
                    height = 43,
                    text =  stringResource(id = R.string.rate),
                    color = Color.White,
                    style = Typography.titleMedium.copy(color = textColor),
                    clickButton = { openRateScreen(product_id)},
                    modifier = Modifier.shadow(5.dp, shape = CircleShape)
                )
            }
            ButtonMain(
                width = 180,
                height = 43,
                text =  stringResource(id = R.string.reOder),
                color = idColor,
                style = Typography.titleMedium.copy(color = Color.White),
                clickButton = {openDetailScreen(product_id)},
                modifier = Modifier.shadow(5.dp, shape = CircleShape)
            )
        }
    }

}

@Composable
fun ItemOder(
    image: Int,
    imageSize: Int,
    imageCorner: Int,
    quantity: Int,
    id: Int,
    size: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    corner: Int,
    textColor: Color,
    labelColor: Color,
    idColor: Color,
    textStyle: TextStyle,
    labelStyle: TextStyle,
    mainStyle: TextStyle,
    text: String,
    img_url : List<Image>,
    canCancel : Boolean = true,
    order_id : Int,
    shoDialog : (Boolean,Int) -> Unit,
    openDetailScreen: (Int) -> Unit,
    product_id :Int,
    price :String,
    adddress:String
) {
    Column(
        modifier = modifier
            .height(size.dp)
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(corner.dp))
            .clip(
                RoundedCornerShape(corner.dp)
            )
            .background(color = backgroundColor)
            .padding(10.dp)
            .clickable { openDetailScreen(product_id) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageSize.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
//            Column(
//                modifier = Modifier.height(imageSize.dp).fillMaxWidth(),
//                verticalArrangement = Arrangement.Top
//            ) {
            if(img_url.isEmpty()){
                Image(
                    painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(imageSize.dp)
                        .clip(RoundedCornerShape(imageCorner.dp)),
                    contentScale = ContentScale.Crop
                )
            }else{
                AsyncImage(
                    model = BASE_URL_API_PRODUCT + img_url[0].imgurl,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(imageSize.dp)
                        .clip(RoundedCornerShape(imageCorner.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "$quantity " + stringResource(id = R.string.item), style = labelStyle.copy(color = labelColor))
                Text(text = text, style = textStyle.copy(color = textColor), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "#$id", style = textStyle.copy(color = idColor))
                Text(text = "$price ₫", style = textStyle.copy(color = idColor))
            }
//            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text =  stringResource(id = R.string.Estimated), style = labelStyle.copy(color = labelColor))
            Text(text =  stringResource(id = R.string.now), style = labelStyle.copy(color = labelColor))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            val randomNumber = Random.nextInt(10, 31)
            Text(text = stringResource(id = R.string.thirtyMins), style = mainStyle)
            Text(text =  stringResource(id = R.string.foodOnWay), style = textStyle.copy(color = textColor))

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if(canCancel) Arrangement.spacedBy(10.dp) else Arrangement.Center,
        ) {
            if(canCancel){
                ButtonMain(
                    width = 115,
                    height = 43,
                    text =  stringResource(id = R.string.cancel),
                    color = Color.White,
                    style = Typography.titleMedium.copy(color = textColor),
                    clickButton = {shoDialog(true,order_id)},
                    modifier = Modifier.shadow(5.dp, shape = CircleShape)
                )
            }
            val restaurantLocation = stringResource(id = R.string.restaurantLocation)
            val address = adddress
            val context = LocalContext.current
            val packageManager = context.packageManager

            val googleMapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/"))
            val googleMapsResolveInfo = packageManager.resolveActivity(googleMapsIntent, PackageManager.MATCH_DEFAULT_ONLY)
            val isGoogleMapsInstalled = googleMapsResolveInfo != null
            val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$restaurantLocation&destination=$address")
            ButtonMain(
                width = 195,
                height = 43,
                text =  stringResource(id = R.string.trackOrder),
                color = idColor,
                style = Typography.titleMedium.copy(color = Color.White),
                clickButton = {
                    val mapIntent = if (isGoogleMapsInstalled) {
                        Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                    } else {
                        Intent(Intent.ACTION_VIEW, uri)
                    }
                    mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.shadow(5.dp, shape = CircleShape)
            )
        }
    }
}