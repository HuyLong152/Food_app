<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;


class CartTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $image = [];
        for ($i = 1; $i < 6; $i++) {
            for ($j = $i ; $j < 5; $j++) {
                $image[] = ["customer_id" => $i, "product_id" => $j,"quantity"=>$j];
            }
        }
        
        foreach ($image as $key => $value) {
            DB::table("cart")->insert($value);
        }
    }
}
