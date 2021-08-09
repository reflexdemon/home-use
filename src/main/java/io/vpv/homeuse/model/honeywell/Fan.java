package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "allowedModes",
        "changeableValues",
        "fanRunning"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Fan {

    @JsonProperty("allowedModes")
    public List<String> allowedModes;
    @JsonProperty("changeableValues")
    public ChangeableValues changeableValues;
    @JsonProperty("fanRunning")
    public Boolean fanRunning;

}
