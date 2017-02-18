#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 colour;
layout (location = 2) in vec3 normal;

uniform mat4 MVP;

out vec3 newColour;
out vec3 newNormal;
out vec3 fragPos;

void main() {
    gl_Position = MVP * vec4(position, 1.0f);
    newColour = colour;
    newNormal = normal;
    fragPos = position;
}
