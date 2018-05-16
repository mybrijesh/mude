package us.mude.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import us.mude.common.ArtistType;
import us.mude.model.*;
import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

    int countDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCase(
            List<String> f,
            List<String> l
    );

    List<Artist> findDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            List<String> f,
            List<String> l,
            Pageable p
    );

    List<Artist> findDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            List<String> f,
            List<String> l
    );

    List<Artist> findDistinctTop5ByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            List<String> f,
            List<String> l
    );

    List<Artist> findDistinctByName_firstNameInIgnoreCaseAndName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
            List<String> f,
            List<String> l
    );
}
