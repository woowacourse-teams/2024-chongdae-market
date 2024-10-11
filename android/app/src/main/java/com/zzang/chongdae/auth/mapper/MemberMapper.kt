package com.zzang.chongdae.auth.mapper

import com.zzang.chongdae.auth.dto.response.MemberResponse
import com.zzang.chongdae.auth.model.Member

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
