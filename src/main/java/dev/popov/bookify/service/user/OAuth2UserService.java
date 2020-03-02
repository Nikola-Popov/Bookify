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
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Autowired
    public OAuth2UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
      final  OAuth2User loadUser = super.loadUser(userRequest);
        Map<String, Object> attributes = loadUser.getAttributes();
        System.out.println(attributes.get("name"));
        System.out.println(attributes.get("login"));
        System.out.println(attributes.get("id"));
        System.out.println(attributes.get("email"));
        UserServiceModel userServiceModel = new UserServiceModel();

        return loadUser;
    }
}
