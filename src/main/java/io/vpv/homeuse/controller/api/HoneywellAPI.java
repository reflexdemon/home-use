package io.vpv.homeuse.controller.api;


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

import io.vpv.homeuse.model.honeywell.Location;
import io.vpv.homeuse.service.HoneywellLocationService;
import io.vpv.homeuse.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.vpv.homeuse.util.SessionUtil.getUserFromSession;
import static io.vpv.homeuse.util.SessionUtil.setUserToSession;

@RestController
public class HoneywellAPI {
    final
    UserService userService;
    private final HoneywellLocationService locationService;

    public HoneywellAPI(UserService userService, HoneywellLocationService locationService) {
        this.userService = userService;
        this.locationService = locationService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/honeywell/locations")
    public Mono<List<Location>> getLocations(ServerWebExchange serverWebExchange) {

        return getUserFromSession(serverWebExchange)
                .flatMap(locationService::getLocations)
                .flatMap(
                        apiResponse -> setUserToSession(serverWebExchange, apiResponse.getUser())
                                .thenReturn(apiResponse.getLocations())
                );
    }
}
