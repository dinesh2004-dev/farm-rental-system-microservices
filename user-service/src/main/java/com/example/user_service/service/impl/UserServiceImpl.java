package com.example.user_service.service.impl;

import com.example.user_service.clients.EquipmentServiceClient;
import com.example.user_service.clients.MapsServiceClient;
import com.example.user_service.dtos.CoordinatesDto;
import com.example.user_service.dtos.EquipmentDTO;
import com.example.user_service.dtos.UserDTO;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EquipmentServiceClient equipmentServiceClient;

    @Autowired
    private MapsServiceClient mapsServiceClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int saveUser(UserDTO userDTO) {

        CoordinatesDto coordinates = mapsServiceClient.geocodeAddress(userDTO.getAddress());
        userDTO.setLatitude(coordinates.getLatitude());
        userDTO.setLongitude(coordinates.getLongitude());
        userDTO.setAddress(coordinates.getAddress());

        User user = mapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }


    @Override
    public UserDTO getUserById(int id) {

        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public List<EquipmentDTO> getEquipmentsByUserId(int userId){
        return equipmentServiceClient.getEquipmentsByUserId(userId);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + username));
    }
}
