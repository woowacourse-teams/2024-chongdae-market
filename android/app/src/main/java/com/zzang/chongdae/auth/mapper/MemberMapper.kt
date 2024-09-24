package com.zzang.chongdae.auth.mapper

import com.zzang.chongdae.auth.model.Member
import com.zzang.chongdae.auth.dto.response.MemberResponse

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
