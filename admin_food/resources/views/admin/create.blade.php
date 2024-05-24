@extends("layout")
@section("content")

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
</style>
<link rel="stylesheet" href="{{asset("../resources/css/admin.css")}}">
<section class="section main-section">
  <div class="card">
    <header class="card-header">
      <p class="card-header-title">
        <span class="icon"><i class="mdi mdi-account-plus"></i></span>
        Tạo tài khoản mới
      </p>
    </header>
    <div class="card-content">
      <form id="create-form">
        <div class="field">
          <label class="label">Ảnh đại diện</label>
          <div class="center-content">
            <div class="avatar-container">
              <img id="avatar-preview" src="https://avatars.dicebear.com/v2/initials/default.svg" alt="image_url" class="avatar-image">
            </div>
            <div class="field-body w-full button-padding">
              <div class="field file flex justify-center items-center">
                <label class="upload control text-center">
                  <a class="button blue">
                    Tải lên ảnh
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
                <input type="text" autocomplete="on" name="full_name" class="input" required>
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
                <input type="email" autocomplete="on" name="email" class="input" required>
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
                <input type="text" autocomplete="on" name="phone_number" class="input" required>
              </div>
              <p class="help">Vui lòng nhập số điện thoại</p>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Tên tài khoản</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="text" autocomplete="on" name="username" class="input" required>
              </div>
              <p class="help">Vui lòng nhập tên tài khoản</p>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Mật khẩu</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="password" autocomplete="on" name="password" class="input" required>
              </div>
              <p class="help">Vui lòng nhập mật khẩu</p>
            </div>
          </div>
        </div>
        <div class="field">
          <label class="label">Xác nhận mật khẩu</label>
          <div class="field-body">
            <div class="field">
              <div class="control">
                <input type="password" autocomplete="on" name="Repassword" class="input" required>
              </div>
              <p class="help">Vui lòng nhập lại mật khẩu</p>
            </div>
          </div>
        </div>
        <hr>
        <div class="field">
          <div class="control">
            <button type="submit" class="button green">
              Tạo tài khoản
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

  reader.onload = function() {
    const image = document.getElementById('avatar-preview');
    image.src = reader.result;
  };

  if (file) {
    reader.readAsDataURL(file); // Đảm bảo rằng chỉ khi có file được chọn, hàm `readAsDataURL` mới được gọi
  }
}

  document.addEventListener('DOMContentLoaded', function() {
  const createForm = document.getElementById('create-form');
  createForm.addEventListener('submit', function(event) {
    event.preventDefault();

    // Lấy giá trị của các trường thông tin
    const fullName = createForm.querySelector('input[name="full_name"]').value;
    const email = createForm.querySelector('input[name="email"]').value;
    const phoneNumber = createForm.querySelector('input[name="phone_number"]').value;
    const password = createForm.querySelector('input[name="password"]').value;
    const username = createForm.querySelector('input[name="username"]').value; // Thêm dòng này để lấy giá trị username
    const repassword = createForm.querySelector('input[name="Repassword"]').value;

    // Kiểm tra mật khẩu có ít nhất 6 ký tự và không viết tắt
    const passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

    if (!passwordPattern.test(password)) {
      alert("Mật khẩu phải có ít nhất 6 ký tự, bao gồm ít nhất 1 ký tự hoa, 1 ký tự số và 1 ký tự đặc biệt.");
      return; // Dừng việc gửi yêu cầu nếu mật khẩu không đáp ứng yêu cầu
    }

    // Kiểm tra mật khẩu và xác nhận mật khẩu
    if (password !== repassword) {
      alert("Mật khẩu và xác nhận mật khẩu không khớp!");
      return; // Dừng việc gửi yêu cầu nếu mật khẩu không khớp
    }

    // Kiểm tra số điện thoại có đúng định dạng hay không (ví dụ: 10 chữ số)
    const phonePattern = /^\d{10}$/;
    if (!phonePattern.test(phoneNumber)) {
      alert("Số điện thoại không hợp lệ. Vui lòng nhập lại!");
      return; // Dừng việc gửi yêu cầu nếu số điện thoại không hợp lệ
    }

    const formData = new FormData(createForm);

    // Kiểm tra xem người dùng đã chọn ảnh mới hay chưa
    const fileInput = document.getElementById('avatar-upload');
    if (fileInput.files.length > 0) {
      const file = fileInput.files[0];
      formData.append('image_url', file);
    }

    // Thêm các trường username, email, phone_number, và full_name vào formData
    formData.append('username', username);
    formData.append('email', email);
    formData.append('phone_number', phoneNumber);
    formData.append('full_name', fullName);

    fetch(BASE_URL_API+'customer/createAdmin', {
        method: 'POST',
        body: formData
      })
      .then(response => response.json())
      .then(data => {
        alert("Tạo tài khoản thành công !");
        // Xử lý dữ liệu trả về nếu cần
        window.location.href = "{{route("home")}}";
      })
      .catch(error => {
        alert("Tạo tài khoản thất bại !");
        console.error('Error creating account:', error);
        // Xử lý lỗi nếu có
      });
  });
});

</script>


@endsection
