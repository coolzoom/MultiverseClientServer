struct TerrainVSOutput
{
	// transformed vertex coordinate
	float4	position				: POSITION;
	
	// lighting intensity color multiplier
	float4	color					: COLOR0;
	
	// texture coordinate from vertex
	float2	texCoord				: TEXCOORD0;
	
	// texture coordinate from vertex that has been scaled
	float2	scaledTexCoord			: TEXCOORD2;
	
	// world coordinates of the vertex (one unit per meter)
	float2	worldTexCoord			: TEXCOORD3;
	
	// world coordinates of the vertex (one unit per page)
	float2	worldPageTexCoord		: TEXCOORD4;
	
	// offset of the vertex within the current terrain page (one unit per page, so its limited to 0..1)
	float2	pageTexCoord			: TEXCOORD5;
	
	// texture coordinates for alpha0 splatting map
	float2 alpha0TexCoord			: TEXCOORD6;
	
	// texture coordinates for alpha1 splatting map
	float2 alpha1TexCoord			: TEXCOORD7;
	
	// fog factor
	float  fog						: FOG;
};

// Vertex program for automatic terrain texture generation
TerrainVSOutput TerrainVP
(
	float4				iPosition			: POSITION,
	float3				iNormal				: NORMAL,
	float2				iTexcoord			: TEXCOORD0,

	// the full World/View/Proj combined matrix
	uniform float4x4	worldViewProj,
	
	// Just the world matrix.  Used to find the world coordinates of the vertices for generating
	// some texture coordinates
	uniform float4x4	worldMat,
	
	// Fog settings
	//	fogSettings[0] - fog near distance
	//	fogSettings[1] - fog far distance
	//	fogSettings[2] - fog scale
	uniform float4		fogSettings,

    uniform float4		LightPosition[2],
    uniform float4		LightAttenuation[2],
	
	// Diffuse color of the light
	uniform float4		LightDiffuse[2],

	// Color of the ambient light
	uniform float4		lightAmbient,
	
	// Diffuse color of the terrain
	uniform float4		materialDiffuse,
	
	// Ambient color of the terrain
	uniform float4		materialAmbient,
	
	// Size of a terrain page
	uniform float		pageSize,
	
	// How many meters is one tiling of the terrain textures
	uniform float		textureTileSize,
	
	// used to convert page texture coord to alpha map texture coords
	uniform float4		alpha0TextureCoordAdjust,
	uniform float4		alpha1TextureCoordAdjust
)
{
	TerrainVSOutput output;

    // Calculate the output position and texture coordinates
	output.position  = mul(iPosition, worldViewProj);
	output.texCoord = float2(iPosition.x, iPosition.z)/pageSize;
    output.scaledTexCoord = output.texCoord * 256.0f / textureTileSize;
    
    
    float3 lightDirection[2];
    float lightAttenuation[2];

      
    // transform position to world space
    float4 position = mul(iPosition, worldMat);
    
	
	// get the light and eye direction in world space (not normalized)
    for (int lightIndex = 0; lightIndex < 2; ++lightIndex) {
        float4 lightPosition = mul(LightPosition[lightIndex], worldMat);

        // Account for whether the light is a point source.
        lightDirection[lightIndex] = lightPosition.xyz - (position * lightPosition.w).xyz;
        // set up attenuation params
        float lightDistance = length(lightDirection[lightIndex]);
        float lightDistanceSquared = lightDistance * lightDistance;

        //lightDistance = 1;
        //LightAttenuation[1].x = 0;

        lightAttenuation[lightIndex] = (lightDistance >
                                        LightAttenuation[lightIndex].x) ?
            0 : (1 / (LightAttenuation[lightIndex].y +
                      LightAttenuation[lightIndex].z * lightDistance +
                      LightAttenuation[lightIndex].w * lightDistanceSquared));
    }
    
    // Do lighting calculations
    output.color = saturate(
					max(dot(iNormal, normalize(lightDirection[0])), 0) *							// diffuse lighting
					LightDiffuse[0] * materialDiffuse * lightAttenuation[0] +	// diffuse color
					max(dot(iNormal, normalize(lightDirection[1])), 0) *							// diffuse lighting
					LightDiffuse[1] * materialDiffuse * lightAttenuation[1] +	// diffuse color
					lightAmbient * materialAmbient);								// ambient color

	// compute page relative offset of vertex, for use as a texture coord
	output.pageTexCoord = fmod( float2(worldMat._m03, worldMat._m23), float2(pageSize, pageSize) );
	if ( output.pageTexCoord.x < 0 ) {
		output.pageTexCoord.x += pageSize;
	}
	if ( output.pageTexCoord.y < 0 ) {
		output.pageTexCoord.y += pageSize;
	}	
	output.pageTexCoord = float2(iPosition.x, iPosition.z) / pageSize;
	
	// compute world world coordinates (just 2d) of vertex, for use as texture coord
	output.worldTexCoord = ( float2(worldMat._m03, worldMat._m23) + float2(iPosition.x, iPosition.z) );
	
	// scale world coords to page size (one unit is one page)
	output.worldPageTexCoord = output.worldTexCoord / pageSize;
	
	// scale world coords to be one unit per meter
	output.worldTexCoord /= 1000;
	
	// compute alpha map texture coords
	output.alpha0TexCoord = output.pageTexCoord * alpha0TextureCoordAdjust.yw + alpha0TextureCoordAdjust.xz;
	output.alpha1TexCoord = output.pageTexCoord * alpha1TextureCoordAdjust.yw + alpha1TextureCoordAdjust.xz;
	
	// compute fog
	float fog = clamp(( output.position.z - fogSettings[0] ) / (fogSettings[1] - fogSettings[0]),0.0,1.0) * fogSettings[2];
    output.fog = 1.0 - fog;
    
    return output;
}

