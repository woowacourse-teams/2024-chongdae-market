package com.zzang.chongdae.auth.data.mapper

import com.zzang.chongdae.auth.data.dto.response.MemberResponse
import com.zzang.chongdae.auth.domain.model.Member

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
