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
using System.Text;
using System.Reflection;
using System.IO;
using System.Security;
using System.Globalization;
using Microsoft.VisualStudio.TemplateWizard;

namespace Microsoft.Samples.VisualStudio.IronPythonWebSiteCustomWizard
{
    /// <summary>
    /// We need to override the default Wizard used by the WebSite Project
    /// in order to enable the itemtemplate to add the App_Code folder. That means that we will override the 
    /// ShouldAddProjectItem to return true when the item to be added is the App_Code folder. 
    ///  In all other methods we will just delegate to the default wizard
    /// </summary>
    public class CustomWizardBase : IWizard
    {
        #region fields
        IWizard aggregatedWizard;
        #endregion

        #region Properties
        internal protected string AggregatedWizardAssemblyName
        {
            get
            {
                return "Microsoft.VisualStudio.Web, Version=9.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a";
            }
        }
        
        internal protected virtual string AggregatedFullClassName
        {
            get
            {
                return "";
            }
        }

        internal protected IWizard AggregatedWizard
        {
            get
            {
                if (aggregatedWizard == null)
                {
                    LoadAggregatedWizard();
                }
                return aggregatedWizard;
            }
        }
        #endregion

        #region IWizard Members

        public virtual void BeforeOpeningFile(EnvDTE.ProjectItem projectItem)
        {
            AggregatedWizard.BeforeOpeningFile(projectItem);
        }

        public virtual void ProjectFinishedGenerating(EnvDTE.Project project)
        {
            AggregatedWizard.ProjectFinishedGenerating(project);
        }

        public virtual void ProjectItemFinishedGenerating(EnvDTE.ProjectItem projectItem)
        {
            AggregatedWizard.ProjectItemFinishedGenerating(projectItem);
        }

        public virtual void RunFinished()
        {
            AggregatedWizard.RunFinished();
        }

        public virtual void RunStarted(object automationObject, Dictionary<string, string> replacementsDictionary, WizardRunKind runKind, object[] customParams)
        {
            AggregatedWizard.RunStarted(automationObject, replacementsDictionary, runKind, customParams);
        }

        /// <summary>
        /// Override original logic that determines if project items should be added.
        /// We want to return true if the add App_Code directory is added to project root
        /// </summary>
        /// <param name="filePath">path to file</param>
        /// <returns>true if file should be added</returns>
        public virtual bool ShouldAddProjectItem(string filePath)
        {
            //Always return true if the project item to be added is the App_Code folder
            DirectoryInfo info = new DirectoryInfo(filePath);
            if (String.Compare(info.Name, "App_Code", true, CultureInfo.InvariantCulture) == 0)
            {
                return true;
            }
            else
            {
                return AggregatedWizard.ShouldAddProjectItem(filePath);
            }
        }

        #endregion

        #region Helper methods
        protected void LoadAggregatedWizard()
        {
            Assembly asm = Assembly.Load(AggregatedWizardAssemblyName);
            aggregatedWizard = (IWizard)asm.CreateInstance(AggregatedFullClassName);
        }
        #endregion
    }

    /// <summary>
    /// WebForm specific custom wizard.
    /// </summary>
    public class WebFormCustomWizard : CustomWizardBase
    {
        #region Properties
        protected internal override string AggregatedFullClassName
        {
            get
            {
                return "Microsoft.VisualStudio.Web.Wizard.VsWebFormItemTemplateWizard";
            }
        }
        #endregion
    }

    /// <summary>
    /// MasterPage specific custom wizard.
    /// </summary>
    public class MasterPageCustomWizard : CustomWizardBase
    {
        #region Properties
        protected internal override string AggregatedFullClassName
        {
	        get 
	        {
                return "Microsoft.VisualStudio.Web.Wizard.VsMasterPageItemTemplateWizard";
	        }
        }
        #endregion
    }
}
