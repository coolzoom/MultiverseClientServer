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

public class ClientParameterEvent extends Event {

    public ClientParameterEvent() {
        super();
    }

    public ClientParameterEvent(MVByteBuffer buf, ClientConnection con) {
        super(buf, con);
    }

    public ClientParameterEvent(String parameterName,
                                String parameterValue) {
        setParameterName(parameterName);
        setParameterValue(parameterValue);
    }

    @Override
    public String getName() {
        return "ClientParameterEvent";
    }

    @Override
    public MVByteBuffer toBytes() {
        /* int msgId = */ Engine.getEventServer().getEventID(this.getClass());

        MVByteBuffer buf = new MVByteBuffer(200);
        buf.putString(getParameterName());
        buf.putString(getParameterValue());

        buf.flip();
        return buf;
    }

    @Override
    protected void parseBytes(MVByteBuffer buf) {
        buf.rewind();
        setParameterName(buf.getString());
        setParameterValue(buf.getString());
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    public String getParameterName() {
        return parameterName;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
    public String getParameterValue() {
        return parameterValue;
    }

    private String parameterName = null;
    private String parameterValue = null;
}
