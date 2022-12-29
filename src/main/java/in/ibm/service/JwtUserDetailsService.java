package in.ibm.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import in.ibm.dao.UserRepository;
import in.ibm.entity.Role;
import in.ibm.entity.User;
import in.ibm.entity.UserDTO;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	public Role findRole(String username) throws UsernameNotFoundException {
		User user = userDao.findByEmail(username);
		return user.getRole();
	}

	public ResponseEntity<?> saveUser(UserDTO user) {

		if (userDao.findByEmail(user.getUsername()) != null) {
			return ResponseEntity.ok("Username already Taken");
		}
		Role role = new Role();
		role.setId(1);
		role.setRole("ROLE_USER");
		User newUser = new User();
		newUser.setName(user.getName());
		newUser.setEmail(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setRole(role);
		return ResponseEntity.ok(userDao.save(newUser));

	}

}
