package com.example.food_app.view.Account

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_app.R
import com.example.food_app.composegooglesignincleanarchitecture.presentation.sign_in.SignInState
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.api.RetrofitClient.authApiService
//import com.example.food_app.ui.theme.CircleColor
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.MainColor
//import com.example.food_app.ui.theme.PlateHolderColor
//import com.example.food_app.ui.theme.TextBlackColor
import com.example.food_app.ui.theme.Typography
//import com.example.food_app.ui.theme.UnClickColor
import com.example.food_app.view.ButtonLoginOther
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.view.TextHeader
import com.example.food_app.viewmodel.AuthViewModel
import com.example.food_app.viewmodel.CartViewModel
import com.example.food_app.viewmodel.CategoryViewModel
import com.example.food_app.viewmodel.CustomerViewModel
import com.example.food_app.viewmodel.FavoriteViewModel
import com.example.food_app.viewmodel.LocationViewModel
import com.example.food_app.viewmodel.ProductViewModel
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess


@Preview(showBackground = true, name = "Background screen")
@Composable
fun DisplayScreen(

) {
//    BackgroundAccount({ InfoScreen() },{ LoginOtherWay() },true,{})
}

@Composable
fun ShowLoading(
    isLoading: Boolean
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .pointerInput(Unit) { },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
//            val painter = rememberImagePainter(
//                data = R.drawable.loading,
//                imageLoader = imageLoader
//            )
//            Image(
//                painter = painter,
//                contentDescription = null,
//                modifier = Modifier.size(200.dp)
//            )
        }
    }
}

@Composable
fun BackgroundAccount(
    content: @Composable() () -> Unit,
    secondContent: @Composable () -> Unit,
    isShowBackButton: Boolean,
    navigationBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
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
                    iconColor = Food_appTheme.myColors.iconColor,
                    buttonColor = Color.White,
                    borderButton = 10,
                    modifier = Modifier.offset((27).dp, (37).dp),
                    navigationFun = navigationBack
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 26.dp, bottom = 25.dp, end = 26.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
            secondContent()
        }
    }
}

@Composable
fun LoginOtherWay(
    state :SignInState,
    onSignInClick :() ->Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    TextWithLines(text = stringResource(id = R.string.signUpWidth), paddingWidth = 20, Typography.bodySmall)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ButtonLoginOther(
            width = 147,
            height = 57,
            color = Color.White,
            astyle = Typography.labelSmall,
            image = R.drawable.logo_facebook,
            imgSize = 38,
            text = stringResource(id = R.string.twister),
            circleSize = 38,
            circleColor = Color.White,
            textColor = Food_appTheme.myColors.iconColor,
            isImage = true,
            openScreen = {}
        )
        ButtonLoginOther(
            width = 147,
            height = 57,
            color = Color.White,
            astyle = Typography.labelSmall,
            image = R.drawable.logo_google,
            imgSize = 38,
            text = stringResource(id = R.string.google),
            circleSize = 38,
            circleColor = Color.White,
            textColor = Food_appTheme.myColors.iconColor,
            isImage = true,
            openScreen = {
                onSignInClick()
            }
        )
    }
}

