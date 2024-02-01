package com.sayas.filmhub.domain.user;

import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import com.sayas.filmhub.domain.user.dto.UserRegistrationDto;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private static final String DEFAULT_USER_ROLE = "USER";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }

    @Transactional
    public void registerUserWithDefaultRole(UserRegistrationDto userRegistration) {
        UserRole defaultRole = userRoleRepository.findByName(DEFAULT_USER_ROLE).orElseThrow();
        if (existsByEmail(userRegistration.getEmail())) {
            throw new EmailExistsException("Email address already in use: " + userRegistration.getEmail());
        }
        if (existsByUsername(userRegistration.getUsername())) {
            throw new EmailExistsException("UserName already in use: " + userRegistration.getUsername());
        }
        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setUsername(userRegistration.getUsername());
        user.setShadowBanned(false);
        user.getRoles().add(defaultRole);
        userRepository.save(user);
    }

    public Optional<UserCredentialsDto> getuserByName(String username) {
        return userRepository.findByUsername(username)
                .map(UserCredentialsDtoMapper::map);
    }

    public boolean isShadowBanned(Long userId) throws NotFoundException {
        Optional<User> userWithShadowBan = userRepository.findById(userId);
        if (userWithShadowBan.isPresent()) {
            User user = userWithShadowBan.get();
            return user.isShadowBanned();
        } else {
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }

    public Page<UserCredentialsDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(UserCredentialsDtoMapper::map);
    }

    @Transactional
    public void shadowBan(String userName) throws NotFoundException {
        Optional<User> userToFind = userRepository.findByUsername(userName);
        if (userToFind.isPresent()) {
            User user = userToFind.get();
            user.setShadowBanned(true);
            userRepository.save(user);
        } else {
            throw new NotFoundException("User not found.");
        }
    }

    @Transactional
    public void unban(String userName) throws NotFoundException {
        Optional<User> userToFind = userRepository.findByUsername(userName);
        if (userToFind.isPresent()) {
            User user = userToFind.get();
            user.setShadowBanned(false);
            userRepository.save(user);
        } else {
            throw new NotFoundException("User not found.");
        }
    }

    @Transactional
    public void deleteUser(String userName) throws NotFoundException {
        Optional<User> userToFind = userRepository.findByUsername(userName);
        if (userToFind.isPresent()) {
            User user = userToFind.get();
            userRepository.delete(user);
        } else {
            throw new NotFoundException("User not found.");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public static class EmailExistsException extends RuntimeException {
        public EmailExistsException(String message) {
            super(message);
        }
    }
}