#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texture;
layout (location=2) in vec3 normals;
layout (location=3) in vec4 jointWeights;
layout (location=4) in ivec4 jointIndices;

out vec2 outTexCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 jointsMatrix[4];
uniform mat4 objectCameraMatrix;
uniform mat4 sceneMatrix;
uniform int animObj;

void main()
{
    vec4 pos = vec4(0, 0, 0, 0);
    vec4 norm = vec4(0, 0, 0, 0);
    
    if(animObj == 1){
	    for(int i = 0; i < 4; i++)
	    {
	    	if(jointWeights[i] > 0){
		        pos += jointWeights[i] * (jointsMatrix[jointIndices[i]] * vec4(position, 1.0));
		        norm += jointWeights[i] * (jointsMatrix[jointIndices[i]] * vec4(normals, 0.0));
	        }
	    }
    }
    else
    {
        pos = vec4(position, 1.0);
        norm = vec4(normals, 0.0);
    } 

    vec4 camPos = objectCameraMatrix * pos;
    gl_Position = sceneMatrix * camPos;
    outTexCoord = texture;
    mvVertexNormal = normalize(objectCameraMatrix * norm).xyz;
    mvVertexPos = camPos.xyz;
}