<?php

namespace App\Http\Controllers;

use App\Models\Discount;
use Illuminate\Http\Request;
use App\Models\Orders;
use App\Models\Location;
use App\Models\OrderDetail;
use App\Models\Cart;
use App\Models\Image;
use App\Models\Product;
use App\Models\Customer_discount;
use Carbon\Carbon;
use Psy\Readline\Hoa\Console;
// use Illuminate\Support\Carbon;



class OrdersController extends Controller
{
    //



    public function getRecentOrdersByCustomerId( $customer_id)
{
    try {
        $today = Carbon::now();

        // Lấy ngày 5 ngày trước ở dạng timestamp
        $fiveDaysAgo = Carbon::now()->subDays(5);

        $nowVietnamTime = $today->setTimezone('Asia/Ho_Chi_Minh');

        // Lấy ngày 5 ngày trước ở dạng timestamp
        $fiveDaysAgo = $nowVietnamTime->copy()->subDays(5);
        // Lấy thông tin đơn hàng và các sản phẩm của đơn hàng đó dựa vào customer_id, status, và created_time
        $orders = Orders::where('customer_id', $customer_id)
            ->whereBetween('created_time', [$fiveDaysAgo, $today]) // Lọc theo ngày tạo trong khoảng 5 ngày gần nhất
            ->orderByDesc('id')
            ->get();

        // Trả về dữ liệu đơn hàng và các sản phẩm tương ứng dưới dạng JSON
        return response()->json($orders, 200);
    } catch (\Exception $e) {
        // Nếu có lỗi xảy ra, trả về thông báo lỗi
        return response()->json(["error" => $e->getMessage()],500);
    }
}
    public function index(Request $request)
    {
        try {
            // Lấy thông tin đơn hàng và các sản phẩm của đơn hàng đó dựa vào customer_id và status
            $orders = Orders::with('orderDetail.product')
                ->where('customer_id', $request->customer_id)
                ->where('status', $request->status)
                ->get();

            // Trả về dữ liệu đơn hàng và các sản phẩm tương ứng dưới dạng JSON
            return response()->json($orders, 200);
        } catch (\Exception $e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            return response()->json(["error" => $e->getMessage()]);
        }
    }

