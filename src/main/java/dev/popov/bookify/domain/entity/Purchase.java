package dev.popov.bookify.domain.entity;

import static javax.persistence.CascadeType.ALL;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
public class Purchase extends BaseEntity {
	@ManyToOne(targetEntity = User.class, cascade = ALL)
	private User user;

	@ManyToOne(targetEntity = Event.class, cascade = ALL)
	private Event event;
}
