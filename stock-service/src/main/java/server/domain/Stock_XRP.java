package server.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "stock_xrp", indexes = {
        @Index(name = "IDX_timestamp", columnList = "timestamp")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stock_XRP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String type;
    String code;

    @Column(name = "opening_price")
    Double openingPrice;

    @Column(name = "high_price")
    Double highPrice;

    @Column(name = "low_price")
    Double lowPrice;

    @Column(name = "trade_price")
    Double tradePrice;

    @Column(name = "prev_closing_price")
    Double prevClosingPrice;

    @Column(name = "changed_price")
    String changed_price;

    @Column(name = "signed_change_price")
    Double signedChangePrice;

    @Column(name = "signed_change_rate")
    Double signedChangeRate;

    @Column(name = "acc_trade_price")
    Double accTradePrice;

    @Column(name = "acc_trade_volume")
    Double accTradeVolume;

    @Column(name = "acc_trade_price_24h")
    Double accTradePrice24h;

    @Column(name = "timestamp")
    Long timestamp;
}