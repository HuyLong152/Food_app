package com.example.food_app.view.Product

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.food_app.BASE_URL_API_PRODUCT
import com.example.food_app.R
import com.example.food_app.data.Repository.CartReponsitory
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.api.RetrofitClient.productApiService
import com.example.food_app.data.model.Product.Categorie
import com.example.food_app.data.model.Product.Image
import com.example.food_app.data.model.Product.ProductResponseItem
import com.example.food_app.data.model.Product.Review
//import com.example.food_app.ui.theme.BackgroundCategoryColor
//import com.example.food_app.ui.theme.BottomNavigationIconColor
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.NormalTextColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.StarColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.ui.theme.defaultFont
import com.example.food_app.view.Account.formatPrice
import com.example.food_app.view.ButtonLoginOther
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.FavoriteButton
import com.example.food_app.view.LabelCategory
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.PriceComponent
import com.example.food_app.view.QuantityComponent
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.FavoriteViewModel
import com.example.food_app.viewmodel.ProductViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.text.DecimalFormat

//@Preview(showBackground = true, name ="detail screen")
@Composable
fun DetailProductScreen(
    product_id: Int,
    navigationBack : () ->Unit,
    customer_id : Int,
    openPaymentScreen :(List<Int>, Int, Float,String,Int) -> Unit,
    openReviewScreen : () ->Unit,
    favoriteViewModel: FavoriteViewModel,
    cartViewModel: CartViewModel
) {
    BackHandler {
        GlobalVariables.clearData()
        navigationBack()
    }
//    val context = LocalContext.current
    var aProduct by remember {
        mutableStateOf(
            ProductResponseItem(
                average_rate = 0.0,
                calo = 0,
                categorie = emptyList(),
                created_time = "",
                description = "",
                discount = 0,
                id = product_id,
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
    val productRepository = ProductRepository(productApiService)
    val productViewModel: ProductViewModel = remember {
        ProductViewModel(productRepository = productRepository)
    }
    productViewModel.getProductById(product_id) { result, product ->
        if (result) {
            aProduct = product!!
        } else {
//            Toast.makeText(
//                context,
//                "Can load category",
//                Toast.LENGTH_LONG
//            ).show()
        }
    }


    var noteForChef by remember { mutableStateOf("") }

//    val cartRepository = CartReponsitory(RetrofitClient.cartApiService)
//    val cartViewModel: CartViewModel = remember {
//        CartViewModel(cartRepository = cartRepository)
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 27.dp, horizontal = 23.dp),
    ) {
        val finalPrice = aProduct.price.toFloat() - (aProduct.price.toFloat() * aProduct.discount/100)
        val context = LocalContext.current
        var quantityProduct by remember {
            mutableStateOf(1)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.93f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
//            contentPadding = PaddingValues(12.dp)
        ) {
            item {
                //images product
                Box(

                ) {
                    ImageProduct(listImage = aProduct.image, sizeW = 323, sizeH = 200, corner = 15)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigateButton(
                            size = 38,
                            icon = R.drawable.back_icon,
                            iconColor = Food_appTheme.myColors.iconColor,
                            buttonColor = Color.White,
                            borderButton = 10,
                            navigationFun = {
                                navigationBack()
                                GlobalVariables.clearData()
                            }
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
            }
            item {
                //name product
               Row(
                   horizontalArrangement = Arrangement.spacedBy(10.dp),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Text(text = aProduct.name, style = Typography.headlineMedium.copy(Food_appTheme.myColors.textBlackColor))
                   if(aProduct.quantity <= 0){
                       Box(
                           modifier = Modifier.background(Food_appTheme.myColors.starColor).padding(2.dp),
                           contentAlignment = Alignment.Center
                       ){
                           Text(text = stringResource(id = R.string.soldOut), style = Typography.titleMedium.copy(Color.White))
                       }
                   }
               }
            }
            item {
                //rate start and see review
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(id = R.drawable.star),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = Food_appTheme.myColors.starColor
                        )
                        val df = DecimalFormat("0.0")
                        val roundedNumber: String = df.format(aProduct.average_rate)
                        Text(
                            text = "$roundedNumber",
                            style = Typography.bodySmall,
                            color = Food_appTheme.myColors.textBlackColor
                        )
                        Text(
                            text = "( ${aProduct.sale_number} )",
                            style = Typography.bodySmall,
                            color = Food_appTheme.myColors.normalTextColor
                        )
                        TextButton(onClick = { openReviewScreen() }) {
                            Text(
                                text =  stringResource(id = R.string.seeReview),
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(defaultFont)),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = Food_appTheme.myColors.mainColor,
                                    textDecoration = TextDecoration.Underline
                                ),
                            )
                        }
                    }
                    Text(
                        text = "${aProduct.calo} Kcal",
                        style = Typography.headlineSmall,
                        color = Food_appTheme.myColors.mainColor
                    )
                }
            }

            item {
                //price and quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if(aProduct.discount != 0){
                        Column {
                            Text(
                                text = "${formatPrice(aProduct.price.toDouble().toInt())} ₫",
                                style = Typography.headlineSmall,
                                color = Food_appTheme.myColors.mainColor,
                                textDecoration = TextDecoration.LineThrough

                            )
                            Text(
                                text = "${formatPrice(finalPrice.toDouble().toInt())} ₫",
                                style = Typography.titleMedium,
                                color = Food_appTheme.myColors.mainColor
                            )
                        }
                    }else{
                        Text(
                            text = "${formatPrice(aProduct.price.toDouble().toInt())} ₫",
                            style = Typography.titleMedium,
                            color = Food_appTheme.myColors.mainColor
                        )
                    }

                    QuantityComponent(
                        color = Food_appTheme.myColors.mainColor,
                        size = 31,
                        iconMinus = R.drawable.minus,
                        iconPlus = R.drawable.plus,
                        style = Typography.titleSmall,
                        maxNumber = aProduct.quantity,
                        changeQuantity = {it -> quantityProduct = it},
                        cartViewModel = cartViewModel
                    )
                }
            }
            item {
                //description
                ExpandableText(
                    text = "${aProduct.description}",
                    maxLines = 4, // Đặt số dòng tối đa bạn muốn hiển thị trước khi nhấn "Xem thêm"
                    textStyle = Typography.headlineSmall,
                    color = Food_appTheme.myColors.bottomNavigationIconColor
                )
            }
            item {
                //detail
                Text(text =  stringResource(id = R.string.moreDetail), style = Typography.bodyLarge.copy(Food_appTheme.myColors.textBlackColor))
            }
            item {
                //detail text
                ExpandableText(
                    text = "${aProduct.ingredient}",
                    maxLines = 1, // Đặt số dòng tối đa bạn muốn hiển thị trước khi nhấn "Xem thêm"
                    textStyle = Typography.headlineSmall,
                    color = Food_appTheme.myColors.bottomNavigationIconColor
                )

            }
