@extends("layout")
@section("content")
<style>
     /* .pagination-button.active {
        background-color: #007bff;
        color: #fff;
    }

    .custom-color {
        background-color: #ffffff;
        color: #000000;
    }

    .custom-color:hover {
        background-color: #ff6600;
        color: #fff;
    }

    .navbar-item {
        display: flex;
        align-items: center;
    }

    .search-control {
        flex: 1;
        margin-right: 10px;
    }

    .dropdown {
        position: relative;
        margin-left: 10px;
    }
    .fix-image-size{
        width: 200px;
        height: 200px;
        object-fit: cover;
    }
    .dropdown-content {
        display: none;
        position: absolute;
        background-color: #f9f9f9;
        right: 0;
        min-width: 160px;
        box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
        z-index: 1;
    }

    .dropdown-content a {
        color: black;
        padding: 12px 16px;
        text-decoration: none;
        display: block;
    }

    .dropdown-content a:hover {
        background-color: #f1f1f1;
    }

    .dropdown:hover .dropdown-content {
        display: block;
    } */
    </style>
    <link rel="stylesheet" href="{{asset("../resources/css/categoryIndex.css")}}">

<div class="flex items-center justify-between">
    <div style="margin-right: 10px; margin-left: 10px;">
        <a href="{{ route("category.create") }}" class="button green">Thêm danh mục</a>
    </div>
    <div class="navbar-item">
        <div class="search-control control">
            <input id="searchInput" placeholder="Tìm kiếm danh mục..." class="input">
        </div>
        <div class="dropdown">
            <button id="sortButton" class="button">Sắp xếp</button>
            <div class="dropdown-content">
                <a href="#" onclick="sortCategories('name')">Sắp xếp theo tên</a>
                <a href="#" onclick="sortCategories('id')">Sắp xếp theo ID</a>
                <a href="#" onclick="sortCategories('created_time')">Sắp xếp theo ngày</a>
            </div>
        </div>
    </div>
</div>
<table>
    <thead>
        <tr>
            <th>Id</th>
            <th>Tên danh mục</th>
            <th>Ảnh minh họa</th>
            <th>Thời gian tạo</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody id="categoryList"></tbody>
