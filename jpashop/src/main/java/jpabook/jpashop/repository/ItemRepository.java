package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null) // item은 jpa에 저장하기 전까지 id값이 없음
            em.persist(item);
        else { // id 값이 있으면 DB에 등록된 어떤 것을 가져옴
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return  em.createQuery("select i from Item i", Item.class).getResultList(); // 여러 개를 찾을 때는 jpql 사용
    }
}
