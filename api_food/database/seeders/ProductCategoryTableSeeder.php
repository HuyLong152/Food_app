<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\Product;
use Illuminate\Support\Facades\DB;
use App\Models\Category;

class ProductCategoryTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        // Lấy ID của sản phẩm và danh mục từ cơ sở dữ liệu hoặc tạo mới nếu cần
        $productId1 = Product::where('name', 'Product 1')->first()->id;
        $productId2 = Product::where('name', 'Product 2')->first()->id;

        $categoryId1 = Category::where('name', 'category 1')->first()->id;
        $categoryId2 = Category::where('name', 'category 2')->first()->id;

        // Thêm dữ liệu vào bảng product_category
        DB::table('product_category')->insert([
            ['product_id' => $productId1, 'category_id' => $categoryId1],
            ['product_id' => $productId1, 'category_id' => $categoryId2],
            ['product_id' => $productId2, 'category_id' => $categoryId2],
        ]);
    }
}
