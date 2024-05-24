<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Message;
use App\Models\Customer;
use Illuminate\Support\Facades\Validator;

class MessageController extends Controller
{
    public function getCustomerMessages($customerId)
    {
        // Kiểm tra khách hàng có tồn tại không
        $customer = Customer::find($customerId);

        // Lấy tin nhắn nơi khách hàng là người gửi hoặc là người nhận
        $messages = Message::where(function ($query) use ($customerId) {
            $query->where('sender_id', $customerId)
                ->where('role', 'customer');
        })->orWhere(function ($query) use ($customerId) {
            $query->where('receiver_id', $customerId)
                ->where('role', 'admin'); // Giả sử rằng chỉ có admin mới là người nhận
        })
            ->orderBy('created_time', 'desc')
            ->get();

        return
            response()->json($messages);
    }
    public function getAllMessage(){
        $listId = Message::where('role','customer')
                        ->distinct()
                        ->pluck('sender_id');
        $listMessageById = [];
        if ($listId->isNotEmpty()) {
            foreach ($listId as $id) {
                $messages = Message::where(function ($query) use ($id) {
                                    $query->where('sender_id', $id)
                                          ->where('role', 'customer');
                                })
                                ->orWhere(function ($query) use ($id) {
                                    $query->where('receiver_id', $id)
                                          ->where('role', 'admin'); 
                                })
                                ->orderBy('created_time', 'desc')
                                ->get();
                $customer = Customer::find($id);
                $listMessageById[] = [
                    'customer_id' => $id,
                    'customer' => $customer,
                    'messages' => $messages
                ];
            }
        }
        
        return $listMessageById;
    }

    public function storeMessage(Request $request)
    {
        // Validate request
        $validator = Validator::make($request->all(), [
            'sender_id' => 'required|integer',
            'receiver_id' => 'integer|nullable',
            'content' => 'required|string',
            'role' => 'required|in:customer,admin'
        ]);

        // Check if validation fails
        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }
        if($request->receiver_id == -1){
            $receiver_id = null;
        }
        else{
            $receiver_id = $request->receiver_id;
        }
        // Create a new message instance and save it to the database
        $message = new Message;
        $message->sender_id = $request->sender_id;
        $message->receiver_id = $receiver_id;
        $message->content = $request->content;
        $message->role = $request->role;
        $message->save();

        // Return a successful response
        return response()->json(['message' => 'Message saved successfully'], 201);
    }

    public function getCustomersWithLastMessage()
    {
        // Lấy tất cả khách hàng cùng với tin nhắn cuối cùng của họ
        $customers = Customer::with([
            'messages' => function ($query) {
                $query->where(function ($q) {
                    $q->where('role', 'customer')
                        ->orWhere('role', 'admin');
                })
                    ->orderBy('created_time', 'desc')
                    ->limit(1);
            }
        ])->get();

        // Biến đổi dữ liệu để bao gồm thông tin tin nhắn cuối cùng
        $customers = $customers->map(function ($customer) {
            $lastMessage = $customer->messages->first(); // Tin nhắn cuối cùng đã được tải qua relationship
            return [
                'id' => $customer->id,
                'full_name' => $customer->full_name,
                'phone_number' => $customer->phone_number,
                'email' => $customer->email,
                'image_url' => $customer->image_url,
                'last_message' => $lastMessage ? [
                    'content' => $lastMessage->content,
                    'created_time' => $lastMessage->created_time,
                    'role' => $lastMessage->role,
                    'receiver_id' => $lastMessage->receiver_id,
                    'sender_id' => $lastMessage->sender_id
                ] : null
            ];
        });

        return response()->json($customers);
    }
}
