package org.onosproject.pcepio.protocol.ver1;

import java.util.LinkedList;

import org.jboss.netty.buffer.ChannelBuffer;
import org.onosproject.pcepio.exceptions.PcepParseException;
import org.onosproject.pcepio.protocol.PcepErrorInfo;
import org.onosproject.pcepio.protocol.PcepErrorMsg;
import org.onosproject.pcepio.protocol.PcepErrorObject;
import org.onosproject.pcepio.protocol.PcepMessageReader;
import org.onosproject.pcepio.protocol.PcepMessageWriter;
import org.onosproject.pcepio.protocol.PcepOpenObject;
import org.onosproject.pcepio.protocol.PcepType;
import org.onosproject.pcepio.protocol.PcepVersion;
import org.onosproject.pcepio.types.ErrorObjListWithOpen;
import org.onosproject.pcepio.types.PcepObjectHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

public class PcepErrorMsgVer1 implements PcepErrorMsg {

    /*
     * PCE Error message format.

       <PCErr Message>                ::= <Common Header>
                                        ( <error-obj-list> [<Open>] ) | <error>
                                          [<error-list>]

       <error-obj-list>               ::=<PCEP-ERROR>[<error-obj-list>]

       <error>                        ::=[<request-id-list> | <te-id-list>]
                                           <error-obj-list>

       <request-id-list>              ::=<RP>[<request-id-list>]

       <te-id-list>                   ::=<TE>[<te-id-list>]

       <error-list>                   ::=<error>[<error-list>]
     */

    protected static final Logger log = LoggerFactory.getLogger(PcepOpenMsgVer1.class);
    public static final byte PACKET_VERSION = 1;
    public static final int PACKET_MINIMUM_LENGTH = 12;
    public static final PcepType MSG_TYPE = PcepType.ERROR;

    //Below either one should be present.
    private ErrorObjListWithOpen errObjListWithOpen; //optional   ( <error-obj-list> [<Open>] )
    private PcepErrorInfo errInfo; //optional     <error> [<error-list>]

    public static final PcepErrorMsgVer1.Reader READER = new Reader();

    /**
     * constructor to initialize variables.
     */
    public PcepErrorMsgVer1() {
        errObjListWithOpen = null;
        errInfo = null;
    }

    /**
     * Constructor to initialize variables.
     *
     * @param errObjListWithOpen error-object-list with open object
     * @param errInfo error information
     */
    public PcepErrorMsgVer1(ErrorObjListWithOpen errObjListWithOpen, PcepErrorInfo errInfo) {
        this.errObjListWithOpen = errObjListWithOpen;
        this.errInfo = errInfo;
    }

    public static class Reader implements PcepMessageReader<PcepErrorMsg> {

        ErrorObjListWithOpen errObjListWithOpen;
        PcepErrorInfo errInfo;
        PcepObjectHeader tempObjHeader;

        @Override
        public PcepErrorMsg readFrom(ChannelBuffer cb) throws PcepParseException {

            errObjListWithOpen = null;
            errInfo = null;
            tempObjHeader = null;

            if (cb.readableBytes() < PACKET_MINIMUM_LENGTH) {
                throw new PcepParseException("Packet size is less than the minimum length.");
            }

            byte version = cb.readByte();
            version = (byte) (version >> PcepMessageVer1.SHIFT_FLAG);
            if (version != PACKET_VERSION) {
                throw new PcepParseException("Wrong version: Expected=PcepVersion.PCEP_1(1), got=" + version);
            }
            // fixed value property type == 1
            byte type = cb.readByte();
            if (type != MSG_TYPE.getType()) {
                throw new PcepParseException("Wrong type: Expected=PcepType.ERROR(6), got=" + type);
            }
            int length = cb.readShort();
            if (length < PACKET_MINIMUM_LENGTH) {
                throw new PcepParseException(
                        "Wrong length: Expected to be >= " + PACKET_MINIMUM_LENGTH + ", was: " + length);
            }

            //parse <PCErr Message>
            parsePCErrMsg(cb);

            // If other than RP or TE or PCEP-ERROR present then it is error.
            if (0 < cb.readableBytes()) {
                PcepObjectHeader tempObjHeader = PcepObjectHeader.read(cb);
                throw new PcepParseException("Unexpected Object found. Object Class : " + tempObjHeader.getObjClass());
            }

            return new PcepErrorMsgVer1(errObjListWithOpen, errInfo);
        }

