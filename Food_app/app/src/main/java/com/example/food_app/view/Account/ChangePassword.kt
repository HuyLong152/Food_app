package com.example.food_app.view.Account

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.AuthRepository
import com.example.food_app.data.api.RetrofitClient.authApiService
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.viewmodel.AuthViewModel

@Composable
fun ChangePasswordScreen(
    customer_id : Int,
    navigationBack :() -> Unit
){
    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    var plateHolderCurrentPassText = stringResource(id = R.string.plateHolderCurrentPassText)
    var plateHolderNewPassText = stringResource(id = R.string.plateHolderNewPassText)
    var plateHolderConfirmPassText = stringResource(id = R.string.plateHolderConfirmPassText)

    val plateHolderColor =  Food_appTheme.myColors.plateHolderColor
    val warningColor =  Food_appTheme.myColors.mainColor
    var plateHolderCurrentPass by remember { mutableStateOf(plateHolderCurrentPassText) }
    var plateHolderNewPass by remember { mutableStateOf(plateHolderNewPassText) }
    var plateHolderConfirmPass by remember { mutableStateOf(plateHolderConfirmPassText) }
    var plateHolderCurrentPassColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderNewPassColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderConfirmPassColor by remember { mutableStateOf(plateHolderColor) }

    val context = LocalContext.current

    val authRepository = AuthRepository(authApiService)
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(authRepository)
    }

    Box {
        RectangleShape(
            sizeW = 174,
            sizeH = 285,
            cornerShape = RoundedCornerShape(topStart = 180.dp, bottomEnd = 180.dp),
            color = Food_appTheme.myColors.mainColor,
            modifier = Modifier.offset((204).dp, (-121).dp)
        )
        CircleDecor(
            modifier = Modifier.offset((102).dp, (-31).dp),
            Food_appTheme.myColors.circleColor,
            96
        )
        RectangleShape(
            sizeW = 124,
            sizeH = 205,
            cornerShape = RoundedCornerShape(topEnd = 180.dp, bottomStart = 180.dp),
            color = Food_appTheme.myColors.starColor,
            modifier = Modifier.offset((0).dp, (-86).dp)
        )
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
            TextFieldNormal(
                plateHolder = plateHolderCurrentPass,
                textLabel =  stringResource(id = R.string.currentPass),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = plateHolderCurrentPassColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 1,
                sizeHeight = 65,
                text = currentPass,
                onValueChange = {currentPass = it},
                isEnable = true
            )
            TextFieldNormal(
                plateHolder = plateHolderNewPass,
                textLabel = stringResource(id = R.string.newPass),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = plateHolderNewPassColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 1,
                sizeHeight = 65,
                text = newPass,
                onValueChange = {newPass = it},
                isEnable = true,
            )
            TextFieldNormal(
                plateHolder = plateHolderConfirmPass,
                textLabel =stringResource(id = R.string.confirmPass),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = plateHolderConfirmPassColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 1,
                sizeHeight = 65,
                text = confirmPass,
                onValueChange = {confirmPass = it},
                isEnable = true
            )
            val emptyField = stringResource(id = R.string.enterThisField)
            val lessThan = stringResource(id = R.string.signUpLessThanHolder)
            val confirmNotSame = stringResource(id = R.string.confirmNotSame)
            val confirmNotRepeat = stringResource(id = R.string.confirmNotRepeat)
            val changePassSuccess = stringResource(id = R.string.changePassSuccess)
            val anError = stringResource(id = R.string.anError)
            ButtonMain(
                width = 1000,
                height = 60,
                text =  stringResource(id = R.string.change),
                color = Food_appTheme.myColors.mainColor,
                style = Typography.labelMedium,
                clickButton = {
                    var canAdd = true
                    if(!checkEmpty(currentPass)){
                        plateHolderCurrentPass = emptyField
                        plateHolderCurrentPassColor = warningColor
                        currentPass = ""
                        canAdd = false
                    }
                    if(!checkEmpty(newPass)){
                        plateHolderNewPass = emptyField
                        plateHolderNewPassColor = warningColor
                        newPass = ""
                        canAdd = false
                    }else if(newPass.length < 6){
                        plateHolderNewPass = lessThan
                        plateHolderNewPassColor = warningColor
                        newPass = ""
                        canAdd = false
                    }
                    if(!checkEmpty(confirmPass)){
                        plateHolderConfirmPass = emptyField
                        plateHolderConfirmPassColor = warningColor
                        confirmPass = ""
                        canAdd = false
                    }else if(confirmPass != newPass){
                            plateHolderConfirmPass = confirmNotSame
                            plateHolderConfirmPassColor = warningColor
                            confirmPass = ""
                            canAdd = false
                    }else if(currentPass == newPass){
                        plateHolderConfirmPass = confirmNotRepeat
                        plateHolderConfirmPassColor = warningColor
                        confirmPass = ""
                        canAdd = false
                    }
                    if(canAdd){
                        authViewModel.changePassword(customer_id,currentPass,confirmPass) { result, category ->
                            if (result) {
                                val toast = Toast.makeText(
                                    context,
                                    changePassSuccess,
                                    Toast.LENGTH_SHORT
                                )
                                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                                toast.show()
                                navigationBack()
                            } else {
                                val toast = Toast.makeText(
                                    context,
                                    anError,
                                    Toast.LENGTH_SHORT
                                )
                                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, -15)
                                toast.show()
                            }
                        }
                    }
                }
            )
        }
    }

}