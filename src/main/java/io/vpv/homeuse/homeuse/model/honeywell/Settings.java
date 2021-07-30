package io.vpv.homeuse.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "homeSetPoints",
        "awaySetPoints",
        "hardwareSettings",
        "fan",
        "temperatureMode",
        "specialMode"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    @JsonProperty("homeSetPoints")
    public HomeSetPoints homeSetPoints;
    @JsonProperty("awaySetPoints")
    public AwaySetPoints awaySetPoints;
    @JsonProperty("hardwareSettings")
    public HardwareSettings hardwareSettings;
    @JsonProperty("fan")
    public Fan fan;
    @JsonProperty("temperatureMode")
    public TemperatureMode temperatureMode;
    @JsonProperty("specialMode")
    public SpecialMode specialMode;

}
