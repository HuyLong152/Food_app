@extends("layout")
@section("content")
    <form id="updateCategoryForm" enctype="multipart/form-data" class="update-form">
        <input type="hidden" id="categoryId" name="id" required><br><br>
        
        <label for="name" class="form-label">Tên danh mục mới:</label><br>
        <input type="text" id="name" name="name" required class="form-input"><br><br>
        
        <label for="image" class="form-label">Ảnh mới:</label><br>
        <input type="file" id="image" name="image" accept="image/*" class="form-input" onchange="previewImage(event)"><br><br>

        <img id="categoryImage" src="" alt="Category Image" class="category-preview"><br><br>
        
        <button type="submit" class="submit-button">Cập nhật danh mục</button>
    </form>

    <style>
        /* Quy tắc CSS */
        /* .update-form {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-label {
            font-weight: bold;
        }

        .form-input {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .category-preview {
            max-width: 200px;
            height: auto;
            margin-bottom: 15px;
        }

        .submit-button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .submit-button:hover {
            background-color: #45a049;
        } */
    </style>
    <link rel="stylesheet" href="{{asset("../resources/css/categoryEdit.css")}}">

    <script>
        // Trích xuất ID từ URL
        const currentURL = window.location.href;
        const urlParts = currentURL.split('/');
        const lastPart = urlParts[urlParts.length - 1];
        const categoryId = parseInt(lastPart);

        document.getElementById('categoryId').value = categoryId;

        // Gửi yêu cầu GET đến API để lấy thông tin danh mục
        fetch(BASE_URL_API+"category/" + categoryId)
            .then(response => response.json())
            .then(data => {
                // Đặt tên danh mục vào trường tương ứng
                document.getElementById('name').value = data.category.name;

                // Hiển thị ảnh danh mục
                document.getElementById('categoryImage').src = BASE_URL_CATEGORY + data.category.image_url;
            })
            .catch(error => console.error('Error:', error));

        function previewImage(event) {
            const input = event.target;
            const file = input.files[0];

            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('categoryImage').src = e.target.result;
                };
                reader.readAsDataURL(file);
            } else {
                document.getElementById('categoryImage').src = "";
            }
        }

        document.getElementById('updateCategoryForm').addEventListener('submit', function(event) {
            event.preventDefault();

            // Tạo đối tượng FormData để gửi dữ liệu form
            var formData = new FormData(this);

            // Gửi yêu cầu POST đến API
            fetch(BASE_URL_API + "category/"+ categoryId, {
                method: 'POST', // Sử dụng phương thức POST
                headers: {
                    'X-HTTP-Method-Override': 'PUT', // Ghi đè phương thức POST bằng PUT
                },
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    alert(response);
                }
                return response.json();
            })
            .then(data => {
                alert(data.message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to update category');
            });
        });
    </script>
@endsection
