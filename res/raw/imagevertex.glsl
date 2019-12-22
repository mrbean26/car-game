attribute vec3 in_position;
attribute vec2 in_texCoord;
varying vec2 texCoord;

uniform mat4 model;

void main(){
	gl_Position = model * vec4(in_position, 1);
	texCoord = in_texCoord;
}