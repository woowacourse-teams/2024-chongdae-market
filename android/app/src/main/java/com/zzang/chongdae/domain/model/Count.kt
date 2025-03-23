package com.zzang.chongdae.domain.model

@JvmInline
value class Count private constructor(val number: Int) {
    fun increase(): Count {
        return Count(number + 1)
    }

    fun decrease(minimum: Int): Count {
        if (number == minimum) return Count(number)
        return Count(number - 1)
    }

    companion object {
        private const val ERROR_INTEGER_FORMAT = -1

        fun fromString(value: String?, minimum: Int): Count {
            val intValue = value?.toIntOrNull() ?: ERROR_INTEGER_FORMAT
            if (intValue < minimum) {
                return Count(ERROR_INTEGER_FORMAT)
            }
            return Count(intValue)
        }
    }
}
