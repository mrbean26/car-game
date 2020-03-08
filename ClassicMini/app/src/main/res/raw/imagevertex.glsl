attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 texCoord;
varying vec3 fragPos;

uniform mat4 orthoModel;

void main(){
	vec3 usedPosition = vec3(inPosition.x, inPosition.y, 0.0);
    gl_Position = orthoModel * vec4(usedPosition, 1.0);

    fragPos = usedPosition;
    texCoord = inTexCoord;
}
