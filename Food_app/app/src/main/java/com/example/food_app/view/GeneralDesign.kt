package com.example.food_app.view

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.VertexMode
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.BASE_URL_API_PRODUCT
import com.example.food_app.R
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.Repository.FavoriteRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.favoriteApiService
import com.example.food_app.data.model.Cart.CartResponseItem
import com.example.food_app.data.model.Product.Image
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.NormalTextColor
//import com.example.food_app.ui.theme.StarColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.Account.TextWithTwoColor
import com.example.food_app.view.Account.formatPrice
import com.example.food_app.view.Product.GlobalVariables
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.FavoriteViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Preview(showBackground = true, name = "Test")
@Composable
fun DisplayItem() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}


@Composable
fun FillterButton(
    modifier : Modifier = Modifier,
    onClick : () -> Unit = {},
    buttonColor : Color,
    style : TextStyle,
    text : String,
    textColor : Color
){
    Button(
        onClick = {onClick()},
        modifier = modifier,
        shape = RoundedCornerShape(1.dp),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(buttonColor)
    ) {
        Text(
            text = text,
            style = style,
            color = textColor,
        )
    }
}
@Composable
fun DiscountDialog(
    sizeH: Int,
    sizeW: Int,
    startColor : Color,
    endColor :Color,
    title: String,
    modifier: Modifier = Modifier,
    discountCode: String,
    discountPercent: Int,
    acceptText: String,
    icon:Int,
    iconSize : Int,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color.Red,
            Color(0xFFFF7F00),  // Orange
            Color.Yellow,
            Color(0xFF00FF00),  // Green
            Color(0xFF0000FF),  // Blue
            Color(0xFF4B0082),  // Indigo
            Color(0xFF9400D3)   // Violet
        )
    )

    AlertDialog(
        onDismissRequest = onCancel,
        title = { },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Text(text = title, style = Typography.headlineLarge.copy(Color.White))
                Text("#$discountCode", style = Typography.headlineMedium.copy(Color.White))
                Text(text = stringResource(id = R.string.usedCupon) + " " + "$discountPercent%" + " " + stringResource(
                    id = R.string.discount
                ),style = Typography.labelLarge.copy(Color.White))
            }
        },
        confirmButton = {
            OutlinedButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(5.dp,gradientBrush), // Sửa màu viền thành màu trắng
                onClick = onConfirm,
            ) {
                Text(text = acceptText,style = Typography.labelLarge.copy(Color.White))
            }
        },
        modifier = modifier
            .height(sizeH.dp)
            .width(sizeW.dp)
            .clip(RoundedCornerShape(16.dp)),
        containerColor = endColor,
        icon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onCancel() }, modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        Color(0xffFFE194)
                    )) {
                    Icon(painterResource(id = icon), contentDescription = "",modifier = Modifier.size(iconSize.dp))
                }
            }
        }
    )
}


@Composable
fun ItemNotification(
    text:String,
    idOrder : String,
    time : String,
    content :String,
    styleText : TextStyle,
    styleTime : TextStyle,
    styleId : TextStyle,
    textColor : Color,
    timeColor : Color,
    idColor : Color,
    modifier: Modifier = Modifier,
    sizeH : Int,
    corner:Int,
    backgroundColor : Color,
    image :Int,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(sizeH.dp)
            .shadow(3.dp, RoundedCornerShape(corner.dp))
            .background(backgroundColor)
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(painterResource(id = image), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
            .size(sizeH.dp)
            .clip(
                CircleShape
            ))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = text, style = styleText.copy(textColor))
            Text(text = content, style = styleTime.copy(textColor))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = time, style = styleTime.copy(timeColor))

                Text(text = "#$idOrder", style = styleId.copy(idColor))
            }
        }
    }
}
@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    text: String,
    sizeH: Int,
    sizeW: Int,
    color: Color,
    isShowTag: Boolean = false,
    discount: Int = 0,
    style: TextStyle,
    colorDiscount: Color
