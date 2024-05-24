<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class FavoriteTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        //
        $favorite = [];
        for ($i = 1; $i < 6; $i++) {
            for ($j = $i ; $j < 5; $j++) {
                $favorite[] = ["customer_id" => $i, "product_id" => $j];
            }
        }
        
        foreach ($favorite as $key => $value) {
            DB::table("favorite")->insert($value);
        }
    }
}
