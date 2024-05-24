@extends("layout")
@section("content")
<link rel="stylesheet" href="{{asset("../resources/css/admin.css")}}">

<style>
  /* Thêm kiểu cho hình tròn */
  /* .avatar-container {
    width: 150px; 
    height: 150px; 
    border-radius: 50%; 
    overflow: hidden; 
    display: flex; 
    justify-content: center; 
    align-items: center; 
  }
  .avatar-image {
    width: 100%; 
    height: auto; 
  }
  .center-content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .button-padding {
    margin-top: 10px;
  } */
  .purple {
    background-color: purple;
    color: white;
  }

  .button-padding {
    margin-left: 10px;
  }
</style>

<section class="section main-section">
  <div class="card">
    <header class="card-header">
      <p class="card-header-title">
        <span class="icon"><i class="mdi mdi-account-circle"></i></span>
        Chỉnh sửa hồ sơ
      </p>
    </header>
    <div class="card-content">
      <form id="update-form">
        <div class="field">
          <label class="label">Ảnh đại diện</label>
          <div class="center-content">
            <div class="avatar-container">
              <img id="avatar-preview" src="https://avatars.dicebear.com/v2/initials/john-doe.svg" alt="image_url"
                class="avatar-image">
            </div>
            <div class="field-body w-full button-padding">
              <div class="field file flex justify-center items-center">
                <label class="upload control text-center">
                  <a class="button blue">
                    Đổi ảnh
                  </a>
                  <input type="file" id="avatar-upload" onchange="displaySelectedImage(event)">
                </label>
              </div>
            </div>
          </div>
        </div>
        <hr>
        <div class="field">
          <label class="label">Tên</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="text" autocomplete="on" name="full_name" value="" class="input" required>
              </div>
              <p class="help">Vui lòng nhập tên</p>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">E-mail</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="email" autocomplete="on" name="email" value="" class="input" required>
              </div>
              <p class="help">Vui lòng nhập e-mail</p>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Số điện thoại</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="text" autocomplete="on" name="phone_number" value="" class="input" required>
              </div>
              <p class="help">Vui lòng nhập số điện thoại</p>
            </div>
          </div>
        </div>
        <hr>
        <div class="field">
          <div class="control">
            <button type="submit" class="button green">
              Cập nhật
            </button>
            <button type="button" class="button purple button-padding changepass">
              Đổi mật khẩu
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</section>

<script>

  function displaySelectedImage(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function () {
      const image = document.getElementById('avatar-preview');
      image.src = reader.result;
    };

    reader.readAsDataURL(file);
  }

  document.addEventListener('DOMContentLoaded', function () {
    const changePasswordButton = document.querySelector('.changepass');
    changePasswordButton.addEventListener('click', function () {

      window.location.href = BASE_HOSTING + 'admin_food/public/admin/password';
    });
  });

  document.addEventListener('DOMContentLoaded', function () {
    const updateForm = document.getElementById('update-form');
    updateForm.addEventListener('submit', function (event) {
      event.preventDefault();
      const customerID = localStorage.getItem('userID');
      const formData = new FormData(updateForm);
      formData.append('id', customerID);

      // Kiểm tra xem người dùng đã chọn ảnh mới hay chưa
      const fileInput = document.getElementById('avatar-upload');
      if (fileInput.files.length > 0) {
        const file = fileInput.files[0];
        formData.append('image_url', file);
      }

      fetch(BASE_URL_API + 'customer/updateAdmin', {
        method: 'POST',
        body: formData
      })
        .then(response => response.json())
        .then(data => {
          alert("Cập nhật thông tin thành công !");
          // console.log(data);
          // Xử lý dữ liệu trả về nếu cần
        })
        .catch(error => {
          alert("Cập nhật thông tin thất bại !");
          console.error('Error updating customer:', error);
          // Xử lý lỗi nếu có
        });
    });

    const userID = localStorage.getItem('userID');
    fetch(BASE_URL_API + `customer/${userID}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        const userImage = data.image_url;
        if (userImage && userImage.trim() !== '') {
          document.querySelector('.avatar-image').src = BASE_URL_AVATAR + `${userImage}`;
        }
        const userName = data.full_name;
        document.querySelector('input[name="full_name"]').value = userName;
        const userEmail = data.email;
        document.querySelector('input[name="email"]').value = userEmail;
        const userPhone = data.phone_number;
        document.querySelector('input[name="phone_number"]').value = userPhone;
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  });

</script>
@endsection