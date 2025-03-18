package com.zzang.chongdae.domain.model

@JvmInline
value class TotalCount private constructor(val number: Int) {
    fun increase(): TotalCount {
        return TotalCount(number + 1)
    }

    fun decrease(): TotalCount {
        if (number == MINIMUM_COUNT) return TotalCount(number)
        return TotalCount(number - 1)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val MINIMUM_COUNT = 2

        fun fromString(value: String?): TotalCount {
            val intValue = value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (intValue < MINIMUM_COUNT) {
                return TotalCount(ERROR_INTEGER_FORMAT)
            }
            return TotalCount(intValue)
        }
    }
}