//
// Fragment program for automatic terrain texture generation
// This fragment program splats 4 different terrain textures, and then applies a shade
// mask to vary the intensity of the terrain
//
float4 TerrainFP(
    // output of vertex shader
	TerrainVSOutput In,
	
	uniform float4		alpha0Mask,
	uniform float4		alpha1Mask,
	
	// alpha map 0
    uniform sampler2D   alphaMap0Sampler,
    
    // alpha map 1
    uniform sampler2D   alphaMap1Sampler,
    
    // layer 0 texture
    uniform sampler2D   layer0Sampler,
    
    // layer 1 texture
    uniform sampler2D   layer1Sampler,
    
    // layer 2 texture
    uniform sampler2D	layer2Sampler,
    
    // layer 3 texture
    uniform sampler2D   layer3Sampler,
    
    // layer 4 texture
    uniform sampler2D   layer4Sampler,
    
    // layer 5 texture
    uniform sampler2D	layer5Sampler,
    
    // layer 6 texture
    uniform sampler2D   layer6Sampler,
    
    // layer 7 texture
    uniform sampler2D	layer7Sampler,
    
    // detail texture
    uniform sampler2D	detailSampler    
        
) : COLOR
{
	float4 outputColor;
	
	//return In.color;
	
	// XXX - use worldPageTexCoord for now
	float4 alpha0 = tex2D(alphaMap0Sampler, In.alpha0TexCoord);
	float4 alpha1 = tex2D(alphaMap1Sampler, In.alpha1TexCoord);
	
	alpha0 = alpha0 * alpha0Mask;
	alpha1 = alpha1 * alpha1Mask;
	
	float4 l0 = tex2D(layer0Sampler, In.scaledTexCoord.xy);
	float4 l1 = tex2D(layer1Sampler, In.scaledTexCoord.xy);
	float4 l2 = tex2D(layer2Sampler, In.scaledTexCoord.xy);
	float4 l3 = tex2D(layer3Sampler, In.scaledTexCoord.xy);
	float4 l4 = tex2D(layer4Sampler, In.scaledTexCoord.xy);
	float4 l5 = tex2D(layer5Sampler, In.scaledTexCoord.xy);
	float4 l6 = tex2D(layer6Sampler, In.scaledTexCoord.xy);
	float4 l7 = tex2D(layer7Sampler, In.scaledTexCoord.xy);
	
	outputColor = alpha0.r * l0 + alpha0.g * l1 + alpha0.b * l2 + alpha0.a * l3
		+ alpha1.r * l4 + alpha1.g * l5 + alpha1.b * l6 + alpha1.a * l7;
	
	// scale output color by the color from the vertex shader, which is the
	// output of the lighting calculations
	outputColor *= In.color;
	
	// add detail map
	outputColor *= tex2D(detailSampler, In.pageTexCoord.xy);
	
	return outputColor;
}

