<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;

class MailController extends Controller
{
    //
    public function sendVerificationEmail($email, $verifyCode)
    {
        $mail = new PHPMailer(true);
        try {
            $mail->SMTPDebug = SMTP::DEBUG_SERVER;                      //Enable verbose debug output
            $mail->IsSMTP(); // enable SMTP
            //$mail->SMTPDebug = 1; // debugging: 1 = errors and messages, 2 = messages only
            $mail->SMTPAuth = true; // authentication enabled
            $mail->SMTPSecure = 'ssl'; // secure transfer enabled REQUIRED for Gmail
            $mail->Host = "smtp.gmail.com";
            $mail->Port = 465; // or 587
            $mail->IsHTML(true);
            $mail->Username = "stockadobe1234@gmail.com";
            $mail->Password = "zxloelcssxirwrxb";
            $mail->SetFrom("stockadobe1234@gmail.com");
            $mail->Subject = "Food shop";
            $logo_url = 'https://cdn.dribbble.com/users/630677/screenshots/3833541/media/201454f743f48c415a38c49419275692.jpg?resize=400x0'; // Đường dẫn đến hình ảnh logo của bạn
            $mail->Body = '
                        <div style="text-align: center;">
                            <img src="' . $logo_url . '" alt="Logo" style="max-width: 300px; height: auto;">
                            <h2 style="font-size: 18px;">Vui lòng nhập mã: <strong>' . $verifyCode . '</strong> để xác nhận tài khoản!</h2>
                            <p>Link có hiệu lực trong vòng 5 phút!</p>
                        </div>';
            $mail->isHTML(true);
            $mail->AddAddress($email);
            $mail->SMTPDebug = SMTP::DEBUG_OFF;
            $mail->send();
            return true;
        } catch (e) {
            return false;
        }
    }

    public function sendNewPasswordEmail($email, $newPassword)
{
    $mail = new PHPMailer(true);
    try {
        $mail->SMTPDebug = SMTP::DEBUG_SERVER;                      //Enable verbose debug output
        $mail->IsSMTP(); // enable SMTP
        //$mail->SMTPDebug = 1; // debugging: 1 = errors and messages, 2 = messages only
        $mail->SMTPAuth = true; // authentication enabled
        $mail->SMTPSecure = 'ssl'; // secure transfer enabled REQUIRED for Gmail
        $mail->Host = "smtp.gmail.com";
        $mail->Port = 465; // or 587
        $mail->IsHTML(true);
        $mail->Username = "stockadobe1234@gmail.com";
        $mail->Password = "zxloelcssxirwrxb";
        $mail->SetFrom("stockadobe1234@gmail.com");
        $mail->Subject = "Food shop - Mật khẩu mới";
        $logo_url = 'https://cdn.dribbble.com/users/630677/screenshots/3833541/media/201454f743f48c415a38c49419275692.jpg?resize=400x0'; // Đường dẫn đến hình ảnh logo của bạn
        $mail->Body = '
                    <div style="text-align: center;">
                        <img src="' . $logo_url . '" alt="Logo" style="max-width: 300px; height: auto;">
                        <h2 style="font-size: 18px;">Mật khẩu mới của bạn là: <strong>' . $newPassword . '</strong></h2>
                        <p>Vui lòng đổi mật khẩu sau khi đăng nhập thành công!</p>
                    </div>';
        $mail->isHTML(true);
        $mail->AddAddress($email);
        $mail->SMTPDebug = SMTP::DEBUG_OFF;
        $mail->send();
        return true;
    } catch (Exception $e) {
        return false;
    }
}
}
