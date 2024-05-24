@extends("layout")
@section("content")
<style>
    /* th,
    td {
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
<link rel="stylesheet" href="{{asset("../resources/css/search.css")}}">

<div class="flex items-center justify-between">
    <!-- <div style="margin-right: 10px; margin-left: 10px;">
            <a href="{{ route("discount.create") }}" class="button green">Thêm mã giảm giá</a>
        </div> -->
    <div class="navbar-item">
        <div class="search-control control">
            <input id="searchInput" placeholder="Tìm kiếm sản phẩm..." class="input">
        </div>
        <div class="dropdown">
            <button id="sortButton" class="button">Sắp xếp</button>
            <div class="dropdown-content">
                <a href="#" onclick="sortProducts('id')">Sắp xếp theo ID</a>
                <a href="#" onclick="sortProducts('text_search')">Sắp xếp theo từ khóa</a>
                <a href="#" onclick="sortProducts('number_search')">Sắp xếp theo lượt tìm kiếm</a>
            </div>
        </div>
    </div>
</div>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Từ khóa</th>
            <th>Lượt tìm kiếm</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody id="searchList">
        <!-- Dữ liệu sản phẩm tìm kiếm sẽ được thêm vào đây -->
    </tbody>
</table>
<div id="pagination"></div>

<script>
    let cachedSearchData = [];
    let sortedSearchsData = [];
    const PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
    let currentPage = 1; // Trang hiện tại, bắt đầu từ 1

    document.addEventListener("DOMContentLoaded", function () {
        fetchSearchResults();
    });
    function fetchSearchResults() {
        if (cachedSearchData.length === 0) {
            fetch(BASE_URL_API+"hotsearch")
                .then(response => response.json())
                .then(data => {
                    cachedSearchData = data;
                    sortedSearchsData = [...cachedSearchData];
                    const totalProducts = sortedSearchsData.length;
                    const totalPages = Math.ceil(totalProducts / PAGE_SIZE);
                    displayPagination(totalPages);
                    displayItems(sortedSearchsData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
                })
                .catch(error => console.error('Error:', error));
        } else {
            const totalProducts = sortedSearchsData.length;
            const totalPages = Math.ceil(totalProducts / PAGE_SIZE);
            displayPagination(totalPages);
            displayItems(sortedSearchsData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
        }
    }
    // function fetchSearchResults() {
    //     const searchText = document.getElementById('searchInput').value.trim();
    //     if (searchText !== '') {
    //         fetch(BASE_URL_API+`search/${searchText}`)
    //             .then(response => response.json())
    //             .then(data => {
    //                 cachedSearchData = data;
    //                 const totalResults = cachedSearchData.length;
    //                 const totalPages = Math.ceil(totalResults / PAGE_SIZE);
    //                 displayPagination(totalPages);
    //                 displayItems(cachedSearchData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
    //             })
    //             .catch(error => console.error('Error:', error));
    //     }
    // }

    // Hàm hiển thị phân trang
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
            pageButton.addEventListener('click', function () {
                currentPage = i;
                fetchSearchResults();
            });
            pagination.appendChild(pageButton);
        }
    }

    // Hàm hiển thị kết quả tìm kiếm
    function displayItems(items) {
        const itemList = document.getElementById('searchList');
        itemList.innerHTML = '';
        items.forEach(result => {
            const row = document.createElement('tr');

            // Thêm cột ID
            addCell(row, result.id, "ID");

            // Thêm cột Từ khóa
            addCell(row, result.text_search, "Từ khóa");

            // Thêm cột Lượt tìm kiếm
            addCell(row, result.number_search, "Lượt tìm kiếm");

            // Thêm cột Hành động
            const actionCell = document.createElement('td');
            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Xóa';
            deleteButton.classList.add('action-button', 'edit', 'edit-button');
            deleteButton.addEventListener('click', function () {
                if (confirm('Bạn có chắc chắn muốn xóa kết quả tìm kiếm này ?')) {
                    deleteSearchResult(result.id);
                }
            });
            actionCell.appendChild(deleteButton);
            row.appendChild(actionCell);

            // Thêm hàng vào bảng
            searchList.appendChild(row);
        });
    }

    // Hàm thêm cột dữ liệu vào hàng
    function addCell(row, data, columnName) {
        const cell = document.createElement('td');
        cell.setAttribute('data-label', columnName);
        const defaultText = document.createElement('span');
        defaultText.textContent = data;
        defaultText.classList.add('default-text');
        cell.appendChild(defaultText);
        row.appendChild(cell);
    }

    // Hàm xóa kết quả tìm kiếm
    function deleteSearchResult(id) {
        fetch(BASE_URL_API+`hotsearch/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Xóa kết quả tìm kiếm thành công!');
                    // fetchSearchResults(); // Tải lại kết quả tìm kiếm sau khi xóa thành công
                    window.location.reload();
                } else {
                    alert('Xóa kết quả tìm kiếm thất bại!');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    document.getElementById('searchInput').addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            fetchSearchResults();
        }
    });
    function searchProduct(searchText) {
        fetch(BASE_URL_API+`hotsearch/search/${searchText}`)
            .then(response => response.json())
            .then(data => {
                cachedSearchData = data;
                displayItems(cachedSearchData);
            })
            .catch(error => console.error('Error:', error));
    }
    document.getElementById('searchInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            const searchText = this.value.trim();
            if (searchText !== '') {
                searchProduct(searchText);
            }
        }
    });
    function sortProducts(sortBy) {
        sortedSearchsData = [...cachedSearchData];
        sortedSearchsData.sort((a, b) => {
            switch (sortBy) {
                case 'id':
                    return a.id - b.id;
                case 'text_search': // Sắp xếp theo từ khóa
                    return a.text_search.localeCompare(b.text_search);
                case 'number_search': // Sắp xếp theo lượt tìm kiếm
                    return b.number_search-a.number_search;
                default:
                    return 0; // Mặc định không thay đổi thứ tự
            }
        });
        console.log(sortedSearchsData);

        currentPage = 1;
        displayItems(sortedSearchsData.slice(0, PAGE_SIZE));
        displayPagination(Math.ceil(sortedSearchsData.length / PAGE_SIZE));
    }

</script>
@endsection