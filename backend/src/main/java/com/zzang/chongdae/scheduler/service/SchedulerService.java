package com.zzang.chongdae.scheduler.service;

import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.OfferingRepository;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final OfferingRepository offeringRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        updateStatusConfirmedDaily(today);
        updateStatusImminentDaily(yesterday);
    }

    private void updateStatusConfirmedDaily(LocalDateTime meetingDate) {
        List<OfferingEntity> offerings
                = offeringRepository.findByMeetingDateAndOfferingStatusNot(meetingDate, OfferingStatus.CONFIRMED);
        offerings.forEach(offering -> offering.updateOfferingStatus(OfferingStatus.CONFIRMED));
        offerings.forEach(offering -> offering.updateRoomStatus(CommentRoomStatus.BUYING));
    }

    public void updateStatusImminentDaily(LocalDateTime meetingDate) {
        List<OfferingEntity> offerings
                = offeringRepository.findByMeetingDateAndOfferingStatusNot(meetingDate, OfferingStatus.CONFIRMED);
        offerings.forEach(offering -> offering.updateOfferingStatus(OfferingStatus.IMMINENT));
    }
}
