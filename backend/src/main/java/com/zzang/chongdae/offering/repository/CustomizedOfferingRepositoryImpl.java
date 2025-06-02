package com.zzang.chongdae.offering.repository;

import static com.zzang.chongdae.offering.domain.OfferingStatus.AVAILABLE;
import static com.zzang.chongdae.offering.domain.OfferingStatus.FULL;
import static com.zzang.chongdae.offering.domain.OfferingStatus.IMMINENT;
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
public class CustomizedOfferingRepositoryImpl implements CustomizedOfferingRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OfferingEntity> findRecentOfferings(Long lastId, String keyword, Pageable pageable) {
        return queryFactory.selectFrom(offeringEntity)
                .where(offeringEntity.id.lt(lastId),
                        likeTitleOrMeetingAddress(keyword))
                .orderBy(offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OfferingEntity> findJoinableOfferings(Long lastId, String keyword, Pageable pageable) {
        return queryFactory.selectFrom(offeringEntity)
                .where(offeringEntity.id.lt(lastId),
                        inOfferingStatus(AVAILABLE, IMMINENT),
                        likeTitleOrMeetingAddress(keyword))
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
                .where(keywordCondition,
                        inOfferingStatus(IMMINENT),
                        offeringEntity.meetingDate.gt(lastMeetingDate)
                                .or(offeringEntity.meetingDate.eq(lastMeetingDate).and(offeringEntity.id.lt(lastId))))
                .orderBy(offeringEntity.meetingDate.asc(), offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OfferingEntity> findHighDiscountOfferingsWithTitleKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable) {
        return findHighDiscountOfferings(lastDiscountRate, lastId, pageable, likeTitle(keyword));
    }

    @Override
    public List<OfferingEntity> findHighDiscountOfferingsWithMeetingAddressKeyword(
            double lastDiscountRate, Long lastId, String keyword, Pageable pageable) {
        return findHighDiscountOfferings(lastDiscountRate, lastId, pageable, likeMeetingAddress(keyword));
    }

    private List<OfferingEntity> findHighDiscountOfferings(
            double lastDiscountRate, Long lastId, Pageable pageable, BooleanExpression keywordCondition) {
        return queryFactory.selectFrom(offeringEntity)
                .where(keywordCondition,
                        inOfferingStatus(AVAILABLE, FULL, IMMINENT),
                        offeringEntity.discountRate.lt(lastDiscountRate)
                                .or(offeringEntity.discountRate.eq(lastDiscountRate).and(offeringEntity.id.lt(lastId))))
                .orderBy(offeringEntity.discountRate.desc(), offeringEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression likeTitleOrMeetingAddress(String keyword) {
        if (keyword == null) {
            return null;
        }
        return likeTitle(keyword).or(likeMeetingAddress(keyword));
    }

    private BooleanExpression likeTitle(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.title.like('%' + keyword + '%');
    }

    private BooleanExpression likeMeetingAddress(String keyword) {
        if (keyword == null) {
            return null;
        }
        return offeringEntity.meetingAddress.like('%' + keyword + '%');
    }

    private BooleanExpression inOfferingStatus(OfferingStatus... offeringStatus) {
        return offeringEntity.offeringStatus.in(offeringStatus);
    }
}
