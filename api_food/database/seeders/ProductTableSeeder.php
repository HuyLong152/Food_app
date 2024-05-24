<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use App\Models\Product;

class ProductTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $products = [
            [
                'name' => 'Product 1',
                'description' => 'Description of Product 1',
                'ingredient' => 'Ingredient of Product 1',
                'calo' => 100,
                'quantity' => 10,
                'price' => 10.99,
            ],
            [
                'name' => 'Product 2',
                'description' => 'Description of Product 2',
                'ingredient' => 'Ingredient of Product 2',
                'calo' => 120,
                'quantity' => 12,
                'price' => 15.99,
            ],
            [
                'name' => 'Product 3',
                'description' => 'Description of Product 3',
                'ingredient' => 'Ingredient of Product 3',
                'calo' => 150,
                'quantity' => 15,
                'price' => 19.99,
            ],
            [
                'name' => 'Product 4',
                'description' => 'Description of Product 4',
                'ingredient' => 'Ingredient of Product 4',
                'calo' => 130,
                'quantity' => 13,
                'price' => 12.99,
            ],
            [
                'name' => 'Product 5',
                'description' => 'Description of Product 5',
                'ingredient' => 'Ingredient of Product 5',
                'calo' => 110,
                'quantity' => 11,
                'price' => 29.99,
            ],
        ];
        
        foreach ($products as $productData) {
            Product::create($productData);
        }
    }
}
