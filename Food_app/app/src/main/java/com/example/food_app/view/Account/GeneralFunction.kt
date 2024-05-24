package com.example.food_app.view.Account

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringResource
import com.example.food_app.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun formatPrice(price: Int): String {
    val formattedValue = StringBuilder()
    val valueString = price.toString().reversed()
    for ((index, char) in valueString.withIndex()) {
        if (index > 0 && index % 3 == 0) {
            formattedValue.append('.')
        }
        formattedValue.append(char)
    }
    return formattedValue.reverse().toString()
}

fun checkEmpty(
    text: String,
): Boolean {

    if (text == "") {
        return false // không có giá trị
    } else
        return true // đúng
}

fun checkEmail(
    text: String,
): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
    if (emailRegex.matches(text)) {
        return true
    }
    return false
}

fun checkPhone(
    text: String,
): Boolean {
    val phoneRegex = Regex("\\d{10,12}")
    if (phoneRegex.matches(text)) {
        return true
    }
    return false
}
@RequiresApi(Build.VERSION_CODES.O)
fun convertDateTimeToRelative(dateTimeString: String): String {
//    val inputFormat = if (dateTimeString.contains("T")) {
//        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
//    } else {
//        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
//    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    val today = LocalDateTime.now()
    val daysDifference = ChronoUnit.DAYS.between(dateTime.toLocalDate(), today.toLocalDate())

    return when {
        daysDifference == 0L ->  "Today"
                daysDifference == 1L -> "Yesterday"
        daysDifference > 1L -> "$daysDifference "
        else -> "ago"
    }
}