        /**
         * Parsing PCErr Message.
         *
         * @param cb channel buffer.
         * @throws PcepParseException if mandatory fields are missing
         * output: this.errObjListWithOpen, this.errInfo
         */
        public void parsePCErrMsg(ChannelBuffer cb) throws PcepParseException {
            //If PCEP-ERROR list is followed by OPEN Object then store into ErrorObjListWithOpen.
            //     ( <error-obj-list> [<Open>]
            //If PCEP-ERROR list is followed by RP or TE Object then store into errInfo. <error> [<error-list>]
            //If only PCEP-ERROR list is present then store into ErrorObjListWithOpen.
            PcepObjectHeader tempObjHeader;
            LinkedList<PcepErrorObject> llErrObjList;

            if (0 >= cb.readableBytes()) {
                throw new PcepParseException("PCEP-ERROR message came with empty objects.");
            }

            //parse PCEP-ERROR list
            llErrObjList = new LinkedList<PcepErrorObject>();
            tempObjHeader = parseErrorObjectList(llErrObjList, cb);

            //check whether OPEN-OBJECT is present.
            if ((tempObjHeader instanceof PcepObjectHeader)
                    && (tempObjHeader.getObjClass() == PcepOpenObjectVer1.OPEN_OBJ_CLASS)) {

                if (llErrObjList.isEmpty()) {
                    throw new PcepParseException("<error-obj-list> should be present if OPEN-OBJECT exists");
                }

                PcepOpenObject pcepOpenObj = PcepOpenObjectVer1.read(cb);
                this.errObjListWithOpen = new ErrorObjListWithOpen(llErrObjList, pcepOpenObj);

            } else if ((tempObjHeader instanceof PcepObjectHeader) //check whether RP or TE Object is present.
                    && ((tempObjHeader.getObjClass() == PcepRPObjectVer1.RP_OBJ_CLASS)
                            || (tempObjHeader.getObjClass() == PcepTEObjectVer1.TE_OBJ_CLASS))) {

                this.errInfo = new PcepErrorInfoVer1(null, null, llErrObjList);
                this.errInfo.read(cb);

            } else if ((null != llErrObjList) && (!llErrObjList.isEmpty())) {
                //If only PCEP-ERROR list is present then store it in errObjListWithOpen.
                this.errObjListWithOpen = new ErrorObjListWithOpen(llErrObjList);
            } else {
                throw new PcepParseException("Empty PCEP-ERROR message.");
            }
        }

        /**
         * Parse error-obj-list.
         *
         * @param llErrObjList error object list output
         * @param cb channel buffer input
         * @throws PcepParseException if mandatory fields are missing
         * @return error object header
         */
        public PcepObjectHeader parseErrorObjectList(LinkedList<PcepErrorObject> llErrObjList, ChannelBuffer cb)
                throws PcepParseException {
            PcepObjectHeader tempObjHeader = null;

            while (0 < cb.readableBytes()) {
                cb.markReaderIndex();
                tempObjHeader = PcepObjectHeader.read(cb);
                cb.resetReaderIndex();
                if (tempObjHeader.getObjClass() == PcepErrorObjectVer1.ERROR_OBJ_CLASS) {
                    llErrObjList.add(PcepErrorObjectVer1.read(cb));
                } else {
                    break;
                }
            }

            return tempObjHeader;
        }
    }

    /**
     * Builder class for PCEP error message.
     */
    public static class Builder implements PcepErrorMsg.Builder {
        // Pcep error message fields

        private ErrorObjListWithOpen errObjListWithOpen = null; //optional   ( <error-obj-list> [<Open>] )
        private PcepErrorInfo errInfo = null; //optional     <error> [<error-list>]

        @Override
        public PcepVersion getVersion() {
            return PcepVersion.PCEP_1;
        }

        @Override
        public PcepType getType() {
            return PcepType.ERROR;
        }

        @Override
        public PcepErrorMsg build() {
            return new PcepErrorMsgVer1(this.errObjListWithOpen, this.errInfo);
        }

        @Override
        public ErrorObjListWithOpen getErrorObjListWithOpen() {
            return this.errObjListWithOpen;
        }

        @Override
        public Builder setErrorObjListWithOpen(ErrorObjListWithOpen errObjListWithOpen) {
            this.errObjListWithOpen = errObjListWithOpen;
            return this;
        }

        @Override
        public PcepErrorInfo getPcepErrorInfo() {
            return this.errInfo;
        }

