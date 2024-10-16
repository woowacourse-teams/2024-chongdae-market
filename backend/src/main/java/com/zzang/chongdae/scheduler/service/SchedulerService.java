package com.zzang.chongdae.scheduler.service;

import com.zzang.chongdae.global.config.WriterDatabase;
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

    @WriterDatabase
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime yesterday = today.minusDays(1);

        updateStatusConfirmedDaily(yesterday);
        updateStatusImminentDaily(tomorrow);
    }

    private void updateStatusConfirmedDaily(LocalDateTime meetingDate) {
        List<OfferingEntity> offerings = offeringRepository.findByMeetingDateAndOfferingStatusNotConfirmed(meetingDate);
        offerings.forEach(offering -> offering.updateOfferingStatus(OfferingStatus.CONFIRMED));
        offerings.forEach(offering -> offering.updateRoomStatus(CommentRoomStatus.BUYING));
    }

    private void updateStatusImminentDaily(LocalDateTime meetingDate) {
        List<OfferingEntity> offerings = offeringRepository.findByMeetingDateAndOfferingStatusNotConfirmed(meetingDate);
        offerings.forEach(offering -> offering.updateOfferingStatus(OfferingStatus.IMMINENT));
    }
}
