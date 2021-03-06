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
package org.onosproject.pcepio.types;

import java.util.Objects;

import org.jboss.netty.buffer.ChannelBuffer;
import org.onosproject.pcepio.protocol.PcepVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

/**
 * Provides TED Capability Tlv.
 */
public class TedCapabilityTlv implements PcepValueType {

    /*
     * Reference :PCEP Extension for Transporting TE Data draft-dhodylee-pce-pcep-te-data-extn-02
     *  0                   1                   2                   3
        0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
       +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
       |               Type=[TBD5]     |            Length=4           |
       +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
       |                             Flags                           |R|
       +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     */

    protected static final Logger log = LoggerFactory.getLogger(TedCapabilityTlv.class);

    public static final short TYPE = 132; //TODO: need to change this TBD5
    public static final short LENGTH = 4;
    public static final int SET = 1;
    public static final byte RFLAG_CHECK = 0x01;

    private final boolean bRFlag;
    private final int rawValue;
    private final boolean isRawValueSet;

    /**
     * Constructor to initialize raw Value.
     *
     * @param rawValue Flags
     */
    public TedCapabilityTlv(final int rawValue) {
        this.rawValue = rawValue;
        this.isRawValueSet = true;
        int temp = rawValue;
        temp = temp & RFLAG_CHECK;
        if (temp == SET) {
            this.bRFlag = true;
        } else {
            this.bRFlag = false;
        }

    }

    /**
     * Constructor to initialize bRFlag.
     *
     * @param bRFlag R-flag
     */
    public TedCapabilityTlv(boolean bRFlag) {
        this.bRFlag = bRFlag;
        this.rawValue = 0;
        this.isRawValueSet = false;
    }

    /**
     * Returns R-flag.
     *
     * @return bRFlag
     */
    public boolean getbRFlag() {
        return bRFlag;
    }

    /**
     * Returns an object of TedCapabilityTlv.
     *
     * @param raw value Flags
     * @return object of TedCapabilityTlv
     */
    public static TedCapabilityTlv of(final int raw) {
        return new TedCapabilityTlv(raw);
    }

    @Override
    public PcepVersion getVersion() {
        return PcepVersion.PCEP_1;
    }

    public int getInt() {
        return rawValue;
    }

    @Override
    public short getType() {
        return TYPE;
    }

    @Override
    public short getLength() {
        return LENGTH;
    }

    @Override
    public int hashCode() {
        if (isRawValueSet) {
            return Objects.hash(rawValue);
        } else {
            return Objects.hash(bRFlag);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TedCapabilityTlv) {
            TedCapabilityTlv other = (TedCapabilityTlv) obj;
            if (isRawValueSet) {
                return Objects.equals(this.rawValue, other.rawValue);
            } else {
                return Objects.equals(this.bRFlag, other.bRFlag);
            }
        }
        return false;
    }

    @Override
    public int write(ChannelBuffer c) {
        int iStartIndex = c.writerIndex();
        int temp = 0;
        c.writeShort(TYPE);
        c.writeShort(LENGTH);
        if (isRawValueSet) {
            c.writeInt(rawValue);
        } else {
            if (bRFlag) {
                temp = temp | RFLAG_CHECK;
            }
            c.writeInt(temp);
        }
        return c.writerIndex() - iStartIndex;
    }

    /**
     * Reads channel buffer and returns object of TedCapabilityTlv.
     *
     * @param c input channel buffer
     * @return object of TedCapabilityTlv
     */
    public static TedCapabilityTlv read(ChannelBuffer c) {
        return TedCapabilityTlv.of(c.readInt());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("Type", TYPE).add("Length", LENGTH).add("Value", rawValue)
                .toString();
    }
}
