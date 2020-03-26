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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;
using Microsoft.MultiverseInterfaceStudio.FrameXml.Controls;
using System.Xml.Serialization;

namespace Microsoft.MultiverseInterfaceStudio.FrameXml.Serialization
{
	public partial class FrameType
	{
		static FrameType()
		{
			RegisterDefaultValues(typeof(FrameType));
		}

		[Category("Layout")]
		[XmlIgnore]
		public Dimension.minResize MinResize
		{
			get
			{
				return this.Properties.GetValue<Dimension.minResize>("MinResize");
			}
			set
			{
				this.Properties["MinResize"] = value;
			}
		}

		[Category("Layout")]
		[XmlIgnore]
		public Dimension.maxResize MaxResize
		{
			get
			{
				return this.Properties.GetValue<Dimension.maxResize>("MaxResize");
			}
			set
			{
				this.Properties["MaxResize"] = value;
			}
		}
	}
}
