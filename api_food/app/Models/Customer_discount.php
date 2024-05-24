<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Customer_discount extends Model
{
    use HasFactory;
    protected $table = "customer_discount";
    public $timestamps = false;

     public function customer()
    {
        return $this->hasOne(Customer::class);
    }
    public function discount()
    {
        return $this->hasOne(Discount::class);
    }
}
