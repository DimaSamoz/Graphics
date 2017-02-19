#version 330

in vec3 newNormal;
in vec3 fragPos;
out vec4 colour;

void main(){

    float ambient = 0.2;

    float diffuseIntensity = 1.0;
    vec3 lightPosition = vec3(150.0, 40.0, 60.0);
    vec3 norm = normalize(newNormal);
    vec3 lightDir = normalize(lightPosition - fragPos);
    float diffuse = diffuseIntensity * max(dot(norm, lightDir), 0.0);

    float specularIntensity = 0.5;
    vec3 viewPosition = vec3(1.5, 1.5, 2.5);
    vec3 viewDir = normalize(fragPos - viewPosition);
    vec3 reflectDir = reflect(lightDir, norm);
    float specular = specularIntensity * (pow(clamp(0, dot(viewDir, reflectDir), 1), 32));

    colour = vec4((ambient + diffuse + specular) * vec3(0.8, 0.4, 0.4), 1.0f);
}