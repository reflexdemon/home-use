package io.vpv.homeuse.model.honeywell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "locationID",
        "name",
        "streetAddress",
        "city",
        "state",
        "country",
        "zipcode",
        "devices",
        "users",
        "timeZoneId",
        "timeZone",
        "ianaTimeZone",
        "daylightSavingTimeEnabled",
        "geoFenceEnabled",
        "predictiveAIREnabled",
        "comfortLevel",
        "geoFenceNotificationEnabled",
        "geoFenceNotificationTypeId",
        "configuration",
        "stateName"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @JsonProperty("locationID")
    public Integer locationID;
    @JsonProperty("name")
    public String name;
    @JsonProperty("streetAddress")
    public String streetAddress;
    @JsonProperty("city")
    public String city;
    @JsonProperty("state")
    public String state;
    @JsonProperty("country")
    public String country;
    @JsonProperty("zipcode")
    public String zipcode;
    @JsonProperty("devices")
    public List<Device> devices;
    @JsonProperty("users")
    public List<User> users;
    @JsonProperty("timeZoneId")
    public String timeZoneId;
    @JsonProperty("timeZone")
    public String timeZone;
    @JsonProperty("ianaTimeZone")
    public String ianaTimeZone;
    @JsonProperty("daylightSavingTimeEnabled")
    public Boolean daylightSavingTimeEnabled;
    @JsonProperty("geoFenceEnabled")
    public Boolean geoFenceEnabled;
    @JsonProperty("predictiveAIREnabled")
    public Boolean predictiveAIREnabled;
    @JsonProperty("comfortLevel")
    public Integer comfortLevel;
    @JsonProperty("geoFenceNotificationEnabled")
    public Boolean geoFenceNotificationEnabled;
    @JsonProperty("geoFenceNotificationTypeId")
    public Integer geoFenceNotificationTypeId;
    @JsonProperty("configuration")
    public Configuration configuration;
    @JsonProperty("stateName")
    public String stateName;
}
