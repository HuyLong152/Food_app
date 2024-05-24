<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class CategoryTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $category = [];
        for ($i = 0; $i < 10; $i++) {
            $category[] = ["name" => "category $i","image_url"=> "category$i.png"];
        }
        foreach ($category as $key => $value) {
            DB::table("category")->insert($value);
        }
    }
}
