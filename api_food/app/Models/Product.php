<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Product extends Model
{
    use HasFactory;
    protected $table = "product";
    public $timestamps = false;

    public function review(){
       return $this->hasMany(Review::class);
    }
    public function image(){
        return $this->hasMany(Image::class);
     }
     public function categorie()
    {
        return $this->belongsToMany(Category::class, 'product_category', 'product_id', 'category_id');
    }
    protected static function boot() // xóa các bảng liên quan đến product
    {
        parent::boot();

        static::deleting(function($product) {
            $product->review()->delete(); // Delete related reviews
            $product->categorie()->detach(); // Detach related categories
            $product->image()->delete(); // Delete related images
        });
    }
}
