package jpabook.jpastudy.domain;

import jakarta.persistence.*;
import jpabook.jpastudy.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;


@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_order_id")
	private Order order;

	private int orderPrice;

	private int count;
}