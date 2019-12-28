attribute vec3 inPosition;
attribute vec3 inNormal;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec3 outNormal;
varying vec3 fragPos;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

void main(){
    gl_Position = projection * view * model * vec4(inPosition, 1);

    texCoord = inTexCoord;
    outNormal = inNormal;
    fragPos = vec3(model * vec4(inPosition, 1.0));
}