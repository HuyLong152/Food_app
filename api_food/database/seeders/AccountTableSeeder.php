<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;


class AccountTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $account = [];
        for ($i = 1; $i <= 10; $i++) {
            $account[] = ["customer_id"=>$i,"username" => "$i@gmail.com","password"=> "12345678"];
        }
        foreach ($account as $key => $value) {
            DB::table("account")->insert($value);
        }
    }
}
