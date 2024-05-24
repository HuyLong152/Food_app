@extends('layout')

@section('content')
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create New Food Item</title>
    <style>
        /* input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 12px; 
            margin-top: 5px;
            margin-bottom: 15px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px; 
        }

        .category-button {
            display: inline-block;
            margin-right: 10px;
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            cursor: pointer;
            background-color: #f9f9f9;
            color: #000;
            transition: background-color 0.3s ease;
        }

        .category-button.selected {
            background-color: #007bff;
            color: #fff;
        }

        .category-button:hover {
            background-color: #007bff;
            color: #fff;
        }

        .image-container-item {
            margin-right: 10px;
            margin-bottom: 10px;
            display: inline-block;
            vertical-align: top;
            position: relative; 
        }

        .image-container-item img {
            max-width: 200px;
            max-height: 200px;
        }

        .delete-button {
            position: absolute; 
            top: 0; 
            right: 0; 
            color: red;
            background-color: transparent;
            border: none;
            cursor: pointer;
            font-size: 20px;
        } */
    </style>
</head>
<link rel="stylesheet" href="{{asset("../resources/css/product.css")}}">

<body>
    <div style="max-width: 600px; margin: 0 auto;">
        <h2 style="text-align: center;">Thêm món ăn mới</h2>
        <form id="createProduct" enctype="multipart/form-data">
            <label for="name">Tên:</label><br>
            <input type="text" id="name" name="name" required><br><br>

            <label for="description">Mô tả:</label><br>
            <textarea id="description" name="description" rows="4" required></textarea><br><br>

            <label for="ingredient">Nguyên liệu:</label><br>
            <textarea id="ingredient" name="ingredient" rows="4" required></textarea><br><br>

            <label for="calories">Calories:</label><br>
            <input type="number" id="calories" name="calo" required min="0"><br><br>

            <label for="quantity">Số lượng:</label><br>
            <input type="number" id="quantity" name="quantity" required min="0"><br><br>

            <label for="price">Đơn giá:</label><br>
            <input type="number" id="price" name="price" required min="0" max="10000000"><br><br>

            <label for="discount">Phần trăm giảm giá:</label><br>
            <input type="number" id="discount" name="discount" required min="0" max="100"><br><br>

            <label>Danh mục:</label><br>
            <div id="categoryButtons" class="checkbox-container"></div><br>

            <label for="image">Ảnh:</label><br>
            <input type="file" id="image" name="image[]" multiple accept="image/*" required><br><br>

            <div id="image-preview" class="image-container"></div><br>

            <input type="submit" class="button green" value="Tạo" style="margin-top: 15px;">
        </form>
    </div>

    <script>
        // JavaScript code for creating new product
        // Fetch categories from API and generate buttons
        fetch(BASE_URL_API+'category')
            .then(function(response) {
                return response.json();
            })
            .then(function(categories) {
                generateCategoryButtons(categories.categories);
            })
            .catch(function(error) {
                console.error('Lỗi hiển thị danh mục:', error);
            });

        // Function to generate buttons for categories
        function generateCategoryButtons(categories) {
            const categoryButtons = document.getElementById('categoryButtons');

            categories.forEach(function(category) {
                const button = document.createElement('button');
                button.type = 'button';
                button.id = 'category-' + category.id;
                button.textContent = category.name;
                button.classList.add('category-button');

                button.addEventListener('click', function() {
                    button.classList.toggle('selected');
                });

                categoryButtons.appendChild(button);
                categoryButtons.appendChild(document.createTextNode(' ')); // Add space between buttons
            });
        }

        // Function to handle image preview
        document.getElementById('image').addEventListener('change', function(event) {
            const files = event.target.files;
            const imageContainer = document.getElementById('image-preview');
            imageContainer.innerHTML = ''; // Clear existing images

            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const reader = new FileReader();

                reader.onload = function(event) {
                    const imgContainer = document.createElement('div'); // Create a container for each image
                    imgContainer.classList.add('image-container-item'); // Add a class for styling

                    const img = document.createElement('img');
                    img.src = event.target.result;
                    imgContainer.appendChild(img);

                    imageContainer.appendChild(imgContainer);
                };

                reader.readAsDataURL(file);
            }
        });

        // Function to submit form data to API
        document.getElementById('createProduct').addEventListener('submit', function(event) {
            event.preventDefault(); 

            const form = document.getElementById('createProduct');
            const formData = new FormData(form);

            const categoryButtons = document.querySelectorAll('.category-button.selected');
            const selectedCategories = Array.from(categoryButtons).map(button => parseInt(button.id.split('-')[1]));
            if (selectedCategories.length === 0) {
                alert('Vui lòng chọn ít nhất một danh mục');
                return; 
            }

            
            formData.append('categories', JSON.stringify(selectedCategories));

            // Validate don gia
            const priceInput = document.getElementById('price');
            const priceValue = parseFloat(priceInput.value);
            if (isNaN(priceValue) || priceValue < 18000 || priceValue > 10000000) {
                alert('Đơn giá bắt đầu từ 18000 đến 10000000');
                return; 
            }

            // Validate quantity field
            const quantityInput = document.getElementById('quantity');
            const quantityValue = parseInt(quantityInput.value);
            if (isNaN(quantityValue) || quantityValue < 0) {
                alert('Số lượng phải là số dương');
                return; // Stop form submission
            }

            // Validate calories field
            const caloriesInput = document.getElementById('calories');
            const caloriesValue = parseInt(caloriesInput.value);
            if (isNaN(caloriesValue) || caloriesValue < 0) {
                alert('Calories phải là số dương.');
                return; // Stop form submission
            }

            // Validate discount field
            const discountInput = document.getElementById('discount');
            const discountValue = parseInt(discountInput.value);
            if (isNaN(discountValue) || discountValue < 0 || discountValue > 100) {
                alert('Giảm giá từ 0 đến 100');
                return; // Stop form submission
            }

            // Perform AJAX request to create new product
            fetch(BASE_URL_API+'product', {
                method: 'POST',
                body: formData,
                headers: {
                    'Accept': 'application/json', // Specify JSON format
                }
            })

            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                alert("Thêm sản phẩm thành công");
                form.reset();
                document.getElementById('image-preview').innerHTML = '';
                document.querySelectorAll('.category-button.selected').forEach(button => button.classList.remove('selected'));
            })
            .catch(function(error) {
                console.error('Error creating product:', error);
            });
        });
    </script>
</body>

</html>


@endsection
