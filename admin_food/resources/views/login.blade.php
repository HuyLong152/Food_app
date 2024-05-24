<!DOCTYPE html>
<html lang="en" class="form-screen">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login - Admin One Tailwind CSS Admin Dashboard</title>

  <!-- Tailwind is included -->
  <link rel="stylesheet" href="{{asset("assets/css/main.css?v=1628755089081")}}">

  <link rel="apple-touch-icon" sizes="180x180" href="apple-touch-icon.png"/>
  <link rel="icon" type="image/png" sizes="32x32" href="favicon-32x32.png"/>
  <link rel="icon" type="image/png" sizes="16x16" href="favicon-16x16.png"/>
  <link rel="mask-icon" href="safari-pinned-tab.svg" color="#00b4b6"/>

  <meta name="description" content="Admin One - free Tailwind dashboard">

  <meta property="og:url" content="https://justboil.github.io/admin-one-tailwind/">
  <meta property="og:site_name" content="JustBoil.me">
  <meta property="og:title" content="Admin One HTML">
  <meta property="og:description" content="Admin One - free Tailwind dashboard">
  <meta property="og:image" content="https://justboil.me/images/one-tailwind/repository-preview-hi-res.png">
  <meta property="og:image:type" content="image/png">
  <meta property="og:image:width" content="1920">
  <meta property="og:image:height" content="960">

  <meta property="twitter:card" content="summary_large_image">
  <meta property="twitter:title" content="Admin One HTML">
  <meta property="twitter:description" content="Admin One - free Tailwind dashboard">
  <meta property="twitter:image:src" content="https://justboil.me/images/one-tailwind/repository-preview-hi-res.png">
  <meta property="twitter:image:width" content="1920">
  <meta property="twitter:image:height" content="960">

  <!-- Global site tag (gtag.js) - Google Analytics -->
  <script async src="https://www.googletagmanager.com/gtag/js?id=UA-130795909-1"></script>
  <script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());
    gtag('config', 'UA-130795909-1');
  </script>

</head>
<body>

<div id="app">

  <section class="section main-section">
    <div class="card">
      <header class="card-header">
        <p class="card-header-title">
          <span class="icon"><i class="mdi mdi-lock"></i></span>
          Đăng nhập
        </p>
      </header>
      <div class="card-content">
        <form id="loginForm" method="post">

          <div class="field spaced">
            <label class="label">Đăng nhập</label>
            <div class="control icons-left">
              <input id="username" class="input" type="text" name="login" placeholder="user@example.com" autocomplete="username">
              <span class="icon is-small left"><i class="mdi mdi-account"></i></span>
            </div>
            <p class="help">
              Vui lòng nhập tài khoản
            </p>
          </div>

          <div class="field spaced">
            <label class="label">Mật khẩu</label>
            <p class="control icons-left">
              <input id="password" class="input" type="password" name="password" placeholder="Password" autocomplete="current-password">
              <span class="icon is-small left"><i class="mdi mdi-asterisk"></i></span>
            </p>
            <p class="help">
              Vui lòng nhập mật khẩu
            </p>
          </div>

          <div class="field spaced">
            <div class="control">
              <label class="checkbox"><input type="checkbox" name="remember" value="1" checked>
                <span class="check"></span>
                <span class="control-label">Nhớ mật khẩu</span>
              </label>
            </div>
          </div>

          <hr>

          <div class="field grouped">
            <div class="control">
              <button id="loginButton" type="submit" class="button blue">
                Đăng nhập
              </button>
            </div>
            <div class="control">
              <a href="{{route("forgot")}}" class="button">
                Quên mật khẩu
              </a>
            </div>
          </div>

        </form>
      </div>
    </div>

  </section>


</div>

<script>
  const BASE_HOSTING = "http://localhost/"
  // Kiểm tra xem biến localStorage có tồn tại hay không
  if(localStorage.getItem('userID')) {
    // Thực hiện hành động chuyển hướng, ví dụ chuyển đến màn hình A
    window.location.href = BASE_HOSTING+'admin_food/public/home';
  }

  document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn gửi form mặc định

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Gửi dữ liệu đăng nhập đến API
    fetch(BASE_HOSTING +'api_food/public/api/login/admin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: username,
        password: password
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
      if (data.message === 'Login successful') {
        console.log("abc");
        alert('Đăng nhập thành công!');
        localStorage.setItem('userID', data.id);
        window.location.href = BASE_HOSTING+'admin_food/public/home';
      } else {
        alert('Đăng nhập thất bại: ' + data.message);
      }
    })
    .catch(error => {
      // Xử lý lỗi nếu không thể kết nối đến API
      console.error('Error:', error);
      alert('Đăng nhập thất bại. Vui lòng thử lại sau.');
    });
  });
</script>



<!-- Scripts below are for demo only -->
<script type="text/javascript" src="{{asset("assets/js/main.min.js?v=1628755089081")}}"></script>


<script>
  !function(f,b,e,v,n,t,s)
  {if(f.fbq)return;n=f.fbq=function(){n.callMethod?
    n.callMethod.apply(n,arguments):n.queue.push(arguments)};
    if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
    n.queue=[];t=b.createElement(e);t.async=!0;
    t.src=v;s=b.getElementsByTagName(e)[0];
    s.parentNode.insertBefore(t,s)}(window, document,'script',
    'https://connect.facebook.net/en_US/fbevents.js');
  fbq('init', '658339141622648');
  fbq('track', 'PageView');
</script>
<noscript><img height="1" width="1" style="display:none" src="https://www.facebook.com/tr?id=658339141622648&ev=PageView&noscript=1"/></noscript>

<!-- Icons below are for demo only. Feel free to use any icon pack. Docs: https://bulma.io/documentation/elements/icon/ -->
<link rel="stylesheet" href="https://cdn.materialdesignicons.com/4.9.95/css/materialdesignicons.min.css">

</body>
</html>
