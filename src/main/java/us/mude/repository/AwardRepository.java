package us.mude.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import us.mude.common.ArtistType;
import us.mude.model.Award;
import us.mude.model.Filmography;
import us.mude.model.Media;


import java.util.List;

public interface AwardRepository extends CrudRepository<Award, Long> {

    List<Award> findByAwardYearInOrderByMedia_ReleaseDate_theatreAsc(
         List<Integer> y,
         Pageable p
    );
    int countByAwardYearInOrderByMedia_ReleaseDate_theatreAsc(
            List<Integer> y
    );

    List<Award> findByAwardYearInOrderByMedia_Mudemeter_criticMudemeterDesc(
        List<Integer> y,
        Pageable p
    );
    int countByAwardYearInOrderByMedia_Mudemeter_criticMudemeterDesc(
            List<Integer> y
    );

    List<Award> findByAwardYearInOrderByMedia_BoxOfficeDesc(
        List<Integer> y,
        Pageable p
    );
    int countByAwardYearInOrderByMedia_BoxOfficeDesc(
            List<Integer> y
    );

    List<Award> findByAwardYearInOrderByMedia_reviewCountDesc(
            List<Integer> y,
            Pageable p
    );
    int countByAwardYearInOrderByMedia_reviewCountDesc(
            List<Integer> y
    );
}
