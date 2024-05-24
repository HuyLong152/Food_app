@extends("layout")
@section("content")
<div class="main-section">
    <h1>Thêm danh mục</h1>
    <form id="addCategoryForm" class="card" enctype="multipart/form-data">
        <div class="card-content">
            <div class="field">
                <label class="label" for="name">Tên danh mục:</label>
                <input type="text" id="name" name="name" class="input" required>
            </div>
            <div class="field">
                <label class="label" for="image">Ảnh danh mục:</label>
                <div class="file">
                    <label class="file-label">
                        <input class="file-input" type="file" id="image" name="image" accept="image/*" required
                            onchange="previewImage(event)">
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
                <img id="categoryImagePreview" src="#" alt="Category Image Preview" class="image-preview"
                    style="display: none;">
            </div>
            <div class="card-footer">
                <button type="submit" class="button green">Tạo</button>
            </div>
        </div>
    </form>
</div>

<script>

    function previewImage(event) {
        const input = event.target;
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('categoryImagePreview').src = e.target.result;
                document.getElementById('categoryImagePreview').style.display = 'block';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    document.getElementById('addCategoryForm').addEventListener('submit', function (event) {
        event.preventDefault();

        // Tạo đối tượng FormData để gửi dữ liệu form
        var formData = new FormData(this);

        // Gửi yêu cầu POST đến API
        fetch(BASE_URL_API + "category", {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    // alert("Thêm danh mục thành công");
                    alert(response);
                }
                return response.json();
            })
            .then(data => {
                alert("Thêm danh mục thành công");
                // alert(data.message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Thêm danh mục thất bại');
            });
    });
</script>
@endsection