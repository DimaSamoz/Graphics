#version 330

out vec4 colour;
in vec4 gl_FragCoord;

void main(){
    float x_pos = floor(gl_FragCoord.x / 100.0);
    float y_pos = floor(gl_FragCoord.y / 75.0);
    float mask = mod(x_pos + mod(y_pos, 2.0), 2.0);

    colour = mask * vec4(0.2f, 0.4f, 0.6f, 0.0f);
}