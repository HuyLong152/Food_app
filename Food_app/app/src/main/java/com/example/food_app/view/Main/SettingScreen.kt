package com.example.food_app.view.Main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.NavigationBar
import com.example.food_app.viewmodel.CustomerViewModel

//@Preview(showBackground = true)
@Composable
fun SettingScreen(
    customer_id : Int,
    navigationBack : () -> Unit,
    changeDarkMode : () -> Unit,
    isDarkMode : Boolean,
    openChangePassScreen : () -> Unit,
    isEng :String,
    changeLanguage : () -> Unit,
    openReport :() ->Unit
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 37.dp),
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        NavigateButton(
            size = 38,
            icon = R.drawable.back_icon,
            iconColor = Food_appTheme.myColors.iconColor,
            buttonColor = Color.White,
            borderButton = 10,
            navigationFun = {
                navigationBack()
            }
        )

        Text(text = stringResource(id = R.string.settingAndPrivacy) , style = Typography.headlineLarge.copy(Food_appTheme.myColors.textBlackColor))
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
        val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
        val customerViewModel: CustomerViewModel = remember {
            CustomerViewModel(customerRepository)
        }
            customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
                if (result) {
                    customerInfo = customerResponse!!
                } else {

                }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ItemSetting(
                icon = R.drawable.profile,
                text =  stringResource(id = R.string.changePass),
                textColor = Food_appTheme.myColors.textBlackColor,
                style = Typography.bodyLarge,
                trailingIcon = R.drawable.right_icon,
                sizeIcon = 25,
                colorIcon = Food_appTheme.myColors.labelColor,
                isShowTrailing = true,
                isCheckSwitch = false,
                isShowSwitch = false,
                modifier = Modifier.clickable {
                    if(customerInfo.social_link == null){
                        openChangePassScreen()
                    }
                }
            )
            ItemSetting(
                icon = R.drawable.dark,
                text = stringResource(id = R.string.darkTheme),
                textColor = Food_appTheme.myColors.textBlackColor,
                style = Typography.bodyLarge,
                trailingIcon = R.drawable.right_icon,
                sizeIcon = 25,
                colorIcon = Food_appTheme.myColors.labelColor,
                isShowTrailing = false,
                isCheckSwitch = isDarkMode,
                changeValueSwitch = {
                    changeDarkMode()
                },
                isShowSwitch = true
            )
            ItemSetting(
                icon = R.drawable.contact,
                text =  stringResource(id = R.string.contactUs),
                textColor = Food_appTheme.myColors.textBlackColor,
                style = Typography.bodyLarge,
                trailingIcon = R.drawable.right_icon,
                sizeIcon = 25,
                colorIcon = Food_appTheme.myColors.labelColor,
                isShowTrailing = true,
                isCheckSwitch = false,
                isShowSwitch = false,
                modifier = Modifier.clickable {
                    val uri = Uri.parse("fb-messenger://user/6828611727229786")
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    // Kiểm tra xem ứng dụng Messenger có được cài đặt trên thiết bị không
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        // Nếu Messenger chưa được cài đặt, chuyển hướng người dùng đến trang cửa hàng Google Play
                        val playStoreUri = Uri.parse("market://details?id=com.facebook.orca")
                        val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
                        // Thêm cờ để ngăn người dùng quay lại ứng dụng trước khi tải xuống Messenger
                        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(playStoreIntent)
                    }
                }
            )
            ItemSetting(
                icon = R.drawable.language,
                text =  stringResource(id = R.string.language),
                textColor = Food_appTheme.myColors.textBlackColor,
                style = Typography.bodyLarge,
                trailingIcon = R.drawable.right_icon,
                sizeIcon = 25,
                colorIcon = Food_appTheme.myColors.labelColor,
                isShowTrailing = false,
                isCheckSwitch = isEng == "vi",
                isShowSwitch = true,
                changeValueSwitch = {
                    changeLanguage()
                }
            )
            ItemSetting(
                icon = R.drawable.flag,
                text = stringResource(id = R.string.report),
                textColor = Food_appTheme.myColors.textBlackColor,
                style = Typography.bodyLarge,
                trailingIcon = R.drawable.right_icon,
                sizeIcon = 25,
                colorIcon = Food_appTheme.myColors.labelColor,
                isShowTrailing = true,
                isCheckSwitch = false,
                isShowSwitch = false,
                modifier = Modifier.clickable {
                        openReport()
                }
            )
        }
    }
}
@Composable
fun ItemSetting(
    icon :Int,
    text: String,
    textColor : Color,
    style : TextStyle,
    trailingIcon : Int,
    sizeIcon : Int,
    colorIcon : Color,
    isShowTrailing : Boolean = true,
    isShowSwitch : Boolean = true,
    isCheckSwitch : Boolean = false,
    changeValueSwitch : () -> Unit = {},
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painterResource(id = icon), contentDescription = "", modifier = Modifier.size(sizeIcon.dp), tint = colorIcon)
            Text(text = text, style = style.copy(textColor))
        }
        if (isShowTrailing){
            Icon(painterResource(id = trailingIcon), contentDescription = "", modifier = Modifier.size(sizeIcon.dp), tint = colorIcon)
        }
        if(isShowSwitch){
            Switch(checked = isCheckSwitch, onCheckedChange = {changeValueSwitch()})
        }
    }
}