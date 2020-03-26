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

namespace Microsoft.Samples.VisualStudio.IronPythonProject
{
    using System;
    using System.Reflection;
    using System.Globalization;
    using System.Resources;
    using System.Text;
    using System.Threading;
    using System.ComponentModel;
    using System.Security.Permissions;

   [AttributeUsage(AttributeTargets.All)]
   internal sealed class SRDescriptionAttribute : DescriptionAttribute
   {

        private bool replaced = false;

        public SRDescriptionAttribute(string description) : base(description)
	{
        }

        public override string Description
        {
            get
            {
                if (!replaced)
                {
                    replaced = true;
                    DescriptionValue = SR.GetString(base.Description);
                }
                return base.Description;
            }
        }
    }

    [AttributeUsage(AttributeTargets.All)]
    internal sealed class SRCategoryAttribute : CategoryAttribute
    {

        public SRCategoryAttribute(string category) : base(category)
        {
        }

        protected override string GetLocalizedString(string value)
        {
            return SR.GetString(value);
        }
    }
    internal sealed class SR
	{
        internal const string ProjectReferenceError = "ProjectReferenceError";
        internal const string ProjectReferenceError2 = "ProjectReferenceError2";
        internal const string Application = "Application";
        internal const string ApplicationIcon = "ApplicationIcon";
        internal const string ApplicationIconDescription = "ApplicationIconDescription";
        internal const string AssemblyName = "AssemblyName";
        internal const string AssemblyNameDescription = "AssemblyNameDescription";
        internal const string DefaultNamespace = "DefaultNamespace";
        internal const string DefaultNamespaceDescription = "DefaultNamespaceDescription";
        internal const string GeneralCaption = "GeneralCaption";
        internal const string OutputFile = "OutputFile";
        internal const string OutputFileDescription = "OutputFileDescription";
        internal const string OutputType = "OutputType";
        internal const string OutputTypeDescription = "OutputTypeDescription";
        internal const string Project = "Project";
        internal const string ProjectFile = "ProjectFile";
        internal const string ProjectFileDescription = "ProjectFileDescription";
        internal const string ProjectFileExtensionFilter = "ProjectFileExtensionFilter";
        internal const string ProjectFolder = "ProjectFolder";
        internal const string ProjectFolderDescription = "ProjectFolderDescription";
        internal const string StartupObject = "StartupObject";
        internal const string StartupObjectDescription = "StartupObjectDescription";
        internal const string TargetPlatform = "TargetPlatform";
        internal const string TargetPlatformDescription = "TargetPlatformDescription";
        internal const string TargetPlatformLocation = "TargetPlatformLocation";
        internal const string TargetPlatformLocationDescription = "TargetPlatformLocationDescription";

        static SR loader = null;
        ResourceManager resources;

        private static Object s_InternalSyncObject;
        private static Object InternalSyncObject
		{
            get
			{
                if (s_InternalSyncObject == null)
				{
                    Object o = new Object();
                    Interlocked.CompareExchange(ref s_InternalSyncObject, o, null);
                }
                return s_InternalSyncObject;
            }
        }
        
        internal SR()
		{
            resources = new System.Resources.ResourceManager("Microsoft.Samples.VisualStudio.IronPythonProject.Resources", this.GetType().Assembly);
        }
        
        private static SR GetLoader()
		{
            if (loader == null)
			{
                lock (InternalSyncObject)
				{
                   if (loader == null)
				   {
                       loader = new SR();
                   }
               }
            }
            
            return loader;
        }

        private static CultureInfo Culture
		{
            get { return null/*use ResourceManager default, CultureInfo.CurrentUICulture*/; }
        }
        
        public static ResourceManager Resources
		{
            get
			{
                return GetLoader().resources;
            }
        }
        
        public static string GetString(string name, params object[] args)
		{
            SR sys = GetLoader();
            if (sys == null)
                return null;
            string res = sys.resources.GetString(name, SR.Culture);

            if (args != null && args.Length > 0)
			{
                return String.Format(CultureInfo.CurrentCulture, res, args);
            }
            else
			{
                return res;
            }
        }

        public static string GetString(string name)
		{
            SR sys = GetLoader();
            if (sys == null)
                return null;
            return sys.resources.GetString(name, SR.Culture);
        }
        
        public static object GetObject(string name)
		{
            SR sys = GetLoader();
            if (sys == null)
                return null;
            return sys.resources.GetObject(name, SR.Culture);
        }
}
}
