<?php

use App\Http\Controllers\AdminController;
use App\Http\Controllers\DiscountController;
use App\Http\Controllers\ProductController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\HomeController;
use App\Http\Controllers\CategoryController;
use App\Http\Controllers\AccountController;
use App\Http\Controllers\OrdersController;
use App\Http\Controllers\LoginController;
use App\Http\Controllers\HotSearchController;
use App\Http\Controllers\ChatController;

Route::get("/login", [LoginController::class, "login"])->name('login');
Route::get("/forgotPass", [LoginController::class, "forgot"])->name('forgot');

Route::get("/home", [HomeController::class, "home"])->name('home');

//category
Route::get("category", [CategoryController::class, "index"])->name('category');
Route::get("category/create", [CategoryController::class, "create"])->name('category.create');
Route::get("category/edit/{id}", [CategoryController::class, "edit"])->name('category.edit');

//product
Route::get("product", [ProductController::class, "index"])->name('product');
Route::get("product/edit/{id}", [ProductController::class, "edit"])->name('product.edit');
Route::get("product/create", [ProductController::class, "create"])->name('product.create');

//account
Route::get("account", [AccountController::class, "index"])->name('account');

//orders
Route::get("orders", [OrdersController::class, "index"])->name('orders');
Route::get("orders/detail", [OrdersController::class, "detail"])->name('orders.detail');

//discount
Route::get("discount", [DiscountController::class, "index"])->name('discount');
Route::get("discount/edit/{id}", [DiscountController::class, "edit"])->name('discount.edit');
Route::get("discount/create", [DiscountController::class, "create"])->name('discount.create');

//admin
Route::get("admin", [AdminController::class, "index"])->name('admin');
Route::get("admin/password", [AdminController::class, "password"])->name('admin.password');
Route::get("admin/create", [AdminController::class, "create"])->name('admin.create');

//hot search
Route::get("hotsearch", [HotSearchController::class, "index"])->name('hotsearch');

//chat
Route::get("chat/detail/{id}", [ChatController::class, "index"])->name('index.detail');
Route::get("chat/view", [ChatController::class, "view"])->name('chat.view');