//    size :Float
) {
    if (isShowTag) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (discount > 0) {
                Box(
                    modifier = modifier
                        .height(sizeH.dp)
                        .width(sizeW.dp)
                        .background(colorDiscount),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$discount %", style = style.copy(Color.White))
                }
                Canvas(
                    modifier = modifier
                        .size(6.dp)
                        .rotate(90f)
                ) {
                    val path = Path().apply {
                        moveTo(0f, size.height) // Đỉnh dưới bên trái
                        lineTo(size.width, size.height) // Đỉnh dưới bên phải
                        lineTo(0f, 0f) // Đỉnh trên bên trái
                        close() // Kết thúc hình tam giác
                    }
                    drawPath(path, color = Color(0xffC03000))
                }
            } else if (discount == 0) {
                Box(
                    modifier = modifier
                        .height(sizeH.dp)
                        .width(sizeW.dp)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = text, style = style.copy(Color.White))
                }
                Canvas(
                    modifier = modifier
                        .size(6.dp)
                        .rotate(90f)
                ) {
                    val path = Path().apply {
                        moveTo(0f, size.height) // Đỉnh dưới bên trái
                        lineTo(size.width, size.height) // Đỉnh dưới bên phải
                        lineTo(0f, 0f) // Đỉnh trên bên trái
                        close() // Kết thúc hình tam giác
                    }
                    drawPath(path, color = Color(0xffC03000))
                }
            }else if (discount == -1) {
            Box(
                modifier = modifier
                    .height(sizeH.dp)
//                    .width(sizeW.dp)
                    .background(colorDiscount),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Top Search", style = style.copy(Color.White))
            }
            Canvas(
                modifier = modifier
                    .size(6.dp)
                    .rotate(90f)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height) // Đỉnh dưới bên trái
                    lineTo(size.width, size.height) // Đỉnh dưới bên phải
                    lineTo(0f, 0f) // Đỉnh trên bên trái
                    close() // Kết thúc hình tam giác
                }
                drawPath(path, color = Color(0xffC03000))
            }
        }
        }
    }
}

@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title :String,
    text : String,
    confirmText :String,
    dismissText :String
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = title) },
        text = { Text(text) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(dismissText)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayoutComponent(
    modifier: Modifier = Modifier,
    tabItems: List<String>,
    uncheckButtonColor: Color,
    firstComposable: @Composable () -> Unit,
    secondComposable: @Composable () -> Unit
) {
//    val tabItems = listOf("Promo code", "Apply")
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
    Column(
//        modifier = modifier.fillMaxSize()
        modifier = modifier
    ) {

        //ScrollableTabRow
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .border(BorderStroke(2.dp, Food_appTheme.myColors.labelColor), shape = CircleShape),
            indicator = { /* Bỏ dấu gạch ở dưới Tab */ },
            divider = {}
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        if (index == selectedTabIndex) {
                            ButtonMain(
                                width = 250,
                                height = 100,
                                text = item,
                                color = Food_appTheme.myColors.mainColor,
                                style = Typography.labelMedium,
                                clickButton = {},
                                isShowLoading = false
                            )
                        } else {
                            Text(
                                text = item,
                                style = Typography.labelMedium,
                                color = uncheckButtonColor
                            )
                        }
                    },
                )
            }
        }
        HorizontalPager(
            state = pagerState,
//            modifier = Modifier.fillMaxSize()
        ) { index ->
            if (index == 0) {
                Box(
//                    modifier = Modifier.fillMaxSize()
                ) {
//                    AllTotal(
//                        subtotalPrice = "$subtotalPrice",
//                        deliveryPrice = "0",
//                        taxFeesPrice = "0",
//                        totalPrice = "$subtotalPrice",
//                        numberItems = "${GlobalVariables.globalValue}",
//                        isShow = true
//                    )
                    firstComposable()
                }
            } else {
                Box(
//                    modifier = Modifier.fillMaxSize()
                ) {
//                AllTotal(
//                    subtotalPrice = "$subtotalPrice",
//                    deliveryPrice = "0",
//                    taxFeesPrice = "0",
//                    totalPrice = "$subtotalPrice",
//                    numberItems = "${GlobalVariables.globalValue}",
//                    isShow = false
//                )
                    secondComposable()
                }
            }

        }
    }
}

