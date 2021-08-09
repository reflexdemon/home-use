package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userID",
        "username",
        "firstname",
        "lastname",
        "created",
        "deleted",
        "activated",
        "connectedHomeAccountExists",
        "locationRoleMapping",
        "isOptOut",
        "isCurrentUser"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @JsonProperty("userID")
    private Integer userID;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("created")
    private Integer created;
    @JsonProperty("deleted")
    private Long deleted;
    @JsonProperty("activated")
    private Boolean activated;
    @JsonProperty("connectedHomeAccountExists")
    private Boolean connectedHomeAccountExists;
    @JsonProperty("locationRoleMapping")
    private List<LocationRoleMapping> locationRoleMapping;
    @JsonProperty("isOptOut")
    private String isOptOut;
    @JsonProperty("isCurrentUser")
    private Boolean isCurrentUser;

}
