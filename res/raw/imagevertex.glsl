attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec3 fragPos;
varying vec2 texCoord;
varying mat4 outModel;

uniform mat4 orthoModel;
uniform mat4 model;

void main(){
    gl_Position = orthoModel * vec4(inPosition, 1.0);

    texCoord = inTexCoord;
    fragPos = vec3(model * vec4(inPosition, 1.0));
    outModel = model;
}