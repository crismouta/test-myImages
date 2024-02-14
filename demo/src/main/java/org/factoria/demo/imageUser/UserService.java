package org.factoria.demo.imageUser;

import jakarta.transaction.Transactional;
import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.factoria.demo.system.exception.UserNameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<ImageUser> findAll() {
        return this.userRepository.findAll();
    }

    public ImageUser findById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectByIdNotFoundException("user", userId));
    }

    public ImageUser save(ImageUser newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public ImageUser update(Long userId, ImageUser updatedUser) {
        ImageUser oldUser =  this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectByIdNotFoundException("user", userId));
        oldUser.setName(updatedUser.getName());
        oldUser.setSurname(updatedUser.getSurname());
        oldUser.setEnabled(updatedUser.isEnabled());
        oldUser.setRoles(updatedUser.getRoles());
       return this.userRepository.save(oldUser);
    }

    public void delete(Long userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectByIdNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByName(username)
                .map(imageUser -> new MyUserPrincipal(imageUser))
                .orElseThrow(() -> new UserNameNotFoundException(username));
    }
}
