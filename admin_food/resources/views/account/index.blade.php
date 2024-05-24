@extends("layout")
@section("content")
<style>
    /* .status-active {
        background-color: #4caf50;
    }

    .status-inactive {
        background-color: #f44336;
    }

    .status-block {
        background-color: #ff9800;
    }

    .status-admin {
        background-color: #2196f3;
    }

    .status-staff {
        background-color: #9c27b0;
    }

    .status-customer {
        background-color: #ff5722;
    }

    .status-text {
        padding: 5px 10px;
        border-radius: 5px;
        font-weight: bold;
        cursor: default;
        color: white; 
    }

    .role-text {
        padding: 5px 10px;
        border-radius: 5px;
        font-weight: bold;
        cursor: default;
        color: white;
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
<link rel="stylesheet" href="{{asset("../resources/css/accountIndex.css")}}">
<div class="navbar-item">
    <div class="search-control control">
        <input id="searchInput" placeholder="Tìm kiếm tài khoản..." class="input">
    </div>
    <div class="dropdown">
        <button id="sortButton" class="button">Sắp xếp</button>
        <div class="dropdown-content">
            <a href="#" onclick="sortAccounts('name')">Theo tên</a>
            <a href="#" onclick="sortAccounts('role')">Theo vai trò</a>
            <a href="#" onclick="sortAccounts('customer_id')">Theo ID khách hàng</a>
            <a href="#" onclick="sortAccounts('id')">Theo ID tài khoản</a>
            <a href="#" onclick="sortAccounts('status')">Theo trạng thái</a>
        </div>
    </div>
</div>

<table>
    <thead>
        <tr>
            <th>ID tài khoản</th>
            <th>ID Khách hàng</th>
            <th>Tên tài khoản</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Thời gian tạo</th>
            <th>Hành động</th>
        </tr>
    </thead> 
    <tbody id="accountList">
    </tbody>
</table>
<div id="pagination" class="pagination"></div>
<div id="pagination"></div>
<script>
    let canClick = true;
    var userID = localStorage.getItem('userID');
    let cachedProductsData = [];
    let sortedProductsData = [];
    const PAGE_SIZE = 10; 
    let currentPage = 1; 

    fetch(BASE_URL_API+'account/getInfor/' + userID)
        .then(response => {
            if (!response.ok) {
                throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
            }
            return response.json();
        })
        .then(data => {

            // Kiểm tra và vô hiệu hóa nút nếu vai trò là staff
            if(data.role === 'staff'){
                canClick = false;
            }
        })
        .catch(error => {
            console.error(error.message);
            alert('Đã xảy ra lỗi khi lấy thông tin tài khoản!');
        });
    // })

    document.addEventListener("DOMContentLoaded", function() {
        fetchAccounts();
    });

    function fetchAccounts(){
        if (cachedProductsData.length === 0) {
            fetch(BASE_URL_API+'account')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                cachedProductsData = data;
                    sortedProductsData = [...cachedProductsData];
                    const totalProducts = sortedProductsData.length;
                    const totalPages = Math.ceil(totalProducts / PAGE_SIZE);
                    displayPagination(totalPages);
                    displayItems(sortedProductsData.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE));
            })
            .catch(error => {console.error(error.message);});
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
                fetchAccounts();
            });
            pagination.appendChild(pageButton);
        }
    }

    function displayItems(accounts){
        const itemList = document.getElementById('accountList');
        itemList.innerHTML = '';
        accounts.forEach(account => {
                    const row = document.createElement('tr');
                    addCell(row, account.id, "ID tài khoản");
                    addCell(row, account.customer_id, "ID khách hàng");
                    addCell(row, account.username, "Tên tài khoản");
                    addCell(row, account.role, "Vai trò", getRoleClass(account.role));
                    addCell(row, account.status, "Trạng thái", getStatusClass(account.status));
                    addCell(row, account.create_time, "Thời gian tạo");
                    const actionsCell = document.createElement('td');
                    actionsCell.classList.add('actions-cell');
                    const buttonsDiv = document.createElement('div');
                    buttonsDiv.classList.add('buttons', 'center', 'nowrap');

                    // xem
                    const viewButton = document.createElement('button');
                    viewButton.classList.add('button', 'small', 'green', '--jb-modal');
                    viewButton.setAttribute('data-target', 'sample-modal-2');
                    viewButton.type = 'button';
                    viewButton.innerHTML = '<span class="icon"><i class="mdi mdi-account-key"></i></span>';
                    viewButton.addEventListener('click', function () {
                        if(canClick){
                            if (account.status.toLowerCase() === 'active') {
                            if (confirm('Bạn có chắc chắn muốn khóa tài khoản này?')) {
                                fetch(BASE_URL_API+'account/' + account.id, {
                                    method: 'POST',
                                    body: JSON.stringify({}),
                                    headers: {
                                        'Content-Type': 'application/json'
                                    }
                                })
                                .then(response => {
                                    if (!response.ok) {
                                        throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                    }
                                    return response.json();
                                })
                                .then(data => {
                                    alert('Tài khoản đã được khóa thành công!');
                                    const statusCell = row.querySelector('.status-text');
                                    if (statusCell) {
                                        statusCell.textContent = 'block';
                                        statusCell.classList.remove('status-active', 'status-inactive');
                                        statusCell.classList.add('status-block');
                                    }
                                })
                                .catch(error => {
                                    console.error(error.message);
                                    alert('Đã xảy ra lỗi khi khóa tài khoản!');
                                });
                            }
                        } else if (account.status.toLowerCase() === 'block') {
                            if (confirm('Bạn có chắc chắn muốn mở khóa tài khoản này?')) {
                                // Gọi API để mở khóa tài khoản
                                fetch(BASE_URL_API+'account/' + account.id, {
                                    method: 'POST',
                                    body: JSON.stringify({}),
                                    headers: {
                                        'Content-Type': 'application/json'
                                    }
                                })
                                .then(response => {
                                    if (!response.ok) {
                                        throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                    }
                                    return response.json();
                                })
                                .then(data => {
                                    alert('Tài khoản đã được mở khóa thành công!');
                                    const statusCell = row.querySelector('.status-text');
                                    if (statusCell) {
                                        statusCell.textContent = 'active'; // Giả sử trạng thái đã được chuyển thành active
                                        statusCell.classList.remove('status-block', 'status-inactive');
                                        statusCell.classList.add('status-active');
                                    }
                                })
                                .catch(error => {
                                    console.error(error.message);
                                    alert('Đã xảy ra lỗi khi mở khóa tài khoản!');
                                });
                            }
                        } else {
                            alert('Không thể chuyển trạng thái của tài khoản này.');
                        }
                        }
                    });

                    buttonsDiv.appendChild(viewButton);

                    // nút
                    const viewMoreButton = document.createElement('button');
                    viewMoreButton.classList.add('button', 'small', 'blue', '--jb-modal');
                    viewMoreButton.setAttribute('data-target', 'sample-modal-3');
                    viewMoreButton.type = 'button';
                    viewMoreButton.innerHTML = '<span class="icon"><i class="mdi mdi-account-convert"></i></span>';
                    viewMoreButton.addEventListener('click', function () {
                    if(canClick){
                        if (account.role.toLowerCase() === 'customer') {
                        if (confirm('Bạn có chắc chắn muốn chuyển vai trò của tài khoản này thành nhân viên?')) {
                            changeAccountRole(account.id);
                        }
                    } else if (account.role.toLowerCase() === 'staff') {
                        if (confirm('Bạn có chắc chắn muốn chuyển vai trò của tài khoản này thành khách hàng?')) {
                            changeAccountRole(account.id);
                        }
                    }
                    }
                });

                    function changeAccountRole(accountId) {
                        fetch(BASE_URL_API+'account/changeRole/' + accountId, {
                            method: 'POST',
                            body: JSON.stringify({}),
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                            }
                            return response.json();
                        })
                        .then(data => {
                            alert('Vai trò của tài khoản đã được thay đổi thành công!');
                            const roleCell = row.querySelector('.role-text');
                            if (roleCell) {
                                if (account.role.toLowerCase() === 'customer') {
                                    roleCell.textContent = 'staff';
                                    roleCell.classList.remove('status-customer');
                                    roleCell.classList.add('status-staff');
                                } else if (account.role.toLowerCase() === 'staff') {
                                    roleCell.textContent = 'customer';
                                    roleCell.classList.remove('status-staff');
                                    roleCell.classList.add('status-customer');
                                }
                            }
                        })
                        .catch(error => {
                            console.error(error.message);
                            alert('Đã xảy ra lỗi khi thay đổi vai trò của tài khoản!');
                        });
                    }
                    buttonsDiv.appendChild(viewMoreButton);

                    actionsCell.appendChild(buttonsDiv);

                    row.appendChild(actionsCell);

                    accountList.appendChild(row);
                });
            }
        // )
    // }

    // Hàm thêm cột dữ liệu vào hàng
    function addCell(row, data, columnName, statusClass) {
    const cell = document.createElement('td');
    cell.setAttribute('data-label', columnName);

    // Kiểm tra tên cột để quyết định sử dụng class nào cho văn bản
    if (columnName.toLowerCase() === 'vai trò') {
        const roleText = document.createElement('span');
        roleText.textContent = data;
        roleText.classList.add('role-text', statusClass); // Sử dụng class role-text cho cột "Vai trò"
        cell.appendChild(roleText);
    } else if (columnName.toLowerCase() === 'trạng thái') {
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
    function getStatusClass(status) {
        switch (status.toLowerCase()) {
            case 'active':
                return 'status-active';
            case 'inactive':
                return 'status-inactive';
            case 'block':
                return 'status-block';
            default:
                return '';
        }
    }

    // Hàm trả về lớp màu nền tương ứng với vai trò
    function getRoleClass(role) {
        switch (role.toLowerCase()) {
            case 'admin':
                return 'status-admin';
            case 'staff':
                return 'status-staff';
            case 'customer':
                return 'status-customer';
            default:
                return '';
        }
    }
    
    function searchProduct(searchText) {
        fetch(BASE_URL_API+`account/search/${searchText}`)
            .then(response => response.json())
            .then(data => {
                cachedProductsData = data;
                displayItems(cachedProductsData);
            })
            .catch(error => console.error('Error:', error));
    }

    function sortAccounts(sortBy) {
    sortedProductsData = [...cachedProductsData];
    sortedProductsData.sort((a, b) => {
        switch (sortBy) {
            case 'name':
                return a.username.localeCompare(b.username); 
            case 'role':
                return a.role.localeCompare(b.role); 
            case 'customer_id':
                return a.customer_id - b.customer_id;
            case 'id':
                return a.id - b.id; 
            case 'status':
                return a.status.localeCompare(b.status); 
            default:
                return 0;
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
