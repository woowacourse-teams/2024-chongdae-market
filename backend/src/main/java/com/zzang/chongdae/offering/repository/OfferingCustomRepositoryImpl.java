package com.zzang.chongdae.offering.repository;

import static com.zzang.chongdae.offering.repository.entity.QOfferingEntity.offeringEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OfferingCustomRepositoryImpl implements OfferingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OfferingEntity> findRecentOfferings(Long lastId, String keyword, Pageable pageable) {
        return queryFactory.selectFrom(offeringEntity)
                .where(offeringEntity.id.lt(lastId), likeKeyword(keyword))
                .orderBy(offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OfferingEntity> findImminentOfferingsWithTitleKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable) {
        return findImminentOfferings(lastMeetingDate, lastId, pageable, likeTitle(keyword));
    }

    @Override
    public List<OfferingEntity> findImminentOfferingsWithMeetingAddressKeyword(
            LocalDateTime lastMeetingDate, Long lastId, String keyword, Pageable pageable) {
        return findImminentOfferings(lastMeetingDate, lastId, pageable, likeMeetingAddress(keyword));
    }

    private List<OfferingEntity> findImminentOfferings(
            LocalDateTime lastMeetingDate, Long lastId, Pageable pageable, BooleanExpression keywordCondition) {
        return queryFactory.selectFrom(offeringEntity)
                .where(
                        keywordCondition,
                        offeringEntity.offeringStatus.eq(OfferingStatus.IMMINENT),
                        offeringEntity.meetingDate.gt(lastMeetingDate)
                                .or(offeringEntity.meetingDate.eq(lastMeetingDate).and(offeringEntity.id.lt(lastId))))
                .orderBy(offeringEntity.meetingDate.asc(), offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression likeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.title.like(keyword + '%')
                .or(offeringEntity.meetingAddress.like(keyword + '%'));
    }

    private BooleanExpression likeTitle(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.title.like(keyword + '%');
    }

    private BooleanExpression likeMeetingAddress(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.meetingAddress.like(keyword + '%');
    }
}
