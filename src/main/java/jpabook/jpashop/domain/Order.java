package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter@Setter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    /* 설명
     * orderItem이 여러개면 아래와 같이 각각 persist()에 넣어 주고 엔티티를 persist 해야하는데
     * persist(orderItemA);
     * persist(orderItemB);
     * persist(orderItemC);
     * persist(order);
     * vvvvvvvvv cascade 옵션을 주면
     * persist(order);   위 코드를 한번에 해결
     * CascadeType.ALL로 해놓았을 경우 delete 할때도 전부 제거
     * */


    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    //위의 cascade도 연관된 엔티티도 같이 persist해줌
    //entity는 원래 각각 persist()해줘야함, 하지만 cascade 옵션으로 함께 처리가능
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    //연관관계(편의) 메서드 -> 컨트롤하는 엔티티쪽이 가지고 있는 것이 좋음
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    //    _위의 메서드가 없을 경우 코드_
//    member.getOrders().add(order);
//    order.setMember(member);
//    ------위 처럼 처리해야하는 코드가 연관관계편의 메서드 사용시--
//    order.setMember(member); 이렇게 깔끔해, 양방향 연관관계 설정해줌
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //매개변수 타입 뒤 ... 이 붙을 경우, 메서드 사용시 매개변수 뒤에 해당 타입의 값을 추가하여 계속 넣어줄 수있음
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비지니스로직
    /**
     * 주문취소
     * */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품입니다. 배송완료 상품은 취소가 불가능합니다. ");
        }
        for (OrderItem item : orderItems) {
            item. cancel();
        }
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice =0;
        for (OrderItem item : orderItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

}
