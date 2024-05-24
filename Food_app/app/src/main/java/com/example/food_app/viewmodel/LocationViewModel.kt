package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Repository.LocationReponsitory
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Location.LocationResponse
import com.example.food_app.data.model.Location.LocationResponseItem
import com.example.food_app.data.model.Product.ProductResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class LocationViewModel(
    private val locationRepository : LocationReponsitory
):ViewModel() {
    var districtsFilter = listOf<String>()
    fun fillDistrictList (city : String){
        if(city != ""){
            if(districtsMap[city.lowercase(Locale.ROOT)] != null){
                districtsFilter = districtsMap[city.lowercase(Locale.ROOT)]!!
            }
        }
    }
    val provinces = listOf(
        "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Bình Dương", "Đồng Nai", "Khánh Hòa",
        "Quảng Nam", "Hải Dương", "Thừa Thiên Huế", "Kiên Giang", "Lâm Đồng", "Bình Thuận", "Thanh Hóa",
        "Nghệ An", "Bình Định", "Vĩnh Phúc", "Đắk Lắk", "Đắk Nông", "Lạng Sơn", "Thái Nguyên", "Lào Cai",
        "Nam Định", "Quảng Ninh", "Bắc Giang", "Vĩnh Long", "Bắc Ninh", "Ninh Bình", "Phú Thọ", "Ninh Thuận",
        "Kon Tum", "Gia Lai", "Sóc Trăng", "Trà Vinh", "Bạc Liêu", "Long An", "Bà Rịa - Vũng Tàu",
        "An Giang", "Tiền Giang", "Tây Ninh", "Hậu Giang", "Bắc Kạn", "Yên Bái", "Tuyên Quang",
        "Cà Mau", "Hà Giang", "Bến Tre", "Điện Biên", "Lai Châu", "Quảng Bình",
        "Quảng Trị", "Sơn La", "Hà Nam", "Bắc Cạn", "Hòa Bình"
    )
    val districtsMap = mapOf(
        "hà nội" to listOf(
            "Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng", "Quận Đống Đa",
            "Quận Tây Hồ", "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai",
            "Quận Long Biên", "Quận Hà Đông", "Quận Nam Từ Liêm","Quận Bắc Từ Liêm"
        ),
        "hồ chí minh" to listOf(
            "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7",
            "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân",
            "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình",
            "Quận Tân Phú", "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ",
            "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"
        ),
        "đà nẵng" to listOf(
            "Quận Hải Châu", "Quận Thanh Khê", "Quận Sơn Trà", "Quận Ngũ Hành Sơn",
            "Quận Liên Chiểu", "Quận Cẩm Lệ", "Huyện Hòa Vang"
        ),
        "hải phòng" to listOf(
            "Quận Hồng Bàng", "Quận Ngô Quyền", "Quận Lê Chân", "Quận Hải An",
            "Quận Kiến An", "Quận Đồ Sơn", "Quận Dương Kinh", "Huyện Thuỷ Nguyên",
            "Huyện An Dương", "Huyện An Lão", "Huyện Kiến Thuỵ", "Huyện Tiên Lãng",
            "Huyện Vĩnh Bảo", "Huyện Cát Hải", "Huyện Bạch Long Vĩ"
        ),
        "cần thơ" to listOf(
            "Quận Ninh Kiều", "Quận Bình Thủy", "Quận Cái Răng", "Quận Ô Môn",
            "Quận Thốt Nốt", "Huyện Vĩnh Thạnh", "Huyện Cờ Đỏ", "Huyện Phong Điền",
            "Huyện Thới Lai"
        ),
        "bình dương" to listOf(
            "Thành phố Thủ Dầu Một", "Thành phố Dĩ An", "Thị xã Bến Cát",
            "Thị xã Tân Uyên", "Thị xã Thuận An", "Huyện Bắc Tân Uyên",
            "Huyện Dầu Tiếng", "Huyện Phú Giáo", "Huyện Bàu Bàng"
        ),
        "đồng nai" to listOf(
            "Thành phố Biên Hòa", "Thành phố Long Khánh", "Thị xã Trảng Bom",
            "Thị xã Thống Nhất", "Huyện Vĩnh Cửu", "Huyện Định Quán",
            "Huyện Tân Phú", "Huyện Long Thành", "Huyện Xuân Lộc",
            "Huyện Nhơn Trạch", "Huyện Cẩm Mỹ", "Huyện Long Khánh"
        ),
        "khánh hòa" to listOf(
            "Thành phố Nha Trang", "Thành phố Cam Ranh", "Huyện Diên Khánh",
            "Huyện Vạn Ninh", "Huyện Cam Lâm", "Huyện Khánh Sơn",
            "Huyện Khánh Vĩnh", "Huyện Trường Sa"
        ),
        "quảng nam" to listOf(
            "Thành phố Tam Kỳ",
            "Thành phố Hội An",
            "Huyện Duy Xuyên",
            "Huyện Đại Lộc",
            "Huyện Điện Bàn",
            "Huyện Quế Sơn",
            "Huyện Nam Giang",
            "Huyện Phước Sơn",
            "Huyện Hiệp Đức",
            "Huyện Thăng Bình",
            "Huyện Tiên Phước",
            "Huyện Bắc Trà My",
            "Huyện Nam Trà My",
            "Huyện Núi Thành",
            "Huyện Phú Ninh"
        ),
        "hải dương" to listOf(
            "Thành phố Hải Dương",
            "Thành phố Chí Linh",
            "Thị xã Kinh Môn",
            "Huyện Cẩm Giàng",
            "Huyện Nam Sách",
            "Huyện Kim Thành",
            "Huyện Thanh Hà",
            "Huyện Thanh Miện",
            "Huyện Gia Lộc",
            "Huyện Tứ Kỳ",
            "Huyện Ninh Giang"
        ),
        "thừa thiên huế" to listOf(
            "Thành phố Huế",
            "Huyện Phong Điền",
            "Huyện Quảng Điền",
            "Huyện Hương Trà",
            "Huyện Hương Thủy",
            "Huyện Phú Lộc",
            "Huyện Nam Đông",
            "Thị xã Hương Thủy",
            "Thị xã Hương Trà"
        ),
        "kiên giang" to listOf(
            "Thành phố Rạch Giá",
            "Thành phố Hà Tiên",
            "Thành phố Long Xuyên",
            "Thị xã Hòn Đất",
            "Thị xã Kiên Lương",
            "Thị xã An Minh",
            "Huyện Kiên Hải",
            "Huyện Kiên Lương",
            "Huyện Phú Quốc",
            "Huyện Giang Thành",
            "Huyện U Minh Thượng",
            "Huyện Giồng Riềng",
            "Huyện Gò Quao",
            "Huyện Tân Hiệp",
            "Huyện Châu Thành",
            "Huyện An Biên",
            "Huyện An Minh",
            "Huyện Vĩnh Thuận"
        ),
        "lâm đồng" to listOf(
            "Thành phố Đà Lạt",
            "Thị xã Bảo Lộc",
            "Huyện Đức Trọng",
            "Huyện Di Linh",
            "Huyện Lâm Hà",
            "Huyện Đơn Dương",
            "Huyện Đạ Huoai",
            "Huyện Đạ Tẻh",
            "Huyện Cát Tiên",
            "Huyện Bảo Lâm",
            "Huyện Lạc Dương"
        ),
        "bình thuận" to listOf(
            "Thành phố Phan Thiết",
            "Thị xã La Gi",
            "Huyện Hàm Thuận Bắc",
            "Huyện Hàm Thuận Nam",
            "Huyện Tuy Phong",
            "Huyện Bắc Bình",
            "Huyện Hàm Tân",
            "Huyện Đức Linh",
            "Huyện Tánh Linh",
            "Huyện Đức Trọng"
        ),
        "thanh hóa" to listOf(
            "Thành phố Thanh Hóa",
            "Thành phố Sầm Sơn",
            "Thị xã Bỉm Sơn",
            "Thị xã Nghi Sơn",
            "Huyện Quảng Xương",
            "Huyện Hoằng Hóa",
            "Huyện Hậu Lộc",
            "Huyện Hậu Lộc",
            "Huyện Hà Trung",
            "Huyện Hà Trung",
            "Huyện Vĩnh Lộc",
            "Huyện Thiệu Hóa",
            "Huyện Triệu Sơn",
            "Huyện Yên Định",
            "Huyện Thọ Xuân",
            "Huyện Thường Xuân",
            "Huyện Thạch Thành",
            "Huyện Quảng Xương",
            "Huyện Nông Cống",
            "Huyện Nga Sơn",
            "Huyện Như Thanh",
            "Huyện Như Xuân",
            "Huyện Quan Hóa",
            "Huyện Nông Cống",
            "Huyện Nông Sơn",
            "Huyện Ngọc Lặc",
            "Huyện Nga Sơn",
            "Huyện Thạch Thành"
        ),
        "nghệ an" to listOf(
            "Thành phố Vinh",
            "Thị xã Cửa Lò",
            "Thị xã Thái Hòa",
            "Huyện Quế Phong",
            "Huyện Quỳ Châu",
            "Huyện Kỳ Sơn",
            "Huyện Tương Dương",
            "Huyện Nghĩa Đàn",
            "Huyện Quỳ Hợp",
            "Huyện Quỳnh Lưu",
            "Huyện Con Cuông",
            "Huyện Tân Kỳ",
            "Huyện Anh Sơn",
            "Huyện Diễn Châu",
            "Huyện Yên Thành",
            "Huyện Đô Lương",
            "Huyện Thanh Chương",
            "Huyện Nam Đàn",
            "Huyện Hưng Nguyên"
        ),
        "bình định" to listOf(
            "Thành phố Quy Nhơn",
            "Thị xã An Nhơn",
            "Huyện Vân Canh",
            "Huyện Hoài Ân",
            "Huyện Hoài Nhơn",
            "Huyện Phù Mỹ",
            "Huyện Tây Sơn",
            "Huyện Tuy Phước",
            "Huyện Vĩnh Thạnh"
        ),
        "vĩnh phúc" to listOf(
            "Thành phố Vĩnh Yên",
            "Thị xã Phúc Yên",
            "Huyện Bình Xuyên",
            "Huyện Lập Thạch",
            "Huyện Sông Lô",
            "Huyện Tam Dương",
            "Huyện Tam Đảo",
            "Huyện Yên Lạc"
        ),
        "đắk lắk" to listOf(
            "Thành phố Buôn Ma Thuột",
            "Thị xã Buôn Hồ",
            "Huyện Buôn Đôn",
            "Huyện Buôn Hồ",
            "Huyện Buôn Đức",
            "Huyện Cư Kuin",
            "Huyện Ea Kar",
            "Huyện Ea Súp",
            "Huyện Krông Ana",
            "Huyện Krông Bông",
            "Huyện Krông Búk",
            "Huyện Krông Năng",
            "Huyện Krông Pắc",
            "Huyện Lắk",
            "Huyện M'Đrắk"
        ),
        "đắk nông" to listOf(
            "Thành phố Gia Nghĩa",
            "Huyện Cư Jút",
            "Huyện Đắk Glong",
            "Huyện Đắk Mil",
            "Huyện Đắk R'Lấp",
            "Huyện Đắk Song",
            "Huyện Krông Nô",
            "Huyện Tuy Đức"
        ),
        "lạng sơn" to listOf(
            "Thành phố Lạng Sơn",
            "Huyện Bắc Sơn",
            "Huyện Bình Gia",
            "Huyện Cao Lộc",
            "Huyện Chi Lăng",
            "Huyện Đình Lập",
            "Huyện Hữu Lũng",
            "Huyện Lộc Bình",
            "Huyện Tràng Định",
            "Huyện Văn Lãng",
            "Huyện Văn Quan"
        ),
        "thái nguyên" to listOf(
            "Thành phố Thái Nguyên",
            "Thị xã Phổ Yên",
            "Huyện Đại Từ",
            "Huyện Định Hóa",
            "Huyện Đồng Hỷ",
            "Huyện Phú Bình",
            "Huyện Phú Lương",
            "Huyện Võ Nhai"
        ),
        "lào cai" to listOf(
            "Thành phố Lào Cai",
            "Thị xã Bảo Thắng",
            "Thị xã Sa Pa",
            "Huyện Bắc Hà",
            "Huyện Bảo Yên",
            "Huyện Bát Xát",
            "Huyện Mường Khương",
            "Huyện Sa Pa",
            "Huyện Si Ma Cai",
            "Huyện Văn Bàn"
        ),
        "nam định" to listOf(
            "Thành phố Nam Định",
            "Thành phố Mỹ Tho",
            "Huyện Giao Thủy",
            "Huyện Hải Hậu",
            "Huyện Mỹ Lộc",
            "Huyện Nam Trực",
            "Huyện Trực Ninh",
            "Huyện Vụ Bản",
            "Huyện Xuân Trường",
            "Huyện Ý Yên"
        ),
        "quảng ninh" to listOf(
            "Thành phố Hạ Long",
            "Thành phố Móng Cái",
            "Thị xã Cẩm Phả",
            "Thị xã Uông Bí",
            "Huyện Bái Cháy",
            "Huyện Bình Liêu",
            "Huyện Cô Tô",
            "Huyện Đầm Hà",
            "Huyện Đông Triều",
            "Huyện Hải Hà",
            "Huyện Hoành Bồ",
            "Huyện Tiên Yên",
            "Huyện Vân Đồn",
            "Huyện Yên Hưng"
        ),
        "bắc giang" to listOf(
            "Thành phố Bắc Giang",
            "Thị xã Hiệp Hòa",
            "Huyện Hiệp Hòa",
            "Huyện Lạng Giang",
            "Huyện Lục Nam",
            "Huyện Lục Ngạn",
            "Huyện Sơn Động",
            "Huyện Tân Yên",
            "Huyện Việt Yên",
            "Huyện Yên Dũng",
            "Huyện Yên Thế"
        ),
        "vĩnh long" to listOf(
            "Thành phố Vĩnh Long",
            "Thị xã Bình Minh",
            "Thị xã Trà Ôn",
            "Huyện Bình Tân",
            "Huyện Long Hồ",
            "Huyện Mang Thít",
            "Huyện Tam Bình",
            "Huyện Trà Ôn",
            "Huyện Vũng Liêm"
        ),
        "bắc ninh" to listOf(
            "Thành phố Bắc Ninh",
            "Thị xã Bắc Ninh",
            "Huyện Gia Bình",
            "Huyện Lương Tài",
            "Huyện Quế Võ",
            "Huyện Thuận Thành",
            "Huyện Tiên Du",
            "Huyện Yên Phong"
        ),
        "ninh bình" to listOf(
            "Thành phố Ninh Bình",
            "Thị xã Tam Điệp",
            "Huyện Gia Viễn",
            "Huyện Hoa Lư",
            "Huyện Kim Sơn",
            "Huyện Nho Quan",
            "Huyện Yên Khánh",
            "Huyện Yên Mô"
        ),
        "phú thọ" to listOf(
            "Thành phố Việt Trì",
            "Thị xã Phú Thọ",
            "Huyện Cẩm Khê",
            "Huyện Đoan Hùng",
            "Huyện Hạ Hoà",
            "Huyện Lâm Thao",
            "Huyện Phù Ninh",
            "Huyện Tam Nông",
            "Huyện Tân Sơn",
            "Huyện Thanh Ba",
            "Huyện Thanh Sơn",
            "Huyện Thanh Thủy",
            "Huyện Yên Lập"
        ),
        "ninh thuận" to listOf(
            "Thành phố Phan Rang-Tháp Chàm",
            "Huyện Bác Ái",
            "Huyện Ninh Hải",
            "Huyện Ninh Phước",
            "Huyện Ninh Sơn",
            "Huyện Thuận Bắc",
            "Huyện Thuận Nam"
        ),
        "kon tum" to listOf(
            "Thành phố Kon Tum",
            "Huyện Đắk Glei",
            "Huyện Đắk Hà",
            "Huyện Đắk Tô",
            "Huyện Kon Plông",
            "Huyện Kon Rẫy",
            "Huyện Ngọc Hồi",
            "Huyện Sa Thầy",
            "Huyện Tu Mơ Rông"
        ),
        "gia lai" to listOf(
            "Thành phố Pleiku",
            "Thành phố An Khê",
            "Huyện Ayun Pa",
            "Huyện Chư Păh",
            "Huyện Chư Prông",
            "Huyện Chư Pưh",
            "Huyện Chư Sê",
            "Huyện Đăk Đoa",
            "Huyện Đăk Pơ",
            "Huyện Đức Cơ",
            "Huyện Ia Grai",
            "Huyện Ia Pa",
            "Huyện K'Bang",
            "Huyện KBang",
            "Huyện Kông Chro",
            "Huyện Krông Pa",
            "Huyện Mang Yang",
            "Huyện Phú Thiện",
            "Huyện Plei Ku",
            "Huyện Sa Thầy",
            "Huyện KBang",
            "Huyện KBang",
            "Huyện Đắk Đoa"
        ),
        "sóc trăng" to listOf(
            "Thành phố Sóc Trăng",
            "Thị xã Vĩnh Châu",
            "Huyện Châu Thành",
            "Huyện Cù Lao Dung",
            "Huyện Kế Sách",
            "Huyện Long Phú",
            "Huyện Mỹ Tú",
            "Huyện Mỹ Xuyên",
            "Huyện Ngã Năm",
            "Huyện Thạnh Trị",
            "Huyện Trần Đề",
            "Huyện Vĩnh Tường"
        ),
        "trà vinh" to listOf(
            "Thành phố Trà Vinh",
            "Huyện Càng Long",
            "Huyện Cầu Kè",
            "Huyện Châu Thành",
            "Huyện Cầu Ngang",
            "Huyện Duyên Hải",
            "Huyện Tiểu Cần",
            "Huyện Trà Cú",
            "Huyện Châu Thành",
            "Huyện Duyên Hải"
        ),
        "bạc liêu" to listOf(
            "Thành phố Bạc Liêu",
            "Huyện Đông Hải",
            "Huyện Giá Rai",
            "Huyện Hòa Bình",
            "Huyện Hồng Dân",
            "Huyện Phước Long",
            "Huyện Vĩnh Lợi"
        ),
        "long an" to listOf(
            "Thành phố Tân An",
            "Thị xã Kiến Tường",
            "Huyện Bến Lức",
            "Huyện Cần Giuộc",
            "Huyện Cần Đước",
            "Huyện Châu Thành",
            "Huyện Đức Hòa",
            "Huyện Đức Huệ",
            "Huyện Mộc Hóa",
            "Huyện Tân Hưng",
            "Huyện Tân Thạnh",
            "Huyện Tân Trụ",
            "Huyện Thạnh Hóa",
            "Huyện Thủ Thừa",
            "Huyện Vĩnh Hưng"
        ),
        "bà rịa - vũng tàu" to listOf(
            "Thành phố Bà Rịa",
            "Thành phố Vũng Tàu",
            "Thị xã Phú Mỹ",
            "Huyện Châu Đức",
            "Huyện Côn Đảo",
            "Huyện Đất Đỏ",
            "Huyện Long Điền",
            "Huyện Tân Thành",
            "Huyện Xuyên Mộc"
        ),
        "an giang" to listOf(
            "Thành phố Long Xuyên",
            "Thành phố Châu Đốc",
            "Thị xã Tân Châu",
            "Huyện An Phú",
            "Huyện Châu Phú",
            "Huyện Châu Thành",
            "Huyện Chợ Mới",
            "Huyện Phú Tân",
            "Huyện Tịnh Biên",
            "Huyện Tri Tôn"
        ),
        "tiền giang" to listOf(
            "Thành phố Mỹ Tho",
            "Thị xã Gò Công",
            "Thị xã Cai Lậy",
            "Huyện Cái Bè",
            "Huyện Cai Lậy",
            "Huyện Châu Thành",
            "Huyện Chợ Gạo",
            "Huyện Gò Công Đông",
            "Huyện Gò Công Tây",
            "Huyện Tân Phước",
            "Huyện Tân Phú Đông"
        ),
        "tây ninh" to listOf(
            "Thành phố Tây Ninh",
            "Huyện Bến Cầu",
            "Huyện Châu Thành",
            "Huyện Dương Minh Châu",
            "Huyện Gò Dầu",
            "Huyện Hòa Thành",
            "Huyện Tân Biên",
            "Huyện Tân Châu",
            "Huyện Trảng Bàng"
        ),
        "hậu giang" to listOf(
            "Thành phố Vị Thanh",
            "Thị xã Ngã Bảy",
            "Huyện Châu Thành",
            "Huyện Châu Thành A",
            "Huyện Long Mỹ",
            "Huyện Phụng Hiệp",
            "Huyện Vị Thủy"
        ),
        "bắc kạn" to listOf(
            "Thành phố Bắc Kạn",
            "Huyện Ba Bể",
            "Huyện Bạch Thông",
            "Huyện Chợ Đồn",
            "Huyện Chợ Mới",
            "Huyện Na Rì",
            "Huyện Ngân Sơn",
            "Huyện Pác Nặm"
        ),
        "yên bái" to listOf(
            "Thành phố Yên Bái",
            "Thị xã Nghĩa Lộ",
            "Huyện Lục Yên",
            "Huyện Mù Cang Chải",
            "Huyện Trạm Tấu",
            "Huyện Trấn Yên",
            "Huyện Văn Chấn",
            "Huyện Văn Yên",
            "Huyện Yên Bình"
        ),
        "tuyên quang" to listOf(
            "Thành phố Tuyên Quang",
            "Huyện Chiêm Hóa",
            "Huyện Hàm Yên",
            "Huyện Lâm Bình",
            "Huyện Na Hang",
            "Huyện Sơn Dương",
            "Huyện Yên Sơn"
        ),
        "cà mau" to listOf(
            "Thành phố Cà Mau",
            "Huyện Cái Nước",
            "Huyện Đầm Dơi",
            "Huyện Năm Căn",
            "Huyện Ngọc Hiển",
            "Huyện Phú Tân",
            "Huyện Thới Bình",
            "Huyện Trần Văn Thời",
            "Huyện U Minh"
        ),
        "hà giang" to listOf(
            "Thành phố Hà Giang",
            "Huyện Bắc Mê",
            "Huyện Bắc Quang",
            "Huyện Hoàng Su Phì",
            "Huyện Mèo Vạc",
            "Huyện Quản Bạ",
            "Huyện Quang Bình",
            "Huyện Vị Xuyên",
            "Huyện Xín Mần",
            "Huyện Yên Minh"
        ),
        "bến tre" to listOf(
            "Thành phố Bến Tre",
            "Thị xã Bến Tre",
            "Huyện Ba Tri",
            "Huyện Bình Đại",
            "Huyện Châu Thành",
            "Huyện Chợ Lách",
            "Huyện Giồng Trôm",
            "Huyện Mỏ Cày Bắc",
            "Huyện Mỏ Cày Nam",
            "Huyện Thạnh Phú"
        ),
        "điện biên" to listOf(
            "Thành phố Điện Biên Phủ",
            "Huyện Điện Biên",
            "Huyện Điện Biên Đông",
            "Huyện Mường Ảng",
            "Huyện Mường Chà",
            "Huyện Mường Nhé",
            "Huyện Mường Ớt",
            "Huyện Nậm Pồ",
            "Huyện Tủa Chùa"
        ),
        "lai châu" to listOf(
            "Thành phố Lai Châu",
            "Huyện Mường Tè",
            "Huyện Phong Thổ",
            "Huyện Sìn Hồ",
            "Huyện Tam Đường",
            "Huyện Tân Uyên",
            "Huyện Than Uyên",
            "Huyện Nậm Nhùn"
        ),
        "quảng bình" to listOf(
            "Thành phố Đồng Hới",
            "Thị xã Ba Đồn",
            "Huyện Bố Trạch",
            "Huyện Lệ Thủy",
            "Huyện Minh Hóa",
            "Huyện Quảng Ninh",
            "Huyện Quảng Trạch",
            "Huyện Tuyên Hóa"
        ),
        "quảng trị" to listOf(
            "Thành phố Đông Hà",
            "Thị xã Quảng Trị",
            "Huyện Cam Lộ",
            "Huyện Cồn Cỏ",
            "Huyện Đa Krông",
            "Huyện Gio Linh",
            "Huyện Hải Lăng",
            "Huyện Hướng Hóa",
            "Huyện Triệu Phong",
            "Huyện Vĩnh Linh"
        ),
        "sơn la" to listOf(
            "Thành phố Sơn La",
            "Thị xã Sơn La",
            "Huyện Bắc Yên",
            "Huyện Mại Sơn",
            "Huyện Mai Sơn",
            "Huyện Phù Yên",
            "Huyện Quỳnh Nhai",
            "Huyện Sông Mã",
            "Huyện Thuận Châu",
            "Huyện Yên Châu"
        ),
        "hà nam" to listOf(
            "Thành phố Phủ Lý",
            "Huyện Bình Lục",
            "Huyện Duy Tiên",
            "Huyện Kim Bảng",
            "Huyện Lý Nhân",
            "Huyện Thanh Liêm"
        ),
        "bắc cạn" to listOf(
            "Thành phố Bắc Kạn",
            "Huyện Ba Bể",
            "Huyện Bạch Thông",
            "Huyện Chợ Đồn",
            "Huyện Chợ Mới",
            "Huyện Na Rì",
            "Huyện Ngân Sơn",
            "Huyện Pác Nặm"
        ),
        "hòa bình" to listOf(
            "Thành phố Hòa Bình",
            "Thị xã Mai Châu",
            "Huyện Cao Phong",
            "Huyện Đà Bắc",
            "Huyện Kim Bôi",
            "Huyện Kỳ Sơn",
            "Huyện Lạc Sơn",
            "Huyện Lạc Thủy",
            "Huyện Lương Sơn",
            "Huyện Tân Lạc",
            "Huyện Yên Thủy"
        ),
        "ha noi" to listOf(
            "Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng", "Quận Đống Đa",
            "Quận Tây Hồ", "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai",
            "Quận Long Biên", "Quận Hà Đông", "Quận Nam Từ Liêm","Quận Bắc Từ Liêm"
        ),
        "ho chi minh" to listOf(
            "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7",
            "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân",
            "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình",
            "Quận Tân Phú", "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ",
            "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"
        ),
        "da nang" to listOf(
            "Quận Hải Châu", "Quận Thanh Khê", "Quận Sơn Trà", "Quận Ngũ Hành Sơn",
            "Quận Liên Chiểu", "Quận Cẩm Lệ", "Huyện Hòa Vang"
        ),
        "hai phong" to listOf(
            "Quận Hồng Bàng", "Quận Ngô Quyền", "Quận Lê Chân", "Quận Hải An",
            "Quận Kiến An", "Quận Đồ Sơn", "Quận Dương Kinh", "Huyện Thuỷ Nguyên",
            "Huyện An Dương", "Huyện An Lão", "Huyện Kiến Thuỵ", "Huyện Tiên Lãng",
            "Huyện Vĩnh Bảo", "Huyện Cát Hải", "Huyện Bạch Long Vĩ"
        ),
        "can tho" to listOf(
            "Quận Ninh Kiều", "Quận Bình Thủy", "Quận Cái Răng", "Quận Ô Môn",
            "Quận Thốt Nốt", "Huyện Vĩnh Thạnh", "Huyện Cờ Đỏ", "Huyện Phong Điền",
            "Huyện Thới Lai"
        ),
        "binh duong" to listOf(
            "Thành phố Thủ Dầu Một", "Thành phố Dĩ An", "Thị xã Bến Cát",
            "Thị xã Tân Uyên", "Thị xã Thuận An", "Huyện Bắc Tân Uyên",
            "Huyện Dầu Tiếng", "Huyện Phú Giáo", "Huyện Bàu Bàng"
        ),
        "dong nai" to listOf(
            "Thành phố Biên Hòa", "Thành phố Long Khánh", "Thị xã Trảng Bom",
            "Thị xã Thống Nhất", "Huyện Vĩnh Cửu", "Huyện Định Quán",
            "Huyện Tân Phú", "Huyện Long Thành", "Huyện Xuân Lộc",
            "Huyện Nhơn Trạch", "Huyện Cẩm Mỹ", "Huyện Long Khánh"
        ),
        "khanh hoa" to listOf(
            "Thành phố Nha Trang", "Thành phố Cam Ranh", "Huyện Diên Khánh",
            "Huyện Vạn Ninh", "Huyện Cam Lâm", "Huyện Khánh Sơn",
            "Huyện Khánh Vĩnh", "Huyện Trường Sa"
        ),
        "quang nam" to listOf(
            "Thành phố Tam Kỳ",
            "Thành phố Hội An",
            "Huyện Duy Xuyên",
            "Huyện Đại Lộc",
            "Huyện Điện Bàn",
            "Huyện Quế Sơn",
            "Huyện Nam Giang",
            "Huyện Phước Sơn",
            "Huyện Hiệp Đức",
            "Huyện Thăng Bình",
            "Huyện Tiên Phước",
            "Huyện Bắc Trà My",
            "Huyện Nam Trà My",
            "Huyện Núi Thành",
            "Huyện Phú Ninh"
        ),
        "hai duong" to listOf(
            "Thành phố Hải Dương",
            "Thành phố Chí Linh",
            "Thị xã Kinh Môn",
            "Huyện Cẩm Giàng",
            "Huyện Nam Sách",
            "Huyện Kim Thành",
            "Huyện Thanh Hà",
            "Huyện Thanh Miện",
            "Huyện Gia Lộc",
            "Huyện Tứ Kỳ",
            "Huyện Ninh Giang"
        ),
        "thua thien hue" to listOf(
            "Thành phố Huế",
            "Huyện Phong Điền",
            "Huyện Quảng Điền",
            "Huyện Hương Trà",
            "Huyện Hương Thủy",
            "Huyện Phú Lộc",
            "Huyện Nam Đông",
            "Thị xã Hương Thủy",
            "Thị xã Hương Trà"
        ),
        "kien giang" to listOf(
            "Thành phố Rạch Giá",
            "Thành phố Hà Tiên",
            "Thành phố Long Xuyên",
            "Thị xã Hòn Đất",
            "Thị xã Kiên Lương",
            "Thị xã An Minh",
            "Huyện Kiên Hải",
            "Huyện Kiên Lương",
            "Huyện Phú Quốc",
            "Huyện Giang Thành",
            "Huyện U Minh Thượng",
            "Huyện Giồng Riềng",
            "Huyện Gò Quao",
            "Huyện Tân Hiệp",
            "Huyện Châu Thành",
            "Huyện An Biên",
            "Huyện An Minh",
            "Huyện Vĩnh Thuận"
        ),
        "lam dong" to listOf(
            "Thành phố Đà Lạt",
            "Thị xã Bảo Lộc",
            "Huyện Đức Trọng",
            "Huyện Di Linh",
            "Huyện Lâm Hà",
            "Huyện Đơn Dương",
            "Huyện Đạ Huoai",
            "Huyện Đạ Tẻh",
            "Huyện Cát Tiên",
            "Huyện Bảo Lâm",
            "Huyện Lạc Dương"
        ),
        "binh thuan" to listOf(
            "Thành phố Phan Thiết",
            "Thị xã La Gi",
            "Huyện Hàm Thuận Bắc",
            "Huyện Hàm Thuận Nam",
            "Huyện Tuy Phong",
            "Huyện Bắc Bình",
            "Huyện Hàm Tân",
            "Huyện Đức Linh",
            "Huyện Tánh Linh",
            "Huyện Đức Trọng"
        ),
        "thanh hoa" to listOf(
            "Thành phố Thanh Hóa",
            "Thành phố Sầm Sơn",
            "Thị xã Bỉm Sơn",
            "Thị xã Nghi Sơn",
            "Huyện Quảng Xương",
            "Huyện Hoằng Hóa",
            "Huyện Hậu Lộc",
            "Huyện Hậu Lộc",
            "Huyện Hà Trung",
            "Huyện Hà Trung",
            "Huyện Vĩnh Lộc",
            "Huyện Thiệu Hóa",
            "Huyện Triệu Sơn",
            "Huyện Yên Định",
            "Huyện Thọ Xuân",
            "Huyện Thường Xuân",
            "Huyện Thạch Thành",
            "Huyện Quảng Xương",
            "Huyện Nông Cống",
            "Huyện Nga Sơn",
            "Huyện Như Thanh",
            "Huyện Như Xuân",
            "Huyện Quan Hóa",
            "Huyện Nông Cống",
            "Huyện Nông Sơn",
            "Huyện Ngọc Lặc",
            "Huyện Nga Sơn",
            "Huyện Thạch Thành"
        ),
        "nghe an" to listOf(
            "Thành phố Vinh",
            "Thị xã Cửa Lò",
            "Thị xã Thái Hòa",
            "Huyện Quế Phong",
            "Huyện Quỳ Châu",
            "Huyện Kỳ Sơn",
            "Huyện Tương Dương",
            "Huyện Nghĩa Đàn",
            "Huyện Quỳ Hợp",
            "Huyện Quỳnh Lưu",
            "Huyện Con Cuông",
            "Huyện Tân Kỳ",
            "Huyện Anh Sơn",
            "Huyện Diễn Châu",
            "Huyện Yên Thành",
            "Huyện Đô Lương",
            "Huyện Thanh Chương",
            "Huyện Nam Đàn",
            "Huyện Hưng Nguyên"
        ),
        "binh dinh" to listOf(
            "Thành phố Quy Nhơn",
            "Thị xã An Nhơn",
            "Huyện Vân Canh",
            "Huyện Hoài Ân",
            "Huyện Hoài Nhơn",
            "Huyện Phù Mỹ",
            "Huyện Tây Sơn",
            "Huyện Tuy Phước",
            "Huyện Vĩnh Thạnh"
        ),
        "vinh phuc" to listOf(
            "Thành phố Vĩnh Yên",
            "Thị xã Phúc Yên",
            "Huyện Bình Xuyên",
            "Huyện Lập Thạch",
            "Huyện Sông Lô",
            "Huyện Tam Dương",
            "Huyện Tam Đảo",
            "Huyện Yên Lạc"
        ),
        "dak lak" to listOf(
            "Thành phố Buôn Ma Thuột",
            "Thị xã Buôn Hồ",
            "Huyện Buôn Đôn",
            "Huyện Buôn Hồ",
            "Huyện Buôn Đức",
            "Huyện Cư Kuin",
            "Huyện Ea Kar",
            "Huyện Ea Súp",
            "Huyện Krông Ana",
            "Huyện Krông Bông",
            "Huyện Krông Búk",
            "Huyện Krông Năng",
            "Huyện Krông Pắc",
            "Huyện Lắk",
            "Huyện M'Đrắk"
        ),
        "dak nong" to listOf(
            "Thành phố Gia Nghĩa",
            "Huyện Cư Jút",
            "Huyện Đắk Glong",
            "Huyện Đắk Mil",
            "Huyện Đắk R'Lấp",
            "Huyện Đắk Song",
            "Huyện Krông Nô",
            "Huyện Tuy Đức"
        ),
        "lang son" to listOf(
            "Thành phố Lạng Sơn",
            "Huyện Bắc Sơn",
            "Huyện Bình Gia",
            "Huyện Cao Lộc",
            "Huyện Chi Lăng",
            "Huyện Đình Lập",
            "Huyện Hữu Lũng",
            "Huyện Lộc Bình",
            "Huyện Tràng Định",
            "Huyện Văn Lãng",
            "Huyện Văn Quan"
        ),
        "thai nguyen" to listOf(
            "Thành phố Thái Nguyên",
            "Thị xã Phổ Yên",
            "Huyện Đại Từ",
            "Huyện Định Hóa",
            "Huyện Đồng Hỷ",
            "Huyện Phú Bình",
            "Huyện Phú Lương",
            "Huyện Võ Nhai"
        ),
        "lao cai" to listOf(
            "Thành phố Lào Cai",
            "Thị xã Bảo Thắng",
            "Thị xã Sa Pa",
            "Huyện Bắc Hà",
            "Huyện Bảo Yên",
            "Huyện Bát Xát",
            "Huyện Mường Khương",
            "Huyện Sa Pa",
            "Huyện Si Ma Cai",
            "Huyện Văn Bàn"
        ),
        "nam dinh" to listOf(
            "Thành phố Nam Định",
            "Thành phố Mỹ Tho",
            "Huyện Giao Thủy",
            "Huyện Hải Hậu",
            "Huyện Mỹ Lộc",
            "Huyện Nam Trực",
            "Huyện Trực Ninh",
            "Huyện Vụ Bản",
            "Huyện Xuân Trường",
            "Huyện Ý Yên"
        ),
        "quang ninh" to listOf(
            "Thành phố Hạ Long",
            "Thành phố Móng Cái",
            "Thị xã Cẩm Phả",
            "Thị xã Uông Bí",
            "Huyện Bái Cháy",
            "Huyện Bình Liêu",
            "Huyện Cô Tô",
            "Huyện Đầm Hà",
            "Huyện Đông Triều",
            "Huyện Hải Hà",
            "Huyện Hoành Bồ",
            "Huyện Tiên Yên",
            "Huyện Vân Đồn",
            "Huyện Yên Hưng"
        ),
        "bac giang" to listOf(
            "Thành phố Bắc Giang",
            "Thị xã Hiệp Hòa",
            "Huyện Hiệp Hòa",
            "Huyện Lạng Giang",
            "Huyện Lục Nam",
            "Huyện Lục Ngạn",
            "Huyện Sơn Động",
            "Huyện Tân Yên",
            "Huyện Việt Yên",
            "Huyện Yên Dũng",
            "Huyện Yên Thế"
        ),
        "vinh long" to listOf(
            "Thành phố Vĩnh Long",
            "Thị xã Bình Minh",
            "Thị xã Trà Ôn",
            "Huyện Bình Tân",
            "Huyện Long Hồ",
            "Huyện Mang Thít",
            "Huyện Tam Bình",
            "Huyện Trà Ôn",
            "Huyện Vũng Liêm"
        ),
        "bac ninh" to listOf(
            "Thành phố Bắc Ninh",
            "Thị xã Bắc Ninh",
            "Huyện Gia Bình",
            "Huyện Lương Tài",
            "Huyện Quế Võ",
            "Huyện Thuận Thành",
            "Huyện Tiên Du",
            "Huyện Yên Phong"
        ),
        "ninh binh" to listOf(
            "Thành phố Ninh Bình",
            "Thị xã Tam Điệp",
            "Huyện Gia Viễn",
            "Huyện Hoa Lư",
            "Huyện Kim Sơn",
            "Huyện Nho Quan",
            "Huyện Yên Khánh",
            "Huyện Yên Mô"
        ),
        "phu tho" to listOf(
            "Thành phố Việt Trì",
            "Thị xã Phú Thọ",
            "Huyện Cẩm Khê",
            "Huyện Đoan Hùng",
            "Huyện Hạ Hoà",
            "Huyện Lâm Thao",
            "Huyện Phù Ninh",
            "Huyện Tam Nông",
            "Huyện Tân Sơn",
            "Huyện Thanh Ba",
            "Huyện Thanh Sơn",
            "Huyện Thanh Thủy",
            "Huyện Yên Lập"
        ),
        "ninh thuan" to listOf(
            "Thành phố Phan Rang-Tháp Chàm",
            "Huyện Bác Ái",
            "Huyện Ninh Hải",
            "Huyện Ninh Phước",
            "Huyện Ninh Sơn",
            "Huyện Thuận Bắc",
            "Huyện Thuận Nam"
        ),
        "soc trang" to listOf(
            "Thành phố Sóc Trăng",
            "Thị xã Vĩnh Châu",
            "Huyện Châu Thành",
            "Huyện Cù Lao Dung",
            "Huyện Kế Sách",
            "Huyện Long Phú",
            "Huyện Mỹ Tú",
            "Huyện Mỹ Xuyên",
            "Huyện Ngã Năm",
            "Huyện Thạnh Trị",
            "Huyện Trần Đề",
            "Huyện Vĩnh Tường"
        ),
        "tra vinh" to listOf(
            "Thành phố Trà Vinh",
            "Huyện Càng Long",
            "Huyện Cầu Kè",
            "Huyện Châu Thành",
            "Huyện Cầu Ngang",
            "Huyện Duyên Hải",
            "Huyện Tiểu Cần",
            "Huyện Trà Cú",
            "Huyện Châu Thành",
            "Huyện Duyên Hải"
        ),
        "bac lieu" to listOf(
            "Thành phố Bạc Liêu",
            "Huyện Đông Hải",
            "Huyện Giá Rai",
            "Huyện Hòa Bình",
            "Huyện Hồng Dân",
            "Huyện Phước Long",
            "Huyện Vĩnh Lợi"
        ),
        "ba ria - vung tau" to listOf(
            "Thành phố Bà Rịa",
            "Thành phố Vũng Tàu",
            "Thị xã Phú Mỹ",
            "Huyện Châu Đức",
            "Huyện Côn Đảo",
            "Huyện Đất Đỏ",
            "Huyện Long Điền",
            "Huyện Tân Thành",
            "Huyện Xuyên Mộc"
        ),
        "tien giang" to listOf(
            "Thành phố Mỹ Tho",
            "Thị xã Gò Công",
            "Thị xã Cai Lậy",
            "Huyện Cái Bè",
            "Huyện Cai Lậy",
            "Huyện Châu Thành",
            "Huyện Chợ Gạo",
            "Huyện Gò Công Đông",
            "Huyện Gò Công Tây",
            "Huyện Tân Phước",
            "Huyện Tân Phú Đông"
        ),
        "tay ninh" to listOf(
            "Thành phố Tây Ninh",
            "Huyện Bến Cầu",
            "Huyện Châu Thành",
            "Huyện Dương Minh Châu",
            "Huyện Gò Dầu",
            "Huyện Hòa Thành",
            "Huyện Tân Biên",
            "Huyện Tân Châu",
            "Huyện Trảng Bàng"
        ),
        "hau giang" to listOf(
            "Thành phố Vị Thanh",
            "Thị xã Ngã Bảy",
            "Huyện Châu Thành",
            "Huyện Châu Thành A",
            "Huyện Long Mỹ",
            "Huyện Phụng Hiệp",
            "Huyện Vị Thủy"
        ),
        "bac kan" to listOf(
            "Thành phố Bắc Kạn",
            "Huyện Ba Bể",
            "Huyện Bạch Thông",
            "Huyện Chợ Đồn",
            "Huyện Chợ Mới",
            "Huyện Na Rì",
            "Huyện Ngân Sơn",
            "Huyện Pác Nặm"
        ),
        "yen bai" to listOf(
            "Thành phố Yên Bái",
            "Thị xã Nghĩa Lộ",
            "Huyện Lục Yên",
            "Huyện Mù Cang Chải",
            "Huyện Trạm Tấu",
            "Huyện Trấn Yên",
            "Huyện Văn Chấn",
            "Huyện Văn Yên",
            "Huyện Yên Bình"
        ),
        "tuyen quang" to listOf(
            "Thành phố Tuyên Quang",
            "Huyện Chiêm Hóa",
            "Huyện Hàm Yên",
            "Huyện Lâm Bình",
            "Huyện Na Hang",
            "Huyện Sơn Dương",
            "Huyện Yên Sơn"
        ),
        "ca mau" to listOf(
            "Thành phố Cà Mau",
            "Huyện Cái Nước",
            "Huyện Đầm Dơi",
            "Huyện Năm Căn",
            "Huyện Ngọc Hiển",
            "Huyện Phú Tân",
            "Huyện Thới Bình",
            "Huyện Trần Văn Thời",
            "Huyện U Minh"
        ),
        "ha giang" to listOf(
            "Thành phố Hà Giang",
            "Huyện Bắc Mê",
            "Huyện Bắc Quang",
            "Huyện Hoàng Su Phì",
            "Huyện Mèo Vạc",
            "Huyện Quản Bạ",
            "Huyện Quang Bình",
            "Huyện Vị Xuyên",
            "Huyện Xín Mần",
            "Huyện Yên Minh"
        ),
        "ben tre" to listOf(
            "Thành phố Bến Tre",
            "Thị xã Bến Tre",
            "Huyện Ba Tri",
            "Huyện Bình Đại",
            "Huyện Châu Thành",
            "Huyện Chợ Lách",
            "Huyện Giồng Trôm",
            "Huyện Mỏ Cày Bắc",
            "Huyện Mỏ Cày Nam",
            "Huyện Thạnh Phú"
        ),
        "dien bien" to listOf(
            "Thành phố Điện Biên Phủ",
            "Huyện Điện Biên",
            "Huyện Điện Biên Đông",
            "Huyện Mường Ảng",
            "Huyện Mường Chà",
            "Huyện Mường Nhé",
            "Huyện Mường Ớt",
            "Huyện Nậm Pồ",
            "Huyện Tủa Chùa"
        ),
        "lai chau" to listOf(
            "Thành phố Lai Châu",
            "Huyện Mường Tè",
            "Huyện Phong Thổ",
            "Huyện Sìn Hồ",
            "Huyện Tam Đường",
            "Huyện Tân Uyên",
            "Huyện Than Uyên",
            "Huyện Nậm Nhùn"
        ),
        "quang binh" to listOf(
            "Thành phố Đồng Hới",
            "Thị xã Ba Đồn",
            "Huyện Bố Trạch",
            "Huyện Lệ Thủy",
            "Huyện Minh Hóa",
            "Huyện Quảng Ninh",
            "Huyện Quảng Trạch",
            "Huyện Tuyên Hóa"
        ),
        "quang tri" to listOf(
            "Thành phố Đông Hà",
            "Thị xã Quảng Trị",
            "Huyện Cam Lộ",
            "Huyện Cồn Cỏ",
            "Huyện Đa Krông",
            "Huyện Gio Linh",
            "Huyện Hải Lăng",
            "Huyện Hướng Hóa",
            "Huyện Triệu Phong",
            "Huyện Vĩnh Linh"
        ),
        "son la" to listOf(
            "Thành phố Sơn La",
            "Thị xã Sơn La",
            "Huyện Bắc Yên",
            "Huyện Mại Sơn",
            "Huyện Mai Sơn",
            "Huyện Phù Yên",
            "Huyện Quỳnh Nhai",
            "Huyện Sông Mã",
            "Huyện Thuận Châu",
            "Huyện Yên Châu"
        ),
        "ha nam" to listOf(
            "Thành phố Phủ Lý",
            "Huyện Bình Lục",
            "Huyện Duy Tiên",
            "Huyện Kim Bảng",
            "Huyện Lý Nhân",
            "Huyện Thanh Liêm"
        ),
        "bac can" to listOf(
            "Thành phố Bắc Kạn",
            "Huyện Ba Bể",
            "Huyện Bạch Thông",
            "Huyện Chợ Đồn",
            "Huyện Chợ Mới",
            "Huyện Na Rì",
            "Huyện Ngân Sơn",
            "Huyện Pác Nặm"
        ),
        "hoa binh" to listOf(
            "Thành phố Hòa Bình",
            "Thị xã Mai Châu",
            "Huyện Cao Phong",
            "Huyện Đà Bắc",
            "Huyện Kim Bôi",
            "Huyện Kỳ Sơn",
            "Huyện Lạc Sơn",
            "Huyện Lạc Thủy",
            "Huyện Lương Sơn",
            "Huyện Tân Lạc",
            "Huyện Yên Thủy"
        )
    )

    val listAllLocationLiveData: MutableLiveData<LocationResponse> by lazy {
        MutableLiveData<LocationResponse>()
    }
    fun getAllAddress(
        customer_id: Int,
        onResult: (Boolean, LocationResponse?) -> Unit
    ) {
            viewModelScope.launch {
                try {
                    var response = locationRepository.getAllAddress(customer_id)

                    response.enqueue(object : Callback<LocationResponse> {
                        override fun onResponse(
                            call: Call<LocationResponse>,
                            response: Response<LocationResponse>
                        ) {
                            if (response.isSuccessful) {
                                val locationResponse = response.body()
                                if (locationResponse != null) {
                                    listAllLocationLiveData.value = locationResponse
                                    onResult(true, locationResponse)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }

                        override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                            onResult(false, null)
                        }

                    })
                } catch (e: Exception) {
                    onResult(false, null)
                }
            }
    }
    fun getDefaultAddress (
        customer_id :Int,
        onResult : (Boolean,LocationResponseItem?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.getDefaultAddress(customer_id)
                response.enqueue(object : Callback<LocationResponseItem> {
                    override fun onResponse(call: Call<LocationResponseItem>, response: Response<LocationResponseItem>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse != null){
                                onResult(true,locationResponse)
                            }
                        }else{
                            onResult(false,null)
                        }
                    }

                    override fun onFailure(call: Call<LocationResponseItem>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }

    fun addLocation (
        customer_id:Int,
        name : String,
        address : String ,
        phone_number:String,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.addLocation(customer_id,name,address,phone_number)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse!!.message == "Add location successful"){
                                onResult(true,"Add location successful")
                            }
                        }else{
                            onResult(false,"Add location fail")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }

    fun getLocationById (
        id :Int,
        onResult : (Boolean,LocationResponseItem?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.getLocationById(id)
                response.enqueue(object : Callback<LocationResponseItem> {
                    override fun onResponse(call: Call<LocationResponseItem>, response: Response<LocationResponseItem>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse != null){
                                onResult(true,locationResponse)
                            }
                        }else{
                            onResult(false,null)
                        }
                    }

                    override fun onFailure(call: Call<LocationResponseItem>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }

    fun updateLocation (
        id:Int,
        name : String,
        address : String ,
        phone_number:String,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.updateLocation(id,name,address,phone_number)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse!!.message == "Update location successful"){
                                onResult(true,"Update location successful")
                            }
                        }else{
                            onResult(false,"Location not found")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }

    fun updateDefault (
        id:Int,
        customer_id:Int,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.updateDefault(id,customer_id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse!!.message == "Update location successful"){
                                onResult(true,"Update location successful")
                            }
                        }else{
                            onResult(false,"Location not found")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }

    fun deleteLocation (
        id:Int,
        onResult : (Boolean,String?) -> Unit
    ){
        viewModelScope.launch {
            try{
                var response = locationRepository.deleteLocation(id)
                response.enqueue(object : Callback<IsExistResponse> {
                    override fun onResponse(call: Call<IsExistResponse>, response: Response<IsExistResponse>) {
                        if(response.isSuccessful){
                            val locationResponse = response.body()
                            if (locationResponse!!.message == "Location delete successful"){
                                onResult(true,"Location delete successful")
                            }
                        }else{
                            onResult(false,"Failed to delete location")
                        }
                    }

                    override fun onFailure(call: Call<IsExistResponse>, t: Throwable) {
                        onResult(false,null)
                    }

                })
            }catch (e : Exception){
                onResult(false,null)
            }
        }
    }
}