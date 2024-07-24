package com.zzang.chongdae.comment.repository;

import com.zzang.chongdae.comment.domain.CommentWithRole;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
            SELECT new com.zzang.chongdae.comment.domain.CommentWithRole(
                c, om.role
            )
            FROM CommentEntity as c JOIN OfferingMemberEntity as om
                ON c.offering = om.offering AND c.member = om.member
            WHERE om.offering = :offering
        """)
    List<CommentWithRole> findAllWithRoleByOffering(OfferingEntity offering);

    Optional<CommentEntity> findTopByOffering(OfferingEntity offering);
}
