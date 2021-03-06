/*
 * Copyright 2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.incubator.net.intf.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onlab.packet.IpAddress;
import org.onlab.packet.VlanId;
import org.onosproject.net.config.NetworkConfigEvent;
import org.onosproject.net.config.NetworkConfigListener;
import org.onosproject.net.config.NetworkConfigService;
import org.onosproject.incubator.net.config.basics.InterfaceConfig;
import org.onosproject.incubator.net.intf.Interface;
import org.onosproject.incubator.net.intf.InterfaceService;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.Device;
import org.onosproject.net.Port;
import org.onosproject.net.device.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * Manages the inventory of interfaces in the system.
 */
@Service
@Component(immediate = true)
public class InterfaceManager implements InterfaceService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected NetworkConfigService configService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected DeviceService deviceService;

    private final InternalConfigListener listener = new InternalConfigListener();

    private final Map<ConnectPoint, Set<Interface>> interfaces = Maps.newConcurrentMap();

    @Activate
    public void activate() {
        configService.addListener(listener);

        for (Device d : deviceService.getDevices()) {
            for (Port p : deviceService.getPorts(d.id())) {
                InterfaceConfig config =
                    configService.getConfig(new ConnectPoint(d.id(), p.number()), InterfaceConfig.class);

                if (config != null) {
                    updateInterfaces(config);
                }
            }
        }

        log.info("Started");
    }

    @Deactivate
    public void deactivate() {
        configService.removeListener(listener);

        log.info("Stopped");
    }

    @Override
    public Set<Interface> getInterfaces() {
        return interfaces.values()
                .stream()
                .flatMap(set -> set.stream())
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));
    }

    @Override
    public Set<Interface> getInterfacesByPort(ConnectPoint port) {
        return ImmutableSet.copyOf(interfaces.get(port));
    }

    @Override
    public Set<Interface> getInterfacesByIp(IpAddress ip) {
        return interfaces.values()
                .stream()
                .flatMap(set -> set.stream())
                .filter(intf -> intf.ipAddresses().contains(ip))
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));
    }

    @Override
    public Interface getMatchingInterface(IpAddress ip) {
        Optional<Interface> match = interfaces.values()
                .stream()
                .flatMap(set -> set.stream())
                .filter(intf -> intf.ipAddresses()
                        .stream()
                        .anyMatch(intfIp -> intfIp.subnetAddress().contains(ip)))
                .findFirst();

        if (match.isPresent()) {
            return match.get();
        }

        return null;
    }

    @Override
    public Set<Interface> getInterfacesByVlan(VlanId vlan) {
        return interfaces.values()
                .stream()
                .flatMap(set -> set.stream())
                .filter(intf -> intf.vlan().equals(vlan))
                .collect(collectingAndThen(toSet(), ImmutableSet::copyOf));
    }

    private void updateInterfaces(InterfaceConfig intfConfig) {
        interfaces.put(intfConfig.subject(), intfConfig.getInterfaces());
    }

    private void removeInterfaces(ConnectPoint port) {
        interfaces.remove(port);
    }

    /**
     * Listener for network config events.
     */
    private class InternalConfigListener implements NetworkConfigListener {

        @Override
        public void event(NetworkConfigEvent event) {
            switch (event.type()) {
            case CONFIG_ADDED:
            case CONFIG_UPDATED:
                if (event.configClass() == InterfaceConfig.class) {
                    InterfaceConfig config =
                            configService.getConfig((ConnectPoint) event.subject(), InterfaceConfig.class);
                    updateInterfaces(config);
                }
                break;
            case CONFIG_REMOVED:
                if (event.configClass() == InterfaceConfig.class) {
                    removeInterfaces((ConnectPoint) event.subject());
                }
                break;
            case CONFIG_REGISTERED:
            case CONFIG_UNREGISTERED:
            default:
                break;
            }
        }
    }
}
