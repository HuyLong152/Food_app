package com.example.food_app.view.Account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.food_app.R
import com.example.food_app.viewmodel.ChatViewModel
import com.example.food_app.viewmodel.MessageItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageScreen(
    chatViewModel: ChatViewModel,
    customer_id: Int,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var textContent by remember {
            mutableStateOf("")
        }

        val listState = rememberLazyListState()
        val messages by chatViewModel.messagesLiveData.observeAsState(listOf())
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .weight(0.9f),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(messages) { _, message ->
                    MessageRow(message, isCurrentUser = message.role == "customer")
                }
            }
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(12.dp)
            ){
                TextField(value = textContent, onValueChange = { textContent = it }, modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    chatViewModel.sendMessage(
                        MessageItem(
                            textContent,
                            LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            -1,
                            -1,
                            customer_id,
                            "customer"
                        )
                    );
                    textContent = ""
                }) {
                    Text(text = "Gửi")
                }
            }
        }

    }
}


@Composable
fun ChatItem(
    message: MessageItem,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        if (message.role == "customer") {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .background(Color.Red)
            ) {
                Text(text = message.content, color = Color.White)
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageRow(message: MessageItem, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 10.dp,
            backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                val timeAgo = convertDateTimeToRelative(message.created_time)
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (timeAgo == "Today") stringResource(id = R.string.today) else if(timeAgo == "Yesterday") stringResource(id = R.string.yesterday) else timeAgo + stringResource(id = R.string.dayAgo),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.7f
                    )
                )
            }
        }
    }
}