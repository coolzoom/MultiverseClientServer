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

import java.util.*;
import java.io.*;
import multiverse.server.engine.*;
import multiverse.server.util.*;
import multiverse.server.network.*;

public class ExtensionMessageEvent extends Event {

    public ExtensionMessageEvent(MVByteBuffer buf, ClientConnection con) {
	super(buf, con);
    }

    public ExtensionMessageEvent() {
    }

    public ExtensionMessageEvent(Long objOid) {
        super(objOid);
    }

    @Override
    public String getName() {
	return "ExtensionMessageEvent";
    }

    @Override
    public String toString() {
        return "[ExtensionMessageEvent: oid=" + getObjectOid() + ", targetOid=" + 
            getTargetOid() + ", clientTargeted=" + getClientTargeted() + "]";        
    }
    
    @Override
    public MVByteBuffer toBytes() {
	throw new MVRuntimeException("ExtensionMessageEvent.toBytes not implemented");
    }

    @Override
    public void parseBytes(MVByteBuffer buf) {
	buf.rewind();
	
	long oid = buf.getLong();
	setObjectOid(oid);
	/* int msgId = */ buf.getInt();
        byte flags = buf.getByte();
        if ((flags & 1) != 0)
            targetOid = buf.getLong();
        clientTargeted = (flags & 2) != 0;
        propertyMap = buf.getPropertyMap();
    }

    public void setExtensionType(String type) {
        propertyMap.put("ext_msg_subtype", type);
    }

    public String getExtensionType() {
        return (String)propertyMap.get("ext_msg_subtype");
    }

    public void setPropertyMap(Map<String, Serializable> v) {
	propertyMap = v;
    }

    public Map<String, Serializable> getPropertyMap() {
	return propertyMap;
    }

    public void setTargetOid(Long v) {
	targetOid = v;
    }

    public Long getTargetOid() {
	return targetOid;
    }

    public void setClientTargeted(Boolean v) {
	clientTargeted = v;
    }
    public Boolean getClientTargeted() {
	return clientTargeted;
    }

    private Map<String, Serializable> propertyMap = null;
    private Long targetOid = null;
    private Boolean clientTargeted = null;
}
