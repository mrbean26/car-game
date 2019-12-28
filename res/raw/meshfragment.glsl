precision mediump float;

varying vec2 texCoord;
varying vec3 fragPos;
varying vec3 outNormal;

uniform sampler2D sampler;

void main(){
    // lighting code here

    gl_FragColor = texture2D(sampler, texCoord);
}