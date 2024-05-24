<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Favotite extends Model
{
    use HasFactory;
    protected $table = "favorite";
    public $timestamps = false;
}
