/********************************************************************

The Multiverse Platform is made available under the MIT License.

Copyright (c) 2012 The Multiverse Foundation

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, 
merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software 
is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
OR OTHER DEALINGS IN THE SOFTWARE.

*********************************************************************/

package multiverse.server.events;

import multiverse.server.engine.*;
import multiverse.server.network.*;

/**
 * 
 */
public class LoginResponseEvent extends Event {
    public LoginResponseEvent() {
        super();
    }

    public LoginResponseEvent(MVByteBuffer buf, ClientConnection con) {
        super(buf, con);
    }

    public LoginResponseEvent(Long oid, boolean successStatus, String msg) {
        super();
        setOid(oid);
        setSuccessStatus(successStatus);
        setTime(System.currentTimeMillis());
        setMessage(msg);
    }

    @Override
    public String getName() {
        return "LoginResponseEvent";
    }

    @Override
    public MVByteBuffer toBytes() {
        int msgId = Engine.getEventServer().getEventID(this.getClass());

        MVByteBuffer buf = new MVByteBuffer(200);
        buf.putLong(getOid()); // user id
        buf.putInt(msgId); // msg id
        buf.putLong(getTime());
        buf.putInt(getSuccessStatus() ? 1 : 0);
        buf.putString(getMessage());
        buf.flip();
        return buf;
    }

    @Override
    protected void parseBytes(MVByteBuffer buf) {
        buf.rewind();
        setOid(buf.getLong());
        // setEntity(Entity.getEntity(oid));

        /* int msgId = */ buf.getInt();

        setTime(buf.getLong());
        setSuccessStatus(buf.getInt() == 1);
        setMessage(buf.getString());
    }

    public void setSuccessStatus(boolean status) {
        successStatus = status;
    }

    public boolean getSuccessStatus() {
        return successStatus;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setOid(long id) {
        setObjectOid(id);
    }

    public long getOid() {
        return getObjectOid();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    // private long oid = -1;
    private long time = 0;

    private boolean successStatus = false;

    private String message = null;
}
