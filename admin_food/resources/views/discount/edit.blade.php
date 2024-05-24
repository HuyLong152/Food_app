@extends("layout")
@section("content")
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Discount Code</title>
    <style>
        /* body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 600px;
            margin: 50px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group input[type="datetime-local"],
        .form-group input[type="radio"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .form-group button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
            background-color: #f0f0f0;
            color: #777;
        } */

        .form-group button#available:checked {
            background-color: #4caf50;
            color: #fff;
        }

        .form-group button#notAvailable:checked {
            background-color: #f44336;
            color: #fff;
        }

        .form-group button#available:disabled,
        .form-group button#notAvailable:disabled {
            background-color: #f0f0f0;
            color: #aaa;
            cursor: not-allowed;
        }

        .form-group button:hover {
            opacity: 0.8;
        }

        .form-group .info {
            font-size: 14px;
            color: #555;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<link rel="stylesheet" href="{{asset("../resources/css/discount.css")}}">

<div class="container">
    <h2>Chỉnh sửa mã giảm giá</h2>
    <form id="editDiscountForm" onsubmit="saveDiscount(event)">
        <div class="form-group">
            <label for="discountCode">Mã giảm giá:</label>
            <input type="text" id="discountCode" name="discountCode" required>
        </div>
        <div class="form-group">
            <label for="discountPercent">% giảm giá:</label>
            <input type="number" id="discountPercent" name="discountPercent" min="0" max="100" required>
        </div>
        <div class="form-group">
            <label for="expirationTime">Ngày hết hạn:</label>
            <input type="datetime-local" id="expirationTime" name="expirationTime">
        </div>
        <div class="form-group">
            <label>Trạng thái:</label>
            <div class="button-group">
                <button type="button" id="available" name="available" value="1" onclick="updateButtonColor(this);">Có sẵn</button>
                <button type="button" id="notAvailable" name="available" value="0" onclick="updateButtonColor(this);">Không có sẵn</button>
            </div>
        </div>
        <div class="form-group">
            <label for="numberOfUses">Lượt sử dụng:</label>
            <input type="number" id="numberOfUses" name="numberOfUses" min="0">
        </div>
        <div class="form-group">
            <button type="submit">Lưu</button>
        </div>
    </form>
</div>

<script>
    const currentURL = window.location.href;
    const urlParts = currentURL.split('/');
    const lastPart = urlParts[urlParts.length - 1];
    const id = parseInt(lastPart);

    window.addEventListener('DOMContentLoaded', (event) => {
        fetch(BASE_URL_API+'discount/aDiscount/' + id)
            .then(response => response.json())
            .then(data => {
                document.getElementById('discountCode').value = data.discount_code;
                document.getElementById('discountPercent').value = data.discount_percent;
                const expirationTime = data.expiration_time ? data.expiration_time.replace(/:\d{2}$/, '') : '';
                document.getElementById('expirationTime').value = expirationTime;
                document.getElementById('numberOfUses').value = data.number_uses !== null ? data.number_uses : '';
                const availableButton = document.getElementById('available');
                const notAvailableButton = document.getElementById('notAvailable');
                if (data.available) {
                    availableButton.checked = true;
                    updateButtonColor(availableButton);
                } else {
                    notAvailableButton.checked = true;
                    updateButtonColor(notAvailableButton);
                }
            })
            .catch(error => console.error('Error:', error));
    });

    function saveDiscount(event) {
    event.preventDefault();

    const discountCode = document.getElementById('discountCode').value;
    const discountPercent = document.getElementById('discountPercent').value;
    const expirationTime = document.getElementById('expirationTime').value || null;
    const available = document.getElementById('available').classList.contains('isChecked') ? 1 : 0;
    const numberOfUses = document.getElementById('numberOfUses').value || null;

    const requestBody = {
        id: id, // Thêm ID của discount vào request body
        discount_code: discountCode,
        discount_percent: discountPercent,
        expiration_time: expirationTime,
        available: available,
        number_uses: numberOfUses
    };
    fetch(BASE_URL_API+'discount/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        window.location.href = BASE_HOSTING +'admin_food/public/discount';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi cập nhật giảm giá. Vui lòng thử lại sau.');
    });
}


    function updateButtonColor(button) {
        const availableButton = document.getElementById('available');
        const notAvailableButton = document.getElementById('notAvailable');
        const checkedClass = 'isChecked';
        if (button === availableButton) {
            availableButton.style.backgroundColor = '#4caf50';
            availableButton.style.color = '#fff';
            notAvailableButton.style.backgroundColor = '#f0f0f0';
            notAvailableButton.style.color = '#777';
            availableButton.classList.add(checkedClass);
            notAvailableButton.classList.remove(checkedClass);
        } else {
            availableButton.style.backgroundColor = '#f0f0f0';
            availableButton.style.color = '#777';
            notAvailableButton.style.backgroundColor = '#f44336';
            notAvailableButton.style.color = '#fff';
            notAvailableButton.classList.add(checkedClass);
            availableButton.classList.remove(checkedClass);
        }
    }
</script>

</body>
</html>

@endsection
