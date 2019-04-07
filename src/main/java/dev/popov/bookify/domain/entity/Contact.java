package dev.popov.bookify.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contacts")
public class Contact extends BaseEntity {
	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Not a valid email")
	private String email;

	@Column(name = "address")
	private String address;
}
