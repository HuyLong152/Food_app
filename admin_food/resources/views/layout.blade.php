<!DOCTYPE html>
<html lang="en" class="">

<head>
  <style>
    .rounded-full {
      border-radius: 50%;
      overflow: hidden;
    }

    .user-avatar img {
      width: 100%;
      height: auto;
    }
  </style>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin Food App</title>

  <link rel="stylesheet" href="{{asset("assets/css/main.css?v=1628755089081")}}">

  <link rel="apple-touch-icon" sizes="180x180" href="{{asset("logo.png")}}" />
  <link rel="icon" type="image/png" sizes="32x32" href="logo.png" />
  <link rel="icon" type="image/png" sizes="16x16" href="logo.png" />
  <link rel="mask-icon" href="safari-pinned-tab.svg" color="#00b4b6" />

  <meta name="description" content="Admin Food App">

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
    const BASE_HOSTING = "http://localhost/"
    const BASE_URL = "https://huylong.io.vn/admin_food/public/"
    const BASE_URL_API = "https://huylong.io.vn/api_food/public/api/"
    const BASE_URL_CATEGORY = "https://huylong.io.vn/api_food/storage/app/public/categoryImage/"
    const BASE_URL_IMAGE = "https://huylong.io.vn/api_food/storage/app/public/images/";
    const BASE_URL_AVATAR = "https://huylong.io.vn/api_food/storage/app/public/avatar/";

    window.dataLayer = window.dataLayer || [];
    function gtag() { dataLayer.push(arguments); }
    gtag('js', new Date());
    gtag('config', 'UA-130795909-1');
  </script>

</head>

