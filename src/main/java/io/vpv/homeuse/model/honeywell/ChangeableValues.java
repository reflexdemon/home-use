package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mode",
        "autoChangeoverActive",
        "heatSetpoint",
        "coolSetpoint",
        "heatCoolMode"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ChangeableValues {

    @JsonProperty("mode")
    public String mode;
    @JsonProperty("autoChangeoverActive")
    public Boolean autoChangeoverActive;
    @JsonProperty("heatSetpoint")
    public Integer heatSetpoint;
    @JsonProperty("coolSetpoint")
    public Integer coolSetpoint;
    @JsonProperty("heatCoolMode")
    public String heatCoolMode;

}
