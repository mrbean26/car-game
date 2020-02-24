attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec3 fragPos;

uniform mat4 orthoModel;

void main(){
    gl_Position = orthoModel * vec4(inPosition, 1.0);

    fragPos = inPosition;
    texCoord = inTexCoord;
}