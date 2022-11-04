package org.example.counter;

/**
 * - 서블릿 객체를 싱글톤으로 관리 (인스턴스 하나만 생성하여 공유하는 방식)
 *     - 상태를 유지(stateful)하게 설계하면 안됨
 *     - Thread safety 하지 않음
 */
// 싱글톤을 상태를 가지도록 설계 -> 어떠한 문제가 생기는지 파악
public class Counter implements Runnable {
    private int count = 0;

    public void increment() {
        count++;
    }

    public void decrement() {
        count--;
    }

    public int getValue() {
        return count;
    }

    @Override
    public void run() {
        this.increment();   // 기대값 1
        System.out.println("Value for Thread After increment" + Thread.currentThread().getName() + " " + this.getValue());
        this.decrement();   // 기대값 0
        System.out.println("Value for Thread at last" + Thread.currentThread().getName() + " " + this.getValue());
    }
}
