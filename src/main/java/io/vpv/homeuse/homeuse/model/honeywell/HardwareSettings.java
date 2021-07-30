package io.vpv.homeuse.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "brightness",
        "volume",
        "maxBrightness",
        "maxVolume"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HardwareSettings {

    @JsonProperty("brightness")
    public Integer brightness;
    @JsonProperty("volume")
    public Integer volume;
    @JsonProperty("maxBrightness")
    public Integer maxBrightness;
    @JsonProperty("maxVolume")
    public Integer maxVolume;

}
