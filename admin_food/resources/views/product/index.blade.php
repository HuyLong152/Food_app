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

    .dropdown-content {
        display: none;
        position: absolute;
        background-color: #f9f9f9;
        right: 0;
        min-width: 160px;
        box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);
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
<link rel="stylesheet" href="{{asset("../resources/css/productIndex.css")}}">

<div class="flex items-center justify-between">
    <div style="margin-right: 10px; margin-left: 10px;">
        <a href="{{ route("product.create") }}" class="button green">Thêm món ăn</a>
    </div>
    <div class="navbar-item">
        <div class="search-control control">
            <input id="searchInput" placeholder="Tìm kiếm sản phẩm..." class="input">
        </div>
        <div class="dropdown">
            <button id="sortButton" class="button">Sắp xếp</button>
            <div class="dropdown-content">
                <a href="#" onclick="sortProducts('name')">Sắp xếp theo tên</a>
                <a href="#" onclick="sortProducts('id')">Sắp xếp theo ID</a>
                <a href="#" onclick="sortProducts('calo')">Sắp xếp theo calo</a>
                <a href="#" onclick="sortProducts('quantity')">Sắp xếp theo số lượng</a>
                <a href="#" onclick="sortProducts('price')">Sắp xếp theo đơn giá</a>
                <a href="#" onclick="sortProducts('discount')">Sắp xếp theo giảm giá</a>
                <a href="#" onclick="sortProducts('updated_time')">Sắp xếp theo cập nhật</a>
            </div>
        </div>
    </div>
</div>
<table>
    <thead>
        <tr>
            <th>Id</th>
            <th>Tên</th>
            <th>Hình ảnh</th>
            <th>Tên danh mục</th>
            <th>Mô tả</th>
            <th>Nguyên liệu</th>
            <th>Calo</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>% Giảm giá</th>
            <th>Thời gian cập nhật</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody id="itemList"></tbody>
</table>
<div id="pagination"></div>

