package server.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpbitTickerEventDto {

    private String market;

    @JsonProperty("opening_price")
    private double openingPrice;

    @JsonProperty("high_price")
    private double highPrice;

    @JsonProperty("low_price")
    private double lowPrice;

    @JsonProperty("trade_price")
    private double tradePrice;

    @JsonProperty("prev_closing_price")
    private double prevClosingPrice;

    @JsonProperty("change")
    private String change;

    @JsonProperty("signed_change_price")
    private double signedChangePrice;

    @JsonProperty("signed_change_rate")
    private double signedChangeRate;

    @JsonProperty("acc_trade_price")
    private double accTradePrice;

    @JsonProperty("acc_trade_volume")
    private double accTradeVolume;

    @JsonProperty("acc_trade_price_24h")
    private double accTradePrice24h;

    @JsonProperty("timestamp")
    private long timestamp;
}