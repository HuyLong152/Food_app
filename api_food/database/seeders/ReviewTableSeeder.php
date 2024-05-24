<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class ReviewTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $reviews = [];
        for ($i = 1; $i < 10; $i++) {
            $reviews[] = ["customer_id" => random_int(1,10), "product_id" => random_int(7,18),"rate" => random_int(1,5),"content" => "good"];
        }
        
        foreach ($reviews as $key => $value) {
            DB::table("review")->insert($value);
        }
    }
}
