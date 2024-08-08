package com.zzang.chongdae.domain.model

import com.zzang.chongdae.domain.mo.Member

data class Signup(
    val member: Member,
    val token: Token,
)
