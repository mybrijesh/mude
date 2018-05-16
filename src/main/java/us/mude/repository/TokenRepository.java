package us.mude.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import us.mude.common.MemberType;
import us.mude.model.*;
import javax.mail.internet.InternetAddress;

public interface TokenRepository extends CrudRepository<Token, Long> {
  public Optional<Token> findByToken( String token );
  public Optional<Token> findByMember(Member member);
}
