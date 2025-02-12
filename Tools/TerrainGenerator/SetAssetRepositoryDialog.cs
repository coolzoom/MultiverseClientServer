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
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace Multiverse.Tools.TerrainGenerator
{
    public partial class SetAssetRepositoryDialog : Form
    {
        protected string downloadPage = "http://192.168.1.6/wiki/index.php/Download_an_Asset_Repository";
        protected string helpPage = "http://192.168.1.6/wiki/index.php/Download_and_Designate_Asset_Repository_-_Help";
        protected TerrainGenerator app;

        public SetAssetRepositoryDialog(TerrainGenerator app)
        {
            this.app = app;
            InitializeComponent();
        }

        private void downloadButton_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start(downloadPage);
        }

        private void helpButton_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start(helpPage);
        }

        private void repositoryButton_Click(object sender, EventArgs e)
        {
            bool setPath = app.DesignateRepository();
            if (setPath)
            {
                continueButton.Enabled = true;
            }
        }
    }
}
