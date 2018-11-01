package com.maxopus.cloud.authorization.mongo.user;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.maxopus.cloud.authorization.mongo.user.domain.User;
import com.maxopus.cloud.authorization.mongo.user.repositories.UserRepository;
import com.maxopus.cloud.authorization.mongo.user.services.SecurityContextService;

@Component
public class MongoUserDetailsManager implements UserDetailsManager {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextService securityContextService;

	/*@Autowired
    private MongoTemplate mongoTemplate;
	
	
	@Autowired
	public MongoUserDetailsManager(final UserRepository userRepository,
			final SecurityContextService securityContextService) {
		this.userRepository = userRepository;
		this.securityContextService = securityContextService;
	}*/

	@Override
	public void createUser(final UserDetails user) {
		validateUserDetails(user);

		User newUser = new User(null, null, 
				user.getUsername(), 
				user.getPassword(), 
				user.getAuthorities(),
				user.isAccountNonExpired(),
				user.isAccountNonLocked(),
				user.isCredentialsNonExpired(),
				user.isEnabled());

		userRepository.save(newUser);
	}

	@Override
	public void updateUser(final UserDetails user) {
		validateUserDetails(user);

		User updateUser = new User(null, null, 
				user.getUsername(), 
				null, 
				user.getAuthorities(),
				user.isAccountNonExpired(),
				user.isAccountNonLocked(),
				user.isCredentialsNonExpired(),
				user.isEnabled());
		
		userRepository.save(updateUser);
	}

	@Override
	public void deleteUser(final String username) {
		final User user = userRepository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("User " + username + " not found in database.");
		}
		userRepository.delete(user);
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword) {
		final Authentication currentUser = securityContextService.getAuthentication();

		if (currentUser == null) {
			// This would indicate bad coding somewhere
			throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
					"for current user.");
		}

		final String username = currentUser.getName();

		// If an authentication manager has been set, re-authenticate the user with the supplied password.
		if (authenticationManager != null) {
			logger.debug("Reauthenticating user '"+ username + "' for password change request.");

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} else {
			logger.debug("No authentication manager set. Password won't be re-checked.");
		}

		logger.debug("Changing password for user '"+ username + "'");

		userRepository.changePassword(oldPassword, newPassword, username);

		securityContextService.setAuthentication(createNewAuthentication(currentUser));
	}

	@Override
	public boolean userExists(final String username) {
		final User user = userRepository.findByUsername(username);
		return user != null;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("User " + username + " not found in database.");
		}
		return user; 
	}

	protected Authentication createNewAuthentication(final Authentication currentAuth) {
		final UserDetails user = loadUserByUsername(currentAuth.getName());

		final UsernamePasswordAuthenticationToken newAuthentication =
				new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		newAuthentication.setDetails(currentAuth.getDetails());

		return newAuthentication;
	}
	
	/*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        User mongoUser = mongoTemplate.findOne(query, User.class);
        if (mongoUser == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
        return mongoUser;
    }*/

	private void validateUserDetails(UserDetails user) {
		Assert.hasText(user.getUsername(), "Username may not be empty or null");
		validateAuthorities(user.getAuthorities());
	}

	private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Authorities list must not be null");

		for (GrantedAuthority authority : authorities) {
			Assert.notNull(authority, "Authorities list contains a null entry");
			Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
		}
	}

	public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
}
