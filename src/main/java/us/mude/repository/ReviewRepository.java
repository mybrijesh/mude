package us.mude.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import us.mude.common.MediaType;
import us.mude.common.MemberType;
import us.mude.common.RatingType;
import us.mude.model.Media;
import us.mude.model.Member;
import us.mude.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    Optional<Review> findByMedia_idAndMember_id(Long mediaID, Long memberID);

    void deleteByMedia_idAndMember_id(Long mediaID, Long memberID);

    List<Review> findTop6ByMedia_IdAndRatingTypeOrderByRegDateDesc(Long mediaID, RatingType r);

    void deleteByMemberAndMedia(Member m, Media media);

    int countByMember_id(Long memberID);

    List<Review> findByRatingTypeOrderByRegDateDesc(
            RatingType r,
            Pageable p
    );

    int countByRatingTypeOrderByRegDateDesc(
            RatingType r
    );

    List<Review> findByReported(
            boolean r
    );

    List<Review> findByRatingTypeAndMedia_idOrderByRegDateDesc(
            RatingType r,
            Long m,
            Pageable p
    );
    List<Review> findByRatingTypeAndMember_TopCriticAndMedia_idOrderByRegDateDesc(
            RatingType r,
            boolean t,
            Long m,
            Pageable p
    );

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "INSERT INTO reviews (memberID, mediaID, score, comment, ratingType) VALUES (?1, ?2, ?3, ?4, ?5)")
    void addReview(Long memberID, Long mediaID, float score, String comment, int m);

    @Transactional
    @Query(
            nativeQuery = true,
            value = "SELECT AVG(score) FROM reviews WHERE mediaID = ?1 AND ratingType = ?2")
    Optional<Double> getAvgScore(Long mediaID, int m);

    @Transactional
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM pine.reviews WHERE mediaID = ?1 AND ratingType = ?2 AND score >= 3.5")
    Long getCertifiedCount(Long mediaID, int m);

    @Transactional
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM pine.reviews WHERE mediaID = ?1 AND ratingType = ?2")
    Long getTotalCount(Long mediaID, int m);

    @Transactional
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM pine.reviews WHERE mediaID = ?1")
    Long getReviewTotal(Long mediaID);

}
