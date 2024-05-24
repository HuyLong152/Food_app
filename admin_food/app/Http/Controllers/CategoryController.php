<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CategoryController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        //
        return view("category.index");
    }


    public function create()
    {
        //
        return view("category.create");
    }
    
    public function edit($id)
{
    // Lấy dữ liệu từ cơ sở dữ liệu hoặc bất kỳ xử lý nào khác

    // Truyền dữ liệu sang view
    return view("category.edit")->with('id', $id);
}

}
