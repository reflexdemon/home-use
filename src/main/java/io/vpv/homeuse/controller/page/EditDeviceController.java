package io.vpv.homeuse.controller.page;


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

import io.vpv.homeuse.model.honeywell.Device;
import io.vpv.homeuse.model.honeywell.Location;
import io.vpv.homeuse.service.HoneywellLocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

import static io.vpv.homeuse.config.security.Constants.LOGGED_IN_USER;
import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;

@Controller
@RequestMapping({"/edit-device.html"})
public class EditDeviceController {
    final HoneywellLocationService locationService;

    public EditDeviceController(HoneywellLocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping
    public Mono<String> editView(Model model, ServerWebExchange serverWebExchange, @RequestParam String deviceID) {


        return getUserFromSession(serverWebExchange)
                .filter(s -> Objects.nonNull(deviceID))
                .flatMap(user -> locationService.getLocations(user)
                        .map(loc -> loc.getLocations().stream()
                                .map(Location::getDevices)
                                .map(devices -> devices.stream()
                                        .filter(device -> deviceID.equalsIgnoreCase(device.getDeviceID()))
                                        .findAny()
                                        .map(device -> {
                                            model.addAttribute("DEVICE", device);
                                            model.addAttribute(LOGGED_IN_USER, loc.getUser());
                                            return device;
                                        }).orElse(new Device())
                                ).collect(Collectors.toList())
                        )
                ).thenReturn("edit-device");
    }
}
