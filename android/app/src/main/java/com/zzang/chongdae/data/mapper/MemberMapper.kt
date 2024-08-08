package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.MemberResponse
import com.zzang.chongdae.domain.model.Member

fun MemberResponse.toDomain(): Member {
    return Member(
        memberId = this.memberId,
        nickName = this.nickname,
    )
}