    public function getOrderInfo($textSearch)
{
    try {
        // Sử dụng model của bảng Orders để truy vấn các đơn hàng đã được nhóm theo order_ids (dựa vào bảng order_detail)
        $groupedOrders = Orders::selectRaw('customer_id, name, phone_number, address, note, payment, payment_status, 
                                            CASE 
                                                WHEN status IN ("rating", "completed") THEN "completed"
                                                ELSE status 
                                            END AS overall_status,
                                            created_time')
            ->selectRaw('GROUP_CONCAT(orders.id) as order_ids')
            ->leftJoin('order_detail', 'orders.id', '=', 'order_detail.order_id') // Tham gia bảng order_detail
            ->groupBy('customer_id', 'name', 'phone_number', 'address', 'note', 'payment', 'payment_status', 'overall_status', 'created_time')
            ->orderBy('created_time', 'desc') // Sắp xếp theo thời gian tạo, giảm dần
            ->where(function ($query) use ($textSearch) {
                $query->where('orders.id', $textSearch)
                    ->orWhere('customer_id', $textSearch)
                    ->orWhere('name', 'like', "%$textSearch%")
                    ->orWhere('payment_status', 'like', "%$textSearch%")
                    ->orWhere('status', 'like', "%$textSearch%");
            })
            ->get();

        // Chuyển cột order_ids thành mảng
        $groupedOrders->transform(function ($order) {
            $order->order_ids = explode(',', $order->order_ids);
            // Lấy toàn bộ thông tin của bảng order_detail dựa trên order_ids
            $order->order_details = OrderDetail::whereIn('order_id', $order->order_ids)->get();
            $order->order_details->transform(function ($orderDetail) {
                $images = Image::where('product_id', $orderDetail->product_id)->pluck('imgurl')->toArray();
                $orderDetail->images = $images;
                return $orderDetail;
            });
            return $order;
        });
        // Trả về danh sách các đơn hàng đã được nhóm và gộp cùng với các chi tiết đơn hàng
        return response()->json($groupedOrders, 200);
    } catch (\Exception $e) {
        // Nếu có lỗi xảy ra, trả về thông báo lỗi
        return response()->json(["error" => $e->getMessage()]);
    }
}


    public function getAllOrders()
{
    try {
        // Sử dụng model của bảng Orders để truy vấn các đơn hàng đã được nhóm theo order_ids (dựa vào bảng order_detail)
        $groupedOrders = Orders::selectRaw('customer_id, name, phone_number, address, note, payment, payment_status, 
                                            CASE 
                                                WHEN status IN ("rating", "completed") THEN "completed"
                                                ELSE status 
                                            END AS overall_status,
                                            created_time')
            ->selectRaw('GROUP_CONCAT(orders.id) as order_ids')
            ->leftJoin('order_detail', 'orders.id', '=', 'order_detail.order_id') // Tham gia bảng order_detail
            ->groupBy('customer_id', 'name', 'phone_number', 'address', 'note', 'payment', 'payment_status', 'overall_status', 'created_time')
            ->orderBy('created_time', 'desc') // Sắp xếp theo thời gian tạo, giảm dần
            ->get();

        // Chuyển cột order_ids thành mảng
        $groupedOrders->transform(function ($order) {
            $order->order_ids = explode(',', $order->order_ids);
            // Lấy toàn bộ thông tin của bảng order_detail dựa trên order_ids
            $order->order_details = OrderDetail::whereIn('order_id', $order->order_ids)->get();
            $order->order_details->transform(function ($orderDetail) {
                $images = Image::where('product_id', $orderDetail->product_id)->pluck('imgurl')->toArray();
                $orderDetail->images = $images;
                return $orderDetail;
            });
            return $order;
        });
        // Trả về danh sách các đơn hàng đã được nhóm và gộp cùng với các chi tiết đơn hàng
        return response()->json($groupedOrders, 200);
    } catch (\Exception $e) {
        // Nếu có lỗi xảy ra, trả về thông báo lỗi
        return response()->json(["error" => $e->getMessage()]);
    }
}

    



public function markOrdersAsCompleted(Request $request)
{
    $orderIds = $request->order_ids ?? []; // Lấy danh sách order_ids từ request, mặc định là một mảng trống nếu không được cung cấp

    // Kiểm tra xem danh sách order_ids có dữ liệu không
    if (!empty($orderIds)) {
        // Chắc chắn rằng $orderIds là một mảng hợp lệ
        if (!is_array($orderIds)) {
            // Trả về lỗi nếu $orderIds không phải là một mảng
            return response()->json(['error' => 'Danh sách order_ids không hợp lệ'], 400);
        }

        // Cập nhật trạng thái thanh toán của các đơn hàng
        Orders::whereIn('id', $orderIds)->update(['payment_status' => 'completed']);
        
        // Trả về thông báo hoặc thực hiện các hành động khác nếu cần thiết
        return response()->json(['message' => 'Cập nhật trạng thái thành công']);
    } else {
        // Trả về thông báo hoặc thực hiện các hành động khác nếu danh sách rỗng
        return response()->json(['message' => 'Không có đơn hàng được cập nhật']);
    }
}


public function updateOrderStatus(Request $request)
{
    $orderIds = $request->order_ids ?? [];
    // Kiểm tra xem mảng order_ids có dữ liệu không
    if (!empty($orderIds)) {
        // Chắc chắn rằng $orderIds là một mảng hợp lệ
        if (!is_array($orderIds)) {
            // Trả về lỗi nếu $orderIds không phải là một mảng
            return ['error' => 'Danh sách order_ids không hợp lệ'];
        }

        // Lấy ra các đơn hàng dựa trên order_ids và trạng thái là initialization hoặc pending
        $ordersToUpdate = Orders::whereIn('id', $orderIds)
            ->where(function ($query) {
                $query->where('status', 'initialization')
                      ->orWhere('status', 'pending');
            })
            ->get();

        // Cập nhật trạng thái của các đơn hàng
        foreach ($ordersToUpdate as $order) {
            if ($order->status === 'initialization') {
                $order->status = 'pending';
            } elseif ($order->status === 'pending') {
                $order->status = 'rating';
            }
            $order->save();
        }
        
        // Trả về thông báo hoặc thực hiện các hành động khác nếu cần thiết
        return ['message' => 'Cập nhật trạng thái thành công'];
    } else {
        // Trả về thông báo hoặc thực hiện các hành động khác nếu danh sách rỗng
        return ['message' => 'Không có đơn hàng được cập nhật'];
    }
}



    public function getByCustomerId(Request $request)
    {
        try {
            // Lấy thông tin đơn hàng và các sản phẩm của đơn hàng đó dựa vào customer_id và status
            $orders = Orders::with('orderDetail.product')
                ->where('customer_id', $request->customer_id)
                ->get();

            // Trả về dữ liệu đơn hàng và các sản phẩm tương ứng dưới dạng JSON
            return response()->json($orders, 200);
        } catch (\Exception $e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            return response()->json(["error" => $e->getMessage()]);
        }
    }

    
    // public function create(Request $request){
    //     try{
    //         // kiểm tra đủ số lượng không
    //     foreach ($request->product_ids as $product_id) {
    //         $quantity = Cart::where("product_id", $product_id)
    //                         ->where("customer_id", $request->customer_id)
    //                             ->first()->quantity;
    //         $product = Product::find($product_id);
    //         if($product -> quantity < $quantity){
    //             return response()->json(["error" => "Not enough quantity for product"]);
    //         }
    //     }//

    //         $location = Location::where("customer_id", $request->customer_id)
    //                             ->where("is_default", 1)
    //                             ->first();
    //         $order = new Orders();
    //         $order->customer_id = $request->customer_id;
    //         $order->name = $location->name;
    //         $order->address = $location->address;
    //         $order->phone_number = $location->phone_number;
    //         $order->note = $request->note;
    //         $order->payment = $request->payment;
    //         $order->payment_status = ($request->payment == "cash") ? "completed" : "initialization";
    //         $order->status = "initialization";
    //         $order->save();

    //         $orderId = $order->id;
    //         foreach ($request->product_ids as $product_id) {
    //             $orderDetail = new OrderDetail();
    //             $orderDetail->product_id = $product_id;
    //             $orderDetail->order_id = $orderId;
    //             $productInCart = Cart::where("product_id", $product_id)
    //                             ->where("customer_id", $request->customer_id)
    //                                 ->first();
    //             $product = Product::find($product_id);
    //             $orderDetail->quantity = $productInCart -> quantity;
    //             $orderDetail->price = $productInCart -> quantity * ($product ->price - $product ->price *($product->discount / 100));
    //             $product -> quantity = $product -> quantity - $productInCart -> quantity;

    //             $orderDetail->save();
    //             $product->save();
    //             $productInCart->delete();
    //         }
    //         return response()->json(['message' => 'Add new order successfully'], 200);

    //     }catch (\Exception $e) {
    //         return response()->json(["error" => "add new order fail"],500);
    //     }
    // }


    public function create(Request $request){
        try {
            $discount = Discount::where("id", $request->discount_id)
                                    ->first();

            if($discount){
                $discount_percent = $discount -> discount_percent;
                if($discount->number_uses !== null){
                    $discount->number_uses = $discount->number_uses - 1;
                }
                $discount->save();
                $customerDiscount = new Customer_discount;
                $customerDiscount->customer_id = $request->customer_id;
                $customerDiscount->discount_id = $discount->id;
                $customerDiscount->save();
            }else{
                $discount_percent = 0;
            }
            // Lặp qua từng product_id trong product_ids
            foreach ($request->product_ids as $product_id) {
                // Kiểm tra số lượng sản phẩm có đủ không
                $quantity = Cart::where("product_id", $product_id)
                                ->where("customer_id", $request->customer_id)
                                ->first()->quantity;
                $product = Product::find($product_id);
                if ($product->quantity < $quantity) {
                    return response()->json(["error" => "Not enough quantity for product"]);
                }
    
                // Tạo đơn hàng mới
                $order = new Orders();
                $order->customer_id = $request->customer_id;
                $location = Location::where("customer_id", $request->customer_id)
                                    ->where("id", $request->location_id)
                                    ->first();
                $order->name = $location->name;
                $order->address = $location->address;
                $order->phone_number = $location->phone_number;
                $order->note = $request->note;
                $order->payment = $request->payment;
                $order->payment_status = ($request->payment == "cash") ? "initialization" : "completed";
                $order->status = ($request->payment == "cash") ? "initialization" : "pending";
                $order->save();
    
            
                // Lấy id của đơn hàng vừa tạo
                $orderId = $order->id;
    
                // Tạo chi tiết đơn hàng cho sản phẩm hiện tại
                $orderDetail = new OrderDetail();
                $orderDetail->product_id = $product_id;
                $orderDetail->order_id = $orderId;
                $productInCart = Cart::where("product_id", $product_id)
                                    ->where("customer_id", $request->customer_id)
                                    ->first();
                $product = Product::find($product_id);
                $orderDetail->quantity = $productInCart->quantity;
                $orderDetail->price = ($productInCart->quantity * ($product->price - $product->price * ($product->discount / 100))) - ($productInCart->quantity * ($product->price - $product->price * ($product->discount / 100)) * ($discount_percent/100));
                $product->quantity -= $productInCart->quantity; // Giảm số lượng sản phẩm trong kho
                $orderDetail->save();
                $product->save();
                $productInCart->delete();
            }
            return response()->json(['message' => 'Add new order successfully'], 200);
        } catch (\Exception $e) {
            return response()->json(["error" => "Add new order failed" . $e->getMessage()], 501);
        }
    }
    
    public function delete($order_id)
{
    try {
        // Tìm đơn hàng cần xóa
        $order = Orders::find($order_id);
        
        $orderDetail = OrderDetail::where('order_id', $order_id)->first();

            if ($orderDetail) {
                $product = Product::find($orderDetail->product_id);
                
                if ($product) {
                    $product->quantity += $orderDetail->quantity;
                    $product->save();
                }

                // Xóa chi tiết đơn hàng
                $orderDetail->delete();
            }
        
        // Xóa đơn hàng
        $order->delete();
        
        return response()->json(['message' => 'Order deleted successfully'], 200);
    } catch (\Exception $e) {
        return response()->json(['error' => 'Delete order failed'], 500);
    }
}

    public function createSingleProductOrder(Request $request){
        $discount = Discount::where("id", $request->discount_id)
                                    ->first();
        if($discount){
            $discount_percent = $discount -> discount_percent;
            $customerDiscount = new Customer_discount;
            $customerDiscount->customer_id = $request->customer_id;
            $customerDiscount->discount_id = $discount->id;
            $customerDiscount->save();
        }else{
            $discount_percent = 0;
        }
        try{
            // Kiểm tra số lượng sản phẩm có đủ không
            $product_id = $request->product_id;
            $quantity = $request->quantity;

            $product = Product::find($product_id);
            if ($product->quantity < $quantity) {
                return response()->json(["error" => "Not enough quantity for product"], 400);
            }
    
            // Tạo đơn hàng
            $location = Location::where("customer_id", $request->customer_id)
                                    ->where("id", $request->location_id)
                                    ->first();
            $order = new Orders();
            $order->customer_id = $request->customer_id;
            $order->name = $location->name;
            $order->address = $location->address;
            $order->phone_number = $location->phone_number;
            $order->note = $request->note;
            $order->payment = $request->payment;
            $order->payment_status = ($request->payment == "cash") ? "initialization" : "completed";
            $order->status = ($request->payment == "cash") ? "initialization" : "pending";
            $order->save();
    
            // Tạo chi tiết đơn hàng
            $orderId = $order->id;
            $orderDetail = new OrderDetail();
            $orderDetail->product_id = $product_id;
            $orderDetail->order_id = $orderId;
            $orderDetail->quantity = $quantity;
            $orderDetail->price = ($quantity * ($product->price - $product->price * ($product->discount / 100))) - (($quantity * ($product->price - $product->price * ($product->discount / 100))) * ($discount_percent/100));
            $product->quantity -= $quantity; // Giảm số lượng sản phẩm
            $orderDetail->save();
            $product->save();
    
            return response()->json(['message' => 'Add new order successfully'], 200);
        } catch (\Exception $e) {
            return response()->json(["error" => "add new order fail" . $e->getMessage()], 500);
        }
    }

    public function getRatedProductsByCustomer($customer_id){
        try{
            // Lấy danh sách đơn hàng đã hoàn thành của khách hàng
            $orders = Orders::where('customer_id', $customer_id)
                           ->where('status', 'rating')
                           ->pluck('id');
            
            // Lấy danh sách sản phẩm từ bảng Order Detail trong các đơn hàng đã hoàn thành
            $orderDetails = OrderDetail::whereIn('order_id', $orders)
                                       ->pluck('product_id');            
            // Lấy danh sách các sản phẩm có status là "rating" và nằm trong các đơn hàng đã hoàn thành
            $products = Product::with('review', 'image', 'categorie')
                        ->whereIn('id', $orderDetails)
                        // ->where('status', 'rating')
                        ->get();
                
        // var_dump($products->all());
            
            // Tính điểm đánh giá trung bình và số lượng sản phẩm đã bán cho từng sản phẩm
            foreach ($products as $product) {
                $totalRate = 0;
                $reviewCount = $product->review->count();
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }
                $product->average_rate = ($reviewCount > 0) ? ($totalRate / $reviewCount) : 0;
                
                $productId = $product->id;
                $saleNumber = OrderDetail::where('product_id', $productId)->count();
                $product->sale_number = $saleNumber;
            }
            
            return response()->json($products);
        } catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    public function getAllRatedProductsByCustomer($customer_id,$status){
        try{
            // Lấy danh sách các sản phẩm từ bảng Order Detail có status là "rating" của khách hàng
            $orderDetails = OrderDetail::join('orders', 'order_detail.order_id', '=', 'orders.id')
                                       ->where('orders.customer_id', $customer_id)
                                       ->where('orders.status', $status)
                                       ->select('order_detail.*', 'orders.id as order_id','orders.created_time as created_time')
                                       ->get();
    
            // Tạo một mảng trống để lưu thông tin của từng sản phẩm
            $products = [];
    
            // Duyệt qua từng chi tiết đơn hàng để lấy thông tin sản phẩm
            foreach ($orderDetails as $orderDetail) {
                // Lấy thông tin sản phẩm từ orderDetail
                $product = Product::with('review', 'image', 'categorie')
                                  ->find($orderDetail->product_id);
    
                // Nếu sản phẩm không tồn tại, tiếp tục vòng lặp
                if (!$product) {
                    continue;
                }
    
                // Tính điểm đánh giá trung bình và số lượng sản phẩm đã bán cho từng sản phẩm
                $totalRate = 0;
                $reviewCount = $product->review->count();
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }
                $product->average_rate = ($reviewCount > 0) ? ($totalRate / $reviewCount) : 0;
    
                // Lấy số lượng sản phẩm từ orderDetail
                $product->quantity = $orderDetail->quantity;
                $product->price = $orderDetail->price;

                $product->created_time = $orderDetail->created_time;
    
                // Lưu thông tin order_id vào sản phẩm
                $product->order_id = $orderDetail->order_id;
    
                // Lưu thông tin sản phẩm vào mảng products
                $products[] = $product;
            }
    
            return response()->json($products);
        } catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    
    public function getAllRatedProductsByStatus($customer_id){
        try{
            $orderDetails = OrderDetail::join('orders', 'order_detail.order_id', '=', 'orders.id')
    ->where('orders.customer_id', $customer_id)
    ->where(function ($query) {
        $query->where('orders.status', 'pending')
            ->orWhere('orders.status', 'initialization');
    })
    ->orderBy('orders.id', 'desc')
    ->select('order_detail.*', 'orders.id as order_id', 'orders.status as status', 'orders.address as address')
    ->get();
    
            // Tạo một mảng trống để lưu thông tin của từng sản phẩm
            $products = [];
    
            // Duyệt qua từng chi tiết đơn hàng để lấy thông tin sản phẩm
            foreach ($orderDetails as $orderDetail) {
                // Lấy thông tin sản phẩm từ orderDetail
                $product = Product::with('review', 'image', 'categorie')
                                  ->find($orderDetail->product_id);
    
                // Nếu sản phẩm không tồn tại, tiếp tục vòng lặp
                if (!$product) {
                    continue;
                }
    
                // Tính điểm đánh giá trung bình và số lượng sản phẩm đã bán cho từng sản phẩm
                $totalRate = 0;
                $reviewCount = $product->review->count();
                foreach ($product->review as $review) {
                    $totalRate += $review->rate;
                }
                $product->average_rate = ($reviewCount > 0) ? ($totalRate / $reviewCount) : 0;
    
                // Lấy số lượng sản phẩm từ orderDetail
                $product->quantity = $orderDetail->quantity;
    
                // Lưu thông tin order_id vào sản phẩm
                $product->order_id = $orderDetail->order_id;
                $product->status = $orderDetail->status;
                $product->price = $orderDetail->price;
                $product->address = $orderDetail->address;
    
                // Lưu thông tin sản phẩm vào mảng products
                $products[] = $product;
            }
    
            return response()->json($products);
        } catch (\Exception $e){
            return response()->json(['message' => 'Product not found'], 404);
        }
    }
    
    
    
}
