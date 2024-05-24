package com.example.food_app.view.Main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.OrderRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Orders.OrderNotifiResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.Account.convertDateTimeToRelative
import com.example.food_app.view.ItemNotification
import com.example.food_app.viewmodel.OrderViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    customer_id : Int,
    openOrderScreen : () -> Unit
){
    val orderRepository = OrderRepository(RetrofitClient.orderApiService)
    val orderViewModel: OrderViewModel = remember {
        OrderViewModel(orderRepository = orderRepository)
    }
    var listNotiffi by remember {
        mutableStateOf(listOf<OrderNotifiResponseItem>())
    }

    orderViewModel.getOrderInFiveDays(customer_id) { result, order ->
        if (result) {
            listNotiffi = order!!
        } else {
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 37.dp)
    ) {
        Text(text = stringResource(id = R.string.notification) , style = Typography.headlineLarge.copy(
            Food_appTheme.myColors.textBlackColor))
        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp)
        ){
            itemsIndexed(listNotiffi){
                index , notifi ->
                if(notifi.status == "completed" || notifi.status == "rating"){
                    ItemNotification(
                        text = stringResource(id = R.string.wasDelivered),
                        idOrder = notifi.id.toString(),
                        time = if(convertDateTimeToRelative(notifi.updated_time) == "Yesterday") stringResource(
                            id = R.string.yesterday
                        ) else if (convertDateTimeToRelative(notifi.updated_time) == "Today") stringResource(
                            id = R.string.today
                        ) else convertDateTimeToRelative(notifi.updated_time) + stringResource(
                            id = R.string.dayAgo
                        ),
                        content = stringResource(id = R.string.rateNow),
                        styleText = Typography.bodyMedium,
                        styleTime = Typography.labelMedium,
                        styleId = Typography.labelMedium,
                        textColor = Food_appTheme.myColors.iconColor,
                        timeColor = Food_appTheme.myColors.labelColor,
                        idColor = Food_appTheme.myColors.mainColor,
                        sizeH = 70,
                        corner = 15,
                        backgroundColor = Food_appTheme.myColors.bankColor,
                        image = R.drawable.delevered,
                        modifier = Modifier.clickable {
                            openOrderScreen()
                        }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(25.dp))
                    ItemNotification(
                        text = stringResource(id = R.string.placeOrder),
                        idOrder = notifi.id.toString(),
                        time = if(convertDateTimeToRelative(notifi.created_time) == "Yesterday") stringResource(
                            id = R.string.yesterday
                        ) else if (convertDateTimeToRelative(notifi.created_time) == "Today") stringResource(
                            id = R.string.today
                        ) else convertDateTimeToRelative(notifi.created_time) + stringResource(
                            id = R.string.dayAgo
                        ),
                        content = stringResource(id = R.string.clickNotifi),
                        styleText = Typography.bodyMedium,
                        styleTime = Typography.labelMedium,
                        styleId = Typography.labelMedium,
                        textColor = Food_appTheme.myColors.iconColor,
                        timeColor = Food_appTheme.myColors.labelColor,
                        idColor = Food_appTheme.myColors.mainColor,
                        sizeH = 70,
                        corner = 15,
                        backgroundColor = Food_appTheme.myColors.bankColor,
                        image = R.drawable.delivery,
                        modifier = Modifier.clickable {
                            openOrderScreen()
                        }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(25.dp))
                }else{
                    ItemNotification(
                        text = stringResource(id = R.string.placeOrder),
                        idOrder = notifi.id.toString(),
                        time = if(convertDateTimeToRelative(notifi.created_time) == "Yesterday") stringResource(
                            id = R.string.yesterday
                        ) else if (convertDateTimeToRelative(notifi.created_time) == "Today") stringResource(
                            id = R.string.today
                        ) else convertDateTimeToRelative(notifi.created_time) + stringResource(
                            id = R.string.dayAgo
                        ),
                        content = stringResource(id = R.string.clickNotifi),
                        styleText = Typography.bodyMedium,
                        styleTime = Typography.labelMedium,
                        styleId = Typography.labelMedium,
                        textColor = Food_appTheme.myColors.textBlackColor,
                        timeColor = Food_appTheme.myColors.labelColor,
                        idColor = Food_appTheme.myColors.mainColor,
                        sizeH = 70,
                        corner = 15,
                        backgroundColor = Food_appTheme.myColors.bankColor,
                        image = R.drawable.delivery,
                        modifier = Modifier.clickable {
                            openOrderScreen()
                        }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(25.dp))
                }


            }
        }
    }
}