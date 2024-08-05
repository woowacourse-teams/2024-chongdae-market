package com.zzang.chongdae.domain.model

@JvmInline
value class DiscountPrice private constructor(val amount: Float) {
    init {
        require(amount >= 0) { "[ERROR] 할인율은 0 이상의 유리수이어야 한다." }
    }

    companion object {
        private const val ERROR_FLOAT_FORMAT = -1f

        fun fromFloat(value: Float?): DiscountPrice {
            val floatValue = value ?: ERROR_FLOAT_FORMAT
            if (floatValue < 0) {
                return DiscountPrice(0f)
            }
            return DiscountPrice(floatValue)
        }
    }
}
