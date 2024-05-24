<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class ImageTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $image = [];
        for ($i = 1; $i < 6; $i++) {
            for ($j = 0; $j < 3; $j++) {
                $image[] = ["product_id" => $i, "imgurl" => "category{$i}{$j}.png"];
            }
        }
        
        foreach ($image as $key => $value) {
            DB::table("image")->insert($value);
        }
        
    }
}
