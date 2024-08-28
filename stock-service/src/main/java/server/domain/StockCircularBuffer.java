package server.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class StockCircularBuffer {
    private static final int MAX_SIZE = 2000;
    private static final int PARTITION_SIZE = 1000;
    private final List<List<Stock>> partitions;

    public StockCircularBuffer() {
        this.partitions = new ArrayList<>(2);
        this.partitions.add(new ArrayList<>(PARTITION_SIZE));
        this.partitions.add(new ArrayList<>(PARTITION_SIZE));
    }

    public void addStock(Stock stock) {
        int partitionIndex = (stock.getId() - 1) / PARTITION_SIZE;
        List<Stock> partition = partitions.get(partitionIndex);

        if (partition.size() >= PARTITION_SIZE) {
            partition.remove(0);
        }
        partition.add(stock);
    }

    public Stock getStock(int id, int index) {
        int partitionIndex = (id - 1) / PARTITION_SIZE;
        List<Stock> partition = partitions.get(partitionIndex);

        if (index < 0 || index >= partition.size()) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        return partition.get(index);
    }

    public int getSize(int id) {
        int partitionIndex = (id - 1) / PARTITION_SIZE;
        return partitions.get(partitionIndex).size();
    }

    // Stock 클래스 정의
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Stock {
        @Id
        @GeneratedValue
        private int id;
        private String data;

    }
}