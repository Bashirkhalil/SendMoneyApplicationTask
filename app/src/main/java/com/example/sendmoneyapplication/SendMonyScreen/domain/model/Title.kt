package com.example.sendmoneyapplication.SendMonyScreen.domain.model

import androidx.compose.ui.text.input.KeyboardType
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sendmoneyapplication.utils.GenericTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language

@Serializable
data class Title (
    @SerialName("en") var en: String? = null,
    @SerialName("ar") var ar: String? = null
)

@Serializable
data class Label (
    @SerialName("en") var en: String? = null
)

@Serializable
data class Service (
    @SerialName("label") var label: Label? = Label(),
    @SerialName("name") var name: String? = null,
    @SerialName("providers") var providers: List<Provider> = arrayListOf()
)

@Serializable
data class Provider (
    @SerialName("name") var name: String? = null,
    @SerialName("id") var id: String? = null,
    @SerialName("required_fields") var requiredRequiredFields: List<RequiredField> = arrayListOf()
)

@Serializable
data class FieldLabel (
    @SerialName("en") var en: String? = null,
    @SerialName("ar") var ar: String? = null
)

@Serializable
data class Options (
    @SerialName("label") var labelName: String? = null,
    @SerialName("name") var nameValue: String? = null
)
enum class FieldType {
    text, number, msisdn, option
}

@Serializable
data class RequiredField (
    @SerialName("label") var label: FieldLabel? = FieldLabel(),
    @SerialName("name") var name: String? = null,
    @SerialName("type") var type: FieldType? = null,
    @SerialName("validation") var validation: String? = null,

    @SerialName("options")
    var options: List<Options>? = null,

    @SerialName("placeholder")
    @Serializable(with = GenericTypeSerializer::class)
    var placeholder: Any? = null,

    @SerialName("max_length")
    @Serializable(with = GenericTypeSerializer::class)
    var maxLength: Any? = null,

    @SerialName("validation_error_message")
    @Serializable(with = GenericTypeSerializer::class)
    var validationErrorMessage: Any? = null,

    )

@Serializable
data class SendMoneyData(
    val title: Title,
    val services: List<Service>
)

@Entity(tableName = "requestInformation")
@Serializable
data class DatabaseRowItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: String,
    val services: Service ,
    val provider: Provider
)




fun RequiredField.getValidationMessage(language: String): String {
    return when (this.validationErrorMessage) {
        is String -> this.validationErrorMessage as String
        is FieldLabel -> {
            if (language == "en") {
                (this.validationErrorMessage as FieldLabel).en ?: "Default error message in English"
            } else {
                (this.validationErrorMessage as FieldLabel).en ?: "Default error message in English"
            }
        }
        else -> "Unknown error message"
    }
}

fun RequiredField.getKeyBoardType() : KeyboardType {
    return when (this.type) {
         FieldType.text -> {
             KeyboardType.Text
         }
        FieldType.number -> {
             KeyboardType.Number
         }
        FieldType.msisdn -> {
             KeyboardType.Phone
         }
        else -> {
            KeyboardType.Text
        }
    }
}

fun RequiredField.getTextLength() : Int {
    var isValueIsString = false
    val  mTextLength = when (this.maxLength) {
        is String -> {
            isValueIsString = true
            this.maxLength as String
        }
        is Int  -> this.maxLength as Int
        else -> "Unknown error message"
    }
  //  return  mTextLength.toString().toInt()
    // all the datab type number related to amount the max length is zero in json data menus
    // ew cannot insert amount at all and this not correct if this the requirement
    // it should at least 2 or there available on json data for testing

    val mLength = if (isValueIsString){
        if (mTextLength.toString().length == 0 ) 10 else mTextLength.toString().toInt()
    }else {
        if (mTextLength.toString().toInt() == 0 ) 5 else mTextLength.toString().toInt()
    }
    return mLength
}


