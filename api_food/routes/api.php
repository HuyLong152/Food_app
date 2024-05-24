<?php

use App\Http\Controllers\ProductController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CategoryController;
use App\Http\Controllers\AccountController;
use App\Http\Controllers\CustomerController;
use App\Http\Controllers\CartController;
use App\Http\Controllers\LocationController;
use App\Http\Controllers\FavoriteController;
use App\Http\Controllers\OrdersController;
use App\Http\Controllers\ReviewController;
use App\Http\Controllers\DiscountController;
use App\Http\Controllers\HotSearchController;
use App\Http\Controllers\MessageController;


//message
Route::post("message",[MessageController::class, "storeMessage"]);
Route::get("message/getAll",[MessageController::class, "getAllMessage"]);
Route::get("message/{customerId}",[MessageController::class, "getCustomerMessages"]);

//hot search
Route::post("hotsearch",[HotSearchController::class, "create"]);
Route::get("hotsearch/search/{text_search}",[HotSearchController::class, "getHotSearch"]);
Route::get("hotsearch",[HotSearchController::class, "getAllSearch"]);
Route::delete("hotsearch/{id}",[HotSearchController::class, "deleteHotSearch"]);
// Route::get("hotsearch",[HotSearchController::class, "getTop5HotSearches"]);


//discount
Route::get("discount/aDiscount/{id}",[DiscountController::class, "getADiscount"]);
Route::get("discount/search/{textSearch}",[DiscountController::class, "getDiscountBySearch"]);
Route::get("discount/new",[DiscountController::class, "getLatestDiscountCode"]);
Route::get("discount/{discountCode}",[DiscountController::class, "checkDiscountCodeExists"]);
Route::get("discount",[DiscountController::class, "getAllDiscount"]);
Route::post("discount/update",[DiscountController::class, "updateDiscount"]);
Route::post("discount/create",[DiscountController::class, "createDiscount"]);
Route::post("discount",[DiscountController::class, "isUsedDiscountCode"]);
Route::delete("discount/{id}",[DiscountController::class, "deleteDiscount"]);


//category
Route::get("category/{id?}",[CategoryController::class, "index"]); 
Route::get("category/search/{textSearch}",[CategoryController::class, "searchCategory"]); 
Route::get("category/product/{id}",[CategoryController::class, "searchByProductId"]);
Route::post("category",[CategoryController::class, "create"]);
Route::put("category/{id}",[CategoryController::class, "update"]);
Route::delete("category/{id}",[CategoryController::class, "destroy"]);

//product
Route::get("product/hotsearch",[ProductController::class, "getProductsByHotSearches"]);
Route::get("product/recommend",[ProductController::class, "getTop10ProductJustSold"]);
Route::get("product/time/{time}",[ProductController::class,"getProductByTime"]);
Route::get("product/priceSort",[ProductController::class,"getProductsByPriceAndSort"]);
Route::get("product/rateSort",[ProductController::class,"getProductsByAverageRateAndSort"]);
Route::get("product/topSaleSort",[ProductController::class,"getProductsByTopSaleAndSort"]);
Route::get("product/name/{name}",[ProductController::class,"getProductByName"]);
Route::get("product/nameId/{name}",[ProductController::class,"getProductByNameId"]);
Route::get("product/popular",[ProductController::class,"getProductsByHighestQuantity"]);
Route::get("product/category/{id}",[ProductController::class,"getProductByCategory"]);
Route::get("product/price/{price}",[ProductController::class,"getProductByPrice"]);
Route::get("product/{id?}",[ProductController::class,"index"]);
Route::post("product/{id}",[ProductController::class,"updateProduct"]);
Route::post("product",[ProductController::class,"createProduct"]);
Route::delete("product/{id}",[ProductController::class,"destroy"]);

//account
Route::post("account/changePass",[AccountController::class,"changePassword"]); // đổi mk
Route::get("account/getInfor/{customer_id}",[AccountController::class,"getInforAccountByCusID"]); // đổi mk
Route::get("account/search/{textSearch}",[AccountController::class,"getAccountInfo"]); // đổi mk
Route::post("account/getByName",[AccountController::class,"getAccountByName"]); // mở/khóa tài khoản
Route::get("account/{username}",[AccountController::class,"isExistAccount"]); // kiểm tra tài khoản tồn tại
Route::get("account/getAccountByCusId/{customer_id}",[AccountController::class,"getAccountByCustomerId"]); // kiểm tra tài khoản tồn tại
// Route::get("account/getIdAccount/{customer_id}",[AccountController::class,"getIdAccount"]); 
Route::get("account",[AccountController::class,"getAllAccount"]); 
Route::post("login/admin",[AccountController::class,"LoginAdmin"]); //đăng nhập
Route::post("login/",[AccountController::class,"Login"]); //đăng nhập
Route::post("verify",[AccountController::class,"checkVerifyCode"]); //check otp
Route::delete("account/{id}",[AccountController::class,"destroy"]); // xóa tài khoản
Route::post("account/changeRole/{id}",[AccountController::class,"managerAccountRole"]); // mở/khóa tài khoản
Route::post("account/{id}",[AccountController::class,"managerAccount"]); // mở/khóa tài khoản
Route::post("resend",[AccountController::class,"reCodeOtp"]); // mở/khóa tài khoản
Route::post("recoveryPass",[AccountController::class,"recoveryPassword"]); // quên mật khẩu
Route::post("recoveryPassAdmin",[AccountController::class,"recoveryPasswordAdmin"]); // quên mật khẩu


