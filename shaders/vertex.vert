#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 colour;

uniform mat4 MVP;
out vec3 newColour;

void main() {
    gl_Position = MVP * vec4(position, 1.0f);
    newColour = colour;
}
