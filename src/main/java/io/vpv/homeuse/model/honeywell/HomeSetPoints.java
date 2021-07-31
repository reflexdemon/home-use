package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "homeHeatSP",
        "homeCoolSP",
        "units"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HomeSetPoints {

    @JsonProperty("homeHeatSP")
    public Integer homeHeatSP;
    @JsonProperty("homeCoolSP")
    public Integer homeCoolSP;
    @JsonProperty("units")
    public String units;

}
