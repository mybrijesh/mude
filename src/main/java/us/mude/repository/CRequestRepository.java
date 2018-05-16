package us.mude.repository;

import org.springframework.data.repository.CrudRepository;
import us.mude.model.CriticRequest;
import us.mude.model.Member;
import us.mude.model.WantToSee;

import java.util.List;
import java.util.Optional;

public interface CRequestRepository extends CrudRepository<CriticRequest, Long> {
    Optional<CriticRequest> findByMember(Member m);
    Optional<CriticRequest> findByMember_id(Long mid);
    List<CriticRequest> findByOrderByRegDateAsc();
}
