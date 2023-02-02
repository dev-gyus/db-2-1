package hello.springtx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void order() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setUsername("정상");
        // when
        orderService.order(order);
        // then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setUsername("예외22");
        // when
        assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);
        // then
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        assertThat(orderOptional.isEmpty()).isTrue();
    }

    @Test
    void bizException() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setUsername("잔고부족");
        // when
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌를 입금하도록 안내");
        }
        // then
        Order orderOptional = orderRepository.findById(order.getId()).get();
        assertThat(orderOptional.getPayStatus()).isEqualTo("대기");
    }
}