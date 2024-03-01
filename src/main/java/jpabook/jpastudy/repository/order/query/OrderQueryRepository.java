package jpabook.jpastudy.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders();

		result.forEach(orderQueryDto -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId());
			orderQueryDto.setOrderItems(orderItems);
		});

		return result;
	}

	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();

		Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

		result.forEach(orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));

		return result;
	}

	private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
		List<OrderItemQueryDto> orderItems = em.createQuery(
				"select new jpabook.jpastudy.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
					"from OrderItem oi " +
					"join oi.item i " +
					"where oi.order.id in :orderIds", OrderItemQueryDto.class)
			.setParameter("orderIds", orderIds)
			.getResultList();

		return orderItems.stream()
			.collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
	}

	private static List<Long> toOrderIds(List<OrderQueryDto> result) {
		return result.stream()
			.map(OrderQueryDto::getOrderId)
			.collect(Collectors.toList());
	}

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
				"select new jpabook.jpastudy.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
					"from OrderItem oi " +
					"join oi.item i " +
					"where oi.order.id = :orderId", OrderItemQueryDto.class)
			.setParameter("orderId", orderId)
			.getResultList();
	}

	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
				"select new jpabook.jpastudy.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
					"from Order o " +
					"join o.member m " +
					"join o.delivery d", OrderQueryDto.class)
			.getResultList();
	}

	public List<OrderFlatDto> findAllByDto_flat() {
		return em.createQuery(
				"select new jpabook.jpastudy.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
					"from Order o " +
					"join o.member m " +
					"join o.delivery d " +
					"join o.orderItems oi " +
					"join oi.item i", OrderFlatDto.class)
			.getResultList();
	}
}
