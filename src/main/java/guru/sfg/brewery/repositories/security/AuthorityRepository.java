package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The interface User repository.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
