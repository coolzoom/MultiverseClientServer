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

package multiverse.server.engine;

import multiverse.server.objects.LightData;
import multiverse.server.objects.Template;
import multiverse.server.objects.Region;
import multiverse.server.objects.RegionConfig;
import multiverse.server.objects.SpawnData;

/** Return true for all loader override methods.
*/
public class DefaultWorldLoaderOverride implements WorldLoaderOverride
{
    @Override
    public boolean adjustLightData(String worldCollectionName,
        String objectName, LightData lightData)
    {
        return true;
    }

    // StaticObject, Marker-based particle effects, Marker-based sounds
    @Override
    public boolean adjustObjectTemplate(String worldCollectionName,
        String objectName, Template template)
    {
        return true;
    }

    @Override
    public boolean adjustRegion(String worldCollectionName,
        String objectName, Region region)
    {
        return true;
    }

    @Override
    public boolean adjustRegionConfig(String worldCollectionName,
        String objectName, Region region, RegionConfig regionConfig)
    {
        return true;
    }

    @Override
    public boolean adjustSpawnData(String worldCollectionName,
        String objectName, SpawnData spawnData)
    {
        return true;
    }

}

