package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.CommonPathConstants.HOME_PATH;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;

public class SocialConnectController extends ConnectController {
	public SocialConnectController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
	}

	@Override
	protected String connectedView(String providerId) {
		return HOME_PATH;
	}
}