//customer
Route::get("customer/{id}",[CustomerController::class,"index"]);// lấy thông tin khách hàng
Route::get("customer",[CustomerController::class,"getAllUser"]);// lấy thông tin khách hàng
Route::post("customer/updateAdmin",[CustomerController::class,"updateAdmin"]);// 
Route::post("customer/avatar",[CustomerController::class,"saveImageFromUri"]);// lưu ảnh
Route::post("customer/google/",[CustomerController::class,"createGoogleCustomer"]);// tạo tài khoản
Route::post("customer/createAdmin",[CustomerController::class,"createAdmin"]);// tạo tài khoản
Route::post("customer/",[CustomerController::class,"create"]);// tạo tài khoản
Route::put("customer",[CustomerController::class,"update"]);

//cart
Route::get("cart/product/{customer_id}/{product_id}",[CartController::class,"getAProductByCustomer"]);
Route::get("cart/payment",[CartController::class,"getProductsInfoByCustomer"]);
Route::get("cart/{customer_id}/{product_id}",[CartController::class,"index"]);
Route::get("cart/search",[CartController::class,"getProductInCart"]);
Route::get("cart/{customer_id}",[CartController::class,"getByCustomer"]);
Route::post("cart/add",[CartController::class,"addNewProduct"]);
Route::post("cart",[CartController::class,"updateAProduct"]);
Route::delete("cart",[CartController::class,"destroy"]);

//location
Route::get("location/all/{customer_id}",[LocationController::class,"getAllLocation"]); // lấy full location
Route::get("location/id/{id}",[LocationController::class,"getLocationById"]); // lấy full location
Route::get("location/{customer_id}",[LocationController::class,"getDefaultLocation"]); // lấy location có default = 1
Route::post("location/updateDefault",[LocationController::class,"changeDefaultLocation"]); // 
Route::post("location/add",[LocationController::class,"addLocation"]); // thêm một location
Route::post("location",[LocationController::class,"updateLocation"]); // cập nhật location
Route::delete("location/{id}",[LocationController::class,"deleteLocation"]); // xóa location

//favorite
Route::get("favorite/{customer_id}/{product_id}",[FavoriteController::class,"index"]); // kiểm tra sản phẩm tồn tại
Route::get("favorite/{customer_id}",[FavoriteController::class,"getProductInFavorite"]); // lấy ds yêu thích
Route::post("favorite/add",[FavoriteController::class,"addNewFavorite"]);
Route::delete("favorite",[FavoriteController::class,"destroy"]);

//order
Route::get("orders/getall",[OrdersController::class,"getByCustomerId"]);
Route::get("orders/search/{textSearch}",[OrdersController::class,"getOrderInfo"]);
Route::get("orders/getAllOrders",[OrdersController::class,"getAllOrders"]);
Route::get("orders/time/{customer_id}",[OrdersController::class,"getRecentOrdersByCustomerId"]);
Route::get("orders/getrating/{customer_id}",[OrdersController::class,"getRatedProductsByCustomer"]);
Route::get("orders/getAllRating/{customer_id}/{status}",[OrdersController::class,"getAllRatedProductsByCustomer"]);
Route::get("orders/status/{customer_id}",[OrdersController::class,"getAllRatedProductsByStatus"]);
Route::get("orders",[OrdersController::class,"index"]);
Route::post("orders/single",[OrdersController::class,"createSingleProductOrder"]);
Route::post("orders/updatePaymentStatus",[OrdersController::class,"markOrdersAsCompleted"]);
Route::post("orders/updateStatus",[OrdersController::class,"updateOrderStatus"]);
Route::post("orders",[OrdersController::class,"create"]);
Route::delete("orders/{order_id}",[OrdersController::class,"delete"]);

//review
Route::get("review/{customer_id}",[ReviewController::class,"index"]); // lấy theo customer_id
Route::get("review/all/{product_id}",[ReviewController::class,"getAllbById"]); // lấy theo product
Route::post("review/add",[ReviewController::class,"addNewReview"]); // thêm review
Route::post("review",[ReviewController::class,"updateNewReview"]); // sửa review
Route::delete("review",[ReviewController::class,"destroy"]); // xóa review








