package io.vpv.homeuse.model.honeywell;


/******************************************************************************
 * Copyright 2021 reflexdemon                                                 *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a    *
 * copy of this software and associated documentation files (the "Software"), *
 * to deal in the Software without restriction, including without limitation  *
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,   *
 * and/or sell copies of the Software, and to permit persons to whom the      *
 * Software is furnished to do so, subject to the following conditions:       *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included    *
 * in all copies or substantial portions of the Software.                     *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS    *
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,*
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL    *
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING    *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER        *
 * DEALINGS IN THE SOFTWARE.                                                  *
 ******************************************************************************/

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
