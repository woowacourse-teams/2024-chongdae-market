package com.zzang.chongdae.offering.repository;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.OfferingWithRole;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OfferingRepository
        extends JpaRepository<OfferingEntity, Long>, JpaSpecificationExecutor<OfferingEntity> {

    List<OfferingEntity> findByIdGreaterThan(Specification<OfferingEntity> spec, Long lastId, Pageable pageable);

    @Query("""
                SELECT new com.zzang.chongdae.offering.domain.OfferingWithRole(o, om.role)
                FROM OfferingEntity as o JOIN OfferingMemberEntity as om
                    ON o.id = om.offering.id
                WHERE om.member = :member
            """)
    List<OfferingWithRole> findAllWithRoleByMember(MemberEntity member);

    interface Specs {
        static Specification<OfferingEntity> hasSearchKeyword(String keyword) {
            return (root, query, builder) -> {
                if (keyword == null) {
                    System.out.println("this is null space");
                    return builder.conjunction();
                }
                String parsedKeyword = "%" + keyword + "%";

                Predicate titlePredicate = builder.like(root.get("title"), keyword);
                Predicate addressPredicate = builder.like(root.get("meetingAddress"), keyword);
                return builder.or(titlePredicate, addressPredicate);
//                System.out.println(keyword);
//                return builder.equal(root.get("title"), keyword);
            };
        }
    }
}
