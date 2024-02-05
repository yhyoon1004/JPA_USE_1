package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d", Order.class
        ).getResultList();

    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o " +      //여기서 distinct 는  db sql 의 distinct 와(실제로 함) 엔티티의 id값이 같을 경우 중복을 제거함
                "join fetch o.member m " +
                "join fetch o.delivery d " +
                "join fetch o.orderItems oi " +
                "join fetch oi.item i", Order.class)
                .getResultList();
    }
}
