package jpabook.jpashop.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {   //이렇게 레포지토리 패키지에서 따로 분리한 이유는, 본 쿼리문은 엔티티가 아닌 DTO객체를 다루어사용하기에 재사용성이 떨어지고
    private final EntityManager em;

    public List<OrderSimpleQueryDTO> findOrderDTOs() {
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDTO(o.id,m.name,o.orderDate,o.status,d.address) from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDTO.class)
                .getResultList();
    }
}
