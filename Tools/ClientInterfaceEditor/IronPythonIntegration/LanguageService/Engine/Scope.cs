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
using System.Collections.Generic;
using System.Reflection;
using System.Diagnostics;

using IronPython.Compiler.Ast;
using IronPython.Runtime;

namespace Microsoft.Samples.VisualStudio.IronPythonInference {
    // Disable the "IdentifiersShouldDifferByMoreThanCase" warning.
    [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Naming", "CA1708")]
    public abstract class Scope {
        private Module module;
        private Scope parent;
        // Disable the "DoNotDeclareVisibleInstanceFields" warning.
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Design", "CA1051")]
        // Disable the "DoNotNestGenericTypesInMemberSignatures" warning.
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Design", "CA1006")]
        protected Dictionary<SymbolId, List<Definition>> definitions = new Dictionary<SymbolId, List<Definition>>();

        protected Scope(Module module, Scope parent) {
            this.module = module;
            this.parent = parent;
        }

        public Scope Parent {
            get { return parent; }
        }

        public Module Module {
            get { return module; }
        }

        public abstract ScopeStatement Statement { get; }

        // Disable the "UsePropertiesWhereAppropriate" warning.
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Design", "CA1024")]
        public IEnumerable<SymbolId> GetNamesCurrent() {
            return definitions.Keys;
        }
        // Disable the "UsePropertiesWhereAppropriate" warning.
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Design", "CA1024")]
        public abstract IEnumerable<SymbolId> GetNamesOuter();

        public void Define(SymbolId name, Definition definition) {
            List<Definition> list;

            if (!definitions.TryGetValue(name, out list)) {
                list = new List<Definition>();
                definitions[name] = list;
            }

            list.Add(definition);
        }

        public IList<Inferred> ResolveCurrent(SymbolId name, Engine engine) {
            List<Definition> defs;
            IList<Inferred> inferred = null;
            if (definitions.TryGetValue(name, out defs)) {
                foreach (Definition definition in defs) {
                    inferred = Engine.Union<Inferred>(inferred, definition.Resolve(engine, this));
                }
            }
            return inferred;
        }

        public abstract IList<Inferred> ResolveOuter(SymbolId name, Engine engine);
    }


    public class FunctionScope : Scope {
        private IronPython.Compiler.Ast.FunctionDefinition statement;

        public FunctionScope(Module module, Scope parent, IronPython.Compiler.Ast.FunctionDefinition statement)
            : base(module, parent) {
            this.statement = statement;
        }

        public override ScopeStatement Statement {
            get { return statement; }
        }

        public override IEnumerable<SymbolId> GetNamesOuter() {
            return GetNamesCurrent();
        }

        public override IList<Inferred> ResolveOuter(SymbolId name, Engine engine) {
            return ResolveCurrent(name, engine);
        }
    }

    public class ClassScope : Scope {
        private IronPython.Compiler.Ast.ClassDefinition statement;

        public ClassScope(Module module, Scope parent, IronPython.Compiler.Ast.ClassDefinition statement)
            : base(module, parent) {
            this.statement = statement;
        }

        public override ScopeStatement Statement {
            get { return statement; }
        }

        public override IEnumerable<SymbolId> GetNamesOuter() {
            return null;
        }

        public override IList<Inferred> ResolveOuter(SymbolId name, Engine engine) {
            return null;
        }
    }

    public class ModuleScope : Scope {
        private GlobalSuite statement;

        public ModuleScope(Module module, Scope parent, GlobalSuite statement)
            : base(module, parent) {
            this.statement = statement;
        }

        public override ScopeStatement Statement {
            get { return statement; }
        }

        public override IEnumerable<SymbolId> GetNamesOuter() {
            return GetNamesCurrent();
        }

        public override IList<Inferred> ResolveOuter(SymbolId name, Engine engine) {
            return ResolveCurrent(name, engine);
        }
    }
}
