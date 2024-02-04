package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDTO(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this.orderId = orderId;
            this.name = name;//Lazy 로딩 초기화  영속성컨텍스트가 해당 값을 가져오기 위해 DB를 조회
            this.orderDate = orderDate;
            this.orderStatus = orderStatus;
            this.address = address;// 마찬가지  Lazy 로딩 초기화
        }
    }

