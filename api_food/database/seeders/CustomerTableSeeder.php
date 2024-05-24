<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;


class CustomerTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $customer = [];
        for ($i = 0; $i < 10; $i++) {
            $customer[] = ["full_name" => "Nguyen Van $i","phone_number"=> "012345678$i","email"=>"c$i@gmail.com","image_url"=>"cusavt$i.png"];
        }
        foreach ($customer as $key => $value) {
            DB::table("customer")->insert($value);
        }
    }
}
