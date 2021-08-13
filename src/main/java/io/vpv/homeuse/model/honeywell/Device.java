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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "thermostatVersion",
        "scheduleStatus",
        "allowedTimeIncrements",
        "settings",
        "deviceClass",
        "deviceType",
        "deviceID",
        "deviceInternalID",
        "userDefinedDeviceName",
        "name",
        "schedule",
        "isAlive",
        "isUpgrading",
        "isProvisioned",
        "macID",
        "deviceSettings",
        "service",
        "deviceRegistrationDate",
        "dataSyncStatus",
        "units",
        "indoorTemperature",
        "outdoorTemperature",
        "allowedModes",
        "deadband",
        "hasDualSetpointStatus",
        "minHeatSetpoint",
        "maxHeatSetpoint",
        "minCoolSetpoint",
        "maxCoolSetpoint",
        "changeableValues",
        "operationStatus",
        "smartAway",
        "indoorHumidity",
        "indoorHumidityStatus",
        "deviceModel"
})
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @JsonProperty("thermostatVersion")
    public String thermostatVersion;
    @JsonProperty("scheduleStatus")
    public String scheduleStatus;
    @JsonProperty("allowedTimeIncrements")
    public Integer allowedTimeIncrements;
    @JsonProperty("settings")
    public Settings settings;
    @JsonProperty("deviceClass")
    public String deviceClass;
    @JsonProperty("deviceType")
    public String deviceType;
    @JsonProperty("deviceID")
    public String deviceID;
    @JsonProperty("deviceInternalID")
    public Integer deviceInternalID;
    @JsonProperty("userDefinedDeviceName")
    public String userDefinedDeviceName;
    @JsonProperty("name")
    public String name;
    @JsonProperty("schedule")
    public Schedule schedule;
    @JsonProperty("isAlive")
    public Boolean isAlive;
    @JsonProperty("isUpgrading")
    public Boolean isUpgrading;
    @JsonProperty("isProvisioned")
    public Boolean isProvisioned;
    @JsonProperty("macID")
    public String macID;
    @JsonProperty("deviceSettings")
    public DeviceSettings deviceSettings;
    @JsonProperty("service")
    public Service service;
    @JsonProperty("deviceRegistrationDate")
    public String deviceRegistrationDate;
    @JsonProperty("dataSyncStatus")
    public String dataSyncStatus;
    @JsonProperty("units")
    public String units;
    @JsonProperty("indoorTemperature")
    public Integer indoorTemperature;
    @JsonProperty("outdoorTemperature")
    public Integer outdoorTemperature;
    @JsonProperty("allowedModes")
    public List<String> allowedModes;
    @JsonProperty("deadband")
    public Integer deadband;
    @JsonProperty("hasDualSetpointStatus")
    public Boolean hasDualSetpointStatus;
    @JsonProperty("minHeatSetpoint")
    public Integer minHeatSetpoint;
    @JsonProperty("maxHeatSetpoint")
    public Integer maxHeatSetpoint;
    @JsonProperty("minCoolSetpoint")
    public Integer minCoolSetpoint;
    @JsonProperty("maxCoolSetpoint")
    public Integer maxCoolSetpoint;
    @JsonProperty("changeableValues")
    public ChangeableValues changeableValues;
    @JsonProperty("operationStatus")
    public OperationStatus operationStatus;
    @JsonProperty("smartAway")
    public SmartAway smartAway;
    @JsonProperty("indoorHumidity")
    public Integer indoorHumidity;
    @JsonProperty("indoorHumidityStatus")
    public String indoorHumidityStatus;
    @JsonProperty("deviceModel")
    public String deviceModel;

}
