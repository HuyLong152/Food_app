<?php

namespace App\Http\Controllers;

use App\Models\Product;
use Illuminate\Http\Request;
use App\Models\Category;

class CategoryController extends Controller
{
    //
   
    public function index($id = null){
        try{
            if ($id !== null) {
                // Lấy ra duy nhất 1 category dựa vào id
                $category = Category::find($id);
                return response()->json(['category' => $category], 200);
            } else {
                // Lấy tất cả các category
                $categories = Category::all();
                return response()->json(['categories' => $categories], 200);
            }
        }catch(\Throwable $e){
            return response()->json(['message' => 'Failed to get Category: '.$e->getMessage()], 500);;
        }
    }

    public function searchCategory($textSearch){
        try {
            // Tìm kiếm các category có tên hoặc ID trùng với $textSearch
            $categories = Category::where('name', 'like', '%' . $textSearch . '%')
                                  ->orWhere('id', $textSearch)
                                  ->get();
            
            return response()->json(['categories' => $categories], 200);
        } catch(\Throwable $e) {
            return response()->json(['message' => 'Failed to get Category: ' . $e->getMessage()], 500);
        }
    }

    // public function index(Int $start){
    //     try{
    //             // Kiểm tra số lượng phần tử
    //             $totalCount = Category::count();
    //             if ($totalCount > 0) {
    //                 // Lấy 10 category bắt đầu từ chỉ số 0
    //                 $categories = Category::skip($start)->take(min(10, $totalCount))->get();
    //                 return response()->json(['categories' => $categories], 200);
    //             } else {
    //                 return response()->json(['categories' => []], 200);
    //             }
    //     }catch(\Throwable $e){
    //         return response()->json(['message' => 'Failed to get Category: '.$e->getMessage()], 500);;
    //     }
    // }
    public function create(Request $request){
        try{
            $category = new Category();
            $category->name = $request->name;
            if($request->hasFile("image")){
                $imageName = $request->file("image")->getClientOriginalName();
                $image_path = $request->file("image")->storeAs("public/categoryImage", $imageName);
                $category->image_url = $imageName;
            }
            $category->save();
            return response()->json(['message' => 'Category added successfully'], 200);
        }catch(\Throwable $e){
            return response()->json(['message' => 'Failed to add Category: '.$e->getMessage()], 500);;
        }
    }
    public function update($id,Request $request){
        try{
            $category = Category::find($id);
            if(!$category){
                return response()->json(['message'=> 'Category not found'],404);
            }
            $category->name = $request->name;
            if($request->hasFile("image")){
                $imageName = $request->file("image")->getClientOriginalName();
                $image_path = $request->file("image")->storeAs("public/categoryImage", $imageName);
                $category->image_url = $imageName;
            }
            $category->save();
            return response()->json(['message' => 'Category added successfully'], 200);
        }catch(\Throwable $e){
            return response()->json(['message' => 'Failed to add Category: '.$e->getMessage()], 500);;
        }
    }
    public function destroy($id){
        try{
            $category = Category::find($id);
            if(!$category){
                return response()->json(['message'=> 'Category not found'],404);
            }
            $category->delete();
            return response()->json(['message'=> 'Category delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete category'.$e->getMessage()],500);
        }
    }
    public function searchByProductId($product_Id){ // lấy category dựa vào id product
        try{
            if(!Product::find($product_Id)){
                return response()->json(['message'=> 'Id product not found'],404);
            }
            $category = Category::whereHas('product', function ($query) use ($product_Id) {
                $query->where('product.id', $product_Id);
            })->get();
        
            return response()->json($category,200);
        }catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to find category'.$e->getMessage()],500);
        }
    }
}
