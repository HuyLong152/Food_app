package com.example.food_app.view.Product

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.R
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.Repository.ReviewReponsitory
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.orderApiService
import com.example.food_app.data.api.RetrofitClient.reviewApiService
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Orders.OrdersResponseItem
import com.example.food_app.data.model.Review.ReviewResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.ConfirmDialog
import com.example.food_app.view.Main.IconWithText
import com.example.food_app.view.NavigationBar
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.OrderViewModel
import com.example.food_app.viewmodel.ReviewViewModel

//@Preview(showBackground = true, name = "Add Address")
@Composable
fun WatchReviewScreen(
    navigationBack: () -> Unit,
    customer_id: Int,
    product_id: Int,
    reloadScreen : (Int,Int) -> Unit,
    customerViewModel: CustomerViewModel
    //
) {

    val reviewRepository = ReviewReponsitory(reviewApiService)
    val reviewViewModel: ReviewViewModel = remember {
        ReviewViewModel(reviewRepository = reviewRepository)
    }


    val orderRepository = OrderRepository(orderApiService)
    val orderViewModel: OrderViewModel = remember {
        OrderViewModel(orderRepository = orderRepository)
    }


    var listProduct by remember {
        mutableStateOf(listOf<OrdersResponseItem>())
    }

    orderViewModel.getRatingProducts(
        customer_id
    ) { result, product ->
        if (result) {
            listProduct = product!!
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
    var listReview by remember {
        mutableStateOf(listOf<ReviewResponseItem>())
    }
    if (reviewViewModel.listAllReviewByIdLiveData.value == null) {
        reviewViewModel.getReviewById(product_id) { result, review ->
            if (result) {
                listReview = review!!
            } else {
            }
        }
    } else {
        listReview = reviewViewModel.listAllReviewByIdLiveData.value!!
    }


    var canWrite by remember {
        mutableStateOf(-1)
    }
    if (!listProduct.isEmpty()) {
        for (i in listProduct.indices) {
            if (listProduct[i].id == product_id) {
                canWrite = listProduct[i].order_id
                break
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 27.dp, horizontal = 36.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationBar(
            isShowAvt = false,
            text =  stringResource(id = R.string.reviews),
            style = Typography.bodyLarge,
            size = 38,
            avtImg = R.drawable.avatar,
            image_url = null,
            navigationBack = navigationBack
        )
        WriteReview(
            image = R.drawable.avatar,
            imageSize = 30,
            plateHolder =  stringResource(id = R.string.writeReviews),
            plateHolderColor = Food_appTheme.myColors.plateHolderColor,
            nonactiveColor = Food_appTheme.myColors.unClickColor,
            activeColor = Food_appTheme.myColors.mainColor,
            style = Typography.bodyLarge,
            sizeHeight = 87,
            canWrite = canWrite,
            image_url = customerInfo.image_url,
            icon = R.drawable.send,
            iconSize = 15,
            iconColor = Food_appTheme.myColors.mainColor,
            activeStar = R.drawable.star,
            nonActiveStar = R.drawable.sort_star,
            activeStarColor = Food_appTheme.myColors.starColor,
            nonActiveStarColor = Food_appTheme.myColors.labelColor,
            sizeStar = 30,
            customer_id = customer_id,
            product_id = product_id,
            reloadScreen = reloadScreen
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {
            itemsIndexed(listReview) { index, review ->
                AReview(
                    avatar = R.drawable.avatar,
                    name = review.full_name,
                    date = review.created_time,
                    rate = review.rate.toDouble(),
                    text = if(review.content == null) "" else review.content,
                    backgroundRate = Food_appTheme.myColors.starColor,
                    imageSize = 48,
                    icon = R.drawable.more,
                    iconSize = 12,
                    iconColor = Food_appTheme.myColors.titleSmallColor,
                    textColor = Food_appTheme.myColors.textReviewColor,
                    nameColor = Food_appTheme.myColors.textBlackColor,
                    sizeH = 139,
                    rateSize = 18,
                    textStyle = Typography.headlineSmall,
                    nameStyle = Typography.headlineSmall,
                    dateStyle = Typography.bodySmall,
                    rateStyle = Typography.displaySmall,
                    avt_image = review.image_url,
                    isAuthor = (customer_id == review.customer_id),
                    sendIcon = R.drawable.send,
                    sendIconSize = 15,
                    customer_id = customer_id,
                    product_id = product_id,
                    reloadScreen = reloadScreen,
                    review_id = review.id
                )
            }
        }
    }
}

@Composable
fun AReview(
    avatar: Int,
    name: String,
    date: String,
    rate: Double,
    text: String,
    backgroundRate: Color,
    modifier: Modifier = Modifier,
    imageSize: Int,
    icon: Int,
    iconSize: Int,
    iconColor: Color,
    textColor: Color,
    nameColor: Color,
    sizeH: Int,
    rateSize: Int,
    textStyle: TextStyle,
    nameStyle: TextStyle,
    dateStyle: TextStyle,
    rateStyle: TextStyle,
    avt_image: String,
    isAuthor: Boolean,
    sendIcon : Int,
    sendIconSize :Int,
    customer_id: Int,
    product_id :Int,
    reloadScreen : (Int,Int) ->Unit,
    review_id : Int
) {
    var isShowUpdate by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    val reviewRepository = ReviewReponsitory(reviewApiService)
    val reviewViewModel: ReviewViewModel = remember {
        ReviewViewModel(reviewRepository = reviewRepository)
    }
    if (showDialog) {
        ConfirmDialog(
            onConfirm = { if(isAuthor){
                reviewViewModel.deleteReview(review_id) { result, review ->
                    if (result) {
                        reloadScreen(customer_id,product_id)
                    }
                }
            } },
            onCancel = {  },
            title = stringResource(id = R.string.confirmToDelete),
            text =  stringResource(id = R.string.confirmDeleteReview),
            confirmText = stringResource(id = R.string.delete) ,
            dismissText = stringResource(id = R.string.cancel)
        )
    }
    Column(
        modifier = modifier
//            .height(sizeH.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Box(
                modifier = Modifier.size(imageSize.dp),
            ) {
                if (avt_image != "") {
                    AsyncImage(
                        model = if(avt_image.startsWith("https")){
                            avt_image
                        }else{
                            BASE_URL_API_AVATAR + avt_image
                        },
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(id = avatar),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(0.dp, (5.dp))
                        .size(rateSize.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .align(Alignment.BottomEnd)
                        .background(backgroundRate),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$rate", style = rateStyle.copy(color = Color.White))
                }

            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(imageSize.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                if (isAuthor) {
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = nameColor,
                                fontFamily = FontFamily(Font(R.font.sofiapro_light)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            )
                        ) {
                            append(name)
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Food_appTheme.myColors.mainColor,
                                fontFamily = FontFamily(Font(R.font.sofiapro_light)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            ),
                        ) {
                            append(stringResource(id = R.string.you))
                        }
                    }, maxLines = 2)
                } else {
                    Text(text = name, style = nameStyle.copy(color = nameColor), maxLines = 2)
                }
                Text(text = date, style = dateStyle.copy(color = iconColor))
            }
            var expanded by remember {
                mutableStateOf(false)
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(iconSize.dp),
                    tint = iconColor
                )
                Spacer(modifier = Modifier.height(5.dp))
                if(isAuthor){
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .shadow(3.dp, RoundedCornerShape(10.dp))

                            .background(Food_appTheme.myColors.menuColor)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text(text =  stringResource(id = R.string.update), color = Food_appTheme.myColors.iconColor)},
                            onClick = {
                                isShowUpdate = !isShowUpdate
                                expanded = !expanded
                            }
                        )
                        DropdownMenuItem(
                            text = {Text(text = stringResource(id = R.string.delete),color = Food_appTheme.myColors.iconColor)},
                            onClick = {
                                showDialog = !showDialog
                                expanded = !expanded
                            }
                        )
                    }
                }else{
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .shadow(3.dp, RoundedCornerShape(10.dp))

                            .background(Food_appTheme.myColors.menuColor)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.report), color = Food_appTheme.myColors.textBlackColor)},
                            onClick = {
                                expanded = !expanded
                            }
                        )
                    }
                }
            }
        }
        if(isShowUpdate && isAuthor){
            var newContent by remember {
                mutableStateOf(text)
            }
            Row {
                OutlinedTextField(
                    value = newContent,
                    onValueChange = {newContent = it},
                    trailingIcon = {
                        IconButton(onClick = {
                            reviewViewModel.updateContent(review_id,if (newContent.isEmpty()) null else newContent) { result, review ->
                                if (result) {
                                    reloadScreen(customer_id,product_id)
                                }
                            }
                        }) {
                            Icon(painterResource(id = sendIcon), contentDescription = "", tint = iconColor, modifier = Modifier.size(sendIconSize.dp))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    maxLines = 5
                    )
                TextButton(onClick = { isShowUpdate = !isShowUpdate }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        }else{
            Text(text = text, style = textStyle.copy(color = textColor, lineHeight = 24.sp))
        }

    }
}

@Composable
fun DeleteReviewDialog(
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit
) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(text = stringResource(id = R.string.confirmToDelete)) },
            text = { Text(stringResource(id = R.string.confirmDeleteReview)) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmDelete()
                    }
                ) {
                    Text(stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReview(
    modifier: Modifier = Modifier,
    image: Int,
    imageSize: Int,
    plateHolder: String,
    plateHolderColor: Color,
    nonactiveColor: Color,
    activeColor: Color,
    style: TextStyle,
    sizeHeight: Int,
    canWrite: Int,
    image_url :String,
    icon : Int,
    iconSize : Int,
    iconColor : Color,
    activeStar :Int,
    nonActiveStar :Int,
    activeStarColor :Color,
    nonActiveStarColor :Color,
    sizeStar : Int,
    customer_id :Int,
    product_id :Int,
    reloadScreen: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val reviewRepository = ReviewReponsitory(reviewApiService)
    val reviewViewModel: ReviewViewModel = remember {
        ReviewViewModel(reviewRepository = reviewRepository)
    }
    if (canWrite != -1) {
        var rating by remember { mutableStateOf(0) }

        Column {
            Row {
                repeat(5) { index ->
                    IconButton(
                        onClick = {
                            rating = index + 1
                        }
                    ) {
                        Icon(
                            painterResource(id = if (index < rating) activeStar else nonActiveStar),
                            contentDescription = "",
                            modifier = Modifier.size(sizeStar.dp),
                            tint = if (index < rating) activeStarColor  else nonActiveStarColor
                        )
                    }
                }
            }
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                var text by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = { Text(text = plateHolder, color = plateHolderColor) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        unfocusedIndicatorColor = nonactiveColor,
                        focusedIndicatorColor = activeColor
                    ),
                    modifier = modifier
                        .fillMaxWidth(),
//                    .height(sizeHeight.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 4,
                    textStyle = style.copy(color = Food_appTheme.myColors.textBlackColor),
                    leadingIcon = {
//                Icon(
//                    painterResource(id = R.drawable.eye),
//                    contentDescription = "",
//                    tint = Food_appTheme.myColors.labelColor
//                )
                        if(image_url != null){
                            AsyncImage(
                                model =
                                    if(image_url.startsWith("https")){
                                        image_url
                                    }else{
                                        BASE_URL_API_AVATAR + image_url
                                    },
                                contentDescription = "",
                                modifier = Modifier
                                    .size(imageSize.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }else{
                            Image(
                                painterResource(id = image),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(imageSize.dp)
                                    .clip(CircleShape)
                            )
                        }
                    },
                    trailingIcon = {
                        val noteSelectStar = stringResource(id = R.string.notSelectStar)
                        IconButton(onClick = {
                                if(rating != 0){
//                                    Log.d("abc","$canWrite,$product_id,$customer_id,$rating,$text")
                                    reviewViewModel.postReview(canWrite,product_id,customer_id,rating,if (text.isEmpty()) null else text) { result, review ->
                                        if (result) {
                                            reloadScreen(customer_id,product_id)
                                        } else {
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, noteSelectStar , Toast.LENGTH_SHORT).show()
                                }
                        }) {
                            Icon(painterResource(id = icon), contentDescription = "", tint = iconColor, modifier = Modifier.size(iconSize.dp))
                        }
                    }
                )
            }
        }
    }
}