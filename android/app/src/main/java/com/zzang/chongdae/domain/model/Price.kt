package com.zzang.chongdae.domain.model

data class Price (val amount: Int) {
    init {
        require(amount >= 0) { "[ERROR] 가격은 0 이상의 정수이어야 한다." }
    }

    companion object {
        const val ERROR_INTEGER_FORMAT = -1

        fun fromString(value: String?): Price {
            val intValue = value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (intValue < 0) {
                return Price(ERROR_INTEGER_FORMAT)
            }
            return Price(intValue)
        }

        fun fromInteger(value: Int?): Price {
            val intValue = value ?: ERROR_INTEGER_FORMAT
            if (intValue < 0) {
                return Price(ERROR_INTEGER_FORMAT)
            }
            return Price(intValue)
        }
    }
}