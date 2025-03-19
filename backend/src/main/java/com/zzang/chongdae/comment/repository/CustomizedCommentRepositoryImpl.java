package com.zzang.chongdae.comment.repository;

import static com.zzang.chongdae.comment.repository.entity.QCommentEntity.commentEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzang.chongdae.comment.domain.SearchDirection;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CustomizedCommentRepositoryImpl implements CustomizedCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentEntity> findCommentWithMemberByOfferingOrderByCreatedDesc(OfferingEntity offering, Long lastId,
                                                                                 Pageable pageable,
                                                                                 SearchDirection direction) {

        BooleanExpression directionExpression = getDirectionExpression(lastId, direction);
        return jpaQueryFactory.selectFrom(commentEntity)
                .join(commentEntity.member).fetchJoin()
                .where(commentEntity.offering.eq(offering).and(directionExpression))
                .orderBy(commentEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression getDirectionExpression(Long lastId, SearchDirection direction) {
        if (direction == SearchDirection.PREVIOUS) {
            return commentEntity.id.lt(lastId);
        }
        return commentEntity.id.gt(lastId);
    }
}
