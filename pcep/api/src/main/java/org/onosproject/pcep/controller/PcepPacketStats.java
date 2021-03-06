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
package org.onosproject.pcep.controller;

/**
 * The representation for PCEP packet statistics.
 */
public interface PcepPacketStats {

    /**
     * Returns the count for no of packets sent out.
     *
     * @return int value of no of packets sent
     */
    public int outPacketCount();

    /**
     * Returns the count for no of packets received.
     *
     * @return int value of no of packets sent
     */
    public int inPacketCount();

    /**
     * Returns the count for no of wrong packets received.
     *
     * @return int value of no of wrong packets received
     */
    public int wrongPacketCount();

    /**
     * Increments the received packet counter.
     */
    public void addInPacket();

    /**
     * Increments the sent packet counter.
     */
    public void addOutPacket();

    /**
     * Increments the sent packet counter by specified value.
     * @param value of no of packets sent
     */
    public void addOutPacket(int value);

    /**
     * Increments the wrong packet counter.
     */
    public void addWrongPacket();

    /**
     * Returns the time value.
     *
     * @return long value of time
     */
    public long getTime();

    /**
     * Sets the time value.
     *
     * @param time long value of time
     */
    public void setTime(long time);

}
