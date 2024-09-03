package com.example.tire.data

import kotlinx.serialization.Serializable

@Serializable
data class TireList (
    var SN:                 String,
    var standardSize:       String,
    var division:           String,
    var model:              String,
    var brand:              String,
    var chassisNumber:      String,
    var lastEvent:          String,
    var event:              String,
    var reason:             String,
    var status:             String,
    var positionOnChassis:  String,
    var heightTread:        String,
    var mileage:            String,
    var events:             List<EventTire>?
)

@Serializable
data class EventTire(
    var id: Int,
    var name_TypeTireEvent: String,
    var dateEvent:String,
    var listPhoto:List<ImageList>?
)