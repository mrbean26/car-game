precision mediump float;

varying vec2 texCoord;
varying vec3 fragPos;

uniform vec3 position;
uniform vec3 scale; // (for rounded rectangles)

uniform sampler2D sampler;
uniform vec4 colour;
uniform float roundedRadius;
uniform vec4 backgroundColour; // applied when fragcolour.a == 0

void main(){

	gl_FragColor = texture2D(sampler, texCoord) * colour;

	// rounded rectangles
	vec3 newFragPos = fragPos + position;
	newFragPos = newFragPos * scale;

	// edge points
	vec3 edgePoints[4];
	edgePoints[0] = vec3(-1.0, -1.0, 0.0);
	edgePoints[1] = vec3(1.0, -1.0, 0.0);
	edgePoints[2] = vec3(-1.0, 1.0, 0.0);
	edgePoints[3] = vec3(1.0, 1.0, 0.0);

	for(int i = 0; i < 4; i++){
		edgePoints[i] = edgePoints[i] + position;
		edgePoints[i] = edgePoints[i] * scale;
	}

	// inner points
	vec3 innerPoints[4];
	innerPoints[0] = edgePoints[0] + vec3(roundedRadius, roundedRadius, 0.0);
	innerPoints[1] = edgePoints[1] + vec3(-roundedRadius, roundedRadius, 0.0);
	innerPoints[2] = edgePoints[2] + vec3(roundedRadius, -roundedRadius, 0.0);
	innerPoints[3] = edgePoints[3] + vec3(-roundedRadius, -roundedRadius, 0.0);

	// compare
	int selectedIndex = 0;
	float lowestDistance = distance(newFragPos, innerPoints[0]);

	for(int i = 1; i < 4; i++){
		float currentDistance = distance(newFragPos, innerPoints[i]);

		if(currentDistance < lowestDistance){
			selectedIndex = i;
			lowestDistance = currentDistance;
		}
	}

	if(lowestDistance > roundedRadius){
		if(distance(newFragPos, edgePoints[selectedIndex]) < distance(newFragPos, innerPoints[selectedIndex])){
			discard;
		}
	}

	if(gl_FragColor.a == 0.0){
		gl_FragColor = backgroundColour;
	}
}