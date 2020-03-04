package dev.popov.bookify.service.user;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;

@Component
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public OAuth2UserService(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oauth2User = super.loadUser(userRequest);
//        final UserServiceModel userServiceModel = modelMapper.map(OAuth2User.class, UserServiceModel.class,
//                SSO_GITHUB_USER_MAPPING);
// 
//        if (!isUserRegistered(userServiceModel)) {
//            userService.register(userServiceModel);
//        }

        return oauth2User;
    }

    private boolean isUserRegistered(final UserServiceModel userServiceModel) {
        return userService.findByUsername(userServiceModel.getUsername()).isPresent();
    }
}
