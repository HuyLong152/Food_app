@extends('layout')

@section('content')
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Food Item</title>
    <style>
        /* label {
            display: block;
            margin-top: 15px;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 15px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .checkbox-container {
            margin-top: 10px;
            display: flex;
            flex-wrap: wrap;
        }

        .checkbox-container label {
            display: inline-block;
            cursor: pointer;
            padding: 8px 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f9f9f9;
            margin-right: 10px;
            margin-bottom: 10px;
        }

        .checkbox-container label:hover {
            background-color: #e9e9e9;
        }

        .checkbox-container input[type="checkbox"] {
            display: none;
        }

        .checkbox-container input[type="checkbox"] + label {
            display: inline-block;
            width: auto;
        }

        .checkbox-container input[type="checkbox"]:checked + label {
            background-color: #007bff;
            color: #fff;
            border-color: #007bff;
        }

        .image-container {
            margin-top: 20px;
            display: flex;
            flex-wrap: wrap;
        }

        .image-container-item {
            position: relative;
            margin-right: 10px;
            margin-bottom: 10px;
            width: 200px;
            height: 200px;
            overflow: hidden;
        }

        .image-container-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .delete-button {
            position: absolute;
            top: 0;
            right: 0;
            background-color: rgba(255, 0, 0, 0.7);
            color: white;
            border: none;
            border-radius: 50%;
            cursor: pointer;
            padding: 5px;
        } */
    </style>
</head>
<link rel="stylesheet" href="{{asset("../resources/css/productEdit.css")}}">

