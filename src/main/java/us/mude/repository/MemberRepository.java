package us.mude.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import us.mude.common.MemberType;
import us.mude.common.RatingType;
import us.mude.model.*;
import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Optional<Member> findById( Long memberID );

    Optional<Member> findByEmail(
            InternetAddress email
    );

    int countDistinctByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            MemberType m,
            List<String> f,
            List<String> l
    );

    List<Member> findDistinctByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            MemberType m,
            List<String> f,
            List<String> l,
            Pageable p
    );

    List<Member> findDistinctTop5ByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            MemberType m,
            List<String> f,
            List<String> l
    );

    List<Member> findDistinctByMemberTypeAndName_firstNameInIgnoreCaseAndName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            MemberType m,
            List<String> f,
            List<String> l
    );

    List<Member> findByMemberTypeOrderByName_firstNameAscName_lastNameAsc(
            MemberType m,
            Pageable page
    );

    int countByMemberTypeOrderByName_firstNameAscName_lastNameAsc(
            MemberType m
    );
    List<Member> findByMemberTypeAndName_lastNameStartsWithOrderByName_firstNameAscName_lastNameAsc(
            MemberType r,
            String a
    );

    @Transactional
    @Query(
            nativeQuery = true,
            value = "select distinct memberID from reviews where memberID in (select ID from members where memberType = 2 and isTopCritic = 1) order by (select count(*) from reviews where memberID in (select ID from members where memberType = 2 and isTopCritic = 1)) desc limit 5")
    List<Integer> findTop5Critics();

    @Modifying
    @Transactional
    @Query("UPDATE Member SET isPrivate = ?2 WHERE ID = ?1")
    void updatePrivacy(Long memberID, boolean privacy);
}
