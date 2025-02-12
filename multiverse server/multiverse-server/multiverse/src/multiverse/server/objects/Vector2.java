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

package multiverse.server.objects;

import multiverse.server.math.*;
import java.io.*;

/**
 * represents a 2d point, useful in 2D boundary calculations
 */
public class Vector2 implements Cloneable, Serializable {

    public Vector2() {
    }
    
    public Vector2(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Vector2(long x, long y) {
	this.x = x;
	this.y = y;
    }

    public Vector2(Point p) {
	this.x = p.getX();
	this.y = p.getZ();
    }

    @Override
    public Object clone() {
	return new Vector2(this.x, this.y);
    }

    @Override
    public String toString() {
	return "[Vector2 x=" + x + " y=" + y + "]";
    }

    public long x = -1;
    public long y = -1;

    private static final long serialVersionUID = 1L;
}
