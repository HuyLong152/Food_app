package com.example.food_app.view.Account

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.food_app.BASE_URL_API_AVATAR
import com.example.food_app.R
import com.example.food_app.data.Repository.CustomerRepository
import com.example.food_app.data.api.RetrofitClient.customerApiService
import com.example.food_app.data.model.Auth.customerResponse
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.NavigateButton
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.viewmodel.CustomerViewModel
import java.io.File

//@Preview(showBackground = true, name = "Edit Profile")
@Composable
fun EditProfileScreen(
    customer_id :Int,
    navigationBack : () ->Unit,
    customerViewModel : CustomerViewModel
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val plateHolderColor =  Food_appTheme.myColors.plateHolderColor
    val warningColor =  Food_appTheme.myColors.mainColor

    var plateHolderNameText = stringResource(id = R.string.enterFullName)
    var plateHolderPhoneText = stringResource(id = R.string.enterPhone)
    var plateHolderEmailText = stringResource(id = R.string.enterHolerEmail)

    var plateHolderName by remember { mutableStateOf(plateHolderNameText) }
    var plateHolderPhone by remember { mutableStateOf(plateHolderPhoneText) }
    var plateHolderEmail by remember { mutableStateOf(plateHolderEmailText) }
    var plateHolderNameColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderPhoneColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderEmailColor by remember { mutableStateOf(plateHolderColor) }


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
//    val customerRepository = CustomerRepository(customerApiService)
//    val customerViewModel: CustomerViewModel = remember {
//        CustomerViewModel(customerRepository)
//    }
    LaunchedEffect(customer_id){
        if (customerViewModel.customerInfoLiveData.value == null) {
            customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
                if (result) {
                    customerInfo = customerResponse!!
                    fullName = customerResponse.full_name
                    email = customerResponse.email
                    phoneNumber = customerResponse.phone_number
                } else {

                }
            }
        } else {
            customerInfo = customerViewModel.customerInfoLiveData.value!!
            fullName = customerViewModel.customerInfoLiveData.value!!.full_name
            email = customerViewModel.customerInfoLiveData.value!!.email
            phoneNumber = customerViewModel.customerInfoLiveData.value!!.phone_number
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
            NavigateButton(
                size = 38,
                icon = R.drawable.back_icon,
                iconColor = Food_appTheme.myColors.iconColor,
                buttonColor = Color.White,
                borderButton = 10,
                modifier = Modifier.offset((27).dp, (37).dp),
                navigationFun = {
                    navigationBack()
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 27.dp, horizontal = 37.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(60.dp)
                ) {
//                    var imageUri by rememberSaveable{
//                        mutableStateOf("")
//                    }
//                    LaunchedEffect(customerInfo.image_url){
//                        imageUri = customerInfo.image_url
//                    }
//                    var painter = rememberAsyncImagePainter(
//                        (BASE_URL_API_AVATAR+ imageUri).ifEmpty { R.drawable.avatar }
//                    )
//                    var laucher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.GetContent()
//                    ){uri : Uri? ->
//                        uri?.let { imageUri = it.toString() }
//                    }
                    var selectedImageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }
                    LaunchedEffect(customerInfo.image_url){
//                        if(customerInfo.social_link != null){
//                            selectedImageUri = Uri.parse(customerInfo.image_url)
//                        }else{
//                            if(customerInfo.image_url != null){
//                                selectedImageUri = Uri.parse(BASE_URL_API_AVATAR + customerInfo.image_url)
//                            }else{
//                                selectedImageUri = null
//                            }
//                        }
                        if(customerInfo.image_url != null){
                            if(customerInfo.image_url.startsWith("https")){
                                selectedImageUri = Uri.parse(customerInfo.image_url)
                            }else{
                                selectedImageUri = Uri.parse(BASE_URL_API_AVATAR + customerInfo.image_url)
                            }
                        }else{
                            null
                        }

                    }
                    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = {uri -> selectedImageUri = uri}
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        EditAvatar(
                            image = selectedImageUri,
                            imageSize = 90,
                            backgroundColor = Food_appTheme.myColors.backgroundItem,
                            icon = R.drawable.camera,
                            iconSize = 13,
                            backgroundIconSize = 30,
                            iconColor = Food_appTheme.myColors.labelColor,
                            image_url = customerInfo.image_url,
                            modifier = Modifier.clickable {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                            },
                            avatar = R.drawable.avatar,
                            social_link = customerInfo.social_link
                        )
                        Text(text = customerInfo.full_name, style = Typography.titleMedium.copy(color = Food_appTheme.myColors.textBlackColor))
                        Text(text =  stringResource(id = R.string.editFrofile), style = Typography.bodySmall.copy(color = Food_appTheme.myColors.normalTextColor))
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        TextFieldNormal(
                            plateHolder = plateHolderName,
                            textLabel = stringResource(id = R.string.fullName),
                            activeColor = Food_appTheme.myColors.mainColor,
                            nonactiveColor = Food_appTheme.myColors.unClickColor,
                            plateHolderColor = plateHolderNameColor,
                            style = Typography.bodyLarge,
                            labelStyle = Typography.labelMedium,
                            isPassField = 0,
                            sizeHeight = 65,
                            text = fullName,
                            onValueChange = {fullName = it}
                        )
                        TextFieldNormal(
                            plateHolder = plateHolderEmail,
                            textLabel = stringResource(id = R.string.recoveryEmail),
                            activeColor = Food_appTheme.myColors.mainColor,
                            nonactiveColor = Food_appTheme.myColors.unClickColor,
                            plateHolderColor = plateHolderEmailColor,
                            style = Typography.bodyLarge,
                            labelStyle = Typography.labelMedium,
                            isPassField = 0,
                            sizeHeight = 65,
                            text = email,
                            onValueChange ={email = it}
                        )
                        TextFieldNormal(
                            plateHolder = plateHolderPhone,
                            textLabel = stringResource(id = R.string.recoveryPhone),
                            activeColor = Food_appTheme.myColors.mainColor,
                            nonactiveColor = Food_appTheme.myColors.unClickColor,
                            plateHolderColor = plateHolderPhoneColor,
                            style = Typography.bodyLarge,
                            labelStyle = Typography.labelMedium,
                            isPassField = 2,
                            sizeHeight = 65,
                            text = phoneNumber,
                            onValueChange ={phoneNumber = it}
                        )
                        val emptyField = stringResource(id = R.string.enterThisField)
                        val lessThan = stringResource(id = R.string.lessThan)
                        val enterPhone = stringResource(id = R.string.signUpEnterPhoneHolder)
                        val enterEmail = stringResource(id = R.string.signUpEnterEmail)
                        val updateInfor = stringResource(id = R.string.updateInfor)
                        ButtonMain(
                            width = 1000,
                            height = 60,
                            text = stringResource(id = R.string.save),
                            color = Food_appTheme.myColors.mainColor,
                            style = Typography.labelMedium,
                            clickButton = {
                                var canAdd = true
                                if(!checkEmpty(fullName)){
                                    plateHolderName = emptyField
                                    plateHolderNameColor = warningColor
                                    fullName = ""
                                    canAdd = false
                                }else if(fullName.length > 35){
                                    plateHolderName = lessThan
                                    plateHolderNameColor = warningColor
                                    fullName = ""
                                    canAdd = false
                                }
                                if(!checkEmpty(phoneNumber) || !checkPhone(phoneNumber)){
                                    plateHolderPhone = enterPhone
                                    plateHolderPhoneColor = warningColor
                                    phoneNumber = ""
                                    canAdd = false
                                }
                                if(!checkEmpty(email) || !checkEmail(email)){
                                    plateHolderEmail = enterEmail
                                    plateHolderEmailColor = warningColor
                                    email = ""
                                    canAdd = false
                                }
                                if (canAdd) {
                                    customerViewModel.updateInfo(customer_id,fullName,phoneNumber,email) { result, message ->
                                        if (result) {
                                            if(selectedImageUri != null && selectedImageUri != Uri.parse( if(customerInfo.social_link != null) customerInfo.image_url else BASE_URL_API_AVATAR + customerInfo.image_url)){
                                                val contentResolver = context.contentResolver
                                                val inputStream = selectedImageUri?.let { contentResolver.openInputStream(it) }
                                                val file = File(context.cacheDir, "image.jpg")
                                                inputStream?.use { input ->
                                                    file.outputStream().use { output ->
                                                        input.copyTo(output)
                                                    }
                                                }
                                                customerViewModel.updateImage(customer_id,file) { result, message ->
                                                    if(result){
                                                        customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
                                                            if (result) {
                                                                customerViewModel.customerInfoLiveData.value = customerResponse!!
                                                                Toast.makeText(context, updateInfor, Toast.LENGTH_SHORT).show()
                                                                navigationBack()
                                                            } else {

                                                            }
                                                        }
                                                    }
                                                }
                                            }else{
                                                customerViewModel.getCustomer(customer_id) { result, message, customerResponse ->
                                                    if (result) {
                                                        customerViewModel.customerInfoLiveData.value = customerResponse!!
                                                    } else {

                                                    }
                                                }
                                                Toast.makeText(context, updateInfor, Toast.LENGTH_SHORT).show()
                                                navigationBack()
                                            }
                                        }
                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun EditAvatar(
    image: Uri?,
    imageSize: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    icon: Int,
    iconSize: Int,
    backgroundIconSize: Int,
    iconColor: Color,
    image_url:String?,
    avatar:Int,
    social_link: Any?
) {
    Box(
    ) {
        Box(
            modifier = Modifier
                .shadow(10.dp, CircleShape)
                .size((imageSize + 20).dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize.dp)
                    .clip(
                        CircleShape
                    )
            ) {

//                BASE_URL_API_AVATAR + image_url
                if(image != null){
                    AsyncImage(
                        model = image,
                        contentDescription = "",
                        modifier = modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Image(
                        painterResource(id = avatar),
                        contentDescription = "",
                        modifier = modifier.fillMaxSize()
                    )
                }

            }
        }
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .size(backgroundIconSize.dp)
                    .clip(
                        CircleShape
                    )
                    .background(backgroundColor)
                    .align(Alignment.BottomEnd),
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painterResource(id = icon),
                        contentDescription = "",
                        modifier = Modifier.size(iconSize.dp),
                        tint = iconColor
                    )
                }
            }
    }
}

@Composable
fun RectangleShape(
    sizeW: Int,
    sizeH: Int,
    cornerShape: RoundedCornerShape,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(sizeH.dp)
            .width(sizeW.dp)
            .clip(
                cornerShape
            )
            .background(color)
    ) {

    }
}