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
import multiverse.server.objects.*;
import multiverse.server.network.*;
import multiverse.server.util.*;

/**
 * send the target object/mob an event so that it can do something
 */
public class ScriptEvent extends Event {
    public ScriptEvent() {
	super();
    }

    public ScriptEvent(MVByteBuffer buf, ClientConnection con) {
	super(buf,con);
    }

    public ScriptEvent(MVObject target) {
	super(target);
    }

    @Override
    public String getName() {
	return "ScriptEvent";
    }

    @Override
    public MVByteBuffer toBytes() {
	throw new MVRuntimeException("ScriptEvent: not implemented");
    }

    @Override
    protected void parseBytes(MVByteBuffer buf) {
	throw new MVRuntimeException("ScriptEvent: not implemented");
    }

    // this is how you can tell the other object what to do or what state
    // its in
    public Object getData() {
	return data;
    }

    public void setData(Object o) {
	data = o;
    }

    private Object data = null;
}