@Composable
fun NavigationBar(
    isShowAvt: Boolean,
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    size: Int,
    avtImg: Int,
    image_url: String?,
    navigationBack: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigateButton(
            size = size,
            icon = R.drawable.back_icon,
            iconColor = Food_appTheme.myColors.iconColor,
            buttonColor = Color.White,
            borderButton = 10,
            navigationFun = { navigationBack() }
        )
        Text(text = text, style = style.copy(color = Food_appTheme.myColors.textBlackColor))
        if (isShowAvt) {
            AvatarButton(
                size = size,
                icon = avtImg,
                buttonColor = Color.White,
                borderButton = 10,
                image_url = image_url
            )
        } else {
            Spacer(modifier = Modifier.size(size.dp))
        }
    }
}

@Composable
fun TextWithIcon(
    text: String,
    color: Color,
    iconColor: Color,
    iconSize: Int,
    modifier: Modifier = Modifier,
    style: TextStyle,
    icon: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(text = text, style = style, color = color)
        Icon(
            painterResource(id = icon),
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier
                .size(iconSize.dp)
                .padding(start = 5.dp)
        )
    }
}

@Composable
fun LabelCategory(
    text: String,
    backgroundColor: Color,
    corner: Int,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    color: Color,
    padding: Int
) {
    Box(
        modifier = modifier
//            .width(sizeW.dp)
//            .height(sizeH.dp)
            .clip(RoundedCornerShape(corner.dp))
            .background(backgroundColor)
            .padding(padding.dp)


    ) {
        Text(text = text, style = style, color = color)
    }
}

