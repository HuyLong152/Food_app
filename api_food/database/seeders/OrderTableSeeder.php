<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;


class OrderTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $order = [];
        for ($i = 1; $i < 10; $i++) {
                $order[] = ["customer_id" => $i, "name" => "user $i","phone_number" => "99999999$i","address" => "address $i","payment" => "successful","payment_status" => "pending","status" => "completed"];
        }
        
        foreach ($order as $key => $value) {
            DB::table("orders")->insert($value);
        }
    }
}
