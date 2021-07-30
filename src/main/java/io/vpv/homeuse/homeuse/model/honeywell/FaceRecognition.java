package io.vpv.homeuse.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "enabled",
        "maxPersons",
        "maxEtas",
        "maxEtaPersons",
        "schedules"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FaceRecognition {

    @JsonProperty("enabled")
    public Boolean enabled;
    @JsonProperty("maxPersons")
    public Integer maxPersons;
    @JsonProperty("maxEtas")
    public Integer maxEtas;
    @JsonProperty("maxEtaPersons")
    public Integer maxEtaPersons;
    @JsonProperty("schedules")
    public List<Schedule> schedules = null;

}
