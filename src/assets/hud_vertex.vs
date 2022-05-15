#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texturePos;

out vec2 outTexCoord;

uniform mat4 hudMatrix;

void main()
{
    gl_Position = hudMatrix * vec4(position, 1.0);
    outTexCoord = texturePos;
}