<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Storage;
use Illuminate\Http\Request;
use App\Models\Customer;
use App\Models\Location;
use App\Models\Account;
use App\Http\Controllers\MailController;
use Illuminate\Support\Facades\Hash;
class CustomerController extends Controller
{
    //
    

    public function index($id){
        $customer = Customer::find($id);
        if ($customer) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($customer,200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Customer not found'], 404);
        }
    }

    public function getAllUser(){
        $customers = Customer::get();
        if ($customers) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($customers,200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Customer not found'], 404);
        }
    }
    
    public function create(Request $request){
        try{
            $customer = new Customer();
            $customer->full_name = $request->full_name;
            $customer->email = $request->email;
            $customer->phone_number = $request->phone_number;
            
            // if($request->hasFile("image_url")){
            //     $imageName = $request->file("image_url")->getClientOriginalName();
            //     $image_path = $request->file("image_url")->storeAs("public/customerImage", $imageName);
            //     $customer->image_url = $imageName;
            // }else{
                $customer->image_url = "";
            // }
            $customer->save();

            $customer_id = $customer->id;

            $location = new Location();
            $location->customer_id = $customer_id;
            $location->name = $request->full_name;
            $location->phone_number = $request->phone_number;
            $location->address = "Việt Nam";
            $location->is_default = true; // Nếu muốn đặt mặc định, bạn có thể đặt giá trị tương ứng ở đây
            $location->save();

            $verifyCode = mt_rand(1000, 9999);
            $hashedPassword = Hash::make($request->password);
            $account = new Account();
            $account->customer_id = $customer_id;
            $account->username = $request->username;
            $account->password = $hashedPassword;
            $account->status = "inactive";
            $account->verify_code = $verifyCode;
            $account->save();
            
            $mail = new MailController();
            if($mail -> sendVerificationEmail($request->email,$verifyCode)){
                return response()->json(['message' => 'Customer added successfully'], 200);
            }else{
                return response()->json(['message' => "Can't send file"], 400);
            }
            }catch(\Throwable $e){
                return response()->json(['message' => 'Failed to add customer:'], 500);;
            }
    }

    public function createAdmin(Request $request){
        try{
            $customer = new Customer();
            $customer->full_name = $request->full_name;
            $customer->email = $request->email;
            $customer->phone_number = $request->phone_number;
            
            
            if($request->hasFile("image_url")){
                $imageName = $request->file("image_url")->getClientOriginalName();
                $image_path = $request->file("image_url")->storeAs("public/avatar", $imageName);
                $customer->image_url = $imageName;
            }
            $customer->save();

            $customer_id = $customer->id;

            $location = new Location();
            $location->customer_id = $customer_id;
            $location->name = $request->full_name;
            $location->phone_number = $request->phone_number;
            $location->address = "Việt Nam";
            $location->is_default = true; 
            $location->save();

            // $verifyCode = mt_rand(1000, 9999);
            $hashedPassword = Hash::make($request->password);
            $account = new Account();
            $account->customer_id = $customer_id;
            $account->username = $request->username;
            $account->password = $hashedPassword;
            $account->status = "active";
            $account->role = "admin";
            // $account->verify_code = $verifyCode;
            $account->save();
            
            // $mail = new MailController();
            // if($mail -> sendVerificationEmail($request->email,$verifyCode)){
                return response()->json(['message' => 'Customer added successfully'], 200);
            // }else{
                // return response()->json(['message' => "Can't send file"], 400);
            // }
            }catch(\Throwable $e){
                return response()->json(['message' => 'Failed to add customer:'], 500);;
            }
    }

    public function createGoogleCustomer(Request $request){
        try{
            $customer_id = -1;
            $checkCustomer = Customer::where('social_link', $request->social_link)->first();
            if(!$checkCustomer){
                $customer = new Customer();
                $customer->full_name = $request->full_name;
                $customer->email = $request->email;
                $customer->phone_number = $request->phone_number;
                $customer->image_url = $request->image_url;
                $customer->social_link = $request->social_link;
                $customer->save();

                $customer_id = $customer->id;

                $location = new Location();
                $location->customer_id = $customer_id;
                $location->name = $request->full_name;
                $location->phone_number = $request->phone_number;
                $location->address = "Việt Nam";
                $location->is_default = true; // Nếu muốn đặt mặc định, bạn có thể đặt giá trị tương ứng ở đây
                $location->save();

            }else{
                $customer_id = $checkCustomer->id;
            }

            return response()->json($customer_id, 200);

            }catch(\Throwable $e){
                return response()->json(['message' => 'Failed to add customer:'], 500);;
            }
    }

    public function update(Request $request){
        $customer = Customer::find($request->id);
        try{
            if(!$customer){
                return response()->json(['message'=> 'Customer not found'],404);
            }
            $customer->full_name = $request->full_name;
            $customer->phone_number = $request->phone_number;
            $customer->email = $request->email;

            // if($request->hasFile("image_url")){
            //     $imageName = $request->file("image_url")->getClientOriginalName();
            //     $image_path = $request->file("image_url")->storeAs("public/customerImage", $imageName);
            //     $customer->image_url = $imageName;
            // }
            $customer->save();
            return response()->json(['message'=> 'Customer update successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to update Customer'.$e->getMessage()],500);
        }
    }

    public function updateAdmin(Request $request){
        $customer = Customer::find($request->id);
        try {
            if(!$customer){
                return response()->json(['message'=> 'Customer not found'],404);
            }
            $customer->full_name = $request->full_name;
            $customer->phone_number = $request->phone_number;
            $customer->email = $request->email;
    
            if($request->hasFile("image_url")){
                $imageName = $request->file("image_url")->getClientOriginalName();
                $image_path = $request->file("image_url")->storeAs("public/avatar", $imageName);
                if($image_path){
                    $customer->image_url = $imageName;
                } else {
                    return response()->json(['message'=> 'Failed to upload image'],500);
                }
            }
            $customer->save();
            return response()->json(['message'=> 'Customer update successful'],200);
        } catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to update Customer: '.$e->getMessage()],500);
        }
    }
    

    public function saveImageFromUri(Request $request)
    {
        var_dump($request->file("image"));
        if($request->hasFile("image")){
            $imageName = 'image_' . time() . '.' . $request->file("image")->getClientOriginalExtension();
            $image_path = $request->file("image")->storeAs("public/avatar", $imageName);

            $customer = Customer::find($request->id);
            $customer->image_url = $imageName;
            $customer->save();
            return response()->json(['message' => 'save avatar successful']);
        }
    
        // Nếu không tìm thấy tệp ảnh trong yêu cầu
        return response()->json(['message' => 'save avatar fail']);
    }
}
