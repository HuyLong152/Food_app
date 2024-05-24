package com.example.food_app.view.Account

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Location.LocationResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.Main.IconWithText
import com.example.food_app.view.NavigationBar
import com.example.food_app.view.OnOffButton
import com.example.food_app.view.TextFieldNormal
import com.example.food_app.viewmodel.LocationViewModel
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlin.math.max

//@Preview(showBackground = true, name = "Add Address")
@SuppressLint("ResourceType")
@Composable
fun AddAddressScreen(
    customer_id: Int,
    location_id: Int,
    navigationBack: () -> Unit,
    locationViewModel: LocationViewModel
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("Việt Nam") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(0) }

    val plateHolderColor =  Food_appTheme.myColors.plateHolderColor
    val warningColor =  Food_appTheme.myColors.mainColor

    val plateHolderNameText = stringResource(id = R.string.enterFullName)
    val plateHolderPhoneText = stringResource(id = R.string.enterPhone)
    val plateHolderCityText = stringResource(id = R.string.enterCity)
    val plateHolderProvinceText = stringResource(id = R.string.enterProvince)
    val plateHolderStreetText = stringResource(id = R.string.enterStreet)

    var plateHolderName by remember { mutableStateOf(plateHolderNameText) }
    var plateHolderPhone by remember { mutableStateOf(plateHolderPhoneText) }
    var plateHolderCity by remember { mutableStateOf(plateHolderCityText) }
    var plateHolderProvince by remember { mutableStateOf(plateHolderProvinceText) }
    var plateHolderStreet by remember { mutableStateOf(plateHolderStreetText) }


    var plateHolderNameColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderPhoneColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderCityColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderProvinceColor by remember { mutableStateOf(plateHolderColor) }
    var plateHolderStreetColor by remember { mutableStateOf(plateHolderColor) }


