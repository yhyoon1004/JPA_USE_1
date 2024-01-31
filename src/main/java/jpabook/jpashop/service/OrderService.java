package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
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


    @Transactional//서비스단의 트렌젝셔널이 있는 상태, 영속성 컨텍스트가 있는 상태에서 처리해야 변경에 대해 추적할 수 있기에
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());//실제는 이렇게 하지않지만 간단한 예제를 위해

        //주문상품 생성
        OrderItem orderItem = OrderItem.creatOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);//엔티티 save(persist)를 오더만 해도 되는 이유는 연관관계 옵션으로 cascade.all 을 주었기 때문이다.
        //이 예제의 경우 order 만 orderItem, delivery 를 사용하니까 order 가 연관관계 주인으로 인지

        return order.getId();
    }


    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }

}
