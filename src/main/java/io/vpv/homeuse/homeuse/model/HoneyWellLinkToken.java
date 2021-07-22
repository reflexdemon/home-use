package io.vpv.homeuse.homeuse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HoneyWellLinkToken {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("refresh_token")
    String refreshToken;
    @JsonProperty("expires_in")
    String expiresIn;
    @JsonProperty("token_type")
    String tokenType;
}