//    val locationRepository = LocationReponsitory(RetrofitClient.locationApiService)
//    val locationViewModel: LocationViewModel = remember {
//        LocationViewModel(locationRepository = locationRepository)
//    }


    if (location_id != -1) {
        locationViewModel.getLocationById(location_id) { result, location ->
            if (result) {
                fullName = location!!.name
                phoneNumber = location!!.phone_number
                isDefault = location.is_default
                if(location.address != "Việt Nam"){
                    val parts = location.address.split(",\\s*".toRegex())
                    state = parts.last()
                    city = parts[parts.size - 2]
                    province = parts[parts.size - 3]
                    street = parts.subList(0, parts.size - 3).joinToString(", ")
                }
            }
        }
    }
    val defaultAddressText = stringResource(id = R.string.defaultAddressToast)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 27.dp, horizontal = 36.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            NavigationBar(
                isShowAvt = false,
                text = stringResource(id = R.string.addAddress),
                style = Typography.bodyLarge,
                size = 38,
                avtImg = R.drawable.avatar,
                image_url = "",
                navigationBack = navigationBack
            )
        }
        if(location_id != -1){
            item {
                var changeDefault by remember {
                    mutableStateOf(false)
                }
                changeDefault = isDefault == 1
                OnOffButton(
                    isActive = changeDefault,
                    sizeW = 50,
                    sizeH = 30,
                    sizeCircle = 20,
                    backgroundActive = Food_appTheme.myColors.plateHolderColor,
                    background = Food_appTheme.myColors.textBlackColor,
                    circleActive = Food_appTheme.myColors.mainColor,
                    circleNonactive = Food_appTheme.myColors.textBlackColor,
                    changeValue = {isActive ->
                        if(!isActive){
                            isDefault = 1
                            locationViewModel.updateDefault(location_id,customer_id) { result, message ->
                                if (result) {
                                    locationViewModel.getAllAddress(customer_id) { result, location ->
                                        if (result) {
                                            locationViewModel.listAllLocationLiveData.value = location!!
                                            Log.d("abc",location.toString())

                                        }
                                    }
//                                    Toast.makeText(context, "Add new location successful", Toast.LENGTH_SHORT).show()
//                                    navigationBack()
                                }
                            }
                        }else
                            Toast.makeText(context, defaultAddressText, Toast.LENGTH_SHORT).show()
                                  },
                    modifier = Modifier.fillMaxWidth()
                )
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    var changeDefault by remember {
//                        mutableStateOf(false)
//                    }
//                    changeDefault = isDefault == 1
//                    Box(
//                        modifier = Modifier
//                            .clip(
//                                CircleShape
//                            )
//                            .border(if(!changeDefault) BorderStroke(2.dp,Food_appTheme.myColors.textBlackColor) else BorderStroke(2.dp,Food_appTheme.myColors.plateHolderColor) , shape = CircleShape)
//                            .width(50.dp)
//                            .height(30.dp)
//                            .background(if(!changeDefault) Color.Transparent else Food_appTheme.myColors.plateHolderColor)
//                            .padding(5.dp)
//
//
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(20.dp)
//                                .clip(CircleShape)
//                                .align(if(!changeDefault) Alignment.CenterStart else Alignment.CenterEnd).clickable { if(isDefault == 0) isDefault = 1 }
//                                .background(if(!changeDefault) Food_appTheme.myColors.textBlackColor else Food_appTheme.myColors.mainColor)
//                        ){
//
//                        }
//                    }
//                }
            }
        }
        item {
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
                onValueChange = { fullName = it }
            )
        }
        item {
            TextFieldNormal(
                plateHolder = plateHolderPhone,
                textLabel = stringResource(id = R.string.phoneNumber),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = plateHolderPhoneColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 2,
                sizeHeight = 65,
                text = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
        }
        item {
            TextFieldNormal(
                plateHolder =  stringResource(id = R.string.enterState),
                textLabel = stringResource(id = R.string.state),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = Food_appTheme.myColors.plateHolderColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 0,
                sizeHeight = 65,
                text = state,
                onValueChange = { state = it },
                isEnable = false
            )
        }


        item {
            var expanded by remember {
                mutableStateOf(false)
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            expanded = false
                        }
                    )
            ) {

                TextFieldNormal(
                    plateHolder = plateHolderCity,
                    textLabel = stringResource(id = R.string.cityDistrict),
                    activeColor = Food_appTheme.myColors.mainColor,
                    nonactiveColor = Food_appTheme.myColors.unClickColor,
                    plateHolderColor = plateHolderCityColor,
                    style = Typography.bodyLarge,
                    labelStyle = Typography.labelMedium,
                    isPassField = 0,
                    sizeHeight = 65,
                    text = city,
                    onValueChange = {
                        city = it
                        expanded = true
                    },
                )
                    locationViewModel.fillDistrictList(city);

                AnimatedVisibility(visible = expanded) {
                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                    ) {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 150.dp)
                        ) {
                            if (city.isNotEmpty()) {
                                items(
                                    locationViewModel.provinces.filter {
                                        it.lowercase()
                                            .contains(city.lowercase()) || it.lowercase()
                                            .contains("Hà Nội")
                                    }
                                        .sorted()
                                ) {
                                    CategoryItems(
                                        title = it,
                                        onSelect = { title ->
                                            city = title;
                                            locationViewModel.fillDistrictList(it);
                                            expanded = false
                                        },
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.textBlackColor
                                    )

                                }
                            } else {
                                items(
                                    locationViewModel.provinces.sorted()
                                ) {
                                    CategoryItems(
                                        title = it,
                                        onSelect = { title -> city = title; expanded = false },
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.textBlackColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        item {
            var expanded by remember {
                mutableStateOf(false)
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            expanded = false
                        }
                    )
            ) {

                TextFieldNormal(
                    plateHolder = plateHolderProvince,
                    textLabel =  stringResource(id = R.string.province),
                    activeColor = Food_appTheme.myColors.mainColor,
                    nonactiveColor = Food_appTheme.myColors.unClickColor,
                    plateHolderColor = plateHolderProvinceColor,
                    style = Typography.bodyLarge,
                    labelStyle = Typography.labelMedium,
                    isPassField = 0,
                    sizeHeight = 65,
                    text = province,
                    onValueChange = { province = it; expanded = true }
                )
//                val listDistrict by remember {
//                    mutableStateOf(locationViewModel.districtsMap[city])
//                }

                AnimatedVisibility(visible = expanded) {
//                val listDistrict1 by remember {
//                    mutableStateOf(listDistrict)
//                }
//
//                Log.d("abc",listDistrict.toString())
                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                    ) {

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 150.dp)
                        ) {
                            if (province.isNotEmpty()) {
                                items(
                                    locationViewModel.districtsFilter!!.filter {
                                        it.lowercase()
                                            .contains(province.lowercase()) || it.lowercase()
                                            .contains("Other")
                                    }
                                        .sorted()
                                ) {
                                    CategoryItems(
                                        title = it,
                                        onSelect = { title -> province = title; expanded = false },
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.textBlackColor
                                    )

                                }
                            } else {
                                items(
                                    locationViewModel.districtsFilter!!.sorted()
                                ) {
                                    CategoryItems(
                                        title = it,
                                        onSelect = { title -> province = title; expanded = false },
                                        style = Typography.bodyLarge,
                                        textColor = Food_appTheme.myColors.textBlackColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            TextFieldNormal(
                plateHolder = plateHolderStreet,
                textLabel = stringResource(id = R.string.fullStreet),
                activeColor = Food_appTheme.myColors.mainColor,
                nonactiveColor = Food_appTheme.myColors.unClickColor,
                plateHolderColor = plateHolderStreetColor,
                style = Typography.bodyLarge,
                labelStyle = Typography.labelMedium,
                isPassField = 0,
                sizeHeight = 65,
                text = street,
                onValueChange = { street = it }
            )
        }
        item {
            val enterField = stringResource(id = R.string.enterThisField)
            val lessThan = stringResource(id = R.string.lessThan)
            val enterPhone = stringResource(id = R.string.enterPhonenumber)
            val addLocation = stringResource(id = R.string.addNewLocation)
            val updateLocation = stringResource(id = R.string.updateLocation)

            ButtonMain(
                width = 248,
                height = 60,
                text = stringResource(id = R.string.save),
                color = Food_appTheme.myColors.mainColor,
                style = Typography.labelMedium,
                clickButton = {
                        var canAdd = true
                        if(!checkEmpty(fullName)){
                            plateHolderName = enterField
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
                        if(!checkEmpty(city)){
                            plateHolderCity = enterField
                            plateHolderCityColor = warningColor
                            city = ""
                            canAdd = false
                        }
                        if(!checkEmpty(province)){
                            plateHolderProvince = enterField
                            plateHolderProvinceColor = warningColor
                            province = ""
                            canAdd = false
                        }
                        if(!checkEmpty(street)){
                            plateHolderStreet = enterField
                            plateHolderStreetColor = warningColor
                            street = ""
                            canAdd = false
                        }
                    if (location_id == -1 && canAdd) {
                        locationViewModel.addLocation(customer_id,fullName,"$street,$province,$city,$state",phoneNumber) { result, message ->
                            if (result) {
                                locationViewModel.getAllAddress(customer_id) { result, location ->
                                    if (result) {
                                        locationViewModel.listAllLocationLiveData.value = location!!
                                    }
                                }
                                Toast.makeText(context, addLocation, Toast.LENGTH_SHORT).show()
                                navigationBack()
                            }
                        }
                    }else if (location_id != -1 && canAdd){
                        locationViewModel.updateLocation(location_id,fullName,"$street,$province,$city,$state",phoneNumber) { result, message ->
                            if (result) {
                                locationViewModel.getAllAddress(customer_id) { result, location ->
                                    if (result) {
                                        locationViewModel.listAllLocationLiveData.value = location!!
                                    }
                                }
                                Toast.makeText(context, updateLocation, Toast.LENGTH_SHORT).show()
                                navigationBack()
                            }
                        }
                    }
                })

        }
    }
}

@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle,
    textColor: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, style = style.copy(color = textColor))
    }
}
