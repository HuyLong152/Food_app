<!DOCTYPE html>
<html lang="en" class="form-screen">
<head>
  <!-- Thẻ meta, title và các liên kết khác -->

  <!-- Stylesheets -->
  <!-- Liên kết đến file CSS của Tailwind -->
  <link rel="stylesheet" href="{{asset("assets/css/main.css?v=1628755089081")}}">
  <!-- Liên kết đến các biểu tượng -->
  <link rel="stylesheet" href="https://cdn.materialdesignicons.com/4.9.95/css/materialdesignicons.min.css">

  <style>
    body, html {
      height: 100%;
    }

    body {
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 0;
    }

    #app {
      width: 100%;
      /* max-width: 400px; Điều chỉnh kích thước phù hợp */
    }
  </style>
</head>
<body>

<div id="app">

  <!-- Phần form quên mật khẩu -->
  <section class="section main-section">
    <div class="card">
      <header class="card-header">
        <p class="card-header-title">
          <span class="icon"><i class="mdi mdi-lock-reset"></i></span>
          Quên mật khẩu
        </p>
      </header>
      <div class="card-content">
        <form id="forgotPasswordForm">

          <div class="field">
            <label class="label">Tên tài khoản</label>
            <div class="control icons-left">
              <input id="forgotUsername" class="input" type="text" name="username" placeholder="Tên tài khoản" autocomplete="username">
              <span class="icon is-small left"><i class="mdi mdi-account"></i></span>
            </div>
            <p class="help">
              Vui lòng nhập tên tài khoản
            </p>
          </div>

          <div class="field">
            <label class="label">Email</label>
            <div class="control icons-left">
              <input id="forgotEmail" class="input" type="email" name="email" placeholder="Email" autocomplete="email">
              <span class="icon is-small left"><i class="mdi mdi-email"></i></span>
            </div>
            <p class="help">
              Vui lòng nhập địa chỉ email đã đăng ký
            </p>
          </div>

          <hr>

          <div class="field">
            <div class="control">
              <button id="forgotPasswordButton" type="submit" class="button blue">
                Gửi yêu cầu
              </button>
            </div>
          </div>

        </form>
      </div>
    </div>
  </section>

</div>

<!-- Kịch bản JavaScript -->
<script>
  document.getElementById('forgotPasswordForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    const username = document.getElementById('forgotUsername').value;
    const email = document.getElementById('forgotEmail').value;

    // Gửi dữ liệu quên mật khẩu đến API
    fetch('https://huylong.io.vn/api_food/public/api/recoveryPassAdmin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: username,
        email: email
      }),
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then(data => {
      // Xử lý kết quả trả về từ API
      if (data.message === 'Recovery password successful') {
        alert('Một liên kết đặt lại mật khẩu đã được gửi đến email của bạn!');
      } else {
        alert('Gửi yêu cầu đặt lại mật khẩu thất bại: ' + data.message);
      }
    })
    .catch(error => {
      // Xử lý lỗi nếu không thể kết nối đến API
      console.error('Error:', error);
      alert('Gửi yêu cầu đặt lại mật khẩu thất bại. Vui lòng kiểm tra lại thông tin.');
    });
  });
</script>

</body>
</html>