@Composable
fun QuantityComponent(
    color: Color,
    size: Int,
    iconMinus: Int,
    iconPlus: Int,
    style: TextStyle,
    maxNumber: Int,
    modifier: Modifier = Modifier,
    start_number: Int = 1,
    isInCart: Boolean = false,
    customer_id: Int = 0,
    product_id: Int = 0,
    isCheck: Boolean = false,
    changeCheck: () -> Unit = {},
    changeQuantity: (Int) -> Unit = {},
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val maxProduct = stringResource(id = R.string.maxProduct)
    val minProduct = stringResource(id = R.string.minProduct)
    if (isInCart) {
//        val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//        val cartViewModel: CartViewModel = remember {
//            CartViewModel(cartRepository = cartRepository)
//        }


        var quantityValue by remember {
            mutableStateOf(start_number)
        }
        var quantity by remember { mutableStateOf(quantityValue) }
        var job by remember { mutableStateOf<Job?>(null) }
        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
//                                Log.d("abc",isCheck.toString())

                                if (quantity > 1) {
//                                    if (isCheck) {
                                        changeCheck()
//                                        Log.d("abc",isCheck.toString())
//                                    }
                                    quantity--
                                    quantityValue--
                                    cartViewModel.updateQuantity(
                                        customer_id,
                                        product_id,
                                        quantity
                                    ) { result, product ->
                                        if (result) {
                                            cartViewModel.getAllCart(customer_id!!) { result, product ->
                                                if (result) {
                                                    cartViewModel.listAllCartLiveData.value = product!!
                                                }
                                            }
                                            // Handle success
                                        } else {
                                            // Handle failure
                                        }
                                    }

                                    job = coroutineScope.launch {
                                        while (true) {
                                            delay(300) // Adjust the delay as needed
                                            if (quantity > 1) {
                                                quantity--
                                                quantityValue--
                                                cartViewModel.updateQuantity(
                                                    customer_id,
                                                    product_id,
                                                    quantity
                                                ) { result, product ->
                                                    if (result) {
                                                        cartViewModel.getAllCart(customer_id!!) { result, product ->
                                                            if (result) {
                                                                cartViewModel.listAllCartLiveData.value = product!!
                                                            }
                                                        }
                                                        // Handle success
                                                    } else {
                                                        // Handle failure
                                                    }
                                                }
                                            } else {
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            minProduct,
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                tryAwaitRelease()
                                job?.cancel()
                            }
                        )
                    }
                    .border(BorderStroke(1.dp, color), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = iconMinus),
                    contentDescription = "",
                    tint = Food_appTheme.myColors.mainColor
                )
            }
            Text(
                text = "$quantityValue",
                style = style,
                color = Food_appTheme.myColors.textBlackColor
            )
            Box(
                modifier = Modifier
                    .size(size.dp)
//                    .clickable {
//                        if (quantityValue < maxNumber) {
//                            quantityValue++
//                            if (isCheck) {
//                                changeCheck()
//                            }
//                            cartViewModel.updateQuantity(
//                                customer_id,
//                                product_id,
//                                quantityValue
//                            ) { result, product ->
//                                if (result) {
//                                } else {
//                                }
//                            }
//
//                        } else {
//                            Toast
//                                .makeText(context, "Product quantity is Max", Toast.LENGTH_LONG)
//                                .show()
//                        }
//                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                if (quantity < maxNumber) {
//                                    if (isCheck) {
                                        changeCheck()
//                                    }
                                    quantity++
                                    quantityValue++
                                    cartViewModel.updateQuantity(
                                        customer_id,
                                        product_id,
                                        quantity
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

                                    job = coroutineScope.launch {
                                        while (true) {
                                            delay(300) // Adjust the delay as needed
                                            if (quantity < maxNumber) {
                                                quantity++
                                                quantityValue++
                                                cartViewModel.updateQuantity(
                                                    customer_id,
                                                    product_id,
                                                    quantity
                                                ) { result, product ->
                                                    if (result) {
                                                        cartViewModel.getAllCart(customer_id!!) { result, product ->
                                                            if (result) {
                                                                cartViewModel.listAllCartLiveData.value = product!!
                                                            }
                                                        }
                                                        // Handle success
                                                    } else {
                                                        // Handle failure
                                                    }
                                                }
                                            } else {
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            maxProduct,
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                tryAwaitRelease()
                                job?.cancel()
                            }
                        )
                    }
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(id = iconPlus), contentDescription = "", tint = Color.White)
            }
        }
    } else {
        var quantityValue by remember {
            mutableStateOf(start_number)
        }
//        var quantity by remember { mutableStateOf(quantityValue) }
        var job by remember { mutableStateOf<Job?>(null) }
        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(size.dp)
//                    .clickable {
//                        if (quantityValue > 1) {
//                            quantityValue--
//                            changeQuantity(quantityValue)
//                        } else {
//                            Toast
//                                .makeText(
//                                    context,
//                                    minProduct,
//                                    Toast.LENGTH_LONG
//                                )
//                                .show()
//                        }
//                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                if (quantityValue > 1) {
                                    quantityValue--
                                    changeQuantity(quantityValue)
                                    job = coroutineScope.launch {
                                        while (true) {
                                            delay(300) // Adjust the delay as needed
                                            if (quantityValue > 1) {
                                                quantityValue--
                                                changeQuantity(quantityValue)
                                            } else {
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            maxProduct,
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                tryAwaitRelease()
                                job?.cancel()
                            }
                        )
                    }
                    .border(BorderStroke(1.dp, color), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = iconMinus),
                    contentDescription = "",
                    tint = Food_appTheme.myColors.mainColor
                )
            }
            Text(
                text = "$quantityValue",
                style = style,
                color = Food_appTheme.myColors.textBlackColor
            )
            Box(
                modifier = Modifier
                    .size(size.dp)
//                    .clickable {
//                        Log.d("abc",maxNumber.toString())
//                        if (quantityValue < maxNumber) {
//                            quantityValue++
//                            changeQuantity(quantityValue)
//                        } else {
//                            Toast
//                                .makeText(
//                                    context,
//                                    "Product quantity is Max",
//                                    Toast.LENGTH_LONG
//                                )
//                                .show()
//                        }
//                    }
                    .pointerInput(maxNumber) {
                        detectTapGestures(
                            onPress = {
                                if (quantityValue < maxNumber) {
                                    quantityValue++
                                    changeQuantity(quantityValue)
                                    job = coroutineScope.launch {
                                        while (true) {
                                            delay(300) // Adjust the delay as needed
                                            if (quantityValue < maxNumber) {
                                                quantityValue++
                                                changeQuantity(quantityValue)
                                            } else {
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            maxProduct,
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                tryAwaitRelease()
                                job?.cancel()
                            }
                        )
                    }
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(id = iconPlus), contentDescription = "", tint = Color.White)
            }
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigateButton(
    size: Int,
    icon: Int,
    modifier: Modifier = Modifier,
    iconColor: Color,
    buttonColor: Color,
    borderButton: Int,
    navigationFun: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .shadow(5.dp, RoundedCornerShape(borderButton.dp))
            .background(buttonColor)
            .clickable { navigationFun() },
        contentAlignment = Alignment.Center,

        ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun WebviewScreen(
    url: String
) {
    var backEnable by remember { mutableStateOf(false) }
    var webView: WebView? by remember { mutableStateOf(null) } // Khai báo biến webView ở đây và gán giá trị null

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webViewFromUpdate ->
            webViewFromUpdate?.loadUrl(url) // Load URL từ giá trị uri được truyền vào
            webView = webViewFromUpdate // Gán webViewFromUpdate vào biến webView
        }
    )
    BackHandler(enabled = backEnable) {
        webView?.goBack()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AvatarButton(
    size: Int,
    icon: Int,
    modifier: Modifier = Modifier,
    buttonColor: Color,
    borderButton: Int,
    image_url: String?
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .shadow(5.dp, RoundedCornerShape(borderButton.dp))
            .background(buttonColor),
        contentAlignment = Alignment.Center,

        ) {
        if (image_url != null) {
            AsyncImage(
                model = image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop

            )
        } else {
            Image(
                painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun TextHeader(
    modifier: Modifier = Modifier,
    style: TextStyle,
    text: String
) {
    Text(text = text, style = style)
}


@Composable
fun OnOffButton(
    isActive : Boolean ,
    sizeW : Int,
    sizeH :Int,
    sizeCircle :Int,
    backgroundActive : Color,
    background : Color,
    circleActive : Color,
    circleNonactive : Color,
    modifier: Modifier = Modifier,
    changeValue : (Boolean) -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
//        var changeDefault by remember {
//            mutableStateOf(false)
//        }
//        changeDefault = isDefault == 1
        Box(
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .border(
                    if (!isActive) BorderStroke(2.dp, background) else BorderStroke(
                        2.dp,
                        backgroundActive
                    ), shape = CircleShape
                )
                .width(sizeW.dp)
                .height(sizeH.dp)
                .background(if (!isActive) Color.Transparent else backgroundActive)
                .padding(5.dp)
                .clickable { changeValue(isActive) }
        ) {
            Box(
                modifier = Modifier
                    .size(sizeCircle.dp)
                    .clip(CircleShape)
                    .align(if (!isActive) Alignment.CenterStart else Alignment.CenterEnd)
                    .background(if (!isActive) circleNonactive else circleActive)
            ){

            }
        }
    }
}
@Composable
fun ButtonMain(
    modifier: Modifier = Modifier,
    width: Int,
    height: Int,
    text: String,
    color: Color,
    style: TextStyle,
    clickButton: () -> Unit,
    isShowLoading: Boolean = false
) {
    Button(
        onClick = { clickButton() },
        modifier = modifier
            .width(width.dp)
            .height(height.dp),
        colors = ButtonDefaults.buttonColors(color),
        enabled = !isShowLoading
    ) {
        if (isShowLoading == true) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(text = text, style = style)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFieldNormal(
    plateHolder: String,
    activeColor: Color,
    nonactiveColor: Color,
    plateHolderColor: Color,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle,
    sizeHeight: Int,
    icon: Int,
    closeIcon: Int,
    isShowTrailingIcon: Boolean = false,
    openSearchScreen: (String, Int, Int,Int) -> Unit,
    customer_id: Int
//    text = phoneOrEmail,
//    onValueChange = { phoneOrEmail = it }
) {
    var text by remember {
        mutableStateOf("")
    }
    var isShowIcon by remember {
        mutableStateOf(false)
    }
    var rememberCheck by remember {    //tạo biến để lưu giá trị đổi mật khẩu
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        placeholder = {
            Text(
                text = plateHolder,
                color = plateHolderColor,
                style = Typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            unfocusedIndicatorColor = nonactiveColor,
            focusedIndicatorColor = activeColor
        ),
        modifier = modifier
            .height(sizeHeight.dp)
            .onFocusChanged { isShowIcon = !isShowIcon },
        shape = RoundedCornerShape(10.dp),
        textStyle = style,
        maxLines = 1,
        leadingIcon = {
            Icon(
                painterResource(id = icon),
                contentDescription = "",
                tint = Food_appTheme.myColors.labelColor,
                modifier = Modifier.clickable {
                    openSearchScreen(text, 0, -1,customer_id)
                }
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Go, // thêm một thuộc tính nữa vd whenDone = 0 , =1 , =2
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(        // bắt các sự kiện khi tương tác với nút trên bàn phím
            onGo = { // bên trên biến enter thành done , còn đây khi ấn done thì ẩn bàn phím
                openSearchScreen(text, 0, -1,customer_id)
            }
        ),
        trailingIcon = {
            if (!isShowIcon && isShowTrailingIcon) {
                Icon(
                    painterResource(id = closeIcon),
                    contentDescription = "",
                    tint = Food_appTheme.myColors.labelColor,
                    modifier = Modifier.clickable {
                        text = ""
                    }
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldNormal(
    plateHolder: String,
    textLabel: String,
    activeColor: Color,
    nonactiveColor: Color,
    plateHolderColor: Color,
    modifier: Modifier = Modifier,
    style: TextStyle,
    labelStyle: TextStyle,
    isPassField: Int,
    sizeHeight: Int,
    text: String,
    onValueChange: (String) -> Unit = {},
    isEnable: Boolean = true,
) {
    var rememberCheck by remember {    //tạo biến để lưu giá trị đổi mật khẩu
        mutableStateOf(false)
    }
    Column(
    ) {
        Text(
            text = textLabel,
            style = labelStyle,
            color = Food_appTheme.myColors.labelColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(text = plateHolder, color = plateHolderColor) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                unfocusedIndicatorColor = nonactiveColor,
                focusedIndicatorColor = activeColor
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(sizeHeight.dp),
            enabled = isEnable,
            shape = RoundedCornerShape(10.dp),
            textStyle = style,
            maxLines = 1,
            trailingIcon = {
                IconButton(onClick = { rememberCheck = !rememberCheck }) {
                    if (isPassField == 1) {
                        if (rememberCheck) {
                            Icon(
                                painterResource(id = R.drawable.no_eye),
                                contentDescription = "",
                                tint = Food_appTheme.myColors.labelColor
                            )
                        } else {
                            Icon(
                                painterResource(id = R.drawable.eye),
                                contentDescription = "",
                                tint = Food_appTheme.myColors.labelColor
                            )
                        }
                    }
                }
            },
            visualTransformation = if (!rememberCheck && isPassField == 1) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },    //kiểu nhập chữ hoặc mật khẩu (dựa vào giá trị của biến)
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // thêm một thuộc tính nữa vd whenDone = 0 , =1 , =2
                keyboardType = if (isPassField == 1) KeyboardType.Password else if (isPassField == 0) KeyboardType.Text else {
                    KeyboardType.Number
                },
            ),
        )
    }
}


@Composable
fun TittleTextMedium(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(text = text, style = style, color = color, modifier = modifier)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RateComponent(
    rateStar: String,
    numberRate: String,
    colorRate: Color,
    colorNumber: Color,
    modifier: Modifier = Modifier,
    sizeW: Int,
    sizeH: Int,
    icon: Int,
    corner: Int,
    backgroundColor: Color
) {
    Scaffold(
        modifier = modifier
            .width(sizeW.dp)
            .height(sizeH.dp)
            .shadow(5.dp, shape = RoundedCornerShape(corner.dp))
            .clip(RoundedCornerShape(corner.dp)),
        containerColor = backgroundColor
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = rateStar, style = Typography.labelSmall, color = colorRate)
            Icon(
                painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(10.dp),
                tint = Food_appTheme.myColors.starColor
            )
            Text(text = "($numberRate)", style = Typography.displaySmall, color = colorNumber)
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    size: Int,
    icon: Int,
    activeColor: Color,
    nonactiveColor: Color,
    iconColor: Color,
    iconSize: Int,
    customer_id: Int,
    product_id: Int,
    favoriteViewModel: FavoriteViewModel
) {
    var isActive by remember {
        mutableStateOf(false)
    }
    var tempValue by remember {
        mutableStateOf(false)
    }
//    val favoriteRepository = FavoriteRepository(favoriteApiService)
//    val favoriteViewModel: FavoriteViewModel = remember {
//        FavoriteViewModel(favoriteRepository = favoriteRepository)
//    }

//        favoriteViewModel.isExistProduct(customer_id, product_id) { result, category ->
//            if (result) {
//                tempValue = result
//            }
//        }


    LaunchedEffect(Unit){
        if(favoriteViewModel.isFavorite(product_id)){
            isActive = true
        }
    }
    var backgroundColor = if (isActive) activeColor else nonactiveColor
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .size(size.dp)
            .background(backgroundColor)
            .clickable {
                if (isActive) {
                    favoriteViewModel.deleteFavorite(customer_id, product_id) { result, message ->
                        if (result) {
                            favoriteViewModel.getAllFavorite(customer_id) { result, listFavorite ->
                                if (result) {
                                    favoriteViewModel.listAllFavoriteLiveData.value = listFavorite
                                } else {
                                }
                            }
                        } else {
                        }
                    }
                    isActive = !isActive
                } else {
                    favoriteViewModel.addFavorite(product_id, customer_id) { result, message ->
                        if (result) {
                            favoriteViewModel.getAllFavorite(customer_id) { result, listFavorite ->
                                if (result) {
                                    favoriteViewModel.listAllFavoriteLiveData.value = listFavorite
                                } else {
                                }
                            }
                        } else {
                        }
                    }
                    isActive = !isActive
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = "", tint = iconColor,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Composable
fun RecommendItem(
    sizeW: Int,
    sizeH: Int,
    image: Int,
//    name: String,
//    ingredient: String,
    modifier: Modifier = Modifier,
//    styleText: TextStyle,
//    styleIngredient: TextStyle,
    corner: Int,
    image_list: List<Image>,
//    rateStar: Double,
//    numberRate: Int,
//    price: String,
//    discount: Int,
//    customer_id: Int,
//    product_id: Int,
) {

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .width(sizeW.dp)
                .height(sizeH.dp)
                .shadow(3.dp, RoundedCornerShape(corner.dp))
                .clip(RoundedCornerShape(corner.dp))
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                if (!image_list.isEmpty()) {
                    AsyncImage(
                        model = BASE_URL_API_PRODUCT + image_list[0].imgurl,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(corner.dp))
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(id = image),
                        contentDescription = "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(corner.dp))
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    PriceComponent(
//                        sizeH = 28,
//                        text = price,
//                        corner = 100,
//                        style = Typography.labelSmall,
//                        color = Color.White,
//                        secondText = " VND"
//                    )
//                    FavoriteButton(
//                        size = 28,
//                        icon = R.drawable.heart,
//                        activeColor = Food_appTheme.myColors.mainColor,
//                        nonactiveColor = Color.White.copy(0.4f),
//                        iconColor = Color.White,
//                        iconSize = 14,
//                        customer_id = customer_id,
//                        product_id = product_id,
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth()
//                        .padding(start = 10.dp)
//                        .offset(0.dp, 15.dp),
//                    verticalAlignment = Alignment.Bottom,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    val df = DecimalFormat("0.0")
//                    val roundedNumber: String = df.format(rateStar)
//                    RateComponent(
//                        rateStar = "$roundedNumber",
//                        numberRate = "$numberRate",
//                        colorRate = Food_appTheme.myColors.iconColor,
//                        colorNumber = Food_appTheme.myColors.normalTextColor,
//                        sizeW = 69,
//                        sizeH = 28,
//                        icon = R.drawable.star,
//                        corner = 100,
//                        backgroundColor = Color.White
//                    )
//
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .weight(0.3f)
//                    .padding(start = 10.dp)
//            ) {
//                Spacer(modifier = Modifier.height(15.dp))
//                Text(text = name, style = styleText, color = Food_appTheme.myColors.iconColor)
//                Text(
//                    text = ingredient,
//                    style = styleIngredient,
//                    color = Food_appTheme.myColors.labelColor
//                )
//            }
//        }
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .offset((5).dp, (-sizeH * 0.3).dp)
//
//        ) {
//            TagItem(
//                text = "",
//                sizeH = 15,
//                sizeW = 40,
//                color = Food_appTheme.myColors.mainColor,
//                discount = discount,
//                style = Typography.titleLarge,
//                isShowTag = if(discount > 0 && discount <= 100) true else false,
//                colorDiscount = Food_appTheme.myColors.starColor,
//            )
            }
        }
    }
}

@Composable
fun NormalItem(
    sizeW: Int,
    sizeH: Int,
    image: Int,
    name: String,
    ingredient: String,
    modifier: Modifier = Modifier,
    styleText: TextStyle,
    styleIngredient: TextStyle,
    corner: Int,
    image_list: List<Image>,
    rateStar: Double,
    numberRate: Int,
    price: String,
    discount: Int,
    customer_id: Int,
    product_id: Int,
    favoriteViewModel: FavoriteViewModel
) {

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
//                .width(sizeW.dp)
                .height(sizeH.dp)
                .shadow(3.dp, RoundedCornerShape(corner.dp))
                .clip(RoundedCornerShape(corner.dp))
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                if (!image_list.isEmpty()) {
                    AsyncImage(
                        model = BASE_URL_API_PRODUCT + image_list[0].imgurl,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(corner.dp))
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(id = image),
                        contentDescription = "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(corner.dp))
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PriceComponent(
                        sizeH = 28,
                        text = formatPrice(price.toDouble().toInt()),
                        corner = 100,
                        style = Typography.labelSmall,
                        color = Color.White,
                        secondText = " ₫"
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
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                        .offset(0.dp, 15.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val df = DecimalFormat("0.0")
                    val roundedNumber: String = df.format(rateStar)
                    RateComponent(
                        rateStar = "$roundedNumber",
                        numberRate = "$numberRate",
                        colorRate = Food_appTheme.myColors.iconColor,
                        colorNumber = Food_appTheme.myColors.normalTextColor,
                        sizeW = 69,
                        sizeH = 28,
                        icon = R.drawable.star,
                        corner = 100,
                        backgroundColor = Color.White
                    )

                }
            }
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(start = 10.dp)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = name, style = styleText, color = Food_appTheme.myColors.iconColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    text = ingredient,
                    style = styleIngredient,
                    color = Food_appTheme.myColors.labelColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((5).dp, (-sizeH * 0.3).dp)

        ) {
            TagItem(
                text = "",
                sizeH = 15,
                sizeW = 40,
                color = Food_appTheme.myColors.mainColor,
                discount = discount,
                style = Typography.titleLarge,
                isShowTag = if(discount > 0 && discount <= 100) true else false,
                colorDiscount = Food_appTheme.myColors.starColor,
            )
        }
    }
}

@Composable
fun PriceComponent(
    sizeH: Int,
    text: String,
    modifier: Modifier = Modifier,
    corner: Int,
    style: TextStyle,
    color: Color,
    secondText: String,
    isNumberImg : Boolean = false
) {
    Box(
        modifier = modifier
            .height(sizeH.dp)
            .clip(RoundedCornerShape(corner.dp))
            .background(color)
            .padding(7.dp)
            .widthIn(30.dp, 100.dp),
        contentAlignment = Alignment.Center
    ) {
        TextWithTwoColor(
            firstText = text,
            secondText = secondText,
            firstColor = Food_appTheme.myColors.iconColor,
            secondColor = Food_appTheme.myColors.mainColor,
            size = 12,
            openSignUpScreen = {}
        )
    }
}

@Composable
fun ButtonLoginOther(
    modifier: Modifier = Modifier,
    width: Int, height: Int,
    color: Color,
    astyle: androidx.compose.ui.text.TextStyle,
    image: Int,
    imgSize: Int,
    text: String,
    circleSize: Int,
    circleColor: Color,
    textColor: Color,
    isImage: Boolean,
    openScreen: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(28.dp))
            .clickable { openScreen() },
        color = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(circleSize.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                if (isImage) {
                    Image(
                        painterResource(id = image),
                        contentDescription = "A logo of brand",
                        modifier = Modifier
                            .size(imgSize.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        painterResource(id = image),
                        contentDescription = "A logo of brand",
                        modifier = Modifier
                            .size(imgSize.dp)
                            .clip(CircleShape),
                        tint = color
                    )
                }

            }
            Text(
                text,
                style = astyle,
                color = textColor
            )
        }
    }
}