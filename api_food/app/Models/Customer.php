<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Customer extends Model
{
    use HasFactory;
    protected $table = "customer";
    public $timestamps = false;

    public function location(){
        return $this->hasMany(Location::class);
    }
     public function account()
    {
        return $this->hasOne(Account::class);
    }
}
