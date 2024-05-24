@extends("layout")
@section("content")
<section class="is-title-bar">
  <div class="flex flex-col md:flex-row items-center justify-between space-y-6 md:space-y-0">
    <ul>
      <li>Admin</li>
      <li>Thống kê</li>
    </ul>
  </div>
</section>

<section class="is-hero-bar">
  <div class="flex flex-col md:flex-row items-center justify-between space-y-6 md:space-y-0">
    <h1 class="title">
      Thống kê
    </h1>
  </div>
</section>

<section class="section main-section">
    <div class="grid gap-6 grid-cols-1 md:grid-cols-3 mb-6">
        <div class="card">
            <div class="card-content">
                <div class="flex items-center justify-between">
                    <div class="widget-label">
                        <h3>
                            Khách hàng
                        </h3>
                        <h1 id="customerCount">
                        </h1>
                    </div>
                    <span class="icon widget-icon text-green-500"><i class="mdi mdi-account-multiple mdi-48px"></i></span>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-content">
                <div class="flex items-center justify-between">
                    <div class="widget-label">
                        <h3>
                            Doanh số
                        </h3>
                        <h1 id="totalRevenue">
                        </h1>
                    </div>
                    <span class="icon widget-icon text-blue-500"> <i class="mdi mdi-finance mdi-48px"></i></span>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-content">
                <div class="flex items-center justify-between">
                    <div class="widget-label">
                        <h3>
                            Số lượng đơn
                        </h3>
                        <h1 id="orderCount">
                        </h1>
                    </div>
                    <span class="icon widget-icon text-red-500"><i class="mdi mdi-cart-outline mdi-48px"></i></span>
                </div>
            </div>
        </div>
    </div>
    <section class="section main-section">
        <div class="grid gap-6 grid-cols-1 md:grid-cols-1 mb-6">
            <div class="card">
                <div class="card-content">
                    <canvas id="customerLineChart" width="800" height="400"></canvas>
                </div>
            </div>
            <div class="card">
                <div class="card-content">
                <canvas id="orderLineChart" width="800" height="400"></canvas>
                </div>
            </div>
            <div class="card">
                <div class="card-content">
                <canvas id="revenueLineChart" width="800" height="400"></canvas>
                </div>
            </div>
        </div>
    </section>
</section>
</section>

<script>
    // Gọi API để lấy số lượng khách hàng
    fetch(BASE_URL_API+'customer')
        .then(response => response.json())
        .then(data => {
            document.getElementById('customerCount').innerText = data.length;
        });

    // Gọi API để lấy tổng doanh số
    fetch(BASE_URL_API+'orders/getAllOrders')
        .then(response => response.json())
        .then(data => {
            let totalRevenue = 0;
            data.forEach(order => {
                order.order_details.forEach(detail => {
                    totalRevenue += parseFloat(detail.price);
                });
            });
            document.getElementById('totalRevenue').innerText = '$' + totalRevenue.toFixed(2);
        });
  // Gọi API để lấy số lượng đơn hàng
  fetch(BASE_URL_API+'orders/getAllOrders')
        .then(response => response.json())
        .then(data => {
            document.getElementById('orderCount').innerText = data.length;
        });
// Gọi API để lấy số lượng khách hàng
fetch(BASE_URL_API+'customer')
    .then(response => response.json())
    .then(customerData => {
        const customerCountByDate = countByDate(customerData, 'created_time');
        const customerDates = Object.keys(customerCountByDate);
        const customerCounts = Object.values(customerCountByDate);

        // Render biểu đồ với dữ liệu khách hàng
        renderCustomerLineChart(customerDates, customerCounts);
    });

fetch(BASE_URL_API+'orders/getAllOrders')
    .then(response => response.json())
    .then(orderData => {
        const orderCountByDate = countByDate(orderData, 'created_time');
        const orderDates = Object.keys(orderCountByDate);
        const orderCounts = Object.values(orderCountByDate);

        // Render biểu đồ với dữ liệu đơn hàng
        renderOrderLineChart(orderDates, orderCounts);
    });

