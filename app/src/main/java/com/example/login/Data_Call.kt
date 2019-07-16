package com.example.login

class Data_call {
    var userID: String? = null
    var phonenumber: String? = null
    var time: String? = null
    var arrive: String? = null
    var arrive_Latitude: String? = null
    var arrive_Longitude: String? = null
    var start: String? = null
    var start_Latitude: String? = null
    var start_Longitude: String? = null
    var distance: String? = null
    var index: String? = null
    var person: Int? = null
    var pay: Int? = null
    var pay_complete : Int? = null
    var service_each : Int? = null

    var driver: String? = null
    var taxinumber: String? = null
    var complete_driver : Boolean? = null
    var complete_client : Boolean? = null

//    var taxi_phonenumber: String? = null

    internal constructor() {}

    internal constructor(userID: String, phonenumber: String, index: String
                         , start: String, start_Latitude: String, start_Longitude: String
                         , arrive: String, arrive_Latitude: String, arrive_Longitude: String
                         , time: String, distance: String, person: Int, pay: Int
                         , driver: String, taxinumber: String,complete_driver:Boolean,complete_client:Boolean,pay_complete:Int,service_each:Int) {
        this.userID = userID
        this.phonenumber = phonenumber
        this.index = index
        this.start = start
        this.start_Latitude = start_Latitude
        this.start_Longitude = start_Longitude
        this.arrive = arrive
        this.arrive_Latitude = arrive_Latitude
        this.arrive_Longitude = arrive_Longitude
        this.time = time
        this.distance = distance
        this.person = person
        this.pay = pay
        this.driver = driver
        this.taxinumber = taxinumber
        this.complete_driver = complete_driver
        this.complete_client = complete_client
        this.pay_complete = pay_complete
        this.service_each = service_each
    }

}