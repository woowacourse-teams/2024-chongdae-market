package com.zzang.chongdae.domain.model

@JvmInline
value class Count private constructor(val number: Int) {
    fun increase(): Count {
        return Count(number + 1)
    }

    fun decrease(): Count {
        if (number == MINIMUM_COUNT) return Count(number)
        return Count(number - 1)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val MINIMUM_COUNT = 2

        fun fromString(value: String?): Count {
            val intValue = value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (intValue < MINIMUM_COUNT) {
                return Count(ERROR_INTEGER_FORMAT)
            }
            return Count(intValue)
        }
    }
}
