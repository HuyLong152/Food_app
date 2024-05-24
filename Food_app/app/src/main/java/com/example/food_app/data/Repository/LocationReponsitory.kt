package com.example.food_app.data.Repository

import com.example.food_app.data.api.LocationApiService
import com.example.food_app.data.model.Auth.IsExistResponse
import com.example.food_app.data.model.Location.LocationResponse
import com.example.food_app.data.model.Location.LocationResponseItem
import retrofit2.Call

class LocationReponsitory(
    private val locationApiService: LocationApiService
) {
    suspend fun getDefaultAddress(customer_id:Int): Call<LocationResponseItem> {
        return locationApiService.getDefaultAddress(customer_id)
    }
    suspend fun getLocationById(id:Int): Call<LocationResponseItem> {
        return locationApiService.getLocationById(id)
    }
    suspend fun getAllAddress(customer_id:Int): Call<LocationResponse> {
        return locationApiService.getAllAddress(customer_id)
    }
    suspend fun addLocation(customer_id:Int, name : String, address : String , phone_number:String): Call<IsExistResponse> {
        return locationApiService.addLocation(customer_id,name,address,phone_number)
    }
    suspend fun updateLocation(id:Int, name : String, address : String , phone_number:String): Call<IsExistResponse> {
        return locationApiService.updateLocation(id,name,address,phone_number)
    }
    suspend fun updateDefault(id:Int,customer_id: Int): Call<IsExistResponse> {
        return locationApiService.updateDefault(id,customer_id)
    }
    suspend fun deleteLocation(id:Int): Call<IsExistResponse> {
        return locationApiService.deleteLocation(id)
    }
}