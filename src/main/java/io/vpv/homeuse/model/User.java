package io.vpv.homeuse.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "app_users")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@With
public class User {

    @Id
    private String id;

    @Indexed
    private String username;

    @Indexed
    private String email;
    private String source;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String profileUrl;
    private Map userAttrMap;
    private HoneyWellLinkToken honeyWellLinkToken;
}
