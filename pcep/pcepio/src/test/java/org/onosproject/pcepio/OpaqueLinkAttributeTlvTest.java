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
package org.onosproject.pcepio;

import org.junit.Test;
import org.onosproject.pcepio.types.OpaqueLinkAttributeTlv;

import com.google.common.testing.EqualsTester;

/**
 * Test of the OpaqueLinkAttributeTlv.
 */
public class OpaqueLinkAttributeTlvTest {
    private final byte[] rawValue1 = new byte[] {0x01, 0x02};
    private final byte[] rawValue2 = new byte[] {0x14, 0x15};

    private final OpaqueLinkAttributeTlv tlv1 = new OpaqueLinkAttributeTlv(rawValue1, (short) rawValue1.length);
    private final OpaqueLinkAttributeTlv sameAsTlv1 = OpaqueLinkAttributeTlv.of(tlv1.getValue(), tlv1.getLength());
    private final OpaqueLinkAttributeTlv tlv2 = new OpaqueLinkAttributeTlv(rawValue2, (short) 0);

    @Test
    public void basics() {
        new EqualsTester()
        .addEqualityGroup(tlv1, sameAsTlv1)
        .addEqualityGroup(tlv2)
        .testEquals();
    }
}
