package jpabook.jpastudy.service;

import jakarta.persistence.EntityManager;
import jpabook.jpastudy.domain.Address;
import jpabook.jpastudy.domain.Member;
import jpabook.jpastudy.domain.Order;
import jpabook.jpastudy.domain.OrderStatus;
import jpabook.jpastudy.domain.item.Book;
import jpabook.jpastudy.exception.NotEnoughStockException;
import jpabook.jpastudy.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	@DisplayName("상품 주문")
	@Test
	void order() {

		// given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123123"));
		em.persist(member);

		Book book = new Book();
		book.setName("시골 JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);

		// when
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());
		assertThat(1).isEqualTo(getOrder.getOrderItems().size());
		assertThat(10000 * orderCount).isEqualTo(getOrder.getTotalPrice());
		assertThat(8).isEqualTo(book.getStockQuantity());
	}

	@DisplayName("상품주문 재고수량 초과")
	@Test
	void orderMoreThanStockQuantity() {
	    
	    // given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123123"));
		em.persist(member);

		Book book = new Book();
		book.setName("시골 JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);

		int orderCount = 11;
		
	    // when // then
	    assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
			.isInstanceOf(NotEnoughStockException.class)
			.hasMessage("need more stock");
	}
	
	@DisplayName("주문 취소")
	@Test
	void cancelOrder() {

		// given
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123123"));
		em.persist(member);

		Book item = new Book();
		item.setName("시골 JPA");
		item.setPrice(10000);
		item.setStockQuantity(10);
		em.persist(item);

		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
		assertThat(10).isEqualTo(item.getStockQuantity());
	}
}