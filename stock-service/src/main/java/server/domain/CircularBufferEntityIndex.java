package server.domain;

import java.util.Arrays;

// 순환 버퍼와 연동하여 엔티티 인덱스를 관리하는 클래스
public class CircularBufferEntityIndex {
    private final Long[] buffer;  // 버퍼 배열, 엔티티 ID를 저장
    private final int size;      // 버퍼의 최대 크기
    private int head;      // 읽기 위치 인덱스
    private int tail;      // 쓰기 위치 인덱스
    private boolean isFull;  // 버퍼가 가득 찼는지 여부

    public CircularBufferEntityIndex(int size) {
        this.size = size;
        this.buffer = new Long[size];
        this.head = 0;
        this.tail = 0;
        this.isFull = false;
    }

    // 엔티티 ID를 버퍼에 추가
    public void addEntityId(Long entityId) {
        if (isFull) {
            // 버퍼가 가득 찼다면 가장 오래된 데이터를 덮어씁니다.
            head = (head + 1) % size;
        }

        buffer[tail] = entityId;        // 데이터를 현재 tail 위치에 쓰기
        tail = (tail + 1) % size;       // tail 인덱스 증가 (순환)

        // 만약 tail이 head와 같아지면 버퍼가 가득 찬 상태임을 표시합니다.
        isFull = (tail == head);
    }

    // 버퍼에서 엔티티 ID 읽기 (FIFO 방식)
    public Long readEntityId() {
        if (isEmpty()) {
            throw new RuntimeException("Buffer is empty");
        }

        Long entityId = buffer[head];    // 현재 head 위치에서 데이터 읽기
        head = (head + 1) % size;        // head 인덱스 증가 (순환)

        // 데이터를 읽었으므로 이제 버퍼는 가득 차지 않았습니다.
        isFull = false;

        return entityId;
    }

    // 버퍼가 비어 있는지 확인
    public boolean isEmpty() {
        return (!isFull && (head == tail));
    }

    // 버퍼가 가득 찼는지 확인
    public boolean isFull() {
        return isFull;
    }

    // 현재 버퍼 상태를 출력 (디버깅용)
    public void printBuffer() {
        System.out.println("Circular Buffer: " + Arrays.toString(buffer));
        System.out.println("Head: " + head + ", Tail: " + tail + ", isFull: " + isFull);
    }
}