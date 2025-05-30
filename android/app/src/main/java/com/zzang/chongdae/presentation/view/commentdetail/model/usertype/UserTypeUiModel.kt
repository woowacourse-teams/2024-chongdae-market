package com.zzang.chongdae.presentation.view.commentdetail.model.usertype

import com.zzang.chongdae.domain.model.analytics.UserType

enum class UserTypeUiModel {
    A,
    B,
    ;

    companion object {
        fun from(type: UserType): UserTypeUiModel = runCatching { valueOf(type.typeName.uppercase()) }.getOrDefault(A)
    }
}