<body>

  <div id="app">
    <nav id="navbar-main" class="navbar is-fixed-top">
      <div class="navbar-brand">
        <a class="navbar-item mobile-aside-button">
          <span class="icon"><i class="mdi mdi-forwardburger mdi-24px"></i></span>
        </a>
      </div>
      <div class="navbar-brand is-right">
        <a class="navbar-item --jb-navbar-menu-toggle" data-target="navbar-menu">
          <span class="icon"><i class="mdi mdi-dots-vertical mdi-24px"></i></span>
        </a>
      </div>
      <div class="navbar-menu" id="navbar-menu">
        <div class="navbar-end">
          <!-- <div class="navbar-item dropdown has-divider">
        <a class="navbar-link">
          <span class="icon"><i class="mdi mdi-menu"></i></span>
          <span>Sample Menu</span>
          <span class="icon">
            <i class="mdi mdi-chevron-down"></i>
          </span>
        </a>
        <div class="navbar-dropdown">
          <a href="profile.html" class="navbar-item">
            <span class="icon"><i class="mdi mdi-account"></i></span>
            <span>My Profile</span>
          </a>
          <a class="navbar-item">
            <span class="icon"><i class="mdi mdi-settings"></i></span>
            <span>Settings</span>
          </a>
          <a class="navbar-item">
            <span class="icon"><i class="mdi mdi-email"></i></span>
            <span>Messages</span>
          </a>
          <hr class="navbar-divider">
          <a class="navbar-item">
            <span class="icon"><i class="mdi mdi-logout"></i></span>
            <span>Log Out</span>
          </a>
        </div>
      </div> -->
          <div class="navbar-item dropdown has-divider has-user-avatar">
            <a class="navbar-link">
              <div class="user-avatar rounded-full">
                <img src="https://avatars.dicebear.com/v2/initials/john-doe.svg" alt="John Doe" class="rounded-full">
              </div>
              <div class="is-user-name"><span>John Doe</span></div>
              <span class="icon"><i class="mdi mdi-chevron-down"></i></span>
            </a>
            <div class="navbar-dropdown">
              <a href="{{route("admin")}}" class="navbar-item">
                <span class="icon"><i class="mdi mdi-account"></i></span>
                <span>Trang cá nhân</span>
              </a>
              <hr class="navbar-divider">
              <a href="{{route("chat.view")}}" id="chat" class="navbar-item">
                <span class="icon"><i class="mdi mdi-comment-multiple-outline"></i></span>
                <span>Tin nhắn</span>
              </a>
              <hr class="navbar-divider">
              <a id="log-out" class="navbar-item">
                <span class="icon"><i class="mdi mdi-logout"></i></span>
                <span>Đăng xuất</span>
              </a>
              
            </div>
          </div>
        </div>
      </div>
    </nav>

    <aside class="aside is-placed-left is-expanded">
      <div class="aside-tools">
        <div>
          <a href="{{route("home")}}">
            <span class="icon"><i class="mdi mdi-desktop-mac"></i></span>
            <span class="menu-item-label">Food App</span>
          </a>
        </div>
      </div>
      <div class="menu is-menu-main">
        <p class="menu-label">Thống kê</p>
        <ul class="menu-list">
          <li class="">
            <a href="{{route("home")}}">
              <span class="icon"><i class="mdi mdi-desktop-mac"></i></span>
              <span class="menu-item-label">Thống kê</span>
            </a>
          </li>
        </ul>
        <p class="menu-label">Danh mục bảng</p>
        <ul class="menu-list">
          <li class="--set-active-tables-html">
            <a href="{{route("category")}}">
              <span class="icon"><i class="mdi mdi-table "></i></span>
              <span class="menu-item-label">Danh mục món ăn</span>
            </a>
          </li>

          <li class="--set-active-forms-html">
            <a href="{{route("product")}}">
              <span class="icon"><i class="mdi mdi-square-edit-outline"></i></span>
              <span class="menu-item-label">Món ăn</span>
            </a>
          </li>
          <li class="--set-active-profile-html">
            <a href="{{route("account")}}">
              <span class="icon"><i class="mdi mdi-account-circle"></i></span>
              <span class="menu-item-label">Tài khoản</span>
            </a>
          </li>
          <li class="--set-active-profile-html">
            <a href="{{route("orders")}}">
              <span class="icon"><i class="mdi mdi-cart"></i></span>
              <span class="menu-item-label">Đơn hàng</span>
            </a>
          </li>
          <li>
            <a href="{{route("discount")}}">
              <span class="icon"><i class="mdi mdi-ticket"></i></span>
              <span class="menu-item-label">Giảm giá</span>
            </a>
          </li>
          <li>
            <a href="{{route("hotsearch")}}">
              <span class="icon"><i class="mdi mdi-account-search"></i></span>
              <span class="menu-item-label">Tìm kiếm</span>
            </a>
          </li>
          <!-- <li>
        <a class="dropdown">
          <span class="icon"><i class="mdi mdi-view-list"></i></span>
          <span class="menu-item-label">Submenus</span>
          <span class="icon"><i class="mdi mdi-plus"></i></span>
        </a>
        <ul>
          <li>
            <a href="#void">
              <span>Sub-item One</span>
            </a>
          </li>
          <li>
            <a href="#void">
              <span>Sub-item Two</span>
            </a>
          </li>
        </ul>
      </li> -->
        </ul>
        <p class="menu-label">Tài khoản</p>
        <ul class="menu-list">
          <li>
            <!-- <a href="https://justboil.me" onclick="alert('Coming soon'); return false" target="_blank" class="has-icon"> -->
            <!-- <span class="icon"><i class="mdi mdi-credit-card-outline"></i></span> -->
            <!-- <span class="menu-item-label">Premium Demo</span> -->
            </a>
          </li>
          <li>
            <a href="{{route("admin")}}" class="has-icon">
              <span class="icon"><i class="mdi mdi-account-card-details"></i></span>
              <span class="menu-item-label">Hồ sơ</span>
            </a>
          </li>
          <li id="adminMenu" style="display: none;">
            <a href="{{route("admin.create")}}" class="has-icon">
              <span class="icon"><i class="mdi mdi-account-multiple-plus"></i></span>
              <span class="menu-item-label">Đăng ký</span>
            </a>
          </li>
        </ul>
      </div>
    </aside>

    <div>@yield('content')</div>

    <!-- <footer class="footer">
      <div class="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0">
        <div class="flex items-center justify-start space-x-3">
        </div>
      </div>
    </footer> -->

    <div id="sample-modal" class="modal">
      <div class="modal-background --jb-modal-close"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <p class="modal-card-title">Sample modal</p>
        </header>
        <section class="modal-card-body">
          <p>Lorem ipsum dolor sit amet <b>adipiscing elit</b></p>
          <p>This is sample modal</p>
        </section>
        <footer class="modal-card-foot">
          <button class="button --jb-modal-close">Cancel</button>
          <button class="button red --jb-modal-close">Confirm</button>
        </footer>
      </div>
    </div>

    <div id="sample-modal-2" class="modal">
      <div class="modal-background --jb-modal-close"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <p class="modal-card-title">Sample modal</p>
        </header>
        <section class="modal-card-body">
          <p>Lorem ipsum dolor sit amet <b>adipiscing elit</b></p>
          <p>This is sample modal</p>
        </section>
        <footer class="modal-card-foot">
          <button class="button --jb-modal-close">Cancel</button>
          <button class="button blue --jb-modal-close">Confirm</button>
        </footer>
      </div>
    </div>

  </div>



  <!-- Scripts below are for demo only -->
  <script type="text/javascript" src="{{asset("assets/js/main.min.js?v=1628755089081")}}"></script>

  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"></script>
  <script type="text/javascript" src="{{asset("assets/js/chart.sample.min.js")}}"></script>
  <script>
    document.addEventListener('DOMContentLoaded', function () {
      const logoutButton = document.getElementById('log-out');
      logoutButton.addEventListener('click', function (event) {
        event.preventDefault();
        localStorage.clear();
        // Chuyển hướng đến trang đăng nhập
        window.location.href = '{{route('login')}}';
      });
    });
    function checkLocalStorage() {
      if (!localStorage.getItem('userID') || localStorage.getItem('userID').trim() === '' || localStorage.getItem('userID') === 'undefined') {
        window.location.href = '{{route('login')}}';
      } else {
        const userID = localStorage.getItem('userID');
        fetch(BASE_URL_API + `customer/${userID}`)
          .then(response => {
            if (!response.ok) {
              window.location.href = '{{route('home')}}';
            }
            return response.json();
          })
          .then(data => {
            if (data.image_url && data.image_url.trim() !== '') {
              const newImageUrl = BASE_URL_AVATAR + `${data.image_url}`;
              document.querySelector('.user-avatar img').src = newImageUrl;
            }
            // console.log(data);
            document.querySelector('.is-user-name span').textContent = data.full_name;
            document.body.style.display = 'block';
          })
          .catch(error => {
            console.error('Error fetching data:', error);
            window.location.href = '{{route('login')}}';
          });
      }
    }

    // Gọi hàm kiểm tra khi trang đã tải hoàn thành
    document.addEventListener('DOMContentLoaded', function () {
      checkLocalStorage();
    });
    function CheckRole() {
            const userID = localStorage.getItem('userID');
            return fetch(BASE_URL_API + `account/getAccountByCusId/${userID}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    return data.role;
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                    return null; // Trả về null trong trường hợp có lỗi
                });
        }

        // Hàm để kiểm tra vai trò và hiển thị thẻ li nếu là admin
        function showAdminMenu() {
            CheckRole().then(role => {
              
                if (role === 'admin') {
                    document.getElementById('adminMenu').style.display = 'block';
                }
            });
        }

        // Gọi hàm để kiểm tra và hiển thị menu khi tải trang
        showAdminMenu();
  </script>

  <script>

    !function (f, b, e, v, n, t, s) {
      if (f.fbq) return; n = f.fbq = function () {
        n.callMethod ?
        n.callMethod.apply(n, arguments) : n.queue.push(arguments)
      };
      if (!f._fbq) f._fbq = n; n.push = n; n.loaded = !0; n.version = '2.0';
      n.queue = []; t = b.createElement(e); t.async = !0;
      t.src = v; s = b.getElementsByTagName(e)[0];
      s.parentNode.insertBefore(t, s)
    }(window, document, 'script',
      'https://connect.facebook.net/en_US/fbevents.js');
    fbq('init', '658339141622648');
    fbq('track', 'PageView');


    const tableItems = document.querySelectorAll('.menu-list li');
    tableItems.forEach(item => {
      item.addEventListener('click', function () {
        console.log("Clicked!");
        tableItems.forEach(item => {
          item.classList.remove('active');
        });

        this.classList.add('active');
      });

    });
  </script>
  <noscript><img height="1" width="1" style="display:none"
      src="https://www.facebook.com/tr?id=658339141622648&ev=PageView&noscript=1" /></noscript>

  <!-- Icons below are for demo only. Feel free to use any icon pack. Docs: https://bulma.io/documentation/elements/icon/ -->
  <link rel="stylesheet" href="https://cdn.materialdesignicons.com/4.9.95/css/materialdesignicons.min.css">

</body>

</html>