attribute vec3 inPosition;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

void main(){
	gl_Position = projection * view * model * vec4(inPosition, 1);
}