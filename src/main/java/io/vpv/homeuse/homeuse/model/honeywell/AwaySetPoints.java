package io.vpv.homeuse.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "awayHeatSP",
        "awayCoolSP",
        "smartCoolSP",
        "smartHeatSP",
        "useAutoSmart",
        "units"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AwaySetPoints {

    @JsonProperty("awayHeatSP")
    public Integer awayHeatSP;
    @JsonProperty("awayCoolSP")
    public Integer awayCoolSP;
    @JsonProperty("smartCoolSP")
    public Integer smartCoolSP;
    @JsonProperty("smartHeatSP")
    public Integer smartHeatSP;
    @JsonProperty("useAutoSmart")
    public Boolean useAutoSmart;
    @JsonProperty("units")
    public String units;

}
