package com.example.cat1.models

import java.security.CodeSource

class Ticket {
    var name:String
    var source: String
    var destination: String
    var ticketno: String
    var date:String
    var position: String
    constructor(name: String, source: String, destination: String, ticketno: String, date: String, position: String){
        this.name = name
        this.source = source
        this.destination  = destination
        this.ticketno = ticketno
       this.position = position
      //  this.recordid = recordid
        this.date = date
    }

}