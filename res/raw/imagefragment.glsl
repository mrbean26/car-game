precision mediump float;

varying vec2 texCoord;
uniform sampler2D sampler;
uniform vec4 colour;

void main(){
	gl_FragColor = texture2D(sampler, texCoord) * colour;
}