@Composable
fun SignInScreen(
    openCollectInfor: (String, String) -> Unit,
    openLoginScreen: () -> Unit
) {
    var isLoading by remember {
        mutableStateOf(false)
    }
    val authRepository = AuthRepository(authApiService)
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(authRepository = authRepository)
    }

    var isSuccess by remember {
        mutableStateOf(false)
    }

    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    var errorColor = Food_appTheme.myColors.mainColor
    var correctColor = Food_appTheme.myColors.plateHolderColor

    var plateHolderPhoneText = stringResource(id = R.string.plateHolerAccount)
    var plateHolderPhone by remember {
        mutableStateOf(plateHolderPhoneText)
    }
    var plateHolderPassText = stringResource(id = R.string.plateHolerPass)
    var plateHolderPassword by remember {
        mutableStateOf(plateHolderPassText)
    }
    var plateHolderRePassText = stringResource(id = R.string.plateHolerRePass)
    var plateHolderRePassword by remember {
        mutableStateOf(plateHolderRePassText)
    }
    var plateHolderColor by remember {
        mutableStateOf(correctColor)
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        TextHeader(style = Typography.headlineLarge.copy(Food_appTheme.myColors.textBlackColor), text = stringResource(
            id = R.string.signUp
        ))
    }
    TextFieldNormal(
        plateHolder = plateHolderPhone,
        textLabel = stringResource(id = R.string.signUpPhoneEmail),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 0,
        sizeHeight = 65,
        text = phoneOrEmail,
        onValueChange = { phoneOrEmail = it }
    )
    TextFieldNormal(
        plateHolder = plateHolderPassword,
        textLabel = stringResource(id = R.string.SignUpPass),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 1,
        sizeHeight = 65,
        text = password,
        onValueChange = { password = it }
    )
    TextFieldNormal(
        plateHolder = plateHolderRePassword,
        textLabel = stringResource(id = R.string.SignUpConfirm),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 1,
        sizeHeight = 65,
        text = rePassword,
        onValueChange = { rePassword = it }
    )
    val signUpEmptyHolderText = stringResource(id = R.string.signUpEmptyHolder)
    val signUpEnterAccountHolderText = stringResource(id = R.string.signUpEnterAccountHolder)
    val signUpLessThanHolderText = stringResource(id = R.string.signUpLessThanHolder)
    val signUpSamePassHolderText = stringResource(id = R.string.signUpSamePassHolder)
    ButtonMain(
        width = 248,
        height = 60,
        text = stringResource(id = R.string.signUpNext),
        color = Food_appTheme.myColors.mainColor,
        style = Typography.labelMedium,
        clickButton = {
            if (!checkEmpty(phoneOrEmail)) {
                plateHolderPhone = signUpEmptyHolderText
                plateHolderColor = errorColor
                phoneOrEmail = ""
            } else if (!checkEmail(phoneOrEmail) && !checkPhone(phoneOrEmail)) {
                plateHolderPhone = signUpEnterAccountHolderText
                phoneOrEmail = ""
                plateHolderColor = errorColor
            } else if (password.length < 6) {
                plateHolderPassword = signUpLessThanHolderText
                password = ""
                plateHolderColor = errorColor
            } else if (rePassword != password) {
                plateHolderRePassword = signUpSamePassHolderText
                rePassword = ""
                plateHolderColor = errorColor
            } else {
                isLoading = true
                authViewModel.isExist(phoneOrEmail) { result, message ->
                    isLoading = false
                    if (!result) {
                        openCollectInfor(phoneOrEmail, password)
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        },
        isShowLoading = isLoading
    )

    TextWithTwoColor(
        firstText = stringResource(id = R.string.alreadyAccount),
        secondText = stringResource(id = R.string.login),
        firstColor = Food_appTheme.myColors.textBlackColor,
        secondColor = Food_appTheme.myColors.mainColor,
        size = 14,
        openSignUpScreen = {
            openLoginScreen()
        })
}


@Composable
fun InfoScreen(
    phoneOrEmail: String,
    password: String,
    openLoginScreen: () -> Unit,
    openOtpScreen: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var errorColor = Food_appTheme.myColors.mainColor
    var correctColor = Food_appTheme.myColors.plateHolderColor


    val signUpAccountHolderText = stringResource(id = R.string.plateHolerAccount)
    val signUpEnterPhoneHolderText = stringResource(id = R.string.signUpEnterPhoneHolder)
    val signUpEnterEmailText = stringResource(id = R.string.signUpEnterEmail)
//    val signUpSamePassHolderText = stringResource(id = R.string.signUpSamePassHolder)

    var plateHolderName by remember {
        mutableStateOf(signUpAccountHolderText)
    }
    var plateHolderPhone by remember {
        mutableStateOf(signUpEnterPhoneHolderText)
    }
    var plateHolderEmail by remember {
        mutableStateOf(signUpEnterEmailText)
    }
    var plateHolderColor by remember {
        mutableStateOf(correctColor)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val authRepository = AuthRepository(authApiService)
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(authRepository = authRepository)
    }

    if (checkPhone(phoneOrEmail)) phoneNumber = phoneOrEmail else email = phoneOrEmail
    Box(modifier = Modifier.fillMaxWidth()) {
        TextHeader(style = Typography.headlineLarge.copy(Food_appTheme.myColors.textBlackColor), text = stringResource(id = R.string.collectInfor))
    }
    TextFieldNormal(
        plateHolder = plateHolderName,
        textLabel = stringResource(id = R.string.fullName),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 0,
        sizeHeight = 65,
        text = fullName,
        onValueChange = { fullName = it }
    )
    TextFieldNormal(
        plateHolder = plateHolderPhone,
        textLabel = stringResource(id = R.string.recoveryPhone),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 2,
        sizeHeight = 65,
        text = phoneNumber,
        onValueChange = { phoneNumber = it },
        isEnable = !checkPhone(phoneOrEmail)
    )
    TextFieldNormal(
        plateHolder = plateHolderEmail,
        textLabel = stringResource(id = R.string.recoveryEmail),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 0,
        sizeHeight = 65,
        text = email,
        onValueChange = { email = it },
        isEnable = !checkEmail(phoneOrEmail)
    )
    val enterThisField = stringResource(id = R.string.signUpEmptyHolder)
    val lessThan = stringResource(id = R.string.lessThan)
    val enterPhoneNumber = stringResource(id = R.string.enterPhoneNumber)
    val enterEmail = stringResource(id = R.string.enterEmail)
    val checkOtp = stringResource(id = R.string.checkOtp)
    val createAccountFail = stringResource(id = R.string.createAccountFail)

    ButtonMain(
        width = 248,
        height = 60,
        text = stringResource(id = R.string.SignUp),
        color = Food_appTheme.myColors.mainColor,
        style = Typography.labelMedium,
        clickButton = {
            if (!checkEmpty(fullName)) {
                plateHolderName = enterThisField
                plateHolderColor = errorColor
                fullName = ""
            }else if (fullName.length > 35) {
                plateHolderName = lessThan
                plateHolderColor = errorColor
                fullName = ""
            }  else if (!checkEmpty(phoneNumber) || !checkPhone(phoneNumber)) {
                plateHolderPhone = enterPhoneNumber
                phoneNumber = ""
                plateHolderColor = errorColor
            } else if (!checkEmpty(email) || !checkEmail(email)) {
                plateHolderPhone = enterEmail
                email = ""
                plateHolderColor = errorColor
            } else {
                /////
//                Log.d("abc",fullName+ email + phoneNumber + phoneOrEmail + password)
                isLoading = true
                authViewModel.signUp(
                    fullName,
                    email,
                    phoneNumber,
                    phoneOrEmail,
                    password
                ) { result, message ->
                    isLoading = false
                    if (result) {
                        Toast.makeText(context, checkOtp, Toast.LENGTH_LONG).show()
                        openOtpScreen(phoneOrEmail)
                    } else {
                        Toast.makeText(
                            context,
                            createAccountFail,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        },
        isShowLoading = isLoading
    )
    TextWithTwoColor(
        firstText = stringResource(id = R.string.alreadyAccount),
        secondText = stringResource(id = R.string.login),
        firstColor = Food_appTheme.myColors.textBlackColor,
        secondColor = Food_appTheme.myColors.mainColor,
        size = 14,
        openSignUpScreen = {
            openLoginScreen()
        }
    )

}

@Composable
fun LoginScreen(
    openSignUpScreen: () -> Unit,
    openResetPassScreen: () -> Unit,
    openHomeScreen: () -> Unit,
    openOtpScreen : (String) ->Unit,
) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("customer_data", Context.MODE_PRIVATE)

//    val customerIdInfo  = sharedPreferences.getInt("customer_id",-1)

    var errorColor = Food_appTheme.myColors.mainColor
    var correctColor = Food_appTheme.myColors.plateHolderColor
    val plateHolderPhoneText = stringResource(id = R.string.plateHolerAccount)
    val plateHolderPassText = stringResource(id = R.string.plateHolerPass)
    var plateHolderPhone by remember {
        mutableStateOf(plateHolderPhoneText)
    }
    var plateHolderPassword by remember {
        mutableStateOf(plateHolderPassText)
    }
    var plateHolderColor by remember {
        mutableStateOf(correctColor)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val authRepository = AuthRepository(authApiService)
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(authRepository = authRepository)
    }
    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    val logInText = stringResource(id = R.string.loginText)
    Box(modifier = Modifier.fillMaxWidth()) {
        TextHeader(
            style = Typography.headlineLarge.copy(Food_appTheme.myColors.textBlackColor),
            text = logInText
        )
    }
    TextFieldNormal(
        plateHolder = plateHolderPhone,
        textLabel = stringResource(id = R.string.signUpPhoneEmail),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 0,
        sizeHeight = 65,
        text = phoneOrEmail,
        onValueChange = { phoneOrEmail = it }
    )
    TextFieldNormal(
        plateHolder = plateHolderPassword,
        textLabel = stringResource(id = R.string.SignUpPass),
        activeColor = Food_appTheme.myColors.mainColor,
        nonactiveColor = Food_appTheme.myColors.unClickColor,
        plateHolderColor = plateHolderColor,
        style = Typography.bodyLarge,
        labelStyle = Typography.labelMedium,
        isPassField = 1,
        sizeHeight = 65,
        text = password,
        onValueChange = { password = it }
    )
    Text(
        text = stringResource(id = R.string.forgotPass),
        style = Typography.bodySmall,
        color = Food_appTheme.myColors.mainColor,
        modifier = Modifier.clickable { openResetPassScreen() })

    val plateHolderPhoneEmptyText = stringResource(id = R.string.signUpEmptyHolder)
    val plateHolderPhoneOrEmailEmptyText = stringResource(id = R.string.signUpEnterAccountHolder)
    ButtonMain(
        width = 248,
        height = 60,
        text = stringResource(id = R.string.loginText).toUpperCase(),
        color = Food_appTheme.myColors.mainColor,
        style = Typography.labelMedium,
        clickButton = {
            if (!checkEmpty(phoneOrEmail)) {
                plateHolderPhone = plateHolderPhoneEmptyText
                plateHolderColor = errorColor
                phoneOrEmail = ""
            } else if (!checkEmail(phoneOrEmail) && !checkPhone(phoneOrEmail)) {
                plateHolderPhone = plateHolderPhoneOrEmailEmptyText
                phoneOrEmail = ""
                plateHolderColor = errorColor
            } else if (!checkEmpty(password)) {
                plateHolderPhone = plateHolderPhoneEmptyText
                plateHolderColor = errorColor
                password = ""
            } else {
                isLoading = true
                authViewModel.login(phoneOrEmail, password) { result, message, id ->
                    isLoading = false
                    if (result == 0) {
                        sharedPreferences.edit().putInt("customer_id", id!!).apply()
                        openHomeScreen()
                    } else if (result == 1){
                        Toast.makeText(
                            context,
                            R.string.accountBlocked,
                            Toast.LENGTH_LONG
                        ).show()
                    }else if (result == 2){
                        Toast.makeText(
                            context,
                            R.string.accountActive,
                            Toast.LENGTH_LONG
                        ).show()
                        openOtpScreen(phoneOrEmail!!)
                    }else if (result == 3){
                        Toast.makeText(
                            context,
                            R.string.accountExist,
                            Toast.LENGTH_LONG
                        ).show()
                    }else if (result == 4){
                        Toast.makeText(
                            context,
                            R.string.accountLoginFail,
                            Toast.LENGTH_LONG
                        ).show()
                    } else{
                        Toast.makeText(
                            context,
                            R.string.accountError,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        },
        isShowLoading = isLoading
    )
    TextWithTwoColor(
        firstText = stringResource(id = R.string.donotHaveAccount),
        secondText = stringResource(id = R.string.signUpNomal),
        firstColor = Food_appTheme.myColors.textBlackColor,
        secondColor = Food_appTheme.myColors.mainColor,
        size = 14,
        openSignUpScreen = openSignUpScreen
    )

}


//.shadow(elevation =  5.dp,RoundedCornerShape(borderButton.dp))


@Composable
fun TextWithTwoColor(
    modifier: Modifier = Modifier,
    firstText: String,
    secondText: String,
    firstColor: Color,
    secondColor: Color,
    size: Int,
    openSignUpScreen: () -> Unit
) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = firstColor,
                fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                fontWeight = FontWeight.Medium,
                fontSize = size.sp,
            )
        ) {  // thay đổi thuộc tính cho từng kí tự hoặc từng cụm kí tự
            append(firstText)
        }

        withStyle(
            style = SpanStyle(
                color = secondColor,
                fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                fontWeight = FontWeight.Medium,
                fontSize = size.sp,
            ),
        ) {
            append(secondText)
        }
    },
        modifier = Modifier.clickable { openSignUpScreen() }

    )
}


@Composable
fun CircleDecor(modifier: Modifier = Modifier, backgroundColor: Color, size: Int) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = CircleShape)
            .size(size.dp)
    ) {
    }
}


@Composable
fun TextWithLines(text: String, paddingWidth: Int, style: TextStyle) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Gray.copy(0.7f),
            modifier = Modifier
                .weight(1f)
                .padding(end = paddingWidth.dp),
            thickness = 1.dp
        )
        Text(text = text, style = style)
        Divider(
            color = Color.Gray.copy(0.7f),
            modifier = Modifier
                .weight(1f)
                .padding(start = paddingWidth.dp),
            thickness = 1.dp
        )
    }
}