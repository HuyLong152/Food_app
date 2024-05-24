package com.example.food_app.view.Account

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.api.RetrofitClient.authApiService
//import com.example.food_app.ui.theme.CircleColor
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.view.TextHeader
import com.example.food_app.viewmodel.AuthViewModel

@Preview(showBackground = true,name= "test")
@Composable
fun DisplayTest(){
ResetPassScreen(true, navigationBack = {})
}

@Composable
fun ResetPassScreen(
    isShowBackButton: Boolean,
    navigationBack:() -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box() {
            Box(contentAlignment = Alignment.Center) {
                CircleDecor(modifier = Modifier.offset((-46).dp, (-21).dp), Food_appTheme.myColors.mainColor, 100)
                CircleDecor(modifier = Modifier.offset((-46).dp, (-21).dp), Color.White, 36)
            }
            CircleDecor(modifier = Modifier.offset((-5).dp, (-99).dp), Food_appTheme.myColors.circleColor, 165)
            CircleDecor(modifier = Modifier.offset((298).dp, (-109).dp), Food_appTheme.myColors.mainColor, 181)
            if (isShowBackButton){
                NavigateButton(
                    size = 38,
                    icon = R.drawable.back_icon,
                    iconColor = Food_appTheme.myColors.iconColor,
                    buttonColor = Color.White,
                    borderButton = 10,
                    modifier = Modifier.offset((27).dp, (37).dp),
                    navigationFun = {navigationBack()}
                )
            }
        }
        Column(
            modifier = Modifier
                .height((LocalConfiguration.current.screenHeightDp * 2 / 5).dp)
                .padding(start = 26.dp, bottom = 25.dp, end = 26.dp)
        ) {
            ContentReset()
        }
    }
}
@Composable
fun ContentReset(){
    var phoneOrEmail by remember { mutableStateOf("") }

    val context = LocalContext.current

    val authRepository = AuthRepository(authApiService)
    val checkYourMail = stringResource(id = R.string.checkYourMail)
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(authRepository = authRepository)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ){

            TextHeader(style = Typography.headlineLarge.copy(Food_appTheme.myColors.textBlackColor), text = stringResource(id = R.string.resetPass))
            Text(text = stringResource(id = R.string.enterYourEmail),style = Typography.labelMedium, color = Food_appTheme.myColors.labelColor, modifier = Modifier.padding(top = 10.dp))
        }
        TextFieldNormal(
            plateHolder = stringResource(id = R.string.plateHolerAccount),
            textLabel = "",
            activeColor = Food_appTheme.myColors.mainColor,
            nonactiveColor = Food_appTheme.myColors.unClickColor,
            plateHolderColor = Food_appTheme.myColors.plateHolderColor,
            style = Typography.bodyLarge,
            labelStyle = Typography.labelMedium,
            isPassField = 0,
            sizeHeight = 65,
            text = phoneOrEmail,
            onValueChange = {phoneOrEmail = it}
        )
        ButtonMain(
            width = 248,
            height = 60,
            text = stringResource(id = R.string.sendNewPass),
            color = Food_appTheme.myColors.mainColor,
            style = Typography.labelMedium,
            clickButton = {
                isLoading = true
                authViewModel.resendPass(phoneOrEmail) { result, message ->
                    isLoading = false
                    if (result) {
                        Toast.makeText(context, checkYourMail, Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            isShowLoading = isLoading
        )
    }
}