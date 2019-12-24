package com.uploadimage.Model

class ModelUser {

    var id : String ? = null
    var email : String ? = null
    var name : String ? = null
    var image : String ? = null
    var outlet : String? = null

    constructor(){

    }

    constructor(id: String?,email: String?,name: String?, image: String?, outlet: String?) {
        this.id = id
        this.email = email
        this.name = name
        this.image = image
        this.outlet = outlet
    }
}
