package dev.popov.bookify.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERCONNECTION")
public class SocialUser {
	@EmbeddedId
	private SocialConnectionId id;

	private int rank;

	@Column(name = "DISPLAYNAME")
	private String displayName;

	@Column(name = "PROFILEURL")
	private String profileUrl;

	@Column(name = "IMAGEURL")
	private String imageUrl;

	@Column(name = "ACCESSTOKEN", nullable = false)
	private String accessToken;

	private String secret;

	@Column(name = "REFRESHTOKEN")
	private String refreshToken;

	@Column(name = "EXPIRETIME")
	private long expireTime;

	@Embeddable
	@Getter
	@Setter
	@NoArgsConstructor
	class SocialConnectionId implements Serializable {
		private static final long serialVersionUID = -5728472528918852398L;

		@Column(name = "USERID", nullable = false)
		private String userId;

		@Column(name = "PROVIDERID", nullable = false)
		private String providerId;

		@Column(name = "PROVIDERUSERID")
		private String providerUserId;
	}
}