<body>
    <div style="max-width: 600px; margin: 0 auto;">
        <h2 style="text-align: center;">Cập nhật sản phẩm</h2>
        <form id="updateProduct" enctype="multipart/form-data">
            <label for="name">Tên:</label>
            <input type="text" id="name" name="name" required>

            <label for="description">Mô tả:</label>
            <textarea id="description" name="description" rows="4" required></textarea>

            <label for="ingredient">Nguyên liệu:</label>
            <textarea id="ingredient" name="ingredient" rows="4" required></textarea>

            <label for="calories">Calories:</label>
            <input type="number" id="calories" name="calo" required>

            <label for="quantity">Số lượng:</label>
            <input type="number" id="quantity" name="quantity" required>

            <label for="price">Đơn giá:</label>
            <input type="number" id="price" name="price" required>

            <label for="discount">Phần trăm giảm giá:</label>
            <input type="number" id="discount" name="discount">

            <label>Danh mục:</label>
            <div id="categoryCheckboxes" class="checkbox-container"></div>

            <label for="image">Ảnh mới:</label>
            <input type="file" id="image" name="image[]" multiple accept="image/*">

            <div id="image-preview" class="image-container"></div>

            <input type="submit" class="button green" value="Sửa" style="margin-top: 15px;">
        </form>
    </div>

    <script>
        const currentURL = window.location.href;
        const urlParts = currentURL.split('/');
        const id = parseInt(urlParts[urlParts.length - 1]);

        let selectedImages = []; // Mảng lưu trữ các ảnh đã chọn
        let previousImageCount = 0; // Biến lưu trữ số lượng ảnh đã chọn trước đó

        fetch(BASE_URL_API+'product/' + id)
            .then(function (response) {
                return response.json();
            })
            .then(function (product) {
                document.getElementById('name').value = product.name;
                document.getElementById('description').value = product.description;
                document.getElementById('ingredient').value = product.ingredient;
                document.getElementById('calories').value = product.calo;
                document.getElementById('quantity').value = product.quantity;
                document.getElementById('price').value = product.price;
                document.getElementById('discount').value = product.discount;

                const imageContainer = document.getElementById('image-preview');
                product.image.forEach(function (image) {
                    const imgContainer = document.createElement('div');
                    imgContainer.classList.add('image-container-item');

                    const img = document.createElement('img');
                    img.src = BASE_URL_IMAGE + image.imgurl;
                    imgContainer.appendChild(img);

                    const deleteBtn = document.createElement('button');
                    deleteBtn.classList.add('delete-button');
                    deleteBtn.innerHTML = 'X';
                    imgContainer.appendChild(deleteBtn);

                    deleteBtn.addEventListener('click', function () {
                        imgContainer.remove();
                        const index = selectedImages.indexOf(image.imgurl);
                        if (index > -1) {
                            selectedImages.splice(index, 1);
                        }
                    });

                    imageContainer.appendChild(imgContainer);
                    selectedImages.push(image.imgurl); // Thêm ảnh vào mảng đã chọn
                });
                previousImageCount = product.image.length; // Cập nhật số lượng ảnh đã chọn
            })
            .catch(function (error) {
                console.error('Lỗi hiển thị dữ liệu sản phẩm', error);
            });

        fetch(BASE_URL_API+'category')
            .then(function (response) {
                return response.json();
            })
            .then(function (categories) {
                generateCategoryCheckboxes(categories.categories);
            })
            .catch(function (error) {
                console.error('Error fetching categories:', error);
            });

        fetch(BASE_URL_API+'category/product/' + id)
            .then(function (response) {
                return response.json();
            })
            .then(function (productCategories) {
                productCategories.forEach(function (category) {
                    const checkbox = document.getElementById('category-' + category.id);
                    if (checkbox) {
                        checkbox.checked = true;
                    }
                });
            })
            .catch(function (error) {
                console.error('Lỗi hiển thị sản phẩm:', error);
            });

        function generateCategoryCheckboxes(categories) {
            const categoryCheckboxes = document.getElementById('categoryCheckboxes');
            categoryCheckboxes.innerHTML = '';

            categories.forEach(function (category) {
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.id = 'category-' + category.id;
                checkbox.name = 'categories';
                checkbox.value = category.id;
                categoryCheckboxes.appendChild(checkbox);

                const label = document.createElement('label');
                label.htmlFor = 'category-' + category.id;
                label.textContent = category.name;
                categoryCheckboxes.appendChild(label);

                categoryCheckboxes.appendChild(document.createElement('br'));
            });
        }

        document.getElementById('image').addEventListener('change', function (event) {
            const files = event.target.files;
            const imageContainer = document.getElementById('image-preview');

            // Xóa các ảnh đã chọn trước đó nếu có
            if (selectedImages.length > previousImageCount) {
                const deleteCount = selectedImages.length - previousImageCount;
                for (let i = 0; i < deleteCount; i++) {
                    imageContainer.lastChild.remove();
                    selectedImages.pop();
                }
            }

            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const reader = new FileReader();

                reader.onload = function (event) {
                    const imgContainer = document.createElement('div');
                    imgContainer.classList.add('image-container-item');

                    const img = document.createElement('img');
                    img.src = event.target.result;
                    imgContainer.appendChild(img);

                    const deleteBtn = document.createElement('button');
                    deleteBtn.classList.add('delete-button');
                    deleteBtn.innerHTML = 'X';
                    imgContainer.appendChild(deleteBtn);

                    deleteBtn.addEventListener('click', function () {
                        imgContainer.remove();
                        const index = selectedImages.indexOf(img.src);
                        if (index > -1) {
                            selectedImages.splice(index, 1);
                        }
                    });

                    imageContainer.appendChild(imgContainer);
                    selectedImages.push(img.src); // Thêm ảnh vào mảng đã chọn
                };

                reader.readAsDataURL(file);
            }
        });

        document.getElementById('updateProduct').addEventListener('submit', function (event) {
            event.preventDefault();

            const form = document.getElementById('updateProduct');
            const formData = new FormData(form);

            const categories = form.querySelectorAll('input[name="categories"]:checked');
            const selectedCategories = [];
            categories.forEach(function (category) {
                selectedCategories.push(category.value);
            });
            formData.append('categories', JSON.stringify(selectedCategories));

            const oldImages = [];
            const oldImageContainers = document.querySelectorAll('.image-container-item img');
            oldImageContainers.forEach(function (img) {
                const lastSlashIndex = img.src.lastIndexOf('/');
                const filename = img.src.substring(lastSlashIndex + 1);

                if (!filename.startsWith('blob:') && (filename.endsWith('.jpg') || filename.endsWith('.png'))) {
                    oldImages.push(filename);
                }
            });
            formData.set('old_image', JSON.stringify(oldImages));

            // Thêm danh sách các ảnh đã chọn vào FormData
            selectedImages.forEach(function (imgUrl) {
                formData.append('selected_images[]', imgUrl);
            });

            if (!formData.has('image[]')) {
                const imageFiles = document.getElementById('image').files;
                for (let i = 0; i < imageFiles.length; i++) {
                    formData.append('image[]', imageFiles[i]);
                }
            }

            console.log('FormData before sending:');
            formData.forEach(function (value, key) {
                console.log(key, value);
            });

            fetch(BASE_URL_API+'product/' + id, {
                method: 'POST',
                body: formData
            })
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    alert(data.message);
                })
                .catch(function (error) {
                    console.error('Error updating product:', error);
                });
        });
    </script>
