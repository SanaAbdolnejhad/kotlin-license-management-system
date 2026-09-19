package com.company.licenseserver.domain.model


import java.time.LocalDate



class License(

    val id: Int,

    val key: String,

    val customer: Customer,

    val product: Product,

    private val maxDevices: Int,

    val expireDate: LocalDate,

    initialStatus: LicenseStatus = LicenseStatus.ACTIVE

) {



    private var status =
        initialStatus





    fun getMaxDevices(): Int {

        return maxDevices

    }






    fun isExpired(): Boolean {


        return LocalDate.now()
            .isAfter(expireDate)


    }







    fun block(){


        status =
            LicenseStatus.BLOCKED


    }







    fun unblock(){


        status =
            LicenseStatus.ACTIVE


    }







    fun getStatus(): LicenseStatus {


        return status


    }



}