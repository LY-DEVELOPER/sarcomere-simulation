#version 330

in  vec2 outTexCoord;
out vec4 fragColour;

uniform sampler2D texture_sampler;
uniform vec4 colour;
uniform int useColour;

void main()
{
    if ( useColour == 1 )
    {
        fragColour = vec4(colour);
    }
    else
    {
        fragColour = colour * texture(texture_sampler, outTexCoord);
    }
}