#version 330

in vec4 gl_FragCoord;
in vec3 newColour;
in vec2 UV;

out vec4 colour;

uniform sampler2D newTexture;

void main(){
    float x_pos = floor(gl_FragCoord.x / 100.0);
    float y_pos = floor(gl_FragCoord.y / 75.0);
    float mask = mod(x_pos + mod(y_pos, 2.0), 2.0);

    colour = vec4(newColour, 1.0f);
}