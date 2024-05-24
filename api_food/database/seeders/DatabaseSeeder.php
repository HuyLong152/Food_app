<?php

namespace Database\Seeders;

use App\Models\User;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $this -> call([
            // CategoryTableSeeder::class,
            // ProductTableSeeder::class,
            // ProductCategoryTableSeeder::class,
            // ImageTableSeeder::class,
            // CustomerTableSeeder::class,
            // AccountTableSeeder::class,
            // CartTableSeeder::class,
            // FavoriteTableSeeder::class,
            // OrderTableSeeder::class,
            // OrderDetailTableSeeder::class,
            ReviewTableSeeder::class,
        ]);
    }
}
