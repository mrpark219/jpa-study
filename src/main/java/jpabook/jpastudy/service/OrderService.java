package jpabook.jpastudy.service;

import jpabook.jpastudy.domain.Delivery;
import jpabook.jpastudy.domain.Member;
import jpabook.jpastudy.domain.Order;
import jpabook.jpastudy.domain.OrderItem;
import jpabook.jpastudy.domain.item.Item;
import jpabook.jpastudy.repository.ItemRepository;
import jpabook.jpastudy.repository.MemberRepository;
import jpabook.jpastudy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	private final MemberRepository memberRepository;

	private final ItemRepository itemRepository;

	/**
	 * 주문
	 */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {

		// 엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		// 배송 정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		// 주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);

		// 주문 저장
		orderRepository.save(order);

		return order.getId();
	}

	/**
	 * 주문 취소
	 */
	@Transactional
	public void cancelOrder(Long orderId) {

		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);

		// 주문 취소
		order.cancel();
	}

	// TODO 검색
}
