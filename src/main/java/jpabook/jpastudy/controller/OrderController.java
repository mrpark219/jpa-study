package jpabook.jpastudy.controller;

import jpabook.jpastudy.domain.Member;
import jpabook.jpastudy.domain.Order;
import jpabook.jpastudy.domain.item.Item;
import jpabook.jpastudy.repository.OrderSearch;
import jpabook.jpastudy.service.ItemService;
import jpabook.jpastudy.service.MemberService;
import jpabook.jpastudy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	private final MemberService memberService;

	private final ItemService itemService;

	@GetMapping("/order")
	public String createForm(Model model) {

		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();

		model.addAttribute("members", members);
		model.addAttribute("items", items);

		return "order/orderForm";
	}

	@PostMapping("/order")
	public String order(@RequestParam("memberId") Long memberId,
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {

		orderService.order(memberId, itemId, count);

		return "redirect:/orders";
	}

	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

		List<Order> orders = orderService.findOrders(orderSearch);
		model.addAttribute("orders", orders);

		return "order/orderList";
	}
}
