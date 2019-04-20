package dev.popov.bookify.domain.entity;

import static javax.persistence.CascadeType.MERGE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
public class Cart extends BaseEntity {
	@OneToOne(cascade = MERGE)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToMany(targetEntity = EventOrder.class, cascade = MERGE)
	@JoinTable(name = "carts_events", joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
	private List<EventOrder> events = new ArrayList<>();

	private BigDecimal totalPrice = BigDecimal.ZERO;
}
