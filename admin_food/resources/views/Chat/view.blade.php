@extends("layout")
@section("content")
<style>
    .message-card {
        border: 1px solid #ddd;
        padding: 10px;
        margin: 10px 0;
        display: flex;
        align-items: center;
    }
    .message-card img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 10px;
    }
    .message-card .message-info {
        flex: 1;
    }
    .message-card .message-info .customer-name {
        font-weight: bold;
    }
    .message-card .message-info .message-content {
        margin: 5px 0;
    }
    .message-card .message-info .message-time {
        color: #888;
    }
</style>

<div id="messagesContainer"></div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/locale/vi.min.js"></script>
<script>
    moment.locale('vi');
</script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        fetchMessages();
    });

    function fetchMessages() {
        fetch('https://huylong.io.vn/api_food/public/api/message/getAll')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                displayMessages(data);
            })
            .catch(error => {
                console.error(error.message);
                alert('Đã xảy ra lỗi khi lấy dữ liệu tin nhắn!');
            });
    }

    function displayMessages(data) {
        const messagesContainer = document.getElementById('messagesContainer');
        messagesContainer.innerHTML = '';

        // Sắp xếp dữ liệu theo thời gian tin nhắn mới nhất
        data.sort((a, b) => {
            const aLatestMessage = a.messages[a.messages.length - 1];
            const bLatestMessage = b.messages[b.messages.length - 1];
            return new Date(bLatestMessage.created_time) - new Date(aLatestMessage.created_time);
        });

        data.forEach(item => {
            console.log(item);
            const customer = item.customer;
            const latestMessage = item.messages[0];

            const messageCard = document.createElement('div');
            messageCard.classList.add('message-card');

            const customerImage = document.createElement('img');
            if (customer.image_url.startsWith('https')) {
                customerImage.src = customer.image_url;
            } else {
                customerImage.src = BASE_URL_AVATAR + customer.image_url;
            }
            customerImage.alt = customer.full_name;

            const messageInfo = document.createElement('div');
            messageInfo.classList.add('message-info');

            const customerName = document.createElement('div');
            customerName.classList.add('customer-name');
            customerName.textContent = customer.full_name;

            const messageContent = document.createElement('div');
            messageContent.classList.add('message-content');
            messageContent.textContent = latestMessage.content;

            const messageTime = document.createElement('div');
            messageTime.classList.add('message-time');
            messageTime.textContent = moment(latestMessage.created_time).format('LLLL');
            messageInfo.appendChild(customerName);
            messageInfo.appendChild(messageContent);
            messageInfo.appendChild(messageTime);

            messageCard.appendChild(customerImage);
            messageCard.appendChild(messageInfo);

            messageCard.addEventListener('click', function() {
        if (item.customer_id) {
            window.location.href =  "https://huylong.io.vn/admin/public/chat/detail/" + item.customer_id;
            // window.location.href =  "http://localhost/admin_food/public/chat/detail/" + item.customer_id;
        } else {
            console.error('Không thể truy cập vào customer_id');
        }
    });
            messagesContainer.appendChild(messageCard);
        });
    }
</script>
@endsection
