package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional//메서드에 따로 트렌젝셔널 어노테이션을 넣어줘서 readonly 에서 제외
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    //-vvv- 아래 메서드가 entityManager.merge(entity) 의 동작 방식. 즉, 파라미터로 넘어온 엔티티 값으로 기존 값을 바꿔넣어줌-> 트렌젹션 커밋될 때 반영되게 됨
    //entityManager.merge(entity) 의 위험한 점
    //파라미터로 넘어온 준영속상태의 엔티티 값으로 기존의 엔티티 값을 싹다 바꿔넣기에 영속상태의 엔티티 값에 null 이 들어갈 수 있음 -> db에 null로 업데이트 될 수 있음
    //> 그러므로 merge() 보단 변경감지 dirty checking 을 사용해야함
    //> 번거롭더라도 내가 변경할 부분만 setColumName(~) 해야함 아래 메서드 처럼
    @Transactional
    //트렌젝셔널 어노테이션에 의해 아래 로직을 수행하고 commit이 됨, 이후 JPA는
    //flush 를 하게 되는 데 이때 flush 하기 전 변경된 것을 찾아 db에 넣어줌 -> [dirty checking]
    //commit -> flush = 영속성 컨텍스트에서 변경된 부분을 다 찾음, 바뀐값들을 db에 update 쿼리를 날림
    public Item updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId); //영속 상태의 엔티티
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        findItem.setCategories(param.getCategories());  //이러한 코드도 않좋음 나중에 추적할 때 찾기 어려움 -> entity 객체 레벨에서 변경 메서드 정의하는 게 좋음(setter 쓰지 말자..)
//        itemRepository.save(findItem); 호출할 필요가 없음 -> why? dirty check 덕분에
        return findItem;
        //결론 : 엔티티를 변경할 때는 꼭 변경감지 dirty check 를 사용하자..., merge 지양
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