<script>
    let cachedProductsData = [];
    let sortedProductsData = [];
    const PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
    let currentPage = 1; // Trang hiện tại, bắt đầu từ 1

    document.addEventListener("DOMContentLoaded", function() {
        fetchProducts();
    });

    function fetchProducts() {
        if (cachedProductsData.length === 0) {
            fetch(BASE_URL_API + 'product')
                .then(response => response.json())
                .then(data => {
                    cachedProductsData = data;
                    sortedProductsData = [...cachedProductsData];
                    const totalProducts = sortedProductsData.length;
                    const totalPages = Math.ceil(totalProducts / PAGE_SIZE);
                    displayPagination(totalPages);
                    displayItems(sortedProductsData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
                })
                .catch(error => console.error('Error:', error));
        } else {
            const totalProducts = sortedProductsData.length;
            const totalPages = Math.ceil(totalProducts / PAGE_SIZE);
            displayPagination(totalPages);
            displayItems(sortedProductsData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
        }
    }

    function displayPagination(totalPages) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.classList.add('button', 'pagination-button', 'custom-color');
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', function() {
                currentPage = i;
                fetchProducts();
            });
            pagination.appendChild(pageButton);
        }
    }

    function displayItems(items) {
        const itemList = document.getElementById('itemList');
        itemList.innerHTML = '';
        items.forEach(item => {
            const row = document.createElement('tr');

            // Thêm các cột dữ liệu
            addCell(row, item.id, 'ID');
            addCell(row, item.name, 'Tên');
            if (item.image.length > 0) {
                addImageCell(row, BASE_URL_IMAGE + item.image[0].imgurl, 'Hình ảnh');
            }
            addCategoriesCell(row, item.categorie, 'Tên danh mục');
            addCell(row, item.description, 'Mô tả');
            addCell(row, item.ingredient, 'Nguyên liệu');
            addCell(row, item.calo, 'Calo');
            addCell(row, item.quantity, 'Số lượng');
            addCell(row, item.price, 'Đơn giá');
            addCell(row, item.discount, '% Giảm giá');
            addCell(row, item.updated_time, 'Thời gian cập nhật');

            // Thêm nút xóa và nút xem
            const actionsCell = document.createElement('td');
            actionsCell.classList.add('actions-cell');
            const buttonsDiv = document.createElement('div');
            buttonsDiv.classList.add('buttons', 'right', 'nowrap');

            const viewButton = document.createElement('button');
            viewButton.classList.add('button', 'small', 'green', '--jb-modal');
            viewButton.setAttribute('data-target', 'sample-modal-2');
            viewButton.innerHTML = '<span class="icon"><i class="mdi mdi-eye"></i></span>';
            viewButton.addEventListener('click', function() {
                window.location.href = "{{ url('product/edit')}}/" + item.id;
            });
            buttonsDiv.appendChild(viewButton);

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('button', 'small', 'red', '--jb-modal');
            deleteButton.setAttribute('data-target', 'sample-modal');
            deleteButton.type = 'button';
            deleteButton.innerHTML = '<span class="icon"><i class="mdi mdi-trash-can"></i></span>';
            deleteButton.addEventListener('click', function() {
                if (confirm('Bạn chắc chắn muốn xóa mục này?')) {
                    deleteItem(item.id);
                }
            });
            buttonsDiv.appendChild(deleteButton);

            actionsCell.appendChild(buttonsDiv);
            row.appendChild(actionsCell);

            itemList.appendChild(row);
        });
    }

    function addCell(row, data, label) {
        const cell = document.createElement('td');
        cell.textContent = data;
        cell.setAttribute('data-label', label);
        row.appendChild(cell);
    }

    function addImageCell(row, imageUrl, label) {
        const cell = document.createElement('td');
        const image = document.createElement('img');
        image.src = imageUrl;
        image.style.maxWidth = '100px';
        image.style.height = 'auto';
        cell.appendChild(image);
        cell.setAttribute('data-label', label);
        row.appendChild(cell);
    }

    function addCategoriesCell(row, categorie, label) {
        const cell = document.createElement('td');
        const categoryNames = categorie.map(category => category.name).join(', '); // Lấy tên của mỗi danh mục và nối chúng thành một chuỗi
        cell.textContent = categoryNames;
        cell.setAttribute('data-label', label);
        row.appendChild(cell);
    }

    function deleteItem(itemId) {
        console.log('Delete item with ID:', itemId);
        fetch(BASE_URL_API + `product/${itemId}`, { // Sửa BASE_URL từ 'items' thành 'product'
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Xóa mục thành công!');
                    window.location.reload(); // Tải lại trang sau khi xóa thành công
                } else {
                    alert('Xóa mục thất bại!');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function searchProduct(searchText) {
        fetch(`${BASE_URL_API}product/nameId/${searchText}`)
            .then(response => response.json())
            .then(data => {
                cachedProductsData = data;
                displayItems(cachedProductsData);
            })
            .catch(error => console.error('Error:', error));
    }

    function sortProducts(sortBy) {
        sortedProductsData = [...cachedProductsData];
        sortedProductsData.sort((a, b) => {
            if (sortBy === 'name') {
                return a.name.localeCompare(b.name);
            } else if (sortBy === 'id') {
                return a.id - b.id;
            } else if (sortBy === 'calo') {
                return a.calo - b.calo;
            } else if (sortBy === 'quantity') {
                return a.quantity - b.quantity;
            } else if (sortBy === 'price') {
                return a.price - b.price;
            } else if (sortBy === 'discount') {
                return a.discount - b.discount;
            } else if (sortBy === 'updated_time') {
                return new Date(a.updated_time) - new Date(b.updated_time);
            }
        });
        currentPage = 1;
        displayItems(sortedProductsData.slice(0, PAGE_SIZE));
        displayPagination(Math.ceil(sortedProductsData.length / PAGE_SIZE));
    }

    document.getElementById('searchInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            const searchText = this.value.trim();
            if (searchText !== '') {
                searchProduct(searchText);
            }
        }
    });
</script>
@endsection
