<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class DiscountController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        return view('discount.index');
    }
    public function create()
    {
        return view('discount.create');
    }
    public function edit($id)
    {
        // Lấy dữ liệu từ cơ sở dữ liệu hoặc bất kỳ xử lý nào khác
    
        // Truyền dữ liệu sang view
        return view("discount.edit")->with('id', $id);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
