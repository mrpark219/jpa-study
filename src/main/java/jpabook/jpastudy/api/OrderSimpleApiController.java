package jpabook.jpastudy.api;

import jpabook.jpastudy.domain.Address;
import jpabook.jpastudy.domain.Order;
import jpabook.jpastudy.domain.OrderStatus;
import jpabook.jpastudy.repository.OrderRepository;
import jpabook.jpastudy.repository.OrderSearch;
import jpabook.jpastudy.repository.order.simpleQuery.OrderSimpleQueryDto;
import jpabook.jpastudy.repository.order.simpleQuery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order(XToOne 관계에서의 최적화)
 * Order -> Member
 * Order -> delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for(Order order : all) {
			// Lazy 강제 초기화
			order.getMember().getName();
			order.getDelivery().getAddress();
		}
		return all;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		// order 조회 쿼리 1번
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		// order 1개 당 Member, Delivery 조회 쿼리 2번 => 총 쿼리 5번 -> 1 + N + N 문제(N + 1 문제)
		return orders.stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		// fetch 조인으로 쿼리 1번에 모든 데이터를 가져옴
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		return orders.stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			// LAZY 초기화
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			// LAZY 초기화
			address = order.getDelivery().getAddress();
		}
	}
}
