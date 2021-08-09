package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "days",
        "scheduleType",
        "scheduleSubType"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @JsonProperty("time")
    public List<Time> time;
    @JsonProperty("days")
    public List<String> days;
    @JsonProperty("scheduleType")
    public String scheduleType;
    @JsonProperty("scheduleSubType")
    public String scheduleSubType;

}
