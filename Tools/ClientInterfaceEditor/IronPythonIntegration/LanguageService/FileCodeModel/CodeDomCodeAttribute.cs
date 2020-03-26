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

/***************************************************************************

Copyright (c) Microsoft Corporation. All rights reserved.
This code is licensed under the Visual Studio SDK license terms.
THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.

***************************************************************************/

using System;
using System.CodeDom;
using System.CodeDom.Compiler;
using System.Diagnostics.CodeAnalysis;
using System.Runtime.InteropServices;
using EnvDTE;

namespace Microsoft.Samples.VisualStudio.CodeDomCodeModel {
    [ComVisible(true)]
    [SuppressMessage("Microsoft.Interoperability", "CA1409:ComVisibleTypesShouldBeCreatable")]
    [SuppressMessage("Microsoft.Naming", "CA1711:IdentifiersShouldNotHaveIncorrectSuffix")]
    public class CodeDomCodeAttribute : SimpleCodeElement, ICodeDomElement, CodeAttribute {
        private CodeElement parent;
        private string attrValue;
        private CodeAttributeDeclaration attr;

        [SuppressMessage("Microsoft.Naming", "CA1704:IdentifiersShouldBeSpelledCorrectly", MessageId = "0#dte")]
        public CodeDomCodeAttribute(DTE dte, CodeElement parent, string name)
            : base(dte, name) {
            this.parent = parent;
            CodeAttributeDeclaration cad = new CodeAttributeDeclaration();
            // !!! name, value

            CodeObject = cad;
        }

        public override TextPoint EndPoint {
            get { throw new NotImplementedException(); }
        }

        public override TextPoint StartPoint {
            get { throw new NotImplementedException(); }
        }

        public override TextPoint GetEndPoint(vsCMPart Part) {
            throw new NotImplementedException();
        }

        public override TextPoint GetStartPoint(vsCMPart Part) {
            throw new NotImplementedException();
        }


        #region CodeAttribute Members


        public void Delete() {
            throw new NotImplementedException();
        }

        public object Parent {
            get {
                return parent;
            }
        }

        public string Value {
            get {
                return attrValue;
            }
            [SuppressMessage("Microsoft.Naming", "CA1725:ParameterNamesShouldMatchBaseDeclaration", MessageId = "0#")]
            set {
                this.attrValue = value;
            }
        }

        #endregion

        #region ICodeDomElement Members

        public object UntypedCodeObject {
            get { return attr; }
        }

        public object ParentElement {
            get {
                return parent;
            }
        }

        #endregion

        public CodeAttributeDeclaration CodeObject {
            get {
                return attr;
            }
            set {
                attr = value;
            }
        }

        public override CodeElements Children {
            get { throw new System.NotImplementedException(); }
        }

        public override CodeElements Collection {
            get { return parent.Children; }
        }


        public override string FullName {
            get { return CodeObject.AttributeType.BaseType; }
        }

        public override vsCMElement Kind {
            get {
                return vsCMElement.vsCMElementAttribute;
            }
        }

        public override ProjectItem ProjectItem {
            get { return parent.ProjectItem; }
        }

    }

}
