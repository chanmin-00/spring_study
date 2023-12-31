package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        // given
        Member member = createMember();

        Book book = createBook("시골 JPA", 10, 10000);
        // when
        int orderCount = 2;
        Long orderId =  orderService.order(member.getId(), book.getId(), orderCount);
        // then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("상품 주문시 상태는 Order", OrderStatus.ORDER, getOrder.getStatus());
        // 2번째 인자가 기댓값, 3번째 인자가 실제값
        Assert.assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격 * 수량이다", 10000 * orderCount, getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다", 8, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception{
        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10, 10000); //이름, 가격, 재고
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(),
                orderCount);
        //When
        orderService.cancelOrder(orderId);
        //Then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 취소시 상태는 CANCEL 이다.",OrderStatus.CANCEL,
                getOrder.getStatus());
        Assert.assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10,
                item.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Item item = createBook("시골JPA", 10, 10000);
        int orderCount = 11;
        // when
        try {
            orderService.order(member.getId(), item.getId(), orderCount);
        }
        catch (NotEnoughStockException e){
            return;
        }
        fail("재고 수량 부족 예외가 발생해야 한다");
        // then
    }

    private Book createBook(String name, int stockQuantity, int price) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}