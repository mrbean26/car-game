attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec3 FragPos;
varying vec3 Normal;
varying vec2 texCoord;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 transposedInversedModel;

void main(){
    gl_Position = projection * view * model * vec4(inPosition, 1);

    texCoord = inTexCoord;
    Normal = mat3(transposedInversedModel) * inNormal;
    FragPos = vec3(model * vec4(inPosition, 1.0));
}