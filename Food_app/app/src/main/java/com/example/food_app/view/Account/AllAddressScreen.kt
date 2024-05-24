package com.example.food_app.view.Account

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.api.RetrofitClient
import com.example.food_app.data.model.Location.LocationResponseItem
import com.example.food_app.ui.theme.Food_appTheme
import com.example.food_app.ui.theme.Typography
import com.example.food_app.view.ButtonMain
import com.example.food_app.view.ConfirmDialog
import com.example.food_app.view.NavigationBar
import com.example.food_app.viewmodel.LocationViewModel


@Composable
fun AllAddressScreen(
    customer_id: Int,
    navigationBack: () -> Unit,
    reloadScreen:() ->Unit,
    openDetailScreen:(Int) ->Unit,
    locationViewModel: LocationViewModel
) {
//    val locationRepository = LocationReponsitory(RetrofitClient.locationApiService)
//    val locationViewModel: LocationViewModel = remember {
//        LocationViewModel(locationRepository = locationRepository)
//    }
    var listLocation by remember {
        mutableStateOf(listOf<LocationResponseItem>())
    }
    if (locationViewModel.listAllLocationLiveData.value == null) {
        locationViewModel.getAllAddress(customer_id) { result, location ->
            if (result) {
                listLocation = location!!
            }
        }
    } else {
        listLocation = locationViewModel.listAllLocationLiveData.value!!
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var locationClick by remember {
        mutableStateOf(-1)
    }
    if(showDialog){
        ConfirmDialog(
            onConfirm = {
                locationViewModel.deleteLocation(locationClick) { result, location ->
                    if (result) {
                        locationViewModel.getAllAddress(customer_id) { result, location ->
                            if (result) {
                                locationViewModel.listAllLocationLiveData.value = location!!
                            }
                        }
                        reloadScreen()
                    }
                }
            },
            onCancel = { showDialog = !showDialog },
            title = stringResource(id = R.string.delete),
            text = stringResource(id = R.string.confirmDelete),
            confirmText = stringResource(id = R.string.yes),
            dismissText = stringResource(id = R.string.no)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 27.dp, horizontal = 36.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationBar(
            isShowAvt = false,
            text = stringResource(id = R.string.myAddress),
            style = Typography.bodyLarge,
            size = 38,
            avtImg = R.drawable.avatar,
            image_url = "",
            navigationBack = navigationBack
        )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ){
                itemsIndexed(listLocation){ index , location ->
                    AnAddress(
                        icon = R.drawable.home_address,
                        text = location.name,
                        address = location.address,
                        background = Food_appTheme.myColors.backgroundAddress,
                        imageSize = 19,
                        iconSize = 20,
                        iconColor = Food_appTheme.myColors.mainColor,
                        textColor = Food_appTheme.myColors.iconColor,
                        addressColor = Food_appTheme.myColors.textReviewColor,
                        sizeH = 110,
                        textStyle = Typography.headlineSmall,
                        addressStyle = Typography.headlineSmall,
                        corner = 15,
                        iconEdit = R.drawable.edit,
                        iconDelete = R.drawable.delete,
                        backgroundIcon = if(location.is_default == 0) Food_appTheme.myColors.backgroundItem else Food_appTheme.myColors.mainColor,
                        boxSize = 48,
                        imageColor = if(location.is_default == 0) Food_appTheme.myColors.addressColor else Color.White,
                        openDetailScreen = openDetailScreen,
                        idLocation = location.id,
                        isDefault = location.is_default,
                        changeDialogValue = {showDialog = it; locationClick = location.id}
                    )
                }
            }
        ButtonMain(width = 248, height = 60, text = stringResource(id = R.string.addNewAddr), color = Food_appTheme.myColors.mainColor, style = Typography.labelMedium,clickButton = {openDetailScreen(-1)})
    }
}
@Composable
fun AnAddress(
    icon:Int,
    text:String,
    address :String,
    background: Color,
    modifier: Modifier = Modifier,
    imageSize:Int,
    iconSize: Int,
    iconColor: Color,
    textColor: Color,
    addressColor : Color,
    sizeH:Int,
    textStyle: TextStyle,
    addressStyle: TextStyle,
    corner :Int,
    iconEdit :Int,
    iconDelete :Int,
    backgroundIcon: Color,
    boxSize :Int,
    imageColor :Color,
    openDetailScreen: (Int) -> Unit,
    idLocation :Int,
    isDefault : Int,
    changeDialogValue : (Boolean) -> Unit
){
    val context = LocalContext.current

    Row(
        modifier = modifier
            .height(sizeH.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(corner.dp))
            .background(background)
            .padding(15.dp)
            .clickable { openDetailScreen(idLocation) },
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(boxSize.dp)
                .clip(CircleShape)
                .background(backgroundIcon),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .size(imageSize.dp),
                tint = imageColor
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = text, style = textStyle.copy(textColor), overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.weight(1f))
                val failDeleteLocation = stringResource(id = R.string.failDeleteLocation)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Icon(painterResource(id = iconEdit), contentDescription = "", modifier = Modifier
                        .size(iconSize.dp)
                        .clickable { openDetailScreen(idLocation) }, tint = iconColor )
                    Icon(painterResource(id = iconDelete), contentDescription = "", modifier = Modifier
                        .size(iconSize.dp)
                        .clickable {
                            if (isDefault == 0) {
                                changeDialogValue(true)
                            } else {
                                val toast = Toast.makeText(
                                    context,
                                    failDeleteLocation,
                                    Toast.LENGTH_SHORT
                                )
                                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                                toast.show()
                            }
                        }, tint = iconColor)
                }
            }
            Text(text = address, style = addressStyle.copy(addressColor), overflow = TextOverflow.Ellipsis)

        }

    }
}
