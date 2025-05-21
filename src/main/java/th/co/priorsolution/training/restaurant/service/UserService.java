package th.co.priorsolution.training.restaurant.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.UserEntity;
import th.co.priorsolution.training.restaurant.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getName(),
                user.getPassword(), // รหัสผ่าน plain text
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );
    }

    public UserEntity registerUser(String username, String password, String role) {
        if (userRepository.findByName(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        UserEntity user = new UserEntity();
        // id เป็น Integer auto generated -> ไม่ต้องเซ็ต id เอง

        user.setName(username);
        user.setPassword(password);  // ใส่ password ตรงๆ
        user.setRole(role);

        return userRepository.save(user);
    }
}
