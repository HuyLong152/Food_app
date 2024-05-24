<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class ProductController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        return view("product.index");
    }

    public function edit($id)
    {
        return view("product.edit")->with('id', $id);
    }

    public function create(){
        return view("product.create");
    }
}
