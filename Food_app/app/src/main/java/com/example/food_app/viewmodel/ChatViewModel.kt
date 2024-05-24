package com.example.food_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.Serializable
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException

class ChatViewModel(
    private var customerId: Int
) : ViewModel() {
//    private val _textLiveData = MutableLiveData<String>()
//    val textLiveData get() = _textLiveData
    private val _messagesLiveData = MutableLiveData<List<MessageItem>>()
    val messagesLiveData: LiveData<List<MessageItem>> = _messagesLiveData
    private lateinit var socket: Socket
    private var gson = Gson()



    init {
//        try {
//            socket = IO.socket("https://chatrealtime-n1cn.onrender.com") // ket noi server
//            initSocketListeners() // ket noi thanh cong server -> goi ham
//        } catch (e: URISyntaxException) {
//            throw RuntimeException(e)
//        }
        initSocket()
    }

    private fun initSocketListeners() {
        socket.on(Socket.EVENT_CONNECT) { // lang nghe su kien connect server success
            socket.emit("join", customerId.toString()) // success -> gọi join trên server và truyền cus_id
        }.on("history") { args ->
            if (args[0] is JSONArray) {
                val messages = mutableListOf<MessageItem>()
                val jsonArray = args[0] as JSONArray
                for (i in 0 until jsonArray.length()) {

                    val messageJson = jsonArray.getJSONObject(i)
                    val message = gson.fromJson(messageJson.toString(), MessageItem::class.java)
                    messages.add(message)
                    Log.e("TAG", "initSocketListeners: ${message.toString()}")
                }
                _messagesLiveData.postValue(messages)
            }
        }.on("thread") { args ->
            val messages =  mutableListOf<MessageItem>()
            val messageJson = JSONObject(args[0] as String)
            Log.d("ChatViewModel", "Received message: ${messageJson.toString()}")
            val message = gson.fromJson(messageJson.toString(), MessageItem::class.java)
            val currentMessages = _messagesLiveData.value?.toMutableList() ?: mutableListOf()
            currentMessages.add(0,message)
            _messagesLiveData.postValue(currentMessages)
            Log.d("ChatViewModel", "Received message: ${message.toString()}")
        }.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected")
        }
        socket.connect()
    }

//    private fun handleIncomingMessage(id: String, msg: String) {
//        if (id == socket.id()) {
//            _textLiveData.postValue("->$msg")
//        } else {
//            _textLiveData.postValue("------>$msg")
//        }
//        Log.e("Chat", "initSocketListeners: ${_textLiveData.value.toString()}")
//    }

    fun connect() {
        if (!socket.connected()) {
            socket.connect()
        }
        socket.emit("register", "id")
    }

    fun disconnect() {
        if (socket.connected()) {
            socket.disconnect()
        }
    }

    fun sendMessage(message: MessageItem) {
        val data = gson.toJson(message)
        socket.emit("message",  data)
    }


    override fun onCleared() {
        super.onCleared()
        socket.disconnect()
    }
    private fun initSocket(){
        try {
            socket = IO.socket("https://chatrealtime-n1cn.onrender.com") // ket noi server
            initSocketListeners() // ket noi thanh cong server -> goi ham
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }
    fun restart(customerId : Int){
        socket.disconnect()
        this.customerId = customerId
        initSocket()
    }
}
@Serializable
data class MessageItem(
    val content: String,
    val created_time: String = "",
    val id: Int,
    val receiver_id: Int = -1,
    val sender_id: Int,
    val role: String,
    val updated_time: String = ""
)