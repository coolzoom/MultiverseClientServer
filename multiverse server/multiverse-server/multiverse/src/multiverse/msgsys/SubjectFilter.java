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

import java.util.*;

/** Match subject messages by message type and OID.
*/
public class SubjectFilter extends MessageTypeFilter
{
    public SubjectFilter()
    {
    }

    /** Match subject messages by OID and target messages by target OID.
     * @param oid
    */
    public SubjectFilter(long oid)
    {
        subjectOid = oid;
    }

    /** Match subject messages by message type and OID, and target messages by message type and target OID.
     * @param types
     * @param oid
    */
    public SubjectFilter(Collection<MessageType> types, long oid)
    {
        super(types);
        subjectOid = oid;
    }

    /** True if {@code message} is a {@link SubjectMessage} with matching
        OID, or if {@code message} is a {@link TargetMessage} with matching
        target OID.
     * @return 
    */
    @Override
    public boolean matchRemaining(Message message)
    {
        if (message instanceof SubjectMessage)
            return ((SubjectMessage)message).getSubject() == subjectOid;
        if (message instanceof TargetMessage)
            return ((TargetMessage)message).getTarget() == subjectOid;
        
        return false;
    }

    @Override
    public String toString()
    {
        return "[SubjectFilter "+toStringInternal()+"]";
    }

    @Override
    protected String toStringInternal()
    {
        return "oid="+subjectOid + " " + super.toStringInternal();
    }

    private long subjectOid;
}

