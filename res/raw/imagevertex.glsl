attribute vec3 inPosition;
attribute vec2 inTexCoord;
varying vec2 texCoord;

uniform mat4 model;

void main(){
	gl_Position = model * vec4(inPosition, 1);
	texCoord = inTexCoord;
}