@extends("layout")
@section("content")
<div class="card">
    <header class="card-header">
        <p class="card-header-title">
            <span class="icon"><i class="mdi mdi-lock"></i></span>
            Đổi mật khẩu
        </p>
    </header>
    <div class="card-content">
        <form id="change-password-form">
            <div class="field">
                <label class="label">Mật khẩu hiện tại</label>
                <div class="control">
                    <input type="password" name="password_current" autocomplete="current-password" class="input" required>
                </div>
                <p class="help">Vui lòng nhập mật khẩu hiện tại</p>
            </div>
            <hr>
            <div class="field">
                <label class="label">Mật khẩu mới</label>
                <div class="control">
                    <input type="password" autocomplete="new-password" name="password" class="input" required>
                </div>
                <p class="help">Mật khẩu phải có ít nhất 6 ký tự, một ký tự hoa, một ký tự số và một ký tự đặc biệt</p>
            </div>
            <div class="field">
                <label class="label">Xác nhận mật khẩu</label>
                <div class="control">
                    <input type="password" autocomplete="new-password" name="password_confirmation" class="input" required>
                </div>
                <p class="help">Vui lòng xác nhận lại mật khẩu</p>
            </div>
            <hr>
            <div class="field">
                <div class="control">
                    <button type="submit" class="button green">
                        Thay đổi
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const changePasswordForm = document.getElementById('change-password-form');
        changePasswordForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const currentPassword = changePasswordForm.querySelector('input[name="password_current"]').value;
            const newPassword = changePasswordForm.querySelector('input[name="password"]').value;
            const confirmPassword = changePasswordForm.querySelector('input[name="password_confirmation"]').value;

            // Kiểm tra xác nhận mật khẩu
            if (newPassword !== confirmPassword) {
                alert('Xác nhận mật khẩu không khớp');
                return;
            }
            // Kiểm tra mật khẩu mới khác mật khẩu cũ
            if (newPassword === currentPassword) {
                alert('Mật khẩu mới không được giống với mật khẩu cũ');
                return;
            }
            // Kiểm tra mật khẩu mới theo yêu cầu: ít nhất 6 ký tự, một ký tự hoa, một ký tự số và một ký tự đặc biệt
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
            if (!passwordRegex.test(newPassword)) {
                alert('Mật khẩu mới phải có ít nhất 6 ký tự, một ký tự hoa, một ký tự số và một ký tự đặc biệt');
                return;
            }

            const customerID = localStorage.getItem('userID');

            // Gọi API để đổi mật khẩu
            fetch(BASE_URL_API+'account/changePass', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    id: customerID,
                    oldPass: currentPassword,
                    newPass: newPassword
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error response from server');
                }
                return response.json();
            })
            .then(data => {
                alert("Đổi mật khẩu thành công!");
                changePasswordForm.reset();
            })
            .catch(error => {
                console.error('Error changing password:', error);
                alert('Đổi mật khẩu không thành công, vui lòng kiểm tra lại');
            });
        });
    });
</script>
@endsection
