<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Account;
use App\Models\Customer;
use Carbon\Carbon;
use Illuminate\Support\Facades\Hash;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;

class AccountController extends Controller
{
    //

    public function getAllAccount(){ 
        $accounts = Account::whereIn('role', ['customer', 'staff'])->get();
        return response()->json($accounts, 200);
    }
    public function getAccountInfo($textSearch)
{
    $accounts = Account::where('username', 'like', "%$textSearch%")
                        ->orWhere('id', $textSearch)
                        ->orWhere('customer_id', $textSearch)
                        ->get();

    if ($accounts->isNotEmpty()) {
        return response()->json($accounts, 200);
    }

    // Trả về thông báo nếu không tìm thấy
    return response()->json(['message' => 'No accounts found'], 404);
}

    // public function getIdAccount($customer_id){
    //     $account = Account::where('customer_id', $customer_id)->first();
    //     if($account) {
    //         return response()->json(['id' => $account->id], 200); // Thêm key 'id' vào JSON response
    //     } else {
    //         return response()->json(['message' => 'Account not found'], 404);
    //     }
    // }

    public function changePassword(Request $request){
        try{
            $id = $request->id;
            $new_password = $request->newPass;
            $old_password = $request->oldPass;
    
            // Lấy thông tin tài khoản
            $account= Account::where('customer_id', $id)->first();
    
            // Kiểm tra xem tài khoản có tồn tại không
            if(!$account) {
                return response()->json(['message' => 'Account does not exist'], 400);
            }
    
            // Kiểm tra mật khẩu cũ
            if (Hash::check($old_password, $account->password)) {
                $account->password = Hash::make($new_password);
                $account->save();
                return response()->json(['message' => 'Password changed successfully'], 200);
            }else{
                return response()->json(['message' => 'Password changed fail'], 400);
            }
    
        } catch(\Throwable $e){
            return response()->json(['message'=> 'Password changed fail1'], 500);
        }
    }
    public function recoveryPasswordAdmin(Request $request){
        $account = Account::where("username", $request->username)->first();
        if(!$account) {
            return response()->json(['message' => 'Account not found'], 404);
        }

        $customer = Customer::find($account->customer_id);

        if(!$customer) {
            return response()->json(['message' => 'Customer not found'], 404);
        }
        $email = $customer->email;
        if($request->email != $email){
            return response()->json(['message' => 'Recovery password fail'], 400);
        }
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 10; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }

