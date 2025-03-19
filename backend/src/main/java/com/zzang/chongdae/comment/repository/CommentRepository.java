package com.zzang.chongdae.comment.repository;

import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>, CustomizedCommentRepository {

    Optional<CommentEntity> findTopByOfferingIdOrderByCreatedAtDesc(Long offeringId);
}
