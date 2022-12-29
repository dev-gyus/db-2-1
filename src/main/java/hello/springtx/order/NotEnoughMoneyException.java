package hello.springtx.order;

/**
 * Order에서 고객의 잔고가 부족한 경우 NotEnoughMoneyException을 띄우는데
 * 이때 잔고가 부족하더라도 주문의 상태를 "대기"로 변경해 처리하는 것을 가정하기때문에
 * NotEnoughException 을 checkedException으로 생성함
 * 롤백하면 안됨
 */
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