//
// Fragment program for alpha terrain texture splatting
// This fragment program splats 8 different terrain textures, and then applies a detail
// map to vary the intensity of the terrain
//
float4 TerrainColorizedHighlightFP(
    // output of vertex shader
	TerrainVSOutput In,
	
	uniform float4		alpha0Mask,
	uniform float4		alpha1Mask,
	uniform float4		highlightColor,
	
	// alpha map 0
    uniform sampler2D   alphaMap0Sampler,
    
    // alpha map 1
    uniform sampler2D   alphaMap1Sampler,
    
    // layer 0 texture
    uniform sampler2D   layer0Sampler,
    
    // layer 1 texture
    uniform sampler2D   layer1Sampler,
    
    // layer 2 texture
    uniform sampler2D	layer2Sampler,
    
    // layer 3 texture
    uniform sampler2D   layer3Sampler,
    
    // layer 4 texture
    uniform sampler2D   layer4Sampler,
    
    // layer 5 texture
    uniform sampler2D	layer5Sampler,
    
    // layer 6 texture
    uniform sampler2D   layer6Sampler,
    
    // layer 7 texture
    uniform sampler2D	layer7Sampler,
    
    // detail texture
    uniform sampler2D	detailSampler,
    
    // highlight Mask
    uniform sampler2D	highlightMaskSampler,
    
    // highlight Texture
    uniform sampler2D	highlightTextureSampler  
        
) : COLOR
{
	float4 outputColor;
	
	float4 alpha0 = tex2D(alphaMap0Sampler, In.alpha0TexCoord);
	float4 alpha1 = tex2D(alphaMap1Sampler, In.alpha1TexCoord);
	
	alpha0 = alpha0 * alpha0Mask;
	alpha1 = alpha1 * alpha1Mask;
	
	float4 l0 = tex2D(layer0Sampler, In.scaledTexCoord.xy);
	float4 l1 = tex2D(layer1Sampler, In.scaledTexCoord.xy);
	float4 l2 = tex2D(layer2Sampler, In.scaledTexCoord.xy);
	float4 l3 = tex2D(layer3Sampler, In.scaledTexCoord.xy);
	float4 l4 = tex2D(layer4Sampler, In.scaledTexCoord.xy);
	float4 l5 = tex2D(layer5Sampler, In.scaledTexCoord.xy);
	float4 l6 = tex2D(layer6Sampler, In.scaledTexCoord.xy);
	float4 l7 = tex2D(layer7Sampler, In.scaledTexCoord.xy);
	
	outputColor = alpha0.r * l0 + alpha0.g * l1 + alpha0.b * l2 + alpha0.a * l3
		+ alpha1.r * l4 + alpha1.g * l5 + alpha1.b * l6 + alpha1.a * l7;
	
	// compute highlight weight by scaling highlight texture by the highlight mask texture
	float highlightWeight = tex2D(highlightTextureSampler, In.worldTexCoord).r * tex2D(highlightMaskSampler, In.pageTexCoord).a;
	
	// output color is a blend of highlight color and previous pixel color
	outputColor = highlightColor * highlightWeight + outputColor * ( 1 - highlightWeight);

	// add detail map
	outputColor *= tex2D(detailSampler, In.pageTexCoord.xy);

	// scale output color by the color from the vertex shader, which is the
	// output of the lighting calculations
	outputColor *= In.color;	
			
	return outputColor;
}

