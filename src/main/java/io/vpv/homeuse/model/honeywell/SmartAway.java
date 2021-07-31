package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "active",
        "timeOfDay",
        "durationInHours",
        "durationInDays",
        "lastUsedFormat",
        "endsIn"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SmartAway {

    @JsonProperty("active")
    public Boolean active;
    @JsonProperty("timeOfDay")
    public String timeOfDay;
    @JsonProperty("durationInHours")
    public Integer durationInHours;
    @JsonProperty("durationInDays")
    public Integer durationInDays;
    @JsonProperty("lastUsedFormat")
    public String lastUsedFormat;
    @JsonProperty("endsIn")
    public String endsIn;

}
