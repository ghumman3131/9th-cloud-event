package com.example.user.myapplication.data_model;

/**
 * Created by charanghumman on 15/05/18.
 */

public class SingleServiceBooking {

    public String title , date , service , vendor_key ;

    public SingleServiceBooking()
    {

    }


    public SingleServiceBooking(String title , String date , String service , String vendor_key)
    {

        this.title = title ;
        this.date = date ;

        this.service = service ;

        this.vendor_key = vendor_key ;
    }
}
