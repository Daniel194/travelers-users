package org.travelers.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.travelers.users.domain.User;
import org.travelers.users.repository.UserRepository;
import org.travelers.users.repository.search.UserSearchRepository;
import org.travelers.users.security.SecurityUtils;
import org.travelers.users.service.dto.UserDTO;
import org.travelers.users.service.dto.UserDetailsDTO;
import org.travelers.users.service.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final CacheManager cacheManager;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserSearchRepository userSearchRepository,
                       CacheManager cacheManager,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> getByLogin(String login) {
        return userRepository.findByLogin(login).map(UserDTO::new);
    }

    public Optional<UserDTO> getCurrent() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByLogin)
            .map(UserDTO::new);
    }

    public Optional<UserDTO> update(UserDetailsDTO userDetailsDTO) {
        User user = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByLogin).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setDescription(userDetailsDTO.getDescription());
        user.setDateOfBirth(userDetailsDTO.getDateOfBirth());
        user.setPlaceOfBirth(userDetailsDTO.getPlaceOfBirth());
        user.setSocialPlatforms(userDetailsDTO.getSocialPlatforms());

        userRepository.save(user);
        userSearchRepository.save(user);
        clearUserCaches(user.getLogin());

        return Optional.of(userMapper.userToUserDTO(user));
    }

    public List<UserDTO> search(String query) {
        return StreamSupport
            .stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(UserDTO::new)
            .collect(Collectors.toList());
    }

    private void clearUserCaches(String login) {
        Cache cache = cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE);

        if (cache != null) {
            cache.evict(login);
        }
    }

}
