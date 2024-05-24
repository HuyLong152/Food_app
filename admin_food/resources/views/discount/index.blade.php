@extends("layout")
@section("content")
    <style>
        /* th, td {
            padding: 12px; 
            text-align: left; 
            border-bottom: 1px solid #ddd; 
        }

        .discount-code,
        .discount-percent,
        .create-time,
        .expiration-time,
        .number-uses {
            font-weight: bold;
            font-size: 1rem; 
            color: #333; 
        }

        .discount-percent {
            color: #4caf50; 
        }

        .expiration-time,
        .number-uses {
            color: #666; 
        }

        .action-buttons {
            display: flex;
            align-items: center; 
        }

        .action-button {
            padding: 8px 12px;
            border-radius: 5px; 
            cursor: pointer;
            background-color: transparent; 
            border: none; 
            transition: background-color 0.3s ease; 
        }
        .action-button.view {
            background-color: #4caf50; 
            color: white;
        }

        .action-button.edit {
            background-color: #f44336; 
            color: white; 
        }

        .edit-button {
            margin-left: 10px; 
        }

        .mdi {
            font-size: 1.2rem;
        }

        .status-text {
            padding: 8px 12px;
            border-radius: 5px;
            font-weight: bold;
            color: white;
        }

        .status-available {
            background-color: #4caf50;
        }

        .status-unavailable {
            background-color: #f44336; 
        }
        .pagination-button.active {
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
    <link rel="stylesheet" href="{{asset("../resources/css/discountIndex.css")}}">

<body>
<div class="flex items-center justify-between">
    <div style="margin-right: 10px; margin-left: 10px;">
        <a href="{{ route("discount.create") }}" class="button green">Thêm mã giảm giá</a>
    </div>
    <div class="navbar-item">
        <div class="search-control control">
            <input id="searchInput" placeholder="Tìm kiếm sản phẩm..." class="input">
        </div>
        <div class="dropdown">
            <button id="sortButton" class="button">Sắp xếp</button>
            <div class="dropdown-content">
                <a href="#" onclick="sortProducts('id')">Sắp xếp theo ID</a>
                <a href="#" onclick="sortProducts('discount_code')">Sắp xếp theo mã</a>
                <a href="#" onclick="sortProducts('discount_percent')">Sắp xếp theo phần trăm</a>
                <a href="#" onclick="sortProducts('available')">Sắp xếp theo trạng thái</a>
                <a href="#" onclick="sortProducts('number_uses')">Sắp xếp theo lượt dùng</a>
            </div>
        </div>
    </div>
</div>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Mã giảm giá</th>
                <th>Phần trăm giảm giá</th>
                <th>Trạng thái</th> <!-- Giữ nguyên cột này -->
                <th>Thời gian tạo</th>
                <th>Ngày hết hạn</th>
                <th>Lượt sử dụng</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody id="discountList">
            <!-- Dữ liệu mã giảm giá sẽ được thêm vào đây -->
        </tbody>
    </table>
    <div id="pagination"></div>

    <script>
        let cachedProductsData = [];
        let sortedProductsData = [];
        const PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
        let currentPage = 1; // Trang hiện tại, bắt đầu từ 1


        document.addEventListener("DOMContentLoaded", function() {
            fetchDiscounts();
        });

        function fetchDiscounts() {
        if (cachedProductsData.length === 0) {
            fetch(BASE_URL_API+"discount")
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
                fetchDiscounts();
            });
            pagination.appendChild(pageButton);
        }
    }
    function displayItems(items) {
        const itemList = document.getElementById('discountList');
        itemList.innerHTML = '';
        items.forEach(discount => {
                const row = document.createElement('tr');

                // Thêm cột ID
                addCell(row, discount.id, "ID");

                // Thêm cột Mã giảm giá
                addCell(row, discount.discount_code, "Mã giảm giá");

                // Thêm cột Phần trăm giảm giá
                addCell(row, discount.discount_percent + '%', "Phần trăm giảm giá");

                // Thêm cột Trạng thái với lớp màu nền tương ứng
                addCell(row, discount.available ? 'Có sẵn' : 'Không có sẵn', "Trạng thái", getStatusClass(discount.available));

                // Thêm cột Thời gian tạo
                addCell(row, discount.create_time, "Thời gian tạo");

                // Kiểm tra và hiển thị expiration_time
                const expirationTime = discount.expiration_time ? discount.expiration_time : 'Vô thời hạn';

                // Kiểm tra và hiển thị number_uses
                const numberUses = discount.number_uses !== null ? discount.number_uses : 'Không giới hạn';

                // Thêm cột Expiration Time
                addCell(row, expirationTime, "Expiration Time");

                // Thêm cột Number of Uses
                addCell(row, numberUses, "Number of Uses");

                // Thêm cột Hành động
                const actionCell = document.createElement('td');
                const actionContainer = document.createElement('div');
                actionContainer.classList.add('action-buttons');

                // Thêm nút Xem
                const viewButton = document.createElement('button');
                viewButton.classList.add('action-button', 'view');
                viewButton.innerHTML = '<span class="mdi mdi-eye"></span>';
                viewButton.addEventListener('click', function () {
                    window.location.href = "{{ url('discount/edit')}}/" + discount.id;
                });
                actionContainer.appendChild(viewButton);

                // Thêm nút Sửa
                const editButton = document.createElement('button');
                editButton.classList.add('action-button', 'edit', 'edit-button');
                editButton.innerHTML = '<span class="mdi mdi-trash-can"></span>';
                editButton.addEventListener('click', function () {
                    if (confirm('Bạn có chắc chắn muốn xóa mã giảm giá này ?')) {
                        deleteDiscount(discount.id);
                    }
                });
                actionContainer.appendChild(editButton);

                actionCell.appendChild(actionContainer);
                row.appendChild(actionCell);

                // Thêm hàng vào bảng
                discountList.appendChild(row);
            });
    }


        // Hàm thêm cột dữ liệu vào hàng
        function addCell(row, data, columnName, statusClass) {
            const cell = document.createElement('td');
            cell.setAttribute('data-label', columnName);

            // Kiểm tra tên cột để quyết định sử dụng class nào cho văn bản
            if (columnName.toLowerCase() === 'trạng thái') {
                const statusText = document.createElement('span');
                statusText.textContent = data;
                statusText.classList.add('status-text', statusClass); // Sử dụng class status-text cho cột "Trạng thái"
                cell.appendChild(statusText);
            } else {
                const defaultText = document.createElement('span');
                defaultText.textContent = data;
                defaultText.classList.add('default-text'); // Sử dụng class default-text cho các cột khác
                cell.appendChild(defaultText);
            }

            row.appendChild(cell);
        }

        // Hàm trả về lớp màu nền tương ứng với trạng thái
        function getStatusClass(available) {
            return available ? 'status-available' : 'status-unavailable';
        }
        function deleteDiscount(id) {
            fetch(BASE_URL_API+`discount/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('Xóa mã giảm giá thành công!');
                    window.location.reload(); // Tải lại trang sau khi xóa thành công
                } else {
                    alert('Xóa mã giảm giá thất bại!');
                }
            })
            .catch(error => console.error('Error:', error));
        }
    function searchProduct(searchText) {
        fetch(BASE_URL_API+`discount/search/${searchText}`)
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
        switch (sortBy) {
            case 'id':
                return a.id - b.id;
            case 'number_uses': // Sắp xếp theo lượt dùng
                const aUses = typeof a.number_uses === 'number' ? a.number_uses : Number.MAX_SAFE_INTEGER;
                const bUses = typeof b.number_uses === 'number' ? b.number_uses : Number.MAX_SAFE_INTEGER;
                return aUses - bUses;
            case 'discount_code': // Sắp xếp theo mã giảm giá
                return a.discount_code.localeCompare(b.discount_code);
            case 'discount_percent': // Sắp xếp theo phần trăm giảm giá
                return a.discount_percent - b.discount_percent;
            case 'available': // Sắp xếp theo trạng thái
                return a.available - b.available;
            default:
                return 0; // Mặc định không thay đổi thứ tự
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