function countByDate(data, dateField) {
    const counts = {};
    data.forEach(item => {
        const date = new Date(item[dateField]);
        const localizedDate = date.toLocaleDateString('en-US', { timeZone: 'Asia/Ho_Chi_Minh' });
        if (!counts[localizedDate]) {
            counts[localizedDate] = 0;
        }
        counts[localizedDate]++;
    });
    return counts;
}

function renderCustomerLineChart(dates, counts) {
  const ctxCustomer = document.getElementById('customerLineChart').getContext('2d');
    // const ctx = document.getElementById('lineChart').getContext('2d');
    const lineChart = new Chart(ctxCustomer, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Số lượng khách hàng',
                data: counts,
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
            scales: {
                x: {
                    type: 'time',
                    time: {
                        unit: 'day',
                        displayFormats: {
                            day: 'DD/MM'
                        }
                    },
                    title: {
                        display: true,
                        text: 'Ngày'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng'
                    }
                }
            }
        }
    });
}

function renderOrderLineChart(dates, counts) {
    const ctxOrder = document.getElementById('orderLineChart').getContext('2d');
    const reversedDates = dates.reverse();
    const reversedCounts = counts.reverse(); // Đảo ngược cả mảng số lượng đơn hàng
    const lineChart = new Chart(ctxOrder, {
        type: 'line',
        data: {
            labels: reversedDates,
            datasets: [{
                label: 'Số lượng đơn hàng',
                data: reversedCounts, // Sử dụng mảng số lượng đơn hàng đã được đảo ngược
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
            scales: {
                x: {
                    type: 'time',
                    time: {
                        unit: 'day',
                        displayFormats: {
                            day: 'DD/MM'
                        }
                    },
                    title: {
                        display: true,
                        text: 'Ngày'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Số lượng'
                    }
                }
            }
        }
    });
}

fetch(BASE_URL_API+'orders/getAllOrders')
    .then(response => response.json())
    .then(orderData => {
        // Xử lý dữ liệu trả về
        const dailyRevenue = {};

        orderData.forEach(order => {
            // Trích xuất ngày và doanh thu từ mỗi đơn hàng
            const date = new Date(order.created_time).toLocaleDateString('en-US', { timeZone: 'Asia/Ho_Chi_Minh' });
            const revenue = calculateOrderRevenue(order);

            // Thêm doanh thu vào từng ngày
            if (!dailyRevenue[date]) {
                dailyRevenue[date] = revenue;
            } else {
                dailyRevenue[date] += revenue;
            }
        });

        // Chuyển dữ liệu thành mảng labels và data
        const labels = Object.keys(dailyRevenue);
        const data = Object.values(dailyRevenue);

        // Vẽ biểu đồ đường
        renderLineChart(labels, data);
    });

// Hàm tính toán doanh thu từ một đơn hàng
function calculateOrderRevenue(order) {
    let totalRevenue = 0;
    order.order_details.forEach(detail => {
        totalRevenue += parseFloat(detail.price);
    });
    return totalRevenue;
}

function renderLineChart(labels, data) {
    const ctx = document.getElementById('revenueLineChart').getContext('2d');
    const lineChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels.reverse(), // Đảo ngược mốc thời gian
            datasets: [{
                label: 'Doanh thu',
                data: data.reverse(), // Đảo ngược dữ liệu doanh thu
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 2,
                fill: false
            }]
        },
        options: {
            scales: {
                x: {
                    type: 'time',
                    time: {
                        unit: 'day',
                        displayFormats: {
                            day: 'DD/MM'
                        }
                    },
                    title: {
                        display: true,
                        text: 'Ngày'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Doanh thu'
                    }
                }
            }
        }
    });
}



</script>
@endsection