</body>

</html>

@endsection









<!-- <div class="main-section">
    <h1>Add Product</h1>
    <form id="addProductForm" class="card" enctype="multipart/form-data">
        <div class="card-content">
            <div class="field">
                <label class="label" for="name">Product Name:</label>
                <input type="text" id="name" name="name" class="input" required>
            </div>
            <div class="field">
                <label class="label" for="description">Description:</label>
                <textarea id="description" name="description" class="textarea" required></textarea>
            </div>
            <div class="field">
                <label class="label" for="ingredient">Ingredient:</label>
                <textarea id="ingredient" name="ingredient" class="textarea" required></textarea>
            </div>
            <div class="field">
                <label class="label" for="calo">Calo:</label>
                <input type="number" id="calo" name="calo" class="input" required>
            </div>
            <div class="field">
                <label class="label" for="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" class="input" required>
            </div>
            <div class="field">
                <label class="label" for="price">Price:</label>
                <input type="number" id="price" name="price" class="input" required>
            </div>
            <div class="field">
                <label class="label" for="discount">Discount:</label>
                <input type="number" id="discount" name="discount" class="input" required>
            </div>
            <div class="field">
            <div class="field">
                <label class="label" for="image">Product Images:</label>
                <div id="productImageContainer" class="image-container" style="display: none;"></div>
            </div>
                <div class="file">
                    <label class="file-label">
                        <input class="file-input" type="file" id="image" name="image" accept="image/*" required onchange="previewImage(event)">
                        <span class="file-cta">
                            <span class="file-icon">
                                <i class="fas fa-upload"></i>
                            </span>
                            <span class="file-label">
                                Choose a file…
                            </span>
                        </span>
                    </label>
                </div>
                <img id="productImagePreview" src="#" alt="Product Image Preview" class="image-preview" style="display: none;">
            </div>
        </div>
        <div class="card-footer">
            <button type="submit" class="button green">Add Product</button>
        </div>
    </form>
</div>

<script>

    // Hàm lấy dữ liệu sản phẩm từ API
    function getProductData(id) {
        fetch(BASE_URL + "product/" + id)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Hiển thị dữ liệu lên form
                document.getElementById('name').value = data.name || "";
                document.getElementById('description').value = data.description || "";
                document.getElementById('ingredient').value = data.ingredient || "";
                document.getElementById('calo').value = data.calo || 0;
                document.getElementById('quantity').value = data.quantity || 0;
                document.getElementById('price').value = data.price || 0;
                document.getElementById('discount').value = data.discount || 0;

                if (data.image && data.image.length > 0) {
                const imageContainer = document.getElementById('productImageContainer');
                imageContainer.innerHTML = ""; // Xóa nội dung hiện có

                data.image.forEach(img => {
                    const imageUrl = BASE_URL_IMAGE + img.imgurl;
                    const imgElement = document.createElement('img');
                    imgElement.src = imageUrl;
                    imgElement.alt = "Product Image";
                    imgElement.className = "image-preview";
                    imageContainer.appendChild(imgElement);
                });

                // Hiển thị container chứa ảnh
                imageContainer.style.display = 'block';
            }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Hàm xem trước hình ảnh sản phẩm
    function previewImage(event) {
    const input = event.target;
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('productImagePreview').src = e.target.result;
            document.getElementById('productImagePreview').style.display = 'block';
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function deleteImage(index) {
    const imageContainer = document.getElementById('productImageContainer');
    imageContainer.removeChild(imageContainer.childNodes[index]);
}
    // Sự kiện submit form
    document.getElementById('addProductForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var formData = new FormData(this);
        fetch(BASE_URL + "product/add", {
            method: 'POST',
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
            alert('Failed to add product');
        });
    });

    // Lấy id sản phẩm từ URL
    const currentURL = window.location.href;
    const urlParts = currentURL.split('/');
    const id = parseInt(urlParts[urlParts.length - 1]);

    // Gọi hàm để lấy dữ liệu sản phẩm từ API
    getProductData(id);
</script> -->