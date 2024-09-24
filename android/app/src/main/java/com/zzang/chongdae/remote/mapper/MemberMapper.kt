package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.Member
import com.zzang.chongdae.remote.dto.response.auth.MemberResponse

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
