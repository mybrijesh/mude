package us.mude.repository;

import org.springframework.data.repository.CrudRepository;
import us.mude.model.Review;
import us.mude.model.WantToSee;

import java.util.Optional;

public interface WantToSeeRepository extends CrudRepository<WantToSee, Long> {

    Optional<WantToSee> findByMediaIDAndMemberID(Long mediaID, Long memberID);

    int countByIsWantToSeeAndMemberID(boolean isWantToSee, Long memberID);


}
