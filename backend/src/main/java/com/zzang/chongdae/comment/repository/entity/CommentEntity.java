package com.zzang.chongdae.comment.repository.entity;

import com.zzang.chongdae.global.repository.entity.BaseTimeEntity;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "comment")
@Entity
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private OfferingEntity offering;

    @NotNull
    @Column(length = 80)
    private String content;

    public CommentEntity(MemberEntity member, OfferingEntity offering, String content) {
        this(null, member, offering, content);
    }

    public boolean isOwnedBy(MemberEntity other) {
        return this.member.isSame(other);
    }
}
