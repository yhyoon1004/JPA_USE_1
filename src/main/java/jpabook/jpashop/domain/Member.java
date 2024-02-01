package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @NotEmpty
    private String name;
    @Embedded
    private Address address;
    @JsonIgnore// json 데이터를 반환해야할 경우 해당 어노테이션이 붙은 필드는 제외하여 반환
    // ‼️하지만 엔티티에 위의 로직 및 처리는 추후 유지보수에 안좋음. API 스펙 변경이 어려움
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
