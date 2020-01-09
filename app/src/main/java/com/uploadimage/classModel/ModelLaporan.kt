package com.uploadimage.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelLaporan (

    var id_laporan : String,
    var id_user : String,
    var oleh : String,
    var date : String,
    var p_name : String,
    var merk : String,
    var harga : String,
    var quantity: String,
    var total : String,
    var image_lap : String

):

    Parcelable {
        constructor(): this("","","","","","","","","","")

    }
