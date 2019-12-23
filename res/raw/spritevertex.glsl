attribute vec3 in_position;
attribute vec2 in_texCoord;
varying vec2 texCoord;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

void main(){
    gl_Position = projection * view * model * vec4(in_position, 1);
    texCoord = in_texCoord;
}