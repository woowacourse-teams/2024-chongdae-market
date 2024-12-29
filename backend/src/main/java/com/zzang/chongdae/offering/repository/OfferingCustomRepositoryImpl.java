package com.zzang.chongdae.offering.repository;

import static com.zzang.chongdae.offering.repository.entity.QOfferingEntity.offeringEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OfferingCustomRepositoryImpl implements OfferingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OfferingEntity> findRecentOfferings(Long lastId, String keyword, Pageable pageable) {
        return queryFactory.selectFrom(offeringEntity)
                .where(offeringEntity.id.lt(lastId), containsKeyword(keyword))
                .orderBy(offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.title.like(keyword + '%')
                .or(offeringEntity.meetingAddress.like(keyword + '%'));
    }
}
