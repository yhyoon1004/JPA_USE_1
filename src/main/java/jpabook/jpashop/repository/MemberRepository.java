package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    //만약 엔티티 메니저 팩토리를 주입받고 싶으면 아래 어노테이션으로 주입 받을 수 있음...
//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;

    public long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        List<Member> result = em.createQuery("select  m from Member m", Member.class).getResultList();
        return result;
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class).setParameter("name", name).getResultList();
    }
}
