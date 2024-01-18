package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional//클래스 레벨에 트렌젝셔널 어노테이션 사용시 클래스 안에 public 메서드은 다 적용됨
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

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //해당 readOnly옵션을 조회하는 곳에 줄 경우 성능을 최적화함 !! 읽기가 아닐 경우 넣으면 안됨, 조회가 많은 클래스면 클래스에 readOnly를 넣어 주고 그클래스 내부에 조회하는 메서드가 아닌 경우 따로 생 어노테이션(@Transactional)을 넣어주면 됨
    @Transactional(readOnly=true)
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select  m from Member m", Member.class).getResultList();
        return result;
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class).setParameter("name", name).getResultList();
    }
}
