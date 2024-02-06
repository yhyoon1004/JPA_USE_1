package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDTO> findOrderQueryDTOs() {
        //findOrders() 로 xToOne 관계를 처리한후,
        List<OrderQueryDTO> result = findOrders();          //||-> query N개
        // 처리한 값을 기반으로 xToMany 관계를 반복  처리
        result.forEach(o -> {                               //||->  query N번
            List<OrderItemQueryDTO> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;

    }

    private List<OrderItemQueryDTO> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name,oi.orderPrice,oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id= :orderId", OrderItemQueryDTO.class
                ).setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDTO(o.id, m.name, o.orderDate, o.status, d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join  o.delivery d ", OrderQueryDTO.class
                )
                .getResultList();
    }

    public List<OrderQueryDTO> findAllByDTO_optimization() {
        List<OrderQueryDTO> result = findOrders();
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDTO> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name,oi.orderPrice,oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id in  :orderIds ", OrderItemQueryDTO.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = orderItems.stream()
                 .collect(Collectors.groupingBy(orderItemQueryDTO -> orderItemQueryDTO.getOrderId()));

        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result ;
    }
}
