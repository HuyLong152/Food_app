package com.example.food_app.view.Product

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.Repository.DiscountRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Cart.CartResponseItem
import com.example.food_app.data.model.Product.Image
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.NormalTextColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.TextBlackColor
//import com.example.food_app.ui.theme.TitleSmallColor
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.Account.formatPrice
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigationBar
import com.example.food_app.view.QuantityComponent
import com.example.food_app.view.TabLayoutComponent
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.DiscountViewModel


class GlobalVariables {
    companion object {
        var globalValue by mutableStateOf(0)
            public set
        var lisProductCheck by mutableStateOf(mutableListOf<Int>())
            public set

        fun clearData() {
            globalValue = 0
            lisProductCheck.clear()
        }
    }
}

@Composable
fun CartScreen(
    customer_id: Int,
    navigationBack: () -> Unit,
    openDetailScreen: (Int, Int) -> Unit,
    openPaymentScreen: (List<Int>, Double, String) -> Unit,
    cartViewModel: CartViewModel
) {
    BackHandler {
        GlobalVariables.clearData()
        navigationBack()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 37.dp, horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBar(
                isShowAvt = false,
                text = stringResource(id = R.string.cart),
                style = Typography.bodyLarge,
                size = 38,
                avtImg = R.drawable.avatar,
                image_url = "",
                navigationBack = {
                    navigationBack()
                    GlobalVariables.clearData()
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ListCartProduct(
                modifier = Modifier.weight(0.6f),
                customer_id = customer_id,
                openDetailScreen = openDetailScreen,
                R.drawable.empty_cart,
                cartViewModel = cartViewModel
            )
            PaymentComponent(
                modifier = Modifier.weight(0.4f),
                customer_id,
                openPaymentScreen = openPaymentScreen,
                cartViewModel = cartViewModel
            )
        }
    }
}


//@Composable
//fun ConfirmationDialog(
//    onConfirm: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    Dialog(
//        onDismissRequest = onDismiss
//    ) {
//        Surface(
//            modifier = Modifier
//                .width(300.dp)
//                .padding(16.dp),
//            color = Color.White
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("You definitely place an order")
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(onClick = onConfirm) {
//                        Text("Yes")
//                    }
//                    Button(onClick = onDismiss) {
//                        Text("No")
//                    }
//                }
//            }
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTotal(
    subtotalPrice: String = "0",
    deliveryPrice: String,
    taxFeesPrice: String,
    totalPrice: String,
    modifier: Modifier = Modifier,
    numberItems: String,
    isShow: Boolean,
    openPaymentScreen: (List<Int>, Double, String) -> Unit,
    customer_id: Int,
) {

    val context = LocalContext.current


    var text by remember {
        mutableStateOf("")
    }

    val discountRepository = DiscountRepository(RetrofitClient.discountApiService)
    val discountViewModel: DiscountViewModel = remember {
        DiscountViewModel(discountRepository = discountRepository)
    }
    val discountDoesNotExist = stringResource(id = R.string.discountUsed)
    val discountUsed = stringResource(id = R.string.hasBeenUsed)
    val discountLimit = stringResource(id = R.string.discountLimit)
    val discountOutDate = stringResource(id = R.string.discountOutDate)
    val discountError = stringResource(id = R.string.error)

//    var discountValue by remember {
//        mutableStateOf(
//            DiscountResponse(
//                create_time = "",
//                discount_code = "",
//                discount_percent = 0,
//                id = 0,
//                update_time = ""
//            )
//        )
//    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            if (isShow) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enterCodeHere),
                            color = Food_appTheme.myColors.plateHolderColor
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        unfocusedIndicatorColor = Food_appTheme.myColors.unClickColor,
                        focusedIndicatorColor = Food_appTheme.myColors.mainColor
                    ),
                    modifier = modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = Typography.bodyLarge,
                    maxLines = 1,
                )
            }
        }
        item {
            SubTotal(
                text = stringResource(id = R.string.subtotal),
                style = Typography.titleSmall.copy(color = Food_appTheme.myColors.textBlackColor),
                price = subtotalPrice,
                numberStyle = Typography.bodyLarge
            )
        }
        item {
            SubTotal(
                text = stringResource(id = R.string.taxAndFax),
                style = Typography.titleSmall.copy(color = Food_appTheme.myColors.textBlackColor),
                price = taxFeesPrice,
                numberStyle = Typography.bodyLarge
            )
        }
        item {
            SubTotal(
                text = stringResource(id = R.string.delivery),
                style = Typography.titleSmall.copy(color = Food_appTheme.myColors.textBlackColor),
                price = deliveryPrice,
                numberStyle = Typography.bodyLarge
            )
        }
        item {
            SubTotal(
                text = stringResource(id = R.string.total) + numberItems + " " + stringResource(id = R.string.items),
                style = Typography.titleSmall.copy(color = Food_appTheme.myColors.mainColor),
                price = totalPrice,
                numberStyle = Typography.bodyLarge.copy(color = Food_appTheme.myColors.mainColor)
            )
        }
        item {
            val chooseItem = stringResource(id = R.string.chooseItem)

            ButtonMain(
                width = 2500,
                height = 57,
                text = stringResource(id = R.string.checkOut),
                color = Food_appTheme.myColors.mainColor,
                style = Typography.titleMedium.copy(color = Color.White),
                clickButton = {
                    if (text != "") {
                        discountViewModel.isExistDiscount(text) { result, discount ->
                            if (result) {
                                discountViewModel.checkUserUsed(
                                    text,
                                    customer_id
                                ) { result, isUsed, discountValue ->
                                    if (result) {
                                        if (isUsed == 1) {
                                            val toast = Toast.makeText(
                                                context,
                                                discountUsed,
                                                Toast.LENGTH_SHORT
                                            )
                                            toast.setGravity(
                                                Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                                0,
                                                0
                                            )
                                            toast.show()
                                        } else if (isUsed == 3) {
                                            val toast = Toast.makeText(
                                                context,
                                                discountLimit,
                                                Toast.LENGTH_SHORT
                                            )
                                            toast.setGravity(
                                                Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                                0,
                                                0
                                            )
                                            toast.show()
                                        } else if (isUsed == 4) {
                                            val toast = Toast.makeText(
                                                context,
                                                discountOutDate,
                                                Toast.LENGTH_SHORT
                                            )
                                            toast.setGravity(
                                                Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                                0,
                                                0
                                            )
                                            toast.show()
                                        } else if (isUsed == 5) {
                                            val toast = Toast.makeText(
                                                context,
                                                discountError,
                                                Toast.LENGTH_SHORT
                                            )
                                            toast.setGravity(
                                                Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                                0,
                                                0
                                            )
                                            toast.show()
                                        }
                                        else {
                                            if (GlobalVariables.lisProductCheck.isEmpty()) {
                                                val toast = Toast.makeText(
                                                    context,
                                                    chooseItem,
                                                    Toast.LENGTH_SHORT
                                                )
                                                toast.setGravity(
                                                    Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                                    0,
                                                    0
                                                )
                                                toast.show()
                                            } else {
                                                openPaymentScreen(
                                                    GlobalVariables.lisProductCheck,
                                                    subtotalPrice.toDouble(),
                                                    if (text == "") "no value" else text
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                val toast = Toast.makeText(
                                    context,
                                    discountDoesNotExist,
                                    Toast.LENGTH_SHORT
                                )
                                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                                toast.show()
                            }
                        }
                    } else {
                        if (GlobalVariables.lisProductCheck.isEmpty()) {
                            Toast.makeText(context, chooseItem, Toast.LENGTH_SHORT).show()
                        } else {
                            openPaymentScreen(
                                GlobalVariables.lisProductCheck,
                                subtotalPrice.toDouble(),
                                if (text == "") "no value" else text,
                            )
                        }
                    }
                }
            )
        }
    }

}

@Composable
fun SubTotal(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    price: String,
    numberStyle: TextStyle,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = style)
        Text(text = formatPrice(price.toDouble().toInt())+ " ₫", style = numberStyle.copy(Food_appTheme.myColors.textBlackColor))
    }
    Divider()
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentComponent(
    modifier: Modifier = Modifier,
    customer_id: Int,
    openPaymentScreen: (List<Int>, Double, String) -> Unit,
    cartViewModel: CartViewModel
) {
    var subtotalPrice by remember {
        mutableStateOf(0.0f)
    }
//    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//    val cartViewModel: CartViewModel = remember {
//        CartViewModel(cartRepository = cartRepository)
//    }


    var listProductIds by remember {
        mutableStateOf(mutableListOf<Int>())
    }

    listProductIds = GlobalVariables.lisProductCheck
    if (!listProductIds.isEmpty()) {
        var listProduct = mutableListOf<CartResponseItem>()
//        Log.d("abc", "day")
        for (i in 0..<listProductIds.size) {
            cartViewModel.getAProductCart(customer_id, listProductIds[i]) { result, product ->
                if (result) {
                    subtotalPrice = 0.0f
                    listProduct.add(product!!)
                    for (i in 0..<listProduct.size) {
                        subtotalPrice += (listProduct[i].price.toFloat() - (listProduct[i].price.toFloat() * ((listProduct[i].discount.toFloat() / 100)))) * listProduct[i].cart_quantity;
                    }
//                    Log.d("abc", listProduct.size.toString())
                } else {
                }
            }
        }

//        Log.d("abc", "sub1"+subtotalPrice.toString())

    } else {
        subtotalPrice = 0.0f;
//        Log.d("abc", "sub2" + subtotalPrice.toString())
    }


//    if (GlobalVariables.lisProductCheck.size == 0) {
//        subtotalPrice = 0.0f;
//    }

    for (i in 0..<GlobalVariables.globalValue) {
//        subtotalPrice += (GlobalVariables.lisProductCheck[i].price.toFloat() - (GlobalVariables.lisProductCheck[i].price.toFloat() * (GlobalVariables.lisProductCheck[i].discount/100))) * GlobalVariables.lisProductCheck[i].cart_quantity;
    }
//    if (!listProduct.isEmpty()) {
//        subtotalPrice = 0.0f
//        Log.d("abc","day1" + listProduct.size.toString())
//        for (i in 0..<listProduct.size) {
//            subtotalPrice += (listProduct[i].price.toFloat() - (listProduct[i].price.toFloat() * (listProduct[i].discount / 100))) * listProduct[i].cart_quantity;
//        }
//    }


    val tabItems =
        listOf(stringResource(id = R.string.promoCode), stringResource(id = R.string.apply))
    TabLayoutComponent(
        tabItems = tabItems,
        uncheckButtonColor = Food_appTheme.myColors.labelColor,
        firstComposable = {
            AllTotal(
                subtotalPrice = "$subtotalPrice",
                deliveryPrice = "0",
                taxFeesPrice = "0",
                totalPrice = "$subtotalPrice",
                numberItems = "${GlobalVariables.globalValue}",
                isShow = true,
                openPaymentScreen = openPaymentScreen,
                customer_id = customer_id
            )
        }) {
        AllTotal(
            subtotalPrice = "$subtotalPrice",
            deliveryPrice = "0",
            taxFeesPrice = "0",
            totalPrice = "$subtotalPrice",
            numberItems = "${GlobalVariables.globalValue}",
            isShow = false,
            openPaymentScreen = openPaymentScreen,
            customer_id = customer_id
        )
    }


}

@Composable
fun ListCartProduct(
    modifier: Modifier = Modifier,
    customer_id: Int,
    openDetailScreen: (Int, Int) -> Unit,
    imageNoItem: Int,
    cartViewModel: CartViewModel
) {
//    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//    val cartViewModel: CartViewModel = remember {
//        CartViewModel(cartRepository = cartRepository)
//    }
    var listProduct by remember {
        mutableStateOf(listOf<CartResponseItem>())
    }
    if (cartViewModel.listAllCartLiveData.value == null) {
        cartViewModel.getAllCart(customer_id!!) { result, product ->
            if (result) {
                listProduct = product!!
            }
        }
    } else {
        listProduct = cartViewModel.listAllCartLiveData.value!!
    }
    if (listProduct.isEmpty()) {
        Image(
            painterResource(id = imageNoItem),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            // truyền danh sách trong giỏ hàng vào đây , lisAbc là tạo tạm bằng các category
            itemsIndexed(listProduct) { index, it ->
                var finalPrice =
                    (it.price.toFloat() - (it.price.toFloat() * (it.discount.toFloat() / 100)))
                ItemCart(
                    image = R.drawable.no_image,
                    imgSize = 100,
                    corner = 15,
                    destroyIcon = R.drawable.remove,
                    textName = it.name,
                    textIngredient = it.ingredient,
                    textPrice = finalPrice.toString(),
                    nameColor = Food_appTheme.myColors.textBlackColor,
                    ingredientColor = Food_appTheme.myColors.labelColor,
                    priceColor = Food_appTheme.myColors.mainColor,
                    iconColor = Food_appTheme.myColors.mainColor,
                    iconSize = 17,
                    styleName = Typography.bodyLarge,
                    styleInge = Typography.bodySmall,
                    stylePrice = Typography.titleSmall,
                    iconCheck = R.drawable.uncheck,
                    iconUncheck = R.drawable.check,
                    iconCheckSize = 20,
                    onItemSelected = { isCheck, listProductCheck ->
                        if (isCheck) {
                            listProductCheck.add(it.id) // thêm id của sản phẩm vào đây
                        } else
                            listProductCheck.remove(it.id)
                    },
                    modifier = Modifier.padding(bottom = 15.dp),
                    list_img = it.image,
                    max_quantity = it.quantity,
                    start_quantity = it.cart_quantity,
                    customer_id = customer_id,
                    product_id = it.id,
                    openDetailScreen = openDetailScreen,
                    cartViewModel = cartViewModel
                )
            }
        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ItemCart(
    image: Int,
    imgSize: Int,
    corner: Int,
    modifier: Modifier = Modifier,
    destroyIcon: Int,
    textName: String,
    textIngredient: String,
    textPrice: String,
    nameColor: Color,
    ingredientColor: Color,
    priceColor: Color,
    iconColor: Color,
    iconSize: Int,
    styleName: TextStyle,
    styleInge: TextStyle,
    stylePrice: TextStyle,
    iconCheck: Int,
    iconUncheck: Int,
    iconCheckSize: Int,
    onItemSelected: (Boolean, MutableList<Int>) -> Unit,
    list_img: List<Image>,
    max_quantity: Int,
    start_quantity: Int,
    customer_id: Int,
    product_id: Int,
    openDetailScreen: (Int, Int) -> Unit,
    cartViewModel: CartViewModel
) {
//    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//    val cartViewModel: CartViewModel = remember {
//        CartViewModel(cartRepository = cartRepository)
//    }

    var isShow by remember {
        mutableStateOf(true)
    }
    if (isShow) {
        Row(
            modifier = modifier
                .height(imgSize.dp)
                .fillMaxWidth()
                .clickable { openDetailScreen(product_id, customer_id) },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var isCheck by remember {
                mutableStateOf(false)
            }
            var checkColor = if (isCheck) priceColor else ingredientColor
            var checkIcon = if (isCheck) iconCheck else iconUncheck

            Column(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(onClick = {
                    onItemSelected(!isCheck, GlobalVariables.lisProductCheck);
                    isCheck = !isCheck
                    GlobalVariables.globalValue = GlobalVariables.lisProductCheck.size;

                }) {
                    Icon(
                        painterResource(id = checkIcon),
                        contentDescription = "",
                        tint = checkColor,
                        modifier = Modifier.size(iconCheckSize.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.weight(0.3f)
            ) {
                if (list_img.isEmpty()) {
                    Image(
                        painterResource(id = image),
                        contentDescription = "",
                        modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(corner.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = BASE_URL_API_PRODUCT + list_img[0].imgurl,
                        contentDescription = "",
                        modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(corner.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = textName,
                        style = styleName,
                        color = nameColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(0.8f)
                    )
                    if (!isCheck) {
                        IconButton(

                            onClick = {
                                isShow = !isShow
                                cartViewModel.deleteCart(
                                    customer_id,
                                    product_id
                                ) { result, product ->
                                    if (result) {
                                        cartViewModel.getAllCart(customer_id!!) { result, product ->
                                            if (result) {
                                                cartViewModel.listAllCartLiveData.value = product!!
                                            }
                                        }
                                    } else {
                                    }
                                }
                            }, modifier = Modifier
                                .size(iconSize.dp)
                                .weight(0.2f)
                        ) {
                            Icon(
                                painterResource(id = destroyIcon),
                                contentDescription = "",
                                tint = iconColor,

                                )
                        }
                    }
                }
                Text(
                    text = textIngredient,
                    style = styleInge,
                    color = ingredientColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${formatPrice(textPrice.toDouble().toInt())} ₫",
                        style = stylePrice,
                        color = priceColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(0.5f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    QuantityComponent(
                        color = Food_appTheme.myColors.mainColor,
                        size = 22,
                        iconMinus = R.drawable.minus,
                        iconPlus = R.drawable.plus,
                        style = Typography.titleSmall,
                        maxNumber = max_quantity,
                        modifier = Modifier.weight(0.5f),
                        start_number = start_quantity,
                        isInCart = true,
                        customer_id = customer_id,
                        product_id = product_id,
                        isCheck = isCheck,
                        changeCheck = {
                            isCheck = false
                            Log.d("abc","1" + isCheck.toString())
                            GlobalVariables.lisProductCheck.remove(product_id)
                            GlobalVariables.globalValue = GlobalVariables.lisProductCheck.size
                        },
                        cartViewModel = cartViewModel
//
                    )
                }
            }
        }
    }
}