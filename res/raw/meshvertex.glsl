attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec3 FragPos;
varying vec3 Normal;
varying vec2 TexCoords;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 transposedInversedModel;

void main(){
    gl_Position = projection * view * model * vec4(inPosition, 1);

    Normal = mat3(transposedInversedModel) * inNormal;
    TexCoords = inTexCoord;
    FragPos = vec3(model * vec4(inPosition, 1.0));
}