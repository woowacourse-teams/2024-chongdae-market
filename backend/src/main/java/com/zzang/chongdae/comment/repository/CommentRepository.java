package com.zzang.chongdae.comment.repository;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
            SELECT c
            FROM CommentEntity c
                JOIN FETCH c.member
            WHERE c.offering = :offering
            ORDER BY c.createdAt
            """)
    List<CommentEntity> findAllWithMemberByOfferingOrderByCreatedAt(OfferingEntity offering);

    Optional<CommentEntity> findTopByOfferingIdOrderByCreatedAtDesc(Long offeringId);
}
