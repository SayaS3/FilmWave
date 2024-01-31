package com.sayas.filmhub.domain.user;

import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import com.sayas.filmhub.domain.user.dto.UserRegistrationDto;
import javassist.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public List<UserCredentialsDto> getAllUsers() {
        Iterable<User> userIterable = userRepository.findAll();
        List<User> users = StreamSupport.stream(userIterable.spliterator(), false)
                .toList();
        return users.stream()
                .map(UserCredentialsDtoMapper::map)
                .collect(Collectors.toList());
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
}