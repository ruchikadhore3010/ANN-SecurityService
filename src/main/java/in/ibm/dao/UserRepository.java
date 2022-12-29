package in.ibm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ibm.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, String>
{
	User findByEmail(String email);
}
