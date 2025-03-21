package com.zzang.chongdae.comment.domain;

import com.zzang.chongdae.comment.repository.CommentRepository;
import com.zzang.chongdae.comment.repository.entity.CommentEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public enum SearchDirection {

    NEXT(CommentRepository::findNextCommentWithMemberByOfferingOrderByCreatedDesc),
    PREVIOUS(CommentRepository::findPreviousCommentWithMemberByOfferingOrderByCreatedDesc);

    private final RepositoryMethod<CommentRepository, OfferingEntity, Long, Pageable, List<CommentEntity>> method;

    public List<CommentEntity> search(CommentRepository repository, OfferingEntity offering, Long lastId,
                                      Pageable pageable) {
        return method.apply(repository, offering, lastId, pageable);
    }
}

@FunctionalInterface
interface RepositoryMethod<T, U, V, W, R> {
    R apply(T t, U u, V v, W w);
}