//            var listCategory = mutableListOf<String>()
//            for (i in 0..5) {
//                listCategory.add("Burger")
//            }
            item {
                //list category
                LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    itemsIndexed(aProduct.categorie) { index , category ->
                        LabelCategory(
                            text = category.name,
                            backgroundColor = Food_appTheme.myColors.backgroundCategoryColor,
                            corner = 5,
                            style = Typography.titleMedium,
                            color = Food_appTheme.myColors.normalTextColor,
                            padding = 10
                        )
                    }
                }
                //note
//                TextFieldNormal(
//                    plateHolder = "Write note for chef",
//                    textLabel = "",
//                    activeColor = Food_appTheme.myColors.mainColor,
//                    nonactiveColor = Food_appTheme.myColors.unClickColor,
//                    plateHolderColor = Food_appTheme.myColors.plateHolderColor,
//                    style = Typography.bodyLarge,
//                    labelStyle = Typography.labelMedium,
//                    isPassField = 0,
//                    sizeHeight = 80,
//                    text = noteForChef,
//                    onValueChange = { noteForChef = it }
//                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.07f)
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val productExist = stringResource(id = R.string.productExist)
                val addProduct = stringResource(id = R.string.addProduct)

                ButtonLoginOther(
                    width = 160,
                    height = 53,
                    color = if(aProduct.quantity <= 0) Food_appTheme.myColors.labelColor else Food_appTheme.myColors.mainColor,
                    astyle = Typography.titleSmall,
                    image = R.drawable.add_cart,
                    imgSize = 17,
                    text =  stringResource(id = R.string.addToCart),
                    circleSize = 40,
                    circleColor = Color.White,
                    textColor = Color.White,
                    isImage = false,
                    openScreen = {
                        if(aProduct.quantity > 0){
                            cartViewModel.isExistInCart(customer_id, product_id) { result, product ->
                                if (result) {
                                    Toast.makeText(context, productExist, Toast.LENGTH_SHORT).show()
                                } else {
                                    cartViewModel.addToCart(customer_id, product_id, quantityProduct) { result, product ->
                                        if (result) {
                                            cartViewModel.getAllCart(customer_id!!) { result, product ->
                                                if (result) {
                                                    cartViewModel.listAllCartLiveData.value = product!!
                                                }
                                            }
                                            Toast.makeText(context, addProduct, Toast.LENGTH_SHORT).show()
                                        } else {
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
                ButtonMain(
                    width = 150,
                    height = 53,
                    text =  stringResource(id = R.string.buyNow),
                    color = if(aProduct.quantity <= 0) Food_appTheme.myColors.labelColor else Food_appTheme.myColors.mainColor,
                    style = Typography.labelMedium,
                    clickButton = {
                        if(aProduct.quantity > 0){
                            val listProduct = listOf(product_id)
                            openPaymentScreen(listProduct,customer_id,finalPrice*quantityProduct,"no value",quantityProduct)
                        }
                    }
                )

            }
        }
    }
}

@Composable
fun NoteComponent(
    sizeH: Int,
    modifier: Modifier = Modifier,
    placeholder: String,
    corner: Int,
    color: Color,
    style: TextStyle
) {
    var valueNote by remember {
        mutableStateOf("")
    }
    TextField(
        value = valueNote,
        onValueChange = { valueNote = it },
        modifier = modifier
            .fillMaxWidth()
            .height(sizeH.dp)
            .border(BorderStroke(1.dp, color)),
        placeholder = { Text(text = placeholder, style = style.copy(color = color)) },
        textStyle = style
    )
}

@Composable
fun ExpandableText(
    text: String,
    maxLines: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    color: Color
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else maxLines,
            modifier = Modifier.clickable { expanded = !expanded },
            style = textStyle.copy(lineHeight = 20.sp),
            overflow = TextOverflow.Ellipsis,
            color = color
        )
        AnimatedVisibility(visible = !expanded) {
            Text(
                text = stringResource(id = R.string.seeMore),
                style = textStyle,
                color = Food_appTheme.myColors.mainColor
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageProduct(
    listImage: List<Image>,
    sizeW: Int,
    sizeH: Int,
    corner: Int,
    modifier: Modifier = Modifier
) {
    if(!listImage.isEmpty()){
        val pagerState = rememberPagerState(pageCount = listImage.size)
        HorizontalPager(state = pagerState, modifier = Modifier) { page ->
            AsyncImage(
                model = BASE_URL_API_PRODUCT+listImage[page].imgurl,
                contentDescription = "",
                modifier = modifier
                    .fillMaxWidth()
                    .height(sizeH.dp)
                    .clip(RoundedCornerShape(corner.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(sizeH.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                PriceComponent(
                    sizeH = 30,
                    text = "${page + 1}",
                    corner = 15,
                    style = Typography.displaySmall,
                    color = Color.White,
                    secondText = "  / ${listImage.size}"
                )
            }
        }
    }else{
        Image(
            painterResource(id = R.drawable.no_image),
            contentDescription = "",
            modifier = modifier
                .fillMaxWidth()
                .height(sizeH.dp)
                .clip(RoundedCornerShape(corner.dp)),
            contentScale = ContentScale.Crop
        )
    }
}