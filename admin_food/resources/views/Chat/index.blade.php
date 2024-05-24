@extends("layout")
@section("content")
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f5f5f5;
        margin: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }

    #chat-container {
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 10px;
        width: 100%; 
        height: 600px; 
        display: flex;
        flex-direction: column;
        overflow: hidden;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); 
    }

    #message-input {
        /*flex:1;*/
        width:100%;
        position: fixed; 
        bottom: 0; 
        padding:15px;
    }
    #message-input input {
        flex-grow: 1;
        width:75%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 20px;
        margin-right: 10px;
        font-size: 16px;
        box-sizing: border-box;
    }

    #message-input button {
        padding: 10px 20px;
        border: none;
        background-color: #007bff;
        color: white;
        border-radius: 20px;
        cursor: pointer;
        font-size: 16px;
        transition: background-color 0.3s;
    }

    #message-input button:hover {
        background-color: #0056b3;
    }

    #messages {
        flex: 1;
        padding: 15px;
        overflow-y: auto;
        display: flex;
        flex-direction: column-reverse;
        box-sizing: border-box;
        background-color: #fafafa;
        /*padding-bottom: 70px*/
    }

    .message {
        display: flex;
        margin-bottom: 15px;
        max-width: 100%;
    }

    .message.user {
        justify-content: flex-start;
    }

    .message.admin {
        justify-content: flex-end;
    }

    .message-content {
        padding: 15px;
        border-radius: 20px;
        position: relative;
        font-size: 16px;
        box-sizing: border-box;
        word-break: break-word;
    }

    .message.user .message-content {
        background-color: #f1f0f0;
        border-bottom-left-radius: 0;
    }

    .message.admin .message-content {
        background-color: #e1f7d5;
        border-bottom-right-radius: 0;
    }

    .timestamp {
        font-size: 0.8em;
        color: #888;
        margin-top: 5px;
        text-align: right;
    }
</style>
<body>
    <div id="chat-container">
        <div id="messages">
            <ul id="messageList" style="list-style-type:none; padding:0; margin:0;"></ul>
        </div>
        <div id="message-input">
            <input type="text" id="chat-content" placeholder="Type your message">
            <button id="btn_send">Send</button>
        </div>
    </div>

    <script src="https://chatrealtime-n1cn.onrender.com/socket.io/socket.io.js"></script>
    <script>
        const currentURL = window.location.href;
        const urlParts = currentURL.split('/');
        const room_id = urlParts[urlParts.length - 1];

        const socket = io("https://chatrealtime-n1cn.onrender.com");

        const btn_send = document.getElementById('btn_send');
        // const room_id = document.getElementById('room_id');
        const chat = document.getElementById('chat-content');
        const messageList = document.getElementById('messageList');

        // Join the room
        socket.emit("join", room_id);
        console.log(`Joined room ${room_id}`);

        // Send message
        btn_send.addEventListener('click', () => {
            console.log("value",chat.value);
            const message = {
                id: -1,
                receiver_id: room_id,
                content: chat.value,
                sender_id: localStorage.getItem('userID'),
                room_id: room_id ? room_id.value : '',
                role: 'admin',
                updated_time: "",
                created_time: getCurrentFormattedTime()
            };
            
            socket.emit("message", JSON.stringify(message));
            chat.value = '';
            console.log(`Message sent: ${JSON.stringify(message)}`);
        });

        socket.on("thread", function (data) {

            const parsedMessage = JSON.parse(data);
            displayMessage(parsedMessage);
            console.log(`Received message: ${JSON.stringify(data)}`);
        });

        socket.on("history", function (data) {
            document.getElementById('messageList').innerHTML = "";
            const messages = JSON.parse(JSON.stringify(data)).reverse();
            messages.forEach(message => {
                displayMessage(message);
            });
            console.log(`Chat history: ${JSON.stringify(data)}`);
        });

        function displayMessage(message) {
            console.log(message);
            const messageElement = document.createElement('li');
            messageElement.classList.add('message');
            messageElement.classList.add(message.role === 'admin' ? 'admin' : 'user');
            messageElement.innerHTML = `
                <div class="message-content">
                    <span>${message.content}</span>
                    <div class="timestamp">${message.created_time}</div>
                </div>
            `;
            messageList.appendChild(messageElement);
        }

        function getCurrentFormattedTime() {
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        }
    </script>
</body>
@endsection