//
// Fragment program for alpha terrain texture splatting
// This fragment program splats 8 different terrain textures, and then applies a detail
// map to vary the intensity of the terrain
//
float4 TerrainEdgeBlendHighlightFP(
    // output of vertex shader
	TerrainVSOutput In,
	
	uniform float4		alpha0Mask,
	uniform float4		alpha1Mask,
	
	// alpha map 0
    uniform sampler2D   alphaMap0Sampler,
    
    // alpha map 1
    uniform sampler2D   alphaMap1Sampler,
    
    // layer 0 texture
    uniform sampler2D   layer0Sampler,
    
    // layer 1 texture
    uniform sampler2D   layer1Sampler,
    
    // layer 2 texture
    uniform sampler2D	layer2Sampler,
    
    // layer 3 texture
    uniform sampler2D   layer3Sampler,
    
    // layer 4 texture
    uniform sampler2D   layer4Sampler,
    
    // layer 5 texture
    uniform sampler2D	layer5Sampler,
    
    // layer 6 texture
    uniform sampler2D   layer6Sampler,
    
    // layer 7 texture
    uniform sampler2D	layer7Sampler,
    
    // detail texture
    uniform sampler2D	detailSampler,
    
    // highlight Mask
    uniform sampler2D	highlightMaskSampler,
    
    // highlight Texture
    uniform sampler2D	highlightTextureSampler  
        
) : COLOR
{
	float4 outputColor;
	
	float4 alpha0 = tex2D(alphaMap0Sampler, In.alpha0TexCoord);
	float4 alpha1 = tex2D(alphaMap1Sampler, In.alpha1TexCoord);
	
	alpha0 = alpha0 * alpha0Mask;
	alpha1 = alpha1 * alpha1Mask;
	
	float4 l0 = tex2D(layer0Sampler, In.scaledTexCoord.xy);
	float4 l1 = tex2D(layer1Sampler, In.scaledTexCoord.xy);
	float4 l2 = tex2D(layer2Sampler, In.scaledTexCoord.xy);
	float4 l3 = tex2D(layer3Sampler, In.scaledTexCoord.xy);
	float4 l4 = tex2D(layer4Sampler, In.scaledTexCoord.xy);
	float4 l5 = tex2D(layer5Sampler, In.scaledTexCoord.xy);
	float4 l6 = tex2D(layer6Sampler, In.scaledTexCoord.xy);
	float4 l7 = tex2D(layer7Sampler, In.scaledTexCoord.xy);
	
	outputColor = alpha0.r * l0 + alpha0.g * l1 + alpha0.b * l2 + alpha0.a * l3
		+ alpha1.r * l4 + alpha1.g * l5 + alpha1.b * l6 + alpha1.a * l7;
	
	// compute hilight weight by scaling hilight texture by the hilight mask texture
	float highlightWeight = tex2D(highlightMaskSampler, In.pageTexCoord).a;
	
	// hilight color comes from hilight texture
	float4 highlightColor = tex2D(highlightTextureSampler, In.worldTexCoord);	
	
	// output color is a blend of highlight color and previous pixel color
	outputColor = highlightColor * highlightWeight + outputColor * ( 1 - highlightWeight);

	// add detail map
	outputColor *= tex2D(detailSampler, In.pageTexCoord.xy);

	// scale output color by the color from the vertex shader, which is the
	// output of the lighting calculations
	outputColor *= In.color;
				
	return outputColor;
}

//
// Fragment program for alpha terrain texture splatting
// This fragment program splats 8 different terrain textures, and then applies a detail
// map to vary the intensity of the terrain
//
float4 TerrainEdgeSharpHighlightFP(
    // output of vertex shader
	TerrainVSOutput In,
	
	uniform float4		alpha0Mask,
	uniform float4		alpha1Mask,
	
	// alpha map 0
    uniform sampler2D   alphaMap0Sampler,
    
    // alpha map 1
    uniform sampler2D   alphaMap1Sampler,
    
    // layer 0 texture
    uniform sampler2D   layer0Sampler,
    
    // layer 1 texture
    uniform sampler2D   layer1Sampler,
    
    // layer 2 texture
    uniform sampler2D	layer2Sampler,
    
    // layer 3 texture
    uniform sampler2D   layer3Sampler,
    
    // layer 4 texture
    uniform sampler2D   layer4Sampler,
    
    // layer 5 texture
    uniform sampler2D	layer5Sampler,
    
    // layer 6 texture
    uniform sampler2D   layer6Sampler,
    
    // layer 7 texture
    uniform sampler2D	layer7Sampler,
    
    // detail texture
    uniform sampler2D	detailSampler,
    
    // highlight Mask
    uniform sampler2D	highlightMaskSampler,
    
    // highlight Texture
    uniform sampler2D	highlightTextureSampler  
        
) : COLOR
{
	float4 outputColor;
	
	float4 alpha0 = tex2D(alphaMap0Sampler, In.alpha0TexCoord);
	float4 alpha1 = tex2D(alphaMap1Sampler, In.alpha1TexCoord);
	
	alpha0 = alpha0 * alpha0Mask;
	alpha1 = alpha1 * alpha1Mask;
	
	float4 l0 = tex2D(layer0Sampler, In.scaledTexCoord.xy);
	float4 l1 = tex2D(layer1Sampler, In.scaledTexCoord.xy);
	float4 l2 = tex2D(layer2Sampler, In.scaledTexCoord.xy);
	float4 l3 = tex2D(layer3Sampler, In.scaledTexCoord.xy);
	float4 l4 = tex2D(layer4Sampler, In.scaledTexCoord.xy);
	float4 l5 = tex2D(layer5Sampler, In.scaledTexCoord.xy);
	float4 l6 = tex2D(layer6Sampler, In.scaledTexCoord.xy);
	float4 l7 = tex2D(layer7Sampler, In.scaledTexCoord.xy);
	
	outputColor = alpha0.r * l0 + alpha0.g * l1 + alpha0.b * l2 + alpha0.a * l3
		+ alpha1.r * l4 + alpha1.g * l5 + alpha1.b * l6 + alpha1.a * l7;
	
	// compute hilight weight by scaling hilight texture by the hilight mask texture
	float highlightWeight = tex2D(highlightMaskSampler, In.pageTexCoord).a;
	
	// hilight color comes from hilight texture
	float4 highlightColor = tex2D(highlightTextureSampler, In.worldTexCoord);
	
	// substitute the hilight color for the splatted terrain color
	if ( highlightWeight > 0.5 ) {
		outputColor = highlightColor;
	}

	// add detail map
	outputColor *= tex2D(detailSampler, In.pageTexCoord.xy);

	// scale output color by the color from the vertex shader, which is the
	// output of the lighting calculations
	outputColor *= In.color;
				
	return outputColor;
}

