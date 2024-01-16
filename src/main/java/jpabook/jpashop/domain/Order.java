package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter@Setter
public class Order {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "order_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
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
}
