package call.api.repo;


import call.api.model.JwtRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<JwtRequest, Integer> {


     JwtRequest findByUsername(String username);
}
