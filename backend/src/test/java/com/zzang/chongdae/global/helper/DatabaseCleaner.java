package com.zzang.chongdae.global.helper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    public void execute() { // TODO: 테이블명 빼와서 반복문으로 돌리기
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        clearMember();
        clearOfferingMember();
        clearOffering();
        // clearComment();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void clearMember() {
        entityManager.createNativeQuery("DELETE FROM member").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE member ALTER COLUMN id RESTART").executeUpdate();
    }

    private void clearOfferingMember() {
        entityManager.createNativeQuery("DELETE FROM offering_member").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE offering_member ALTER COLUMN id RESTART").executeUpdate();
    }

    private void clearOffering() {
        entityManager.createNativeQuery("DELETE FROM offering").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE offering ALTER COLUMN id RESTART").executeUpdate();
    }

//    private void clearComment() {
//        entityManager.createNativeQuery("DELETE FROM comment").executeUpdate();
//        entityManager.createNativeQuery("ALTER TABLE comment ALTER COLUMN id RESTART").executeUpdate();
//    }
}
