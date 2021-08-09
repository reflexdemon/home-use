package io.vpv.homeuse.model;

import io.vpv.homeuse.model.honeywell.Location;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@With
public class APIResponseData {
    User user;
    List<Location> locations;
}
