package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    //변경감지를 dirty checking 이라고함
    //준영속 엔티티 = JPA가 더 이상 관리하지 않는 엔티티, 이전에 DB에서 가져오긴했던 엔티티, 기존의 엔티티 식별자를 가지고 있으면 준영속 엔티티라고 함
    //준영속 엔티티를 수정하는 2가지 방법
    //1. 변경 감지 ( dirty checking ) 기능 사용
    //2. 병합 ( merge ) 사용
    @Test
    public void updateTest () throws Exception{
        //given
        em.find(Book.class, 1L);
        //when

        //then

    }
}
