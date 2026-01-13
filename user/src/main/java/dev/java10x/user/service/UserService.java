package dev.java10x.user.service;

import dev.java10x.user.entities.UserModel;
import dev.java10x.user.producer.UserProducer;
import dev.java10x.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    @Transactional
    public UserModel saveAndPublish (UserModel userModel) {
        userModel = userRepository.save(userModel);
        userProducer.sendProducerMessage(userModel);
        return userModel;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public void DeleteUser(String id) {
        userRepository.deleteById(UUID.fromString(id));
    }

    @Transactional
    public UserModel patchUser(String id, UserModel partialUser) {
        UserModel user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (partialUser.getName() != null) user.setName(partialUser.getName());
        if (partialUser.getEmail() != null) user.setEmail(partialUser.getEmail());

        return userRepository.save(user);
    }

    public List<UserModel> filterUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
}
