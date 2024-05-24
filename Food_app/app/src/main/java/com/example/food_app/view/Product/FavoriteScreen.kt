package com.example.food_app.view.Product

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.R
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.Repository.FavoriteRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.data.model.Product.ProductResponseItem
//import com.example.food_app.data.model.Product
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.NavigationBar
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.FavoriteViewModel

//@Preview(showBackground = true, name = "favorite screen")
@Composable
fun FavoriteScreen(
    customer_id : Int,
    navigationBack : () -> Unit,
    openDetailScreen : (Int,Int) -> Unit,
    customerViewModel: CustomerViewModel,
    favoriteViewModel: FavoriteViewModel
) {
//    val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
//    val customerViewModel: CustomerViewModel = remember {
//        CustomerViewModel(customerRepository)
//    }
//    val favoriteRepository = FavoriteRepository(RetrofitClient.favoriteApiService)
//    val favoriteViewModel: FavoriteViewModel = remember {
//        FavoriteViewModel(favoriteRepository = favoriteRepository)
//    }
    var listProduct by remember {
        mutableStateOf(listOf<ProductResponseItem>())
    }


    if (favoriteViewModel.listAllFavoriteLiveData.value != null) {
        listProduct = favoriteViewModel.listAllFavoriteLiveData.value!!
    } else {
        favoriteViewModel.getAllFavorite(customer_id){ result, listFavorite ->
            if (result) {
                listProduct = listFavorite!!
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 26.dp, end = 26.dp, top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        NavigationBar(
            isShowAvt = true,
            text =  stringResource(id = R.string.favorite),
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

        BottomCategoryScreen(
            modifier = Modifier
                .weight(0.6f)
                .padding(bottom = 15.dp),
            style = Typography.bodySmall,
            icon = R.drawable.sort,
            listProduct = listProduct,
            isShowFilter = false,
            imageNoResult = R.drawable.empty_box,
            openDetailScreen = openDetailScreen,
            customer_id = customer_id,
            fillterIcon = R.drawable.fillter,
            favoriteViewModel = favoriteViewModel
        )
    }
}