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
    }
    .table-container {
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse;
    }
    
    .table-container th,
    .table-container td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }
    
    .table-container tr:nth-child(even) {
        background-color: #f2f2f2;
    }
    
    .table-container th {
        background-color: #4CAF50;
        color: white;
    }

    .status-button {
        display: inline-block;
        padding: 6px 12px;
        border-radius: 4px;
        font-size: 14px;
        font-weight: bold;
        cursor: default;
    }

    .status-completed {
        background-color: #4CAF50;
        color: white;
    }

    .status-pending {
        background-color: #FFC107;
        color: white;
    }

    .status-initialization {
        background-color: #2196F3;
        color: white;
    }

    .status-cancelled {
        background-color: #F44336;
        color: white;
    }
    .modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0,0,0,0.5);
}

.modal-content {
    background-color: #fefefe;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    border-radius: 8px;
    max-width: 80%;
}

.close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}

.product-image {
    max-width: 100px;
    max-height: 100px;
} */

</style>
<link rel="stylesheet" href="{{asset("../resources/css/orders.css")}}">

<div class="table-container">
<div class="navbar-item">
        <div class="search-control control">
            <input id="searchInput" placeholder="Tìm kiếm sản phẩm..." class="input">
        </div>
        <div class="dropdown">
            <button id="sortButton" class="button">Sắp xếp</button>
            <div class="dropdown-content">
                <a href="#" onclick="sortProducts('id')">Sắp xếp theo mã đơn</a>
                <a href="#" onclick="sortProducts('customer_id')">Sắp xếp theo mã khách</a>
                <a href="#" onclick="sortProducts('name')">Sắp xếp theo tên khách</a>
                <a href="#" onclick="sortProducts('address')">Sắp xếp theo địa chỉ</a>
                <a href="#" onclick="sortProducts('payment')">Sắp xếp theo thanh toán</a>
                <a href="#" onclick="sortProducts('payment_status')">Sắp xếp theo trạng thái</a>
                <a href="#" onclick="sortProducts('overall_status')">Sắp xếp theo đơn hàng</a>
            </div>
        </div>
    </div>
    <table>
        <thead>
            <tr>
                <th>Mã đơn hàng</th>
                <th>Mã khách hàng</th>
                <th>Tên khách hàng</th>
                <th>Địa chỉ</th>
                <th>Ghi chú</th>
                <th>Hình thức thanh toán</th>
                <th>Trạng thái thanh toán</th>
                <th>Trạng thái đơn hàng</th>
                <th>Thời gian tạo</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody id="orderList">
            <!-- Dữ liệu đơn hàng sẽ được thêm vào đây -->
        </tbody>
    </table>
<div id="pagination"></div>
</div>

