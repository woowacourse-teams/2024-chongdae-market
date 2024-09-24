package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.auth.MemberResponse
import com.zzang.chongdae.domain.model.Member

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
