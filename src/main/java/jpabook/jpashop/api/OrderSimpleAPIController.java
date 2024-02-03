package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne (oneToOne, ManyToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleAPIController {

    //    private OrderService orderService;
     final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();    //lazy 로딩으로 설정한 엔티티 객체의 속성값을 일부로 호출해서
            order.getDelivery().getAddress();//proxy 객체의 값이 아닌 조회해온 엔티티 객체의 값을 가져오게하여 lazy proxy 객체 에러를 해결하는 방법
        }

        return all;
    }
}
