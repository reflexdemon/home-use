package io.vpv.homeuse.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "locationID",
        "role",
        "locationName",
        "status"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LocationRoleMapping {

    @JsonProperty("locationID")
    public Integer locationID;
    @JsonProperty("role")
    public String role;
    @JsonProperty("locationName")
    public String locationName;
    @JsonProperty("status")
    public Integer status;

}
