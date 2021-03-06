package dev.popov.bookify.domain.entity;

import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_LENGTH;
import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_TOO_SHORT_MESSAGE;
import static java.util.Collections.emptySet;
import static javax.persistence.CascadeType.ALL;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
	private static final long serialVersionUID = -8730650508917923555L;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	@Size(min = PASSWORD_LENGTH, message = PASSWORD_TOO_SHORT_MESSAGE)
	private String password;

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> authorities = emptySet();

	@OneToOne(cascade = ALL)
	@JoinColumn(name = "contact_id")
	private Contact contact;

	@Column(name = "image")
	private String image;

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}
}
