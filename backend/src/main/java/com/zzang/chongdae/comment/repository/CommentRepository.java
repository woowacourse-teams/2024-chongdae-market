package com.zzang.chongdae.comment.repository;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByOfferingOrderByCreatedAt(OfferingEntity offering);

    Optional<CommentEntity> findTopByOfferingIdOrderByCreatedAtDesc(Long offeringId);
}
