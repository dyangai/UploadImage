package com.uploadimage.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelProduk (
    val id_produk: String,
    val id_user: String,
    val nama_produk: String,
    val merk: String,
    val stok: String,
    val image: String,
    val harga: String,
    val totalstok_harga: String):
        Parcelable{
            constructor(): this("","","","","","","","")

        }