</table>
<div id="pagination" class="flex justify-center"></div>
<script>
  let cachedCategoriesData = [];
  let sortedCategoriesData = [];
  const PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
  let currentPage = 1; // Trang hiện tại, bắt đầu từ 1

    document.addEventListener("DOMContentLoaded", function() {
        fetchCategories();
    });
    function fetchCategories() {
    if (cachedCategoriesData.length === 0) {

        // Nếu không có dữ liệu đã tải trước đó, thì mới gọi API để tải dữ liệu mới
        fetch(BASE_URL_API + 'category')
            .then(response => response.json())
            .then(data => {
                cachedCategoriesData = data.categories; // Lưu trữ dữ liệu vào biến cachedCategoriesData
                sortedCategoriesData = [...cachedCategoriesData];
                const totalCategories = sortedCategoriesData.length;
                const totalPages = Math.ceil(totalCategories / PAGE_SIZE);
                displayPagination(totalPages);
                displayCategories(sortedCategoriesData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
            })
            .catch(error => console.error('Error:', error));
    } else {
        // Nếu có dữ liệu đã tải trước đó, sử dụng dữ liệu đó để hiển thị
        const totalCategories = sortedCategoriesData.length;
        const totalPages = Math.ceil(totalCategories / PAGE_SIZE);
        displayPagination(totalPages);
        displayCategories(sortedCategoriesData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
    }
}

    function displayPagination(totalPages) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.classList.add('button','pagination-button','custom-color');
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', function() {
                currentPage = i;
                fetchCategories();
            });
            pagination.appendChild(pageButton);
        }
    }
    function displayCategories(categories) {
        const categoryList = document.getElementById('categoryList');
        categoryList.innerHTML = '';
        categories.forEach(category => {
            const row = document.createElement('tr');

            const idCell = document.createElement('td');
            idCell.textContent = category.id;
            idCell.setAttribute('data-label', 'ID')
            row.appendChild(idCell);

            const nameCell = document.createElement('td');
            nameCell.textContent = category.name;
            nameCell.setAttribute('data-label', 'Tên danh mục')
            row.appendChild(nameCell);

            const imageCell = document.createElement('td');
            const image = document.createElement('img');
            image.src = BASE_URL_CATEGORY + category.image_url;
            image.style.maxWidth = '300px';
            image.style.height = '200px';
            image.style.objectFit = "cover";
            imageCell.appendChild(image);
            imageCell.setAttribute('data-label', 'Ảnh minh họa');
            // imageCell.classList.add("fix-image-size");
            row.appendChild(imageCell);

            const createdCell = document.createElement('td');
            const small = document.createElement('small');
            small.classList.add('text-gray-900');
            small.title = category.created_time;
            small.textContent = category.created_time;
            createdCell.appendChild(small);
            small.style.fontSize = '16px';
            createdCell.setAttribute('data-label', 'Thời gian tạo')
            row.appendChild(createdCell);

            const actionsCell = document.createElement('td');
            const buttonsDiv = document.createElement('div');
            buttonsDiv.classList.add('buttons', 'nowrap','actions-buttons-left');

            const viewButton = document.createElement('a');
            viewButton.classList.add('button', 'small', 'green', '--jb-modal');
            viewButton.setAttribute('data-target', 'sample-modal-2');
            viewButton.setAttribute('href', "{{ url('category/edit') }}/" + category.id);
            const viewIcon = document.createElement('span');
            viewIcon.classList.add('icon');
            viewIcon.innerHTML = '<i class="mdi mdi-eye"></i>';
            viewButton.appendChild(viewIcon);
            buttonsDiv.appendChild(viewButton);

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('button', 'small', 'red', '--jb-modal');
            deleteButton.setAttribute('data-target', 'sample-modal');
            deleteButton.type = 'button';

            const deleteIcon = document.createElement('span');
            deleteIcon.classList.add('icon');
            deleteIcon.innerHTML = '<i class="mdi mdi-trash-can"></i>';
            deleteButton.appendChild(deleteIcon);

            // Bắt sự kiện click vào nút xóa
            deleteButton.addEventListener('click', function() {
                // Hiển thị hộp thoại xác nhận
                if (confirm('Bạn chắc chắn muốn xóa danh mục này?')) {
                    // Gọi hàm để xử lý việc xóa category
                    deleteCategory(category.id);
                }
            });

            buttonsDiv.appendChild(deleteButton);
            actionsCell.appendChild(buttonsDiv);
            row.appendChild(actionsCell);

            categoryList.appendChild(row);
        });
    }

    function addCategory() {
        // Redirect to add category page or show a modal form
        console.log('Add category button clicked');
        // Example: window.location.href = '/add-category';
    }

    function editCategory(categoryId) {
        console.log('Edit category with ID:', categoryId);
        // Redirect or other functionality
    }

    function deleteCategory(categoryId) {
    console.log('Delete category with ID:', categoryId);
    fetch(BASE_URL_API +`category/${categoryId}`, {
        method: 'DELETE'
    })
    .then(response => {
        // Xử lý phản hồi từ server sau khi xóa thành công
        if (response.ok) {
            alert('Xóa danh mục thành công!');
            window.location.reload(); // Tải lại trang sau khi xóa thành công
        } else {
            alert('Xóa danh mục thất bại!');
        }
    })
    .catch(error => console.error('Error:', error));
}
document.getElementById('searchInput').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        const searchText = this.value;
        searchCategory(searchText);
    console.log(searchText);
    }
});
// Hàm gọi API tìm kiếm category
function searchCategory(searchText) {
    console.log(searchText);
    fetch(`${BASE_URL_API}category/search/${searchText}`)
        .then(response => response.json())
        .then(data => {
            cachedCategoriesData = data.categories
            displayCategories(cachedCategoriesData);
        })
        .catch(error => console.error('Error:', error));
}
function sortCategories(sortBy) {
    sortedCategoriesData = [...cachedCategoriesData]; // Sử dụng dữ liệu đã tải trước đó
    sortedCategoriesData.sort((a, b) => {
        if (sortBy === 'name') {
            return a.name.localeCompare(b.name);
        } else if (sortBy === 'id') {
            return a.id - b.id;
        } else if (sortBy === 'created_time') {
            return new Date(a.created_time) - new Date(b.created_time);
        }
    });
    currentPage = 1; // Reset trang về trang đầu tiên sau khi sắp xếp
    displayCategories(sortedCategoriesData.slice(0, PAGE_SIZE)); // Sử dụng sortedCategoriesData
    displayPagination(Math.ceil(sortedCategoriesData.length / PAGE_SIZE)); // Sử dụng sortedCategoriesData
}
</script>
@endsection
