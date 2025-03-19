package com.zzang.chongdae.comment.repository;

import com.zzang.chongdae.comment.domain.SearchDirection;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomizedCommentRepository {

    List<CommentEntity> findCommentWithMemberByOfferingOrderByCreatedDesc(OfferingEntity offering, Long lastId,
                                                                          Pageable pageable, SearchDirection direction);
}
