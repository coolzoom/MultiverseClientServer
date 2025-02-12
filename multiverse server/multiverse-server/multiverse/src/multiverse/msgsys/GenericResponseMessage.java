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

package multiverse.msgsys;

import java.util.Map;
import java.util.HashMap;


public class GenericResponseMessage extends ResponseMessage
{
    public GenericResponseMessage() {
        super();
    }

    public GenericResponseMessage(MessageType msgType) {
        super(msgType);
    }

    public GenericResponseMessage(Message requestMessage)
    {
        super(requestMessage);
    }

    public GenericResponseMessage(Message requestMessage, Object data)
    {
        super(requestMessage);
        setData(data);
    }

    public Object getProperty(String key) {
        if (properties == null)
            return null;
        return properties.get(key);
    }

    public void setProperty(String key, Object value)
    {
        if (properties == null)
            properties = new HashMap<>();
        properties.put(key, value);
    }

    public Map<String,Object> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String,Object> props)
    {
        properties = props;
    }

    public void addProperties(Map<String,Object> props)
    {
        properties.putAll(props);
    }

    public Object getData()
    {
        return this.data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    protected Object data;
    protected Map<String, Object> properties;
    
    private static final long serialVersionUID = 1L;
}

