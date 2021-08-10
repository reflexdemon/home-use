package io.vpv.homeuse.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "audit_log")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Log {
    @Id
    private String id;
    private String action;
    private String username;
    private Date timestamp;
    private Map<String, String> attributes;
    private Map<String, String> headers;

}
