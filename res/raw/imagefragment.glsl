precision mediump float;

varying vec2 texCoord;
varying vec3 fragPos;
varying mat4 outModel;

uniform sampler2D sampler;
uniform vec4 colour;
uniform float roundedRadius;

void main(){
	gl_FragColor = texture2D(sampler, texCoord) * colour;

	// original points
	vec3 originalPoints[4];

	// select vector
	vec3 corners[4];

	corners[0] = vec3(outModel * vec4(-1.0, -1.0, 0.0, 1.0));
	corners[1] = vec3(outModel * vec4(1.0, -1.0, 0.0, 1.0));
	corners[2] = vec3(outModel * vec4(-1.0, 1.0, 0.0, 1.0));
	corners[3] = vec3(outModel * vec4(1.0, 1.0, 0.0, 1.0));

	originalPoints[0] = corners[0];
	originalPoints[1] = corners[1];
	originalPoints[2] = corners[2];
	originalPoints[3] = corners[3];

	corners[0] = corners[0] + vec3(roundedRadius, roundedRadius, 0.0);
	corners[1] = corners[1] + vec3(-roundedRadius, roundedRadius, 0.0);
	corners[2] = corners[2] + vec3(roundedRadius, -roundedRadius, 0.0);
	corners[3] = corners[3] + vec3(-roundedRadius, -roundedRadius, 0.0);

	float lowestDistance = distance(fragPos, corners[0]);
	int selectedIndex = 0;

	for(int i = 1; i < 4; i++){
		float thisDistance = distance(fragPos, corners[i]);
		if(thisDistance < lowestDistance){
			selectedIndex = i;
			lowestDistance = thisDistance;
		}
	}

	// discard info
	if(lowestDistance > roundedRadius){
		if(distance(fragPos, originalPoints[selectedIndex]) < distance(fragPos, corners[selectedIndex])){
			discard;
		}
	}
}