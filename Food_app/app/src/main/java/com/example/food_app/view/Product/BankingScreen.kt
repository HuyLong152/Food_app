package com.example.food_app.view.Product

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.food_app.BASE_BANKING
import com.example.food_app.R
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.NavigationBar
import com.example.food_app.view.WebviewScreen
import com.example.food_app.viewmodel.CartViewModel

@Composable
fun BankingScreen(
    product_id: String,
    navigationBack: () -> Unit,
    note : String,
    customer_id: Int,
    totalPrice: Float,
    discountCode: Int,
    quantity :Int,
    payment:String,
    location_id :Int,
    cartViewModel: CartViewModel
){
    BackHandler {
        cartViewModel.getAllCart(customer_id!!) { result, product ->
            if (result) {
                cartViewModel.listAllCartLiveData.value = product!!
            }
        }
        GlobalVariables.clearData()
        navigationBack()
    }
    WebviewScreen(url = BASE_BANKING+"?listProduct=[${product_id}]&note=${note}&customer_id=${customer_id}&totalPrice=${totalPrice.toInt()}&discountCode=${discountCode}&quantity=${quantity}&payment=${payment}&location_id=${location_id}")
//    Log.d("abc",BASE_BANKING+"?listProduct=${product_id}&note=${note}&customer_id=${customer_id}&totalPrice=${totalPrice}&discountCode=${discountCode}&quantity=${quantity}&payment=${payment}")
}