<script>
    let cachedProductsData = [];
    let sortedProductsData = [];
    const PAGE_SIZE = 10; // Số lượng bản ghi trên mỗi trang
    let currentPage = 1; // Trang hiện tại, bắt đầu từ 1

    document.addEventListener("DOMContentLoaded", function() {
        fetchOrders();
    });
    function fetchOrders() {
        if (cachedProductsData.length === 0) {
            fetch(BASE_URL_API+"orders/getAllOrders")
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
                fetchOrders();
            });
            pagination.appendChild(pageButton);
        }
    }
    function displayItems(items){
        const itemList = document.getElementById('orderList');
        itemList.innerHTML = '';
        items.forEach(order => {
                const row = document.createElement('tr');

                // Thêm cột Mã đơn hàng
                addCell(row, order.order_ids,"Mã đơn hàng");

                // Thêm cột Mã khách hàng
                addCell(row, order.customer_id, "Mã khách hàng");

                // Thêm cột Tên khách hàng
                addCell(row, order.name,"Tên khách hàng");

                // Thêm cột Địa chỉ
                addCell(row, order.address,"Địa chỉ");

                // Thêm cột Ghi chú
                addCell(row, order.note,"Ghi chú");

                // Thêm cột Hình thức thanh toán
                addCell(row, order.payment,"Hình thức thanh toán");

                // Thêm cột Trạng thái thanh toán
                addCell(row, order.payment_status,"Trạng thái thanh toán");

                // Thêm cột Trạng thái đơn hàng với màu sắc tương ứng
                const statusCell = document.createElement('td');
                statusCell.classList.add('status-cell');
                statusCell.innerHTML = `<span class="status-button status-${order.overall_status.toLowerCase()}">${order.overall_status}</span>`;
                row.appendChild(statusCell);

                // Thêm cột Thời gian tạo
                addCell(row, order.created_time, "Thời gian tạo");
                
                const actionsCell = document.createElement('td');
                actionsCell.classList.add('actions-cell');
                const buttonsDiv = document.createElement('div');
                buttonsDiv.classList.add('buttons', 'center', 'nowrap'); // Thêm class 'center' vào đây

                // Thêm nút xem
                const viewButton = document.createElement('button');
                viewButton.classList.add('button', 'small', 'green', '--jb-modal');
                viewButton.setAttribute('data-target', 'sample-modal-2');
                viewButton.type = 'button';
                viewButton.innerHTML = '<span class="icon"><i class="mdi mdi mdi-eye"></i></span>';
                // Thêm sự kiện click cho nút xem
                viewButton.addEventListener('click', function () {
                    // Tạo modal element
                    const modal = document.createElement('div');
                    modal.classList.add('modal');

                    // Tạo nội dung modal
                    const modalContent = document.createElement('div');
                    modalContent.classList.add('modal-content');

                    // Tạo nút close
                    const closeButton = document.createElement('span');
                    closeButton.classList.add('close');
                    closeButton.innerHTML = '&times;';
                    closeButton.addEventListener('click', function() {
                        modal.style.display = 'none';
                    });
                    modalContent.appendChild(closeButton);

                    // Tạo bảng
                    const table = document.createElement('table');
                    const tableHeader = document.createElement('thead');
                    const tableBody = document.createElement('tbody');

                    // Tạo hàng tiêu đề
                    const headerRow = document.createElement('tr');
                    const headerProductID = document.createElement('th');
                    headerProductID.textContent = 'Product ID';
                    const headerQuantity = document.createElement('th');
                    headerQuantity.textContent = 'Quantity';
                    const headerPrice = document.createElement('th');
                    headerPrice.textContent = 'Price';
                    const headerImage = document.createElement('th');
                    headerImage.textContent = 'Image';

                    headerRow.appendChild(headerProductID);
                    headerRow.appendChild(headerQuantity);
                    headerRow.appendChild(headerPrice);
                    headerRow.appendChild(headerImage);
                    tableHeader.appendChild(headerRow);
                    table.appendChild(tableHeader);

                    // Thêm dữ liệu vào bảng
                    order.order_details.forEach(detail => {
                        const detailRow = document.createElement('tr');

                        const productIDCell = document.createElement('td');
                        productIDCell.textContent = detail.product_id;
                        const quantityCell = document.createElement('td');
                        quantityCell.textContent = detail.quantity;
                        const priceCell = document.createElement('td');
                        priceCell.textContent = detail.price + ' VND';
                        const imageCell = document.createElement('td');
                        if (detail.images.length > 0) {
                            const productImage = document.createElement('img');
                            productImage.src = BASE_URL_IMAGE+`${detail.images[0]}`;
                            productImage.classList.add('product-image');
                            imageCell.appendChild(productImage);
                        }

                        detailRow.appendChild(productIDCell);
                        detailRow.appendChild(quantityCell);
                        detailRow.appendChild(priceCell);
                        detailRow.appendChild(imageCell);

                        tableBody.appendChild(detailRow);
                    });

                    table.appendChild(tableBody);
                    modalContent.appendChild(table);

                    modal.appendChild(modalContent);
                    document.body.appendChild(modal);

                    // Hiển thị modal
                    modal.style.display = 'block';
                });
                buttonsDiv.appendChild(viewButton);
                // Thêm nút xem thêm
                if(order.payment_status === 'initialization' || order.payment_status === 'pending') { // Chỉ thêm nút xem thêm nếu trạng thái thanh toán là "initialization"
                    const viewMoreButton = document.createElement('button');
                    viewMoreButton.classList.add('button', 'small', 'blue', '--jb-modal');
                    viewMoreButton.setAttribute('data-target', 'sample-modal-3');
                    viewMoreButton.type = 'button';
                    viewMoreButton.innerHTML = '<span class="icon"><i class="mdi mdi-cash"></i></span>';
                    viewMoreButton.addEventListener('click', function () {
                        // Hiển thị hộp thoại xác nhận
                        if (confirm('Bạn đã nhận được tiền từ đơn hàng này?')) {
                            // Nếu người dùng xác nhận, gọi API để cập nhật trạng thái thanh toán
                            fetch(BASE_URL_API+'orders/updatePaymentStatus', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({ order_ids: order.order_ids })
                            })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                }
                                return response.json();
                            })
                            .then(data => {
                                // Xử lý phản hồi từ API (nếu cần)
                                console.log(data);
                                // Hiển thị thông báo cập nhật thành công
                                alert('Cập nhật trạng thái thanh toán thành công');
                                // Cập nhật giao diện người dùng hoặc thực hiện các hành động khác sau khi cập nhật thành công (nếu cần)
                                window.location.reload();
                            })
                            .catch(error => {
                                console.error(error.message);
                                // Hiển thị thông báo lỗi nếu có lỗi xảy ra
                                alert('Đã xảy ra lỗi khi cập nhật trạng thái thanh toán');
                            });
                        }
                    });
                    buttonsDiv.appendChild(viewMoreButton);
                }
                // Thêm sự kiện click cho nút xác nhận đơn hàng
                if (order.overall_status === 'initialization') {
                    const confirmOrderButton = document.createElement('button');
                    confirmOrderButton.classList.add('button');
                    confirmOrderButton.classList.add('small');
                    confirmOrderButton.classList.add('red');
                    confirmOrderButton.type = 'button';
                    confirmOrderButton.innerHTML = '<span class="icon"><i class="mdi mdi-checkbox-marked-circle-outline"></i></span> Xác nhận đơn hàng';
                    // Thêm sự kiện click cho nút xác nhận đơn hàng
                    confirmOrderButton.addEventListener('click', function () {
                        if (confirm('Bạn có chắc chắn muốn xác nhận đơn hàng này?')) {
                            // Gọi API để cập nhật trạng thái đơn hàng
                            fetch(BASE_URL_API+'orders/updateStatus', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({ order_ids: order.order_ids })
                            })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                }
                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                alert('Đơn hàng đã được xác nhận!');
                                order.overall_status = 'pending'; // Cập nhật trạng thái mới
                                buttonsDiv.removeChild(confirmOrderButton);
                                const deliverButton = document.createElement('button');
                                deliverButton.classList.add('button');
                                deliverButton.classList.add('small');
                                deliverButton.classList.add('orange');
                                deliverButton.type = 'button';
                                deliverButton.innerHTML = '<span class="icon"><i class="mdi mdi-truck"></i></span> Giao hàng';
                                // Thêm sự kiện click cho nút giao hàng
                                deliverButton.addEventListener('click', function () {
                                    if (confirm('Bạn có chắc chắn muốn giao hàng cho đơn hàng này?')) {
                                        fetch(BASE_URL_API+'orders/updateStatus', {
                                            method: 'POST',
                                            headers: {
                                                'Content-Type': 'application/json'
                                            },
                                            body: JSON.stringify({ order_ids: order.order_ids })
                                        })
                                        .then(response => {
                                            if (!response.ok) {
                                                throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                            }
                                            return response.json();
                                        })
                                        .then(data => {
                                            console.log(data);
                                            alert('Đơn hàng đã được giao!');
                                            // Cập nhật trạng thái đơn hàng thành công
                                            order.overall_status = 'completed'; // Cập nhật trạng thái mới
                                            // Xóa nút "Giao hàng"
                                            buttonsDiv.removeChild(deliverButton);
                                            // Thêm nút "Đã giao hàng"
                                            const deliveredButton = document.createElement('button');
                                            deliveredButton.classList.add('button');
                                            deliveredButton.classList.add('small');
                                            deliveredButton.classList.add('green');
                                            deliveredButton.type = 'button';
                                            deliveredButton.innerHTML = '<span class="icon"><i class="mdi mdi-check-circle-outline"></i></span> Đã giao hàng';
                                            buttonsDiv.appendChild(deliveredButton);
                                            window.location.reload();
                                        })
                                        .catch(error => {
                                            console.error(error.message);
                                            alert('Đã xảy ra lỗi khi giao hàng');
                                        });
                                    }
                                });
                                buttonsDiv.appendChild(deliverButton);
                                window.location.reload();
                            })
                            .catch(error => {
                                console.error(error.message);
                                alert('Đã xảy ra lỗi khi xác nhận đơn hàng');
                            });
                        }
                    });
                    buttonsDiv.appendChild(confirmOrderButton);
                }
                // Thêm nút giao hàng
                if (order.overall_status === 'pending') {
                    const deliverButton = document.createElement('button');
                    deliverButton.classList.add('button');
                    deliverButton.classList.add('small');
                    deliverButton.classList.add('orange');
                    deliverButton.type = 'button';
                    deliverButton.innerHTML = '<span class="icon"><i class="mdi mdi-truck"></i></span> Giao hàng';
                    // Thêm sự kiện click cho nút giao hàng
                    deliverButton.addEventListener('click', function () {
                        if (confirm('Bạn có chắc chắn muốn giao hàng cho đơn hàng này?')) {
                            fetch(BASE_URL_API+'orders/updateStatus', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({ order_ids: order.order_ids })
                            })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
                                }
                                return response.json();
                            })
                            .then(data => {
                                console.log(data);
                                alert('Đơn hàng đã được giao!');
                                // Cập nhật trạng thái đơn hàng thành công
                                order.overall_status = 'completed'; // Cập nhật trạng thái mới
                                // Xóa nút "Giao hàng"
                                buttonsDiv.removeChild(deliverButton);
                                // Thêm nút "Đã giao hàng"
                                const deliveredButton = document.createElement('button');
                                deliveredButton.classList.add('button');
                                deliveredButton.classList.add('small');
                                deliveredButton.classList.add('green');
                                deliveredButton.type = 'button';
                                deliveredButton.innerHTML = '<span class="icon"><i class="mdi mdi-check-circle-outline"></i></span> Đã giao hàng';
                                buttonsDiv.appendChild(deliveredButton);
                                window.location.reload();
                            })
                            .catch(error => {
                                console.error(error.message);
                                alert('Đã xảy ra lỗi khi giao hàng');
                            });
                        }
                    }
                );
                    buttonsDiv.appendChild(deliverButton);
                }
                // Thêm nút đã giao hàng
                if (order.overall_status === 'completed') {
                    const deliveredButton = document.createElement('button');
                    deliveredButton.classList.add('button');
                    deliveredButton.classList.add('small');
                    deliveredButton.classList.add('green');
                    deliveredButton.type = 'button';
                    deliveredButton.innerHTML = '<span class="icon"><i class="mdi mdi-check-circle-outline"></i></span> Đã giao hàng';
                    buttonsDiv.appendChild(deliveredButton);
                }
                actionsCell.appendChild(buttonsDiv);
                row.appendChild(actionsCell);
                orderList.appendChild(row);
            });
    }
    ///////////
//     document.addEventListener("DOMContentLoaded", function() {
//     const orderList = document.getElementById('orderList');
    
//     // Gửi yêu cầu GET đến API để lấy dữ liệu đơn hàng
//     fetch('http://localhost/api_food/public/api/orders/getAllOrders')
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Đã xảy ra lỗi khi gửi yêu cầu đến API: ' + response.status);
//             }
//             return response.json();
//         })
//         .then(data => {
//             // Xử lý dữ liệu nhận được từ API
//             const orders = data;
            
//             // Hiển thị thông tin của từng đơn hàng
            
//         })
//         .catch(error => {
//             console.error(error.message);
//         });
// });

// Hàm thêm cột dữ liệu vào hàng
function addCell(row, data, rowName) {
    const cell = document.createElement('td');
    cell.textContent = data;
    cell.setAttribute('data-label', rowName);
    row.appendChild(cell);
}
function searchProduct(searchText) {
        fetch(BASE_URL_API+`orders/search/${searchText}`)
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
            case 'customer_id':
                return a[sortBy] - b[sortBy];
            case 'name':
            case 'address':
            case 'payment':
            case 'payment_status':
            case 'overall_status':
                return a[sortBy].localeCompare(b[sortBy]);
            default:
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
