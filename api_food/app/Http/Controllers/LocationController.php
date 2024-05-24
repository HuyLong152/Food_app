<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Location;

class LocationController extends Controller
{
    //
    public function getDefaultLocation($customer_id){ // lấy location có is_default = 1
        $location = Location::where("customer_id",$customer_id)
                    ->where("is_default",1)
                    ->first();
        if ($location) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($location, 200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Location does not exist'], 404);
        }
    }
    public function getLocationById($id){ // lấy location có is_default = 1
        $location = Location::where("id",$id)
                    ->first();
        if ($location) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($location, 200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Location does not exist'], 404);
        }
    }
    public function getAllLocation($customer_id){ // lấy location có is_default = 1
        $location = Location::where("customer_id",$customer_id)
                    ->get();
        if ($location) {
            // Nếu tài khoản tồn tại, trả về thông báo rằng tài khoản đã tồn tại
            return response()->json($location, 200);
        } else {
            // Nếu tài khoản không tồn tại, trả về thông báo rằng tài khoản không tồn tại
            return response()->json(['message' => 'Location does not exist'], 404);
        }
    }

    public function updateLocation(Request $request){
        $location = Location::find($request->id);
        if ($location) {
            $location->name = $request->name;
            $location->address = $request->address;
            $location->phone_number = $request->phone_number;
            $location->save();
            return response()->json(['message' => "Update location successful"], 200);
        } else {
            return response()->json(['message'=> 'Location not found'],404);
        }
    }

    public function changeDefaultLocation(Request $request){
        $location = Location::where("customer_id",$request -> customer_id)
                    ->where("is_default",1)
                    ->first();
        $location->is_default = 0;
        $location->save();

        $location = Location::find($request->id);
        if ($location) {
            $location->is_default = 1;
            $location->save();
            return response()->json(['message' => "Update location successful"], 200);
        } else {
            return response()->json(['message'=> 'Location not found'],404);
        }
    }
    public function addLocation(Request $request){
        try{
            $location = new Location();
            $location->customer_id = $request->customer_id;
            $location->name = $request->name;
            $location->address = $request->address;
            $location->phone_number = $request->phone_number;
            $location->save();
            return response()->json(['message' => "Add location successful"], 200);
        } catch(\Exception $e){
            return response()->json(['message'=> 'Add location faild'.$e->getMessage()],404);
        }
    }
    public function deleteLocation($id){
        try{
            $location = Location::find($id);
            if(!$location){
                return response()->json(['message'=> 'Location not found'],404);
            }
            $location->delete();
            return response()->json(['message'=> 'Location delete successful'],200);
        }
        catch(\Throwable $e){
            return response()->json(['message'=> 'Failed to delete location'.$e->getMessage()],500);
        }
    }
}
