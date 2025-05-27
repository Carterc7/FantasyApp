package com.fantasy.fantasyapi.mongoServices;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fantasy.fantasyapi.leagueModels.FantasyTeam;
import com.fantasy.fantasyapi.leagueModels.User;
import com.fantasy.fantasyapi.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder; // Secure encoder bean

    public void deleteUserByUserID(String userID) {
        userRepository.deleteByUserID(userID);
    }

    public void deleteTeamFromMocks(String userID, String teamName) {
        Optional<User> userOptional = userRepository.findById(userID);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<List<FantasyTeam>> completedMocks = user.getCompletedMocks();

            completedMocks
                    .removeIf(teamList -> teamList.stream().anyMatch(team -> team.getTeamName().equals(teamName)));

            user.setCompletedMocks(completedMocks);
            userRepository.save(user);
        }
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // ✅ Updated to securely compare hashed password
    public boolean authenticateUser(String username, String rawPassword) {
        System.out.println("Authenticating user...");
        System.out.println("Username input: " + username);
        System.out.println("Raw password input: " + rawPassword);

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String storedHash = user.getPassword();
            System.out.println("User found. Stored hashed password: " + storedHash);

            boolean match = passwordEncoder.matches(rawPassword, storedHash);
            System.out.println("Password match result: " + match);
            return match;
        } else {
            System.out.println("User not found with username: " + username);
        }

        return false;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // ✅ Hash password before saving new user
    public User addUser(User user) {
        System.out.println("Adding new user...");
        System.out.println("Original username: " + user.getUsername());
        System.out.println("Original password (raw): " + user.getPassword());

        // Generate and set unique user ID
        String generatedId = UUID.randomUUID().toString();
        user.setUserID(generatedId);
        System.out.println("Generated userID: " + generatedId);

        // Hash and set password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        System.out.println("Encoded password: " + encodedPassword);

        // Save user
        User savedUser = userRepository.save(user);
        System.out.println("User successfully saved with ID: " + savedUser.getUserID());

        return savedUser;
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + user.getUserID()));

        existingUser.setUsername(user.getUsername());

        // Only update the password if it was changed and is not already hashed
        String incomingPassword = user.getPassword();
        String currentEncodedPassword = existingUser.getPassword();

        if (!incomingPassword.equals(currentEncodedPassword)) {
            // Check if the incoming password is a raw password (not hashed)
            if (!passwordEncoder.matches(incomingPassword, currentEncodedPassword)) {
                // It's a raw password and has changed, so encode it
                String newEncoded = passwordEncoder.encode(incomingPassword);
                existingUser.setPassword(newEncoded);
            } else {
                // Incoming password is raw but matches the current hash — keep existing hash
                existingUser.setPassword(currentEncodedPassword);
            }
        }

        existingUser.setCompletedMocks(user.getCompletedMocks());
        return userRepository.save(existingUser);
    }

}
