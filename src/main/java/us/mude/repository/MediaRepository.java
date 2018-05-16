package us.mude.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import us.mude.common.FilmRatingType;
import us.mude.common.GenreType;
import us.mude.common.MediaType;
import us.mude.model.Media;

import javax.transaction.Transactional;

public interface MediaRepository extends CrudRepository<Media, Long>, JpaSpecificationExecutor<Media> {

    List<Media> findDistinctTop5ByOrderByBoxOfficeDesc();

    List<Media> findDistinctTop10ByOrderByBoxOfficeDesc();

    List<Media> findDistinctTop5ByOrderByMudemeter_criticMudemeterDesc();

    int countDistinctByMediaTypeAndMediaNameIgnoreCaseContaining(
            MediaType m,
            String keyword
    );

    List<Media> findDistinctTop5ByMediaTypeAndReleaseDate_theatre(
            MediaType m,
            Date today
    );

    List<Media> findDistinctTop10ByMediaTypeAndReleaseDate_theatre(
            MediaType m,
            Date today
    );

    List<Media> findDistinctTop5ByMediaTypeOrderByReviewCountDesc(
            MediaType m
    );

    List<Media> findDistinctTop10ByMediaTypeOrderByReviewCountDesc(
            MediaType m
    );

    List<Media> findDistinctTop5ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
            MediaType m
    );

    List<Media> findDistinctTop10ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
            MediaType m
    );

    List<Media> findDistinctTop5ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
            MediaType m
    );

    List<Media> findDistinctTop10ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
            MediaType m
    );

    List<Media> findDistinctTop5ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
            MediaType m,
            boolean isComingSoon
    );

    List<Media> findDistinctTop10ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
            MediaType m,
            boolean isComingSoon
    );

    List<Media> findDistinctTop5ByReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
            boolean isComingSoon
    );

    List<Media> findDistinctTop5ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreDesc (
            MediaType m,
            boolean isComingSoon
    );

    List<Media> findDistinctByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
            MediaType m,
            String mediaName,
            Pageable page
    );

    List<Media> findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
            MediaType m,
            String mediaName
    );

    List<Media> findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseInOrderByReleaseDate_TheatreDesc(
            MediaType m,
            List<String> mediaName
    );

    List<Media> findDistinctTop5ByMediaTypeAndReleaseDate_theatreGreaterThanEqualAndReleaseDate_theatreLessThanEqualOrderByReleaseDate_TheatreAsc(
            MediaType m,
            Date start,
            Date end
    );

    List<Media> findDistinctTop10ByMediaTypeAndReleaseDate_theatreGreaterThanEqualAndReleaseDate_theatreLessThanEqualOrderByReleaseDate_TheatreAsc(
            MediaType m,
            Date start,
            Date end
    );

    List<Media> findDistinctTop10ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByMediaNameAsc(
            MediaType m,
            String keyword
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            Date dateStart,
            Date dateEnd,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            boolean isComingSoon,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );

    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMediaNameAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g
    );

    List<Media> findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
            MediaType m,
            Date c,
            float start,
            float end,
            List<GenreType> g
    );

    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );
    List<Media> findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g,
            Pageable p
    );
    int countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
            MediaType m,
            float start,
            float end,
            List<GenreType> g
    );

    @Modifying
    @Query("UPDATE Media SET isComingSoon = 0 WHERE releaseDateTheatre <= CURRENT_DATE AND isComingSoon = 1")
    int markAsReleased();

    @Modifying
    @Query("UPDATE Media SET isComingSoon = 1 WHERE releaseDateTheatre > CURRENT_DATE AND isComingSoon = 0")
    int markAsComingSoon();

    @Modifying
    @Transactional
    @Query("UPDATE Media SET reviewCount = ?2 WHERE ID = ?1")
    int updateReviewCount(Long mediaID, Long reviewCount);


    @Modifying
    @Transactional
    @Query("UPDATE Media SET audienceAvgScore = ?1, audienceMudemeter = ?2  WHERE ID = ?3")
    void updateAudienceRatings(Double avgScore, float mudemeter, Long mediaID);

    @Modifying
    @Transactional
    @Query("UPDATE Media SET criticAvgScore = ?1, criticMudemeter = ?2  WHERE ID = ?3")
    void updateCriticRatings(Double avgScore, float mudemeter, Long mediaID);

}