        @Override
        public Builder setPcepErrorInfo(PcepErrorInfo errInfo) {
            this.errInfo = errInfo;
            return this;
        }
    }

    @Override
    public void writeTo(ChannelBuffer cb) throws PcepParseException {
        WRITER.write(cb, this);
    }

    public static final Writer WRITER = new Writer();

    static class Writer implements PcepMessageWriter<PcepErrorMsgVer1> {
        @Override
        public void write(ChannelBuffer cb, PcepErrorMsgVer1 message) throws PcepParseException {
            int startIndex = cb.writerIndex();
            // first 3 bits set to version
            cb.writeByte((byte) (PACKET_VERSION << PcepMessageVer1.SHIFT_FLAG));
            // message type 0xC
            cb.writeByte(MSG_TYPE.getType());
            // length is length of variable message, will be updated at the end
            // Store the position of message
            // length in buffer
            int msgLenIndex = cb.writerIndex();
            cb.writeShort(0);
            ErrorObjListWithOpen errObjListWithOpen = message.getErrorObjListWithOpen();
            PcepErrorInfo errInfo = message.getPcepErrorInfo();

            // write ( <error-obj-list> [<Open>] ) if exists.
            // otherwise write <error> [<error-list>]

            if ((errObjListWithOpen instanceof ErrorObjListWithOpen)
                    && (errObjListWithOpen.isErrorObjListWithOpenPresent())) {
                errObjListWithOpen.write(cb);
            } else if ((errInfo instanceof PcepErrorInfo) && (errInfo.isErrorInfoPresent())) {
                errInfo.write(cb);
            } else {
                throw new PcepParseException("Empty PCEP-ERROR message.");
            }
            // PcepErrorMessage message length field
            int length = cb.writerIndex() - startIndex;
            cb.setShort(msgLenIndex, (short) length);
        }
    }

    @Override
    public PcepVersion getVersion() {
        return PcepVersion.PCEP_1;
    }

    @Override
    public PcepType getType() {
        return MSG_TYPE;
    }

    @Override
    public ErrorObjListWithOpen getErrorObjListWithOpen() {
        return this.errObjListWithOpen;
    }

    @Override
    public void setErrorObjListWithOpen(ErrorObjListWithOpen errObjListWithOpen) {
        this.errObjListWithOpen = errObjListWithOpen;
    }

    @Override
    public PcepErrorInfo getPcepErrorInfo() {
        return this.errInfo;
    }

    @Override
    public void setPcepErrorInfo(PcepErrorInfo errInfo) {
        this.errInfo = errInfo;
    }

    /**
     * Return list of Error types.
     *
     * @return error types list
     */
    public LinkedList<Integer> getErrorType() {
        LinkedList<Integer> llErrorType = new LinkedList<Integer>();
        if ((errObjListWithOpen instanceof ErrorObjListWithOpen)
                && (errObjListWithOpen.isErrorObjListWithOpenPresent())) {
            llErrorType = errObjListWithOpen.getErrorType();
        } else if ((errInfo instanceof PcepErrorInfo) && (errInfo.isErrorInfoPresent())) {
            llErrorType = errInfo.getErrorType();
        }

        return llErrorType;
    }

    /**
     * Return list of Error values.
     *
     * @return error value list
     */
    public LinkedList<Integer> getErrorValue() {
        LinkedList<Integer> llErrorValue = new LinkedList<Integer>();
        if ((errObjListWithOpen instanceof ErrorObjListWithOpen)
                && (errObjListWithOpen.isErrorObjListWithOpenPresent())) {
            llErrorValue = errObjListWithOpen.getErrorValue();
        } else if ((errInfo instanceof PcepErrorInfo) && (errInfo.isErrorInfoPresent())) {
            llErrorValue = errInfo.getErrorValue();
        }

        return llErrorValue;
    }

    @Override
    public String toString() {
        ToStringHelper toStrHelper = MoreObjects.toStringHelper(getClass());

        if ((errObjListWithOpen instanceof ErrorObjListWithOpen)
                && (errObjListWithOpen.isErrorObjListWithOpenPresent())) {
            toStrHelper.add("ErrorObjectListWithOpen", errObjListWithOpen);
        }
        if ((errInfo instanceof PcepErrorInfo) && (errInfo.isErrorInfoPresent())) {
            toStrHelper.add("ErrorInfo", errInfo);
        }

        return toStrHelper.toString();
    }
}
