package com.zzang.chongdae.domain.model

@JvmInline
value class MyCount private constructor(val number: Int) {
    fun increase(): MyCount {
        return MyCount(number + 1)
    }

    fun decrease(): MyCount {
        if (number == MINIMUM_COUNT) return MyCount(number)
        return MyCount(number - 1)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1
        private const val MINIMUM_COUNT = 1

        fun fromString(value: String?): MyCount {
            val intValue = value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (intValue < MINIMUM_COUNT) {
                return MyCount(ERROR_INTEGER_FORMAT)
            }
            return MyCount(intValue)
        }
    }
}
