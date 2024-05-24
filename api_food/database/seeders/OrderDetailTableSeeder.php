<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;


class OrderDetailTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $orderDetail = [];
        for ($i = 1; $i < 10; $i++) {
            for( $j = 1; $j < 3; $j++) {
                $orderDetail[] = ["product_id" => random_int(1,20), "order_id" => $i,"quantity" => random_int(1,10),"price" => random_int(1000,200000)];
            }
        }
        
        foreach ($orderDetail as $key => $value) {
            DB::table("order_detail")->insert($value);
        }
    }
}
