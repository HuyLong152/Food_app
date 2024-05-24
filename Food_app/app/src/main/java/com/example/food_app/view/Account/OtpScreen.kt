package com.example.food_app.view.Account

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Auth.Account
import com.example.food_app.data.model.Customer.AccountResponse
import com.example.food_app.data.model.Auth.customerResponse
//import com.example.food_app.ui.theme.CircleColor
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.LableColor
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.TextBlackColor
//import com.example.food_app.ui.theme.TitleSmallColor
import com.example.food_app.ui.theme.Typography
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.view.Main.KeyboardStatus
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.TextHeader
import com.example.food_app.view.Main.keyboardAsState
import com.example.food_app.viewmodel.AuthViewModel
import com.example.food_app.viewmodel.CustomerViewModel

@Preview(showBackground = true, name = "test")
@Composable
fun display() {
}

@Composable
fun OtpScreen(
    username: String,
//    email: String,
    isShowBackButton: Boolean,
    navigationBack: () -> Unit,
    openLoginScreen: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box() {
            Box(contentAlignment = Alignment.Center) {
                CircleDecor(
                    modifier = Modifier.offset((-46).dp, (-21).dp),
                    Food_appTheme.myColors.mainColor,
                    100
                )
                CircleDecor(modifier = Modifier.offset((-46).dp, (-21).dp), Color.White, 36)
            }
            CircleDecor(
                modifier = Modifier.offset((-5).dp, (-99).dp),
                Food_appTheme.myColors.circleColor,
                165
            )
            CircleDecor(
                modifier = Modifier.offset((298).dp, (-109).dp),
                Food_appTheme.myColors.mainColor,
                181
            )
            if (isShowBackButton) {
                NavigateButton(
                    size = 38,
                    icon = R.drawable.back_icon,
                    iconColor = Food_appTheme.myColors.textBlackColor,
                    buttonColor = Color.White,
                    borderButton = 10,
                    modifier = Modifier.offset((27).dp, (37).dp),
                    navigationFun = {
                        navigationBack()
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .height((LocalConfiguration.current.screenHeightDp / 2).dp)
                .padding(start = 26.dp, bottom = 25.dp, end = 26.dp)
        ) {
            val authRepository = AuthRepository(RetrofitClient.authApiService)
            val authViewModel: AuthViewModel = remember {
                AuthViewModel(authRepository)
            }
            var accountInfo by remember {
                mutableStateOf(
                    AccountResponse(
                        account = Account(
                            create_time = "",
                            customer_id = 0,
                            id = 0,
                            password = "",
                            role = "customer",
                            status = "inactive",
                            update_time = "",
                            username = "",
                            verify_code = 0
                        )
                    )
                )
            }
            var customer_id by remember {
                mutableStateOf(0)
            }
            authViewModel.getAccountByName(username) { result,accountResponse ->
                if (result) {
                    accountInfo = accountResponse!!
                    customer_id = accountInfo.account.customer_id
                } else {

                }
            }
            //lấy ra customer_id từ username
            OtpTextField(
                style = Typography.headlineLarge,
                modifier = Modifier,
                openLoginScreen,
                customer_id,
                username
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OtpTextField(
    style: TextStyle,
    modifier: Modifier,
    openLoginScreen: () -> Unit,
    customer_id: Int,
    username: String
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var otpValue by remember {
            mutableStateOf("")
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        val context = LocalContext.current

        val authRepository = AuthRepository(RetrofitClient.authApiService)
        val authViewModel: AuthViewModel = remember {
            AuthViewModel(authRepository = authRepository)
        }

        val isEnableButton = derivedStateOf {
            otpValue.length == 4
        }
        Column(

            modifier = Modifier.fillMaxWidth()
        ) {
            TextHeader(style = Typography.headlineLarge, text = stringResource(id = R.string.verify))
            Text(
                text =  stringResource(id = R.string.typeTheVerifyCode),
                style = Typography.labelMedium,
                color = Food_appTheme.myColors.labelColor,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Column(
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            OtpInputField(otpLength = 4, onOtpChanged = {
                otpValue = it
            })
        }
        val titleSmallColor = Food_appTheme.myColors.titleSmallColor
        var buttonColor by remember {
            mutableStateOf(titleSmallColor)
        }
        if (isEnableButton.value) {
            buttonColor = Food_appTheme.myColors.mainColor
        }

        var email_customer by remember {
            mutableStateOf("")
        }

        //từ customer_id truyền vào lấy ra email
        val customerRepository = CustomerRepository(RetrofitClient.customerApiService)
        val customerViewModel: CustomerViewModel = remember {
            CustomerViewModel(customerRepository)
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

        customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
            if (result) {
                customerInfo = customerResponse!!
                email_customer = customerInfo.email
            } else {

            }
        }

//        Toast.makeText(context, email_customer+otpValue, Toast.LENGTH_LONG).show()
//    Log.d("abc",email_customer+otpValue)
        val verifySuccess = stringResource(id = R.string.verifySuccess)
        val verifyNotCorrect = stringResource(id = R.string.verifyNotCorrect)
        val verifyOutOfDate = stringResource(id = R.string.verifyOutOfDate)
        val codeHasBeenResend = stringResource(id = R.string.codeHasBeenResend)
        val codeResendFail = stringResource(id = R.string.codeResendFail)
        ButtonMain(
            width = 248,
            height = 60,
            text =  stringResource(id = R.string.verifycation),
            color = buttonColor,
            style = Typography.labelMedium,
            clickButton = {
                isLoading = true
                authViewModel.isCorrectOTP(username, otpValue) { result, message ->
                    isLoading = false
                    if (result == 0) {
                        Toast.makeText(context, verifySuccess, Toast.LENGTH_LONG).show()
                        openLoginScreen()
                    } else if (result == 1) {
                        Toast.makeText(
                            context,
                            verifyNotCorrect,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            verifyOutOfDate,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        TextWithTwoColor(firstText = stringResource(id = R.string.dontHaveCode),
            secondText = stringResource(id = R.string.resend),
            firstColor = Food_appTheme.myColors.textBlackColor,
            secondColor = Food_appTheme.myColors.mainColor,
            size = 14,
            openSignUpScreen = {
                Toast.makeText(context, codeHasBeenResend, Toast.LENGTH_LONG).show()
                authViewModel.resendOtp(customer_id, email_customer) { result, message ->
                    if (result) {
//                        Toast.makeText(context, "Verify code has been resend", Toast.LENGTH_LONG)
//                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            codeResendFail,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpInputField(
    otpLength: Int,
    onOtpChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var otpValue by remember {
        mutableStateOf("")
    }

    val keyboardState = keyboardAsState(KeyboardStatus.Closed)

    val isShowWarning by remember(keyboardState) {
        derivedStateOf {
            if (keyboardState.value == KeyboardStatus.Closed) {
                if (otpValue.length != otpLength) {
                    return@derivedStateOf true
                }
            }
            false

        }
    }

    val focusRequester = remember {
        FocusRequester()
    }

    BasicTextField(
        modifier = Modifier.focusRequester(focusRequester),
        value = otpValue, onValueChange = { value ->
            if (value.length <= otpLength) {
                otpValue = value
                onOtpChanged(otpValue)
            }
        },
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(otpLength) { index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }

                    val isFocus = index == otpValue.length
                    OtpCell(
                        char = char,
                        isFocus = isFocus,
                        isShowWarning = isShowWarning,
                        modifier = Modifier.weight(1f),
                        borderValue = 12,
                        size = 65
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()

        }
        )
    )
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

}

@Composable
fun OtpCell(
    char: String,
    isFocus: Boolean,
    isShowWarning: Boolean,
    modifier: Modifier = Modifier,
    borderValue: Int,
    size: Int
) {

    val borderColor = if (isShowWarning) {
        Color.Red
    } else if (isFocus) {
        Food_appTheme.myColors.mainColor
    } else {
        Food_appTheme.myColors.unClickColor
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(size.dp)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(borderValue.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = Typography.headlineLarge,
            modifier = Modifier.wrapContentSize(align = Alignment.Center)
        )
    }
}

