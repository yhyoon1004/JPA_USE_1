package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        System.out.println("orderSearch = name: " + orderSearch.getMemberName()+" / status :  "+orderSearch.getOrderStatus());
        return em.createQuery("select o from Order o join o.member  m " +
                        "where o.status = :status " +
                        "and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)//페이징할 때 사용되는 메서드 100(넣어준 값) 부터 결과를 가져옴
                .setMaxResults(1000)//조회된 결과를 최대 1000(넣어준 값) 개 까지 받아오는 메서드
                .getResultList();

    }
}
