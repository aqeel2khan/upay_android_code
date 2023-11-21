package com.upayment.upaymentsdk.track.charge

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class ExtraMerchantDatumBuilder {

     var amount: Float= 0.0f
     var knetCharge: String =""
    var knetChargeType: String =""
     var ccCharge: Float=0.0f
   var ccChargeType: String =""
   var ibanNumber: String =""

    fun build(): ExtraMerchantDatum = ExtraMerchantDatum(amount, knetCharge, knetChargeType, ccCharge,ccChargeType,ibanNumber)


//    fun build(): ExtraMerchantDatum {
//        return ExtraMerchantDatum(
//            amount = amount,
//            knetCharge = knetCharge,
//            knetChargeType = knetChargeType
//
//        )
//    }


}


@Parcelize
class ExtraMerchantListBuilder : Parcelable {
    var listExtraMerchantData: MutableList<ExtraMerchantDatum> = mutableListOf()


    fun extraMerchant(block: ExtraMerchantDatumBuilder.() -> Unit) {
        val extraMerchantDatum = ExtraMerchantDatumBuilder().apply(block).build()
        listExtraMerchantData.add(extraMerchantDatum)
    }

    fun build(): MutableList<ExtraMerchantDatum> = listExtraMerchantData
}