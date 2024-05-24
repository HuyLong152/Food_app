package com.example.food_app.view.Product

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.HotSearchRepository
import com.example.food_app.data.Repository.ProductRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Product.ProductResponseItem
//import com.example.food_app.ui.theme.BottomNavigationIconColor
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.NormalTextColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.FillterButton
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.view.Main.IconWithText
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.NormalItem
import com.example.food_app.view.SearchFieldNormal
import com.example.food_app.view.TextWithIcon
import com.example.food_app.viewmodel.FavoriteViewModel
import com.example.food_app.viewmodel.HotSearchViewModel
import com.example.food_app.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

//@Preview(showBackground = true, name = "Category Screen")
//@OptIn( ExperimentalMaterial3Api::class)
//@Composable
//fun demoBottomSheetScaffold(){
//    val scaffoldState = rememberBottomSheetScaffoldState() // tạo state để điều khiển trạng thái của bottomsheet
//    val coroutineScope = rememberCoroutineScope()
//
//    val ShowBottomSheet : () -> Unit = { // tạo hàm để hiển thị bottom sheet
//        coroutineScope.launch {
//            scaffoldState.bottomSheetState.expand()
//        }
//    }
//    val HideBottomSheet : () -> Unit = { // tạo hàm để ẩn bottom sheet
//        coroutineScope.launch {
//            scaffoldState.bottomSheetState.partialExpand()
//        }
//    }
//    BottomSheetScaffold(scaffoldState = scaffoldState,sheetContent = {// tạo bottom sheet
//        BottomSheetContent{ // nội dung hiển thị của bottom sheet
//            HideBottomSheet() // truyền vào hàm ẩn bottom sheet
//        }
//    } ) {
//        BodyDemo() {
//            ShowBottomSheet()
//        }
//    }
//}
@Composable
fun BottomSheetContent(modifier: Modifier = Modifier , HideBottomSheet:() -> Unit, changeFilterNumber : (Int) -> Unit){ // nội dung của bottom sheet
    Column(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .fillMaxWidth()
    ) {
//        Spacer(modifier = Modifier.height(150.dp))
//        Text(text = "content scaffold sheet")
//        Button(onClick = {HideBottomSheet()}) {
//            Text(text = "close")
//        }
        Column {
            Text(modifier = Modifier.padding(10.dp),text = stringResource(id = R.string.filterByPrice), style = Typography.bodyLarge, color = Food_appTheme.myColors.iconColor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.under100
                ), textColor = Color.White, onClick = {changeFilterNumber(1); HideBottomSheet()})
                Spacer(modifier = Modifier.width(10.dp))
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.from100to200
                ), textColor = Color.White, onClick = {changeFilterNumber(2); HideBottomSheet()})
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.from200to500
                ), textColor = Color.White, onClick = {changeFilterNumber(3); HideBottomSheet()})
                Spacer(modifier = Modifier.width(10.dp))
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.over500
                ), textColor = Color.White, onClick = {changeFilterNumber(4); HideBottomSheet()})
            }
            Text(modifier = Modifier.padding(10.dp),text = stringResource(id = R.string.filterByRate), style = Typography.bodyLarge, color = Food_appTheme.myColors.iconColor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.from1to2
                ), textColor = Color.White, onClick = {changeFilterNumber(5); HideBottomSheet()})
                Spacer(modifier = Modifier.width(10.dp))
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.from2to4
                ), textColor = Color.White, onClick = {changeFilterNumber(6); HideBottomSheet()})
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FillterButton(
                    Modifier
                        .weight(0.5f),buttonColor = Food_appTheme.myColors.plateHolderColor, style = Typography.bodyLarge, text = stringResource(
                    id = R.string.fiveStar
                ), textColor = Color.White, onClick = {changeFilterNumber(7); HideBottomSheet()})
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryScreen(
    customer_id:Int,
    text: String?,
    value: Int?,
    category_id : Int?,
    openHomeScreen: () -> Unit,
    openSearchScreen: (String, Int,Int,Int) -> Unit,
    openDetailScreen : (Int,Int) -> Unit,
    favoriteViewModel: FavoriteViewModel
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden) // tạo state lưu trạng thái và khởi tạo giá trị mặc định là hidden
    val coroutinScope = rememberCoroutineScope()
    val showBottomSheet: () -> Unit = { // hiện sheet sate
        coroutinScope.launch {
            sheetState.show()
        }
    }
    var numberFilter by remember {
        mutableStateOf(-1)
    }
    val hidenBottomSheet: () -> Unit = { // ẩn sheet sate
        coroutinScope.launch {
            sheetState.hide()
        }
    }
    ModalBottomSheetLayout(sheetState = sheetState,sheetContent = {
        BottomSheetContent(changeFilterNumber = {numberFilter = it}, HideBottomSheet = hidenBottomSheet)
    }) {

        val productRepository = ProductRepository(RetrofitClient.productApiService)
        val productViewModel: ProductViewModel = remember {
            ProductViewModel(productRepository = productRepository)
        }
        val hotSearchRepository = HotSearchRepository(RetrofitClient.hotSearchApiService)
        val hotSearchViewModel: HotSearchViewModel = remember {
            HotSearchViewModel(hotSearchRepository = hotSearchRepository)
        }
        var listProduct by remember {
            mutableStateOf(listOf<ProductResponseItem>())
        }
        if (value != 0) {
            if (value == 1) {
//                if (productViewModel.listAllProductPriceSortLiveData.value == null) {
                    productViewModel.getProductsByPriceAndSort() { result, product ->
                        if (result) {
                            listProduct = product!!
                        } else {
                        }
                    }
//                } else {
//                    listProduct = productViewModel.listAllProductPriceSortLiveData.value!!
//                }
            } else if (value == 2) {
//                if (productViewModel.listAllProductPriceRateLiveData.value == null) {
                    productViewModel.getProductsByRateAndSort() { result, product ->
                        if (result) {
                            listProduct = product!!
                        } else {
                        }
                    }
//                } else {
//                    listProduct = productViewModel.listAllProductPriceRateLiveData.value!!
//                }
            } else if (value == 3) {
//                if (productViewModel.listAllProductTopSaleLiveData.value == null) {
                    productViewModel.getProductsByTopSaleAndSort() { result, product ->
                        if (result) {
                            listProduct = product!!
                        } else {
                        }
                    }
//                } else {
//                    listProduct = productViewModel.listAllProductTopSaleLiveData.value!!
//                }
            }else if (value == 4) {
//                if (productViewModel.listAllProductByTimeLiveData.value == null) {
                    productViewModel.getAllProductsByTime(30) { result, product ->
                        if (result) {
                            listProduct = product!!
                        } else {
                        }
                    }
//                } else {
//                    listProduct = productViewModel.listAllProductByTimeLiveData.value!!
//                }
            }else if (value == 5) {
//            if (productViewModel.listAllProductByTimeLiveData.value == null) {
//                productViewModel.getAllProductsByTime(30) { result, product ->
//                    if (result) {
//                        listProduct = product!!
//                    } else {
//                    }
//                }
//            } else {
//                listProduct = productViewModel.listAllProductByTimeLiveData.value!!
//            }
                ///
//                if (productViewModel.listAllProductRecommendLiveData.value != null) {
//
//                    listProduct = productViewModel.listAllProductRecommendLiveData.value!!
//                } else {

                    productViewModel.getProductsRecommend { result, listAllPNew ->
                        if (result) {
                            listProduct = listAllPNew!!
                        }
                    }
//                }
            }else if (value == 6) {
//                if (productViewModel.listAllProductByTimeLiveData.value == null) {
                    productViewModel.getAllProductsByHotSearch { result, product ->
                        if (result) {
                            listProduct = product!!
                        } else {
                        }
                    }
//                } else {
//                    listProduct = productViewModel.listAllProductByTimeLiveData.value!!
//                }
            }
        }else if (category_id != -1) {
//            if (productViewModel.listAllProductByCategoryLiveData.value != null) {
//                listProduct = productViewModel.listAllProductByCategoryLiveData.value!!
//            } else {

                productViewModel.getAllProductsByCategory(category_id = category_id!!) { result, listAllPNew ->
                    if (result) {
                        listProduct = listAllPNew!!
                    } else {
                    }
                }
//            }
        }
        else {
//            if (productViewModel.listAllProductByNameLiveData.value == null) {
                productViewModel.getAllProductsByName(text!!) { result, product ->
                    if (result) {
                        listProduct = product!!
                        hotSearchViewModel.create(text!!) { result, product ->
                        }
                    } else {
                    }
                }
//            } else {
//                listProduct = productViewModel.listAllProductByNameLiveData.value!!
//            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 37.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TopCategoryScreen(
                stringResource(id = R.string.fastAnhFrest),
                Modifier.weight(0.4f),
                listProduct.size,
                listOf(R.drawable.food_06,R.drawable.food_07,R.drawable.food_08,R.drawable.food_09,R.drawable.food_10),
                Typography.displayLarge,
                Food_appTheme.myColors.mainColor,
                Typography.titleMedium,
                Food_appTheme.myColors.labelColor,
                openHomeScreen,
                openSearchScreen,
                customer_id
            )

            BottomCategoryScreen(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(bottom = 15.dp),
                style = Typography.bodySmall,
                icon = R.drawable.sort,
                listProduct = listProduct,
                isShowFilter = true,
                imageNoResult = R.drawable.no_result2,
                openDetailScreen = openDetailScreen,
                customer_id = customer_id,
                fillterIcon = R.drawable.fillter,
                isShowSort = true,
                clickSortIcon = showBottomSheet,
                filterNumber = numberFilter,
                favoriteViewModel = favoriteViewModel
            )
        }
    }

}

@Composable
fun TopCategoryScreen(
    categoryName : String,
    modifier: Modifier = Modifier,
    numberProduct: Int,
    image: List<Int>,
    headerStyle: TextStyle,
    color: Color,
    productStyle: TextStyle,
    productColor: Color,
    openHomeScreen: () -> Unit,
    openSearchScreen: (String, Int,Int,Int) -> Unit,
    customer_id :Int
) {
    var randomIndex = Random.nextInt(0, 5)
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = image[randomIndex]),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = modifier.fillMaxSize()
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = categoryName, style = headerStyle.copy(color = color))
                    Text(
                        text = "$numberProduct "+ stringResource(id = R.string.resultFound),
                        style = productStyle.copy(color = productColor)
                    )
                }
                Column(
                    modifier = modifier.fillMaxSize(),
                ) {
                }
            }
        }
        SearchFieldNormal(
            plateHolder = stringResource(id = R.string.findFood),
            activeColor = Food_appTheme.myColors.mainColor,
            nonactiveColor = Food_appTheme.myColors.unClickColor,
            plateHolderColor = Food_appTheme.myColors.plateHolderColor,
            style = Typography.bodyLarge,
            sizeHeight = 51,
            icon = R.drawable.search,
            modifier = Modifier.align(Alignment.TopEnd),
            closeIcon = R.drawable.remove,
            openSearchScreen = openSearchScreen,
            customer_id = customer_id
        )
        NavigateButton(
            size = 38,
            icon = R.drawable.back_icon,
            iconColor = Food_appTheme.myColors.iconColor,
            buttonColor = Color.White,
            borderButton = 10,
            navigationFun = {
                openHomeScreen()
            }
        )
    }
}

@Composable
fun BottomCategoryScreen(
    modifier: Modifier = Modifier,
    style: TextStyle,
    icon: Int,
    listProduct: List<ProductResponseItem>,
    isShowFilter: Boolean,
    imageNoResult: Int,
    openDetailScreen : (Int,Int) -> Unit,
    customer_id: Int,
    fillterIcon :Int,
    isShowSort : Boolean = false,
    clickSortIcon : ()-> Unit = {},
    filterNumber : Int = -1,
    favoriteViewModel: FavoriteViewModel
) {
//    var listProductV2 by remember {
//        mutableStateOf(listProduct)
//    }
    var listProductV2 by remember {
        mutableStateOf(listOf<ProductResponseItem>())
    }
    val context = LocalContext.current
    val noResultFound = stringResource(id = R.string.noResultFound)


    LaunchedEffect(filterNumber){
        if(filterNumber == 1){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.price.toDouble().toInt() < 100000  }
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        } else if(filterNumber == 2){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.price.toDouble().toInt() >= 100000 &&  it.price.toDouble().toInt() < 200000}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }else if(filterNumber == 3){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.price.toDouble().toInt() >= 200000 &&  it.price.toDouble().toInt() < 500000}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }else if(filterNumber == 4){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.price.toDouble().toInt() >= 500000}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }else if(filterNumber == 5){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.average_rate >= 1.0 &&  it.average_rate <= 2.0}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }else if(filterNumber == 6){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.average_rate > 2.0 &&  it.average_rate <= 4.0}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }else if(filterNumber == 7){
            listProductV2 = listProduct
            listProductV2 = listProductV2.filter { it.average_rate == 5.0}
            if(listProductV2.isEmpty()){
                Toast.makeText(context, noResultFound, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        if (isShowFilter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text =  stringResource(id = R.string.sortby), style = style.copy(color = Food_appTheme.myColors.textBlackColor))
                    Spacer(modifier = Modifier.width(10.dp))
                    TextWithIcon(
                        text = stringResource(id = R.string.viewAll),
                        color = Food_appTheme.myColors.mainColor,
                        iconColor = Food_appTheme.myColors.mainColor,
                        iconSize = 10,
                        style = style,
                        icon = R.drawable.right_icon
                    )
                }
                var expanded by remember {
                    mutableStateOf(false)
                }
                Row(){
                    if(isShowSort){
                        Icon(
                            painterResource(id = fillterIcon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { clickSortIcon() },
                            tint = Food_appTheme.myColors.mainColor
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                    Column {
                        Icon(
                            painterResource(id = icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { expanded = !expanded },
                            tint = Food_appTheme.myColors.mainColor
                        )
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
                                        iconColor = Food_appTheme.myColors.mainColor,
                                        iconSize = 22,
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.mainColor,
                                    )
                                },
                                onClick = {
                                    listProductV2 = listProduct
                                    listProductV2 =
                                        listProductV2.sortedByDescending { product -> product.price.toFloat() }
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    IconWithText(
                                        icon = R.drawable.sort_star,
                                        text = stringResource(id = R.string.sortByRate),
                                        iconColor = Food_appTheme.myColors.mainColor,
                                        iconSize = 22,
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.mainColor,
                                    )
                                },
                                onClick = {
                                    listProductV2 = listProduct
                                    listProductV2 =
                                        listProductV2.sortedByDescending { product -> product.average_rate }
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    IconWithText(
                                        icon = R.drawable.chart,
                                        text = stringResource(id = R.string.sortBySale),
                                        iconColor = Food_appTheme.myColors.mainColor,
                                        iconSize = 22,
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.mainColor,
                                    )
                                },
                                onClick = {
                                    listProductV2 = listProduct
                                    listProductV2 =
                                        listProductV2.sortedByDescending { product -> product.sale_number }
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }
        }

        if (listProduct.isEmpty()) {
            Image(
                painterResource(id = imageNoResult),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 15.dp)
            ) {

                itemsIndexed(if(!listProductV2.isEmpty()) listProductV2 else listProduct) { index, product ->
                    Row(
                    ) {
                        NormalItem(
                            sizeW = 154,
                            sizeH = 216,
                            image = R.drawable.food_04,
                            name = product.name,
                            ingredient = product.ingredient,
                            styleText = Typography.labelMedium,
                            styleIngredient = Typography.displayMedium,
                            corner = 15,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { openDetailScreen(product.id, customer_id) },
                            image_list = product.image,
                            rateStar = product.average_rate,
                            numberRate = product.review.size,
                            price = product.price,
                            discount = product.discount,
                            customer_id = customer_id,
                            product_id = product.id,
                            favoriteViewModel = favoriteViewModel
                        )
                    }
                }
            }
        }
    }
}