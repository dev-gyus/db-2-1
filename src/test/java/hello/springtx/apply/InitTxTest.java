package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@SpringBootTest
public class InitTxTest {
    @Test
    void go() {
        // 초기화 코드는 스프링이 초기화 시점에 호출된다.
    }

    @TestConfiguration
    static class InitTxTestConfig {
        @Bean
        Hello hello() {
            return new Hello();
        }
    }
    @Slf4j
    static class Hello {

        @PostConstruct // 스프링 컨테이너가 초기화 된다음에 스프링 aop가 적용되기때문에, @PostConstruct에서 tx활성화 여부를 확인하면 적용이 안되어있다
        @Transactional
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("Hello init @PostConstruct tx active={}", isActive);
        }

        // ApplicationReadyEvent <- 스프링이 완전히 초기화된 다음에 호출되는 이벤트
        // 해당 코드는 AOP부터 모든 스프링이 초기화되는데 필요한 것들이 다 수행 된 다음 호출됩니다
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            boolean isActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("Hello init @PostConstruct tx active={}", isActive);
        }
    }
}
