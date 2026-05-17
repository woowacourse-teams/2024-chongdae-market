package com.zzang.chongdae.domain.model.participant

enum class OfferingCondition {
    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE, ;

    companion object {
        fun OfferingCondition.isAvailable(): Boolean {
            return this == AVAILABLE || this == IMMINENT
        }
    }
}
