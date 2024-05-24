@extends("layout")
@section("content")
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Discount Code</title>
    <style>
        /* CSS cho màn hình */
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
        }

        .form-group button:hover {
            opacity: 0.8;
        }

        .form-group .info {
            font-size: 14px;
            color: #555;
            margin-top: 5px;
        }
        .add-button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    margin-right: 10px;
    background-color: #4caf50 !important; 
    color: white !important; 
}

.add-button:hover {
    opacity: 0.8;
} */
.form-group button:hover {
    opacity: 0.8;
}

.form-group .info {
    font-size: 14px;
    color: #555;
    margin-top: 5px;
}
.add-button {
padding: 10px 20px;
border: none;
border-radius: 4px;
cursor: pointer;
font-size: 16px;
margin-right: 10px;
background-color: #4caf50 !important; 
color: white !important;
}

.add-button:hover {
opacity: 0.8;
}
    </style>
</head>
<body>
<link rel="stylesheet" href="{{asset("../resources/css/discount.css")}}">

<div class="container">
    <h2>Thêm mã giảm giá mới</h2>
    <form id="addDiscountForm" onsubmit="saveDiscount(event)">
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
            <label for="numberOfUses">Lượt sử dụng:</label>
            <input type="number" id="numberOfUses" name="numberOfUses" min=0>
        </div>
        <div class="form-group">
            <label for="status">Trạng thái:</label>
            <select id="status" name="status" required>
                <option value="1">Có sẵn</option>
                <option value="0">Không có sẵn</option>
            </select>
        </div>
        <div class="form-group ">
            <button class="add-button" type="submit" >Thêm</button>
        </div>
    </form>
</div>

<script>
    function saveDiscount(event) {
        event.preventDefault();

        const discountCode = document.getElementById('discountCode').value;
        const hasSpecialChar = /[^\w]/.test(discountCode); // Kiểm tra xem có ký tự đặc biệt hay không

        if (hasSpecialChar) {
            alert('Mã giảm giá không được chứa ký tự đặc biệt và dấu!');
            return;
        }
    const discountPercent = document.getElementById('discountPercent').value;
    const expirationTime = document.getElementById('expirationTime').value || null;
    const numberOfUses = document.getElementById('numberOfUses').value >= 0 ? document.getElementById('numberOfUses').value : null;
    const status = document.getElementById('status').value;

        const requestBody = {
            discount_code: discountCode,
            discount_percent: discountPercent,
            expiration_time: expirationTime,
            number_uses: numberOfUses,
            available: status
        };
        console.log('Request Body:', requestBody);
        // Gửi request POST tới API để thêm discount mới
        fetch(BASE_URL_API+'discount/create', {
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
        .then(data=> {
            if (data.message === "Discount code already exists") {
                alert('Mã giảm giá đã tồn tại!');
            } else {
                alert('Thêm mã giảm giá thành công!');
                window.location.href = BASE_HOSTING + 'admin_food/public/discount';
            }
        })
        .catch(error => {
            alert('Thêm mã giảm giá thất bại!');
            console.error('Error:', error);
        });
    }
</script>

</body>
</html>

@endsection