//
// Fragment program for alpha terrain texture splatting
// This fragment program splats 8 different terrain textures, and then applies a detail
// map to vary the intensity of the terrain
//
float4 TerrainEdgeSharpBlendHighlightFP(
    // output of vertex shader
	TerrainVSOutput In,
	
	uniform float4		alpha0Mask,
	uniform float4		alpha1Mask,
	
	// alpha map 0
    uniform sampler2D   alphaMap0Sampler,
    
    // alpha map 1
    uniform sampler2D   alphaMap1Sampler,
    
    // layer 0 texture
    uniform sampler2D   layer0Sampler,
    
    // layer 1 texture
    uniform sampler2D   layer1Sampler,
    
    // layer 2 texture
    uniform sampler2D	layer2Sampler,
    
    // layer 3 texture
    uniform sampler2D   layer3Sampler,
    
    // layer 4 texture
    uniform sampler2D   layer4Sampler,
    
    // layer 5 texture
    uniform sampler2D	layer5Sampler,
    
    // layer 6 texture
    uniform sampler2D   layer6Sampler,
    
    // layer 7 texture
    uniform sampler2D	layer7Sampler,
    
    // detail texture
    uniform sampler2D	detailSampler,
    
    // highlight Mask
    uniform sampler2D	highlightMaskSampler,
    
    // highlight Texture
    uniform sampler2D	highlightTextureSampler  
        
) : COLOR
{
	float4 outputColor;
	
	float4 alpha0 = tex2D(alphaMap0Sampler, In.alpha0TexCoord);
	float4 alpha1 = tex2D(alphaMap1Sampler, In.alpha1TexCoord);
	
	alpha0 = alpha0 * alpha0Mask;
	alpha1 = alpha1 * alpha1Mask;
	
	float4 l0 = tex2D(layer0Sampler, In.scaledTexCoord.xy);
	float4 l1 = tex2D(layer1Sampler, In.scaledTexCoord.xy);
	float4 l2 = tex2D(layer2Sampler, In.scaledTexCoord.xy);
	float4 l3 = tex2D(layer3Sampler, In.scaledTexCoord.xy);
	float4 l4 = tex2D(layer4Sampler, In.scaledTexCoord.xy);
	float4 l5 = tex2D(layer5Sampler, In.scaledTexCoord.xy);
	float4 l6 = tex2D(layer6Sampler, In.scaledTexCoord.xy);
	float4 l7 = tex2D(layer7Sampler, In.scaledTexCoord.xy);
	
	outputColor = alpha0.r * l0 + alpha0.g * l1 + alpha0.b * l2 + alpha0.a * l3
		+ alpha1.r * l4 + alpha1.g * l5 + alpha1.b * l6 + alpha1.a * l7;
	
	// compute hilight weight by scaling hilight texture by the hilight mask texture
	float highlightWeight = tex2D(highlightMaskSampler, In.pageTexCoord).a;
	
	// hilight color comes from hilight texture
	float4 highlightColor = tex2D(highlightTextureSampler, In.worldTexCoord);	
	
	// make a sharper cutoff for the blend effect
	highlightWeight = clamp(highlightWeight * 2, 0, 1);
	
	// output color is a blend of highlight color and previous pixel color
	outputColor = highlightColor * highlightWeight + outputColor * ( 1 - highlightWeight);

	// add detail map
	outputColor *= tex2D(detailSampler, In.pageTexCoord.xy);

	// scale output color by the color from the vertex shader, which is the
	// output of the lighting calculations
	outputColor *= In.color;
				
	return outputColor;
}
