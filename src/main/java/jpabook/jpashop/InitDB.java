package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/**
 * 총 2개 주문
 * *userA
 *  *JPA1 BOOK
 *  *JPA2 BOOK
 * *userB
 *  *SPRING1 BOOK
 *  *SPRING2 BOOK
 * */
@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct//sprig Bean 이 모두 주입되고 나면 해당 메서드를 실행해주는 어노테이션
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    //아래와 같이 별도의 클래스를 Bean 으로 등록해준 것은 위의 @PostConstruct 에서 트렌젝션같은 어노테이션이 적용이 잘안되기 때문에 별로도로 빼줌
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA","서울", "12", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000,100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000,100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 8);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }
        public void dbInit2() {
            Member member = createMember("userB","부산", "32", "23154");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 25000,150);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000,300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 4);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 3);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Member createMember(String name,String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }


}