        $mail = new MailController();
            if($mail -> sendNewPasswordEmail($email,$randomString)){
                $account->password = Hash::make($randomString);
                $account->save();
                return response()->json(['message' => 'Recovery password successful'], 200);
            }else{
                return response()->json(['message' => "Recovery password fail"], 400);
            }
    }
    public function recoveryPassword(Request $request){
        $account = Account::where("username", $request->username)->first();
        if(!$account) {
            return response()->json(['message' => 'Account not found'], 404);
        }

        $customer = Customer::find($account->customer_id);

        if(!$customer) {
            return response()->json(['message' => 'Customer not found'], 404);
        }
        $email = $customer->email;

        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 10; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }

        $mail = new MailController();
            if($mail -> sendNewPasswordEmail($email,$randomString)){
                $account->password = Hash::make($randomString);
                $account->save();
                return response()->json(['message' => 'Recovery password successful'], 200);
            }else{
                return response()->json(['message' => "Recovery password fail"], 400);
            }
    }

    public function getAccountByName(Request $request){ // kiểm tra tài khoản tồn tại 
        $account = Account::where("username",$request->username)->first();
        if ($account) {
            return response()->json(['account' => $account], 200);
        } else {
                return response()->json(['message' => 'Account does not exist'], 404);
        }
    }

    public function isExistAccount($username){ // kiểm tra tài khoản tồn tại 
        $account = Account::where("username",$username)->first();
        if ($account) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json(['message' => 'Account already exists'], 200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Account does not exist'], 404);
        }
    }


    public function getAccountByCustomerId($customer_id){ // kiểm tra tài khoản tồn tại 
        $account = Account::where("customer_id",$customer_id)->first();
        if ($account) {
            return response()->json($account, 200);
        } else {
                return response()->json(['message' => 'Account does not exist'], 404);
        }
    }

    public function checkVerifyCode(Request $request){
        $username = $request ->username;
        $verifyCode = $request ->verifyCode;

        $account = Account::where('username',$username)->first();

        $now = now()->setTimezone('Asia/Ho_Chi_Minh');
        $time = $now->diffInMinutes($account->update_time);
        // var_dump($account);
        // var_dump($account->verify_code == $verifyCode);
        if($account->verify_code == $verifyCode){
            if(abs($time) < 5 && abs($time) > 0){
                // if($account->verify_code == $verifyCode){
                    $account->status = 'active';
                    $account->verify_code = null;
                    $account->save();
                    return response()->json(['message'=> 'Verify success'], 200);
            }else{
                    return response()->json(['message'=> 'Verify code is out of date'], 200);
                }
            }else{
                return response()->json(['message'=> 'Verify code is not correct'], 200);
            }
        // }
        
    }

    public function reCodeOtp(Request $request){
        try{
            $account = Account::where('customer_id',$request->customer_id)->first();
            $verifyCode = mt_rand(1000, 9999);
            $account->verify_code = $verifyCode;
            $account->save();
            $mail = new MailController();
            if($mail -> sendVerificationEmail($request->email,$verifyCode)){
                return response()->json(['message' => 'Resend OTP successful'], 200);
            }else{
                return response()->json(['message' => "Can't send file"], 400);
            }
        }catch(\Throwable $e){
            return response()->json(['message' => 'Resend OTP faild'], 500);
        }
    }
    public function Login(Request $request){
        try{
            $user_name = $request->username;
            $password = $request->password;
    
    
            $account= Account::where('username',$user_name)->first();
            if(!$account) {
                return response()->json(['message' => 'Account do not exist'], 200);
            }else{

                if (!Hash::check($password, $account->password)) {

                    return response()->json(['message' => 'Login failed'], 200);
                }else{
                    if($account->status == 'inactive'){
                        return response()->json(['message' => 'Active account','id_customer' => $account['customer_id']], 200);
                    }else if($account->status == 'block'){
                        return response()->json(['message' => 'Account has been block'], 200);   
                    }else{
                        return response()->json(['message' => 'Login successful','id_customer' => $account['customer_id']], 200);
                    }
                }
                
            }
        }catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to sign in account'.$e->getMessage()],500);
        }

    }


    public function getInforAccountByCusID($customer_id){
        $account = Account::where('customer_id', $customer_id)->first();
        if($account) {
            return $account;
        } else {
            return response()->json(['message' => 'Account do not exist'], 200);
        }
    }

    public function LoginAdmin(Request $request){
        try{
            $user_name = $request->username;
            $password = $request->password;
    
    
            $account= Account::where('username',$user_name)->first();
            if(!$account) {
                return response()->json(['message' => 'Account do not exist'], 200);
            }else{

                if (!Hash::check($password, $account->password)) {

                    return response()->json(['message' => 'Login failed'], 200);
                }else{
                    if($account->role == 'customer'){
                        return response()->json(['message' => 'Can not use customer account'], 200);
                    }else{
                        return response()->json(['message' => 'Login successful','id' => $account['customer_id']], 200);
                    }
                }
                
            }
        }catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to sign in account'.$e->getMessage()],500);
        }

    }

    
    // public function getInforAccount(Request $request){
    //     try{
    //         $user_name = $request->username;
    //         $password = $request->password;
    
    
    //         $account= Account::where('username',$user_name)->first();
    //         if(!$account) {
    //             return response()->json(['message' => 'Account do not exist'], 200);
    //         }else{

    //             if (!Hash::check($password, $account->password)) {

    //                 return response()->json(['message' => 'Login failed'], 200);
    //             }else{
    //                 if($account->role == 'customer'){
    //                     return response()->json(['message' => 'Can not use customer account'], 200);
    //                 }else{
    //                     return response()->json(['message' => 'Login successful','id' => $account['customer_id']], 200);
    //                 }
    //             }
                
    //         }
    //     }catch(\Throwable $e){
    //         return response()->json(['message'=> 'Failed to sign in account'.$e->getMessage()],500);
    //     }

    // }

    public function destroy($id){ // xóa tài khoản
        $account = Account::find( $id );
        try{
            if(!$account){
                return response()->json(['message'=> 'Account not found'],404);
            }
            $account->delete();
            return response()->json(['message'=> 'Account delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete account'.$e->getMessage()],500);
        }
    }

    public function managerAccount($id){ // khóa tài khoản
        $account = Account::find( $id );
        try{
            if(!$account){
                return response()->json(['message'=> 'Account not found'],404);
            }
            if($account->status == 'active'){
                $account->status = 'block';
            }else if ($account->status == 'block'){
                $account->status = 'active';
            }
            $account->save();
            return response()->json(['message'=> 'Account status change successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to change account status'],500);
        }
    }

    public function managerAccountRole($id){ // khóa tài khoản
        $account = Account::find( $id );
        try{
            if(!$account){
                return response()->json(['message'=> 'Account not found'],404);
            }
            if($account->role == 'customer'){
                $account->role = 'staff';
            }else if ($account->role == 'staff'){
                $account->role = 'customer';
            }else{
                return response()->json(['message'=> 'Can not change role of admin'],400);
            }
            $account->save();
            return response()->json(['message'=> 'Account role change successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to change account role'],500);
        }
    }
}
