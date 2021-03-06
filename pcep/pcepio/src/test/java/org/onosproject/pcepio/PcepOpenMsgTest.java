/*
 * Copyright 2014-2015 Open Networking Laboratory
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

import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onosproject.pcepio.exceptions.PcepParseException;
import org.onosproject.pcepio.protocol.PcepFactories;
import org.onosproject.pcepio.protocol.PcepMessage;
import org.onosproject.pcepio.protocol.PcepMessageReader;
import org.onosproject.pcepio.protocol.PcepOpenMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test cases for PCEP OPEN Message.
 */
public class PcepOpenMsgTest {

    protected static final Logger log = LoggerFactory.getLogger(PcepOpenMsgTest.class);

    @Before
    public void startUp() {
    }

    @After
    public void tearDown() {

    }

    @Test
    public void openMessageTest1() throws PcepParseException {

        // OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV, )
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, 0x01, 0x10, 0x00, 0x20, 0x20, 0x1e, 0x78, (byte) 0xbd,
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f, //STATEFUL-PCE-CAPABILITY
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, //GMPLS-CAPABILITY-TLV
                0x00, 0x20, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, //PCECC-CAPABILITY-TLV
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest2() throws PcepParseException {

        // OPEN OBJECT (STATEFUL-PCE-CAPABILITY).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x14, // common header
                0x01, 0x10, 0x00, 0x10, // common object header
                0x20, 0x1E, 0x78, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f}; // STATEFUL-PCE-CAPABILITY
        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest3() throws PcepParseException {

        // OPEN OBJECT (GmplsCapability).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x14, // common header
                0x01, 0x10, 0x00, 0x10, // common object header
                0x20, 0x1E, 0x78, 0x01, // OPEN object
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00}; //GMPLS-CAPABILITY-TLV

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest4() throws PcepParseException {

        // OPEN OBJECT (StatefulLspDbVerTlv).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x18,
                0x01, 0x10, 0x00, 0x14, 0x20, 0x1e, 0x78, 0x20,
                0x00, 0x17, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02 }; //StatefulLspDbVerTlv

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest5() throws PcepParseException {

        // OPEN OBJECT (no Tlvs).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x0C,
                0x01, 0x10, 0x00, 0x08, 0x20, 0x1e, 0x78, (byte) 0xbd }; // no Tlvs in open messsage

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest6() throws PcepParseException {

        /* OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV) with
        p bit not set & i bit set.
         */
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, 0x01, 0x11, 0x00, 0x20, //p bit not set & i bit set
                0x20, 0x1e, 0x78, (byte) 0xbd,
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f, // STATEFUL-PCE-CAPABILITY
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, //GMPLS-CAPABILITY-TLV
                0x00, 0x20, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, //PCECC-CAPABILITY-TLV
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest7() throws PcepParseException {

        /* OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV)
        with p bit set & i bit not set.
         */
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, 0x01, 0x12, 0x00, 0x20, //p bit set & i bit not set
                0x20, 0x1e, 0x78, (byte) 0xbd,
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f, //STATEFUL-PCE-CAPABILITY
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, //GMPLS-CAPABILITY-TLV
                0x00, 0x20, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, //PCECC-CAPABILITY-TLV
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest8() throws PcepParseException {

        /* OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV)
        with p bit set & i bit set.
         */
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, 0x01, 0x13, 0x00, 0x20, //p bit set & i bit set
                0x20, 0x1e, 0x78, (byte) 0xbd,
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f, //STATEFUL-PCE-CAPABILITY
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, //GMPLS-CAPABILITY-TLV
                0x00, 0x20, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, //PCECC-CAPABILITY-TLV
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest9() throws PcepParseException {

        /* OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV)
        with p bit set & i bit set.
         */
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, 0x01, 0x13, 0x00, 0x20, //p bit set & i bit set
                0x20, 0x1e, 0x78, 0x00, //invalid sessionID
                0x00, 0x10, 0x00, 0x04, 0x00, 0x00, 0x00, 0x0f, //STATEFUL-PCE-CAPABILITY
                0x00, 0x0e, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, //GMPLS-CAPABILITY-TLV
                0x00, 0x20, 0x00, 0x04, 0x00, 0x00, 0x00, 0x07, //PCECC-CAPABILITY-TLV
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest10() throws PcepParseException {

        //OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x1C, // common header
                0x01, 0x10, 0x00, 0x18, // common object header
                0x20, 0x05, 0x1E, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, // STATEFUL-PCE-CAPABILITY
                0x00, 0x00, 0x00, 0x05,
                0x00, 0x0E, 0x00, 0x04, // GMPLS-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x00};

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest11() throws PcepParseException {

        //OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV, TED Capability TLV).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x2C, // common header
                0x01, 0x10, 0x00, 0x28, // common object header
                0x20, 0x05, 0x1E, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, // STATEFUL-PCE-CAPABILITY
                0x00, 0x00, 0x00, 0x05, 0x00, 0x0E, 0x00, 0x04, // GMPLS-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, // PCECC-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x03, 0x00, (byte) 0x84, 0x00, 0x04, // TED Capability TLV
                0x00, 0x00, 0x00, 0x00 };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest12() throws PcepParseException {

        //OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV, PCECC-CAPABILITY-TLV).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x24, // common header
                0x01, 0x10, 0x00, 0x20, // common object header
                0x20, 0x05, 0x1E, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, // STATEFUL-PCE-CAPABILITY
                0x00, 0x00, 0x00, 0x05, 0x00, 0x0E, 0x00, 0x04, // GMPLS-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x00, 0x04, // PCECC-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x03};

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest13() throws PcepParseException {

        //OPEN OBJECT (STATEFUL-PCE-CAPABILITY, GMPLS-CAPABILITY-TLV).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x1c, // common header
                0x01, 0x10, 0x00, 0x18, // common object header
                0x20, 0x05, 0x1E, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, // STATEFUL-PCE-CAPABILITY
                0x00, 0x00, 0x00, 0x05, 0x00, 0x0E, 0x00, 0x04, // GMPLS-CAPABILITY-TLV
                0x00, 0x00, 0x00, 0x00};

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed ");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest14() throws PcepParseException {

        //OPEN OBJECT (STATEFUL-PCE-CAPABILITY).
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x14, // common header
                0x01, 0x10, 0x00, 0x10, // common object header
                0x20, 0x05, 0x1E, 0x01, // OPEN object
                0x00, 0x10, 0x00, 0x04, // STATEFUL-PCE-CAPABILITY
                0x00, 0x00, 0x00, 0x05};

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }

    @Test
    public void openMessageTest15() throws PcepParseException {

        // OPEN OBJECT.
        byte[] openMsg = new byte[] {0x20, 0x01, 0x00, 0x0c, // common header
                0x01, 0x10, 0x00, 0x08, // common object header
                0x20, 0x05, 0x1E, 0x01 // OPEN object
        };

        byte[] testOpenMsg = {0};
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        buffer.writeBytes(openMsg);

        PcepMessageReader<PcepMessage> reader = PcepFactories.getGenericReader();
        PcepMessage message = null;
        try {
            message = reader.readFrom(buffer);
        } catch (PcepParseException e) {
            e.printStackTrace();
        }

        if (message instanceof PcepOpenMsg) {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            message.writeTo(buf);
            testOpenMsg = buf.array();

            int iReadLen = buf.writerIndex() - 0;
            testOpenMsg = new byte[iReadLen];
            buf.readBytes(testOpenMsg, 0, iReadLen);

            if (Arrays.equals(openMsg, testOpenMsg)) {
                Assert.assertArrayEquals(openMsg, testOpenMsg);
                log.debug("openMsg are equal :" + openMsg);
            } else {
                Assert.fail("test case failed");
                log.debug("not equal");
            }
        } else {
            Assert.fail("test case failed");
            log.debug("not equal");
        }
    }
}
