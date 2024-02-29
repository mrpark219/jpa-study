package jpabook.jpastudy.api;

import jpabook.jpastudy.domain.Order;
import jpabook.jpastudy.domain.OrderItem;
import jpabook.jpastudy.repository.OrderRepository;
import jpabook.jpastudy.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;

	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for(Order order : all) {
			// LAZY 로딩 초기화
			order.getMember().getName();
			order.getDelivery().getAddress();

			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(orderItem -> orderItem.getItem().getName());
		}

		return all;
	}
}
