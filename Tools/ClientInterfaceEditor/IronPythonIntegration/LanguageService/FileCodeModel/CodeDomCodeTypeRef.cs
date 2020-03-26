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
    [SuppressMessage("Microsoft.Interoperability", "CA1405:ComVisibleTypeBaseTypesShouldBeComVisible")]
    public class CodeDomCodeTypeRef : CodeDomCodeElement<CodeTypeReference>, CodeTypeRef {
        [SuppressMessage("Microsoft.Naming", "CA1704:IdentifiersShouldBeSpelledCorrectly", MessageId = "0#dte")]
        public CodeDomCodeTypeRef(DTE dte, string name)
            : base(dte, name) {
            CodeObject = new CodeTypeReference(name);
            CodeObject.UserData[CodeKey] = this;
        }

        [SuppressMessage("Microsoft.Interoperability", "CA1407:AvoidStaticMembersInComVisibleTypes")]
        public static CodeTypeReference ToCodeTypeReference(CodeTypeRef typeRef) {
            if (null == typeRef) {
                throw new ArgumentNullException("typeRef");
            }
            CodeDomCodeTypeRef cdTypeRef = typeRef as CodeDomCodeTypeRef;
            if (cdTypeRef != null) return cdTypeRef.CodeObject;

            CodeTypeReference ctr = new CodeTypeReference();
            ctr.BaseType = typeRef.AsFullName;
            if (typeRef.Rank != 0) {
                ctr.ArrayRank = typeRef.Rank;
                ctr.ArrayElementType = ToCodeTypeReference(typeRef.ElementType);
            }
            ctr.UserData[CodeKey] = cdTypeRef;
            return ctr;
        }

        [SuppressMessage("Microsoft.Interoperability", "CA1407:AvoidStaticMembersInComVisibleTypes")]
        [SuppressMessage("Microsoft.Design", "CA1011:ConsiderPassingBaseTypesAsParameters")]
        public static CodeDomCodeTypeRef FromCodeTypeReference(CodeTypeReference typeRef) {
            if (null == typeRef) {
                throw new ArgumentNullException("typeRef");
            }
            return (CodeDomCodeTypeRef)typeRef.UserData[CodeKey];
        }

        #region CodeTypeRef Members

        public string AsFullName {
            get { return CodeObject.BaseType; }
        }

        public string AsString {
            get { return CodeObject.BaseType; }
        }

        public CodeType CodeType {
            get {
                throw new NotImplementedException();
            }
            [SuppressMessage("Microsoft.Naming", "CA1725:ParameterNamesShouldMatchBaseDeclaration", MessageId = "0#")]
            set {
                throw new NotImplementedException();
            }
        }

        public CodeTypeRef CreateArrayType(int Rank) {
            throw new NotImplementedException();
        }

        public CodeTypeRef ElementType {
            get {
                throw new NotImplementedException();
            }
            [SuppressMessage("Microsoft.Naming", "CA1725:ParameterNamesShouldMatchBaseDeclaration", MessageId = "0#")]
            set {
                throw new NotImplementedException();
            }
        }

        public object Parent {
            get { throw new NotImplementedException(); }
        }

        public int Rank {
            get {
                return CodeObject.ArrayRank;
            }
            [SuppressMessage("Microsoft.Naming", "CA1725:ParameterNamesShouldMatchBaseDeclaration", MessageId = "0#")]
            set {
                CodeObject.ArrayRank = value;

                CommitChanges();
            }
        }

        public vsCMTypeRef TypeKind {
            get { throw new NotImplementedException(); }
        }

        #endregion

        public override object ParentElement {
            get { return null; }
        }

        public override CodeElements Children {
            get { return null; }
        }

        public override CodeElements Collection {
            get { return null; }
        }

        public override string FullName {
            get { return CodeObject.BaseType; }
        }

        public override vsCMInfoLocation InfoLocation {
            get { return vsCMInfoLocation.vsCMInfoLocationNone; }
        }

        public override vsCMElement Kind {
            get { return vsCMElement.vsCMElementOther; }
        }

        public override ProjectItem ProjectItem {
            get { return null; }
        }
    }
}
