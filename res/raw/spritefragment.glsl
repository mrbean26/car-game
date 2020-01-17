precision mediump float;

varying vec2 texCoord;
varying vec3 Normal;
varying vec3 FragPos;

uniform vec3 multiplyColour;

uniform sampler2D sampler;
uniform bool useLight;
uniform float lightInfoArray[130];
uniform vec3 viewPos;

int modFunction(int numOne, int numTwo){
    int result = int(numOne / numTwo);
    result = result * numTwo;

    int returned = numOne - result;
    return returned;
}

vec4 calculateLight(float constant, float linear, float quadratic, float ambient, float diffuse, float specular, vec3 colour, vec3 position, vec4 startColourWithoutLight){
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 lightDir = normalize(position - FragPos);
    // diffuse
    float diff = max(dot(Normal, lightDir), 0.0);
    // specular
    vec3 reflectDir = reflect(-lightDir, Normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    //
    float distance = length(position - FragPos);
    float attenuation = 1.0 / (constant + linear * distance * quadratic * (distance * distance));
    // combine
    vec3 ambientLight = vec3(ambient) * vec3(startColourWithoutLight) * attenuation * colour;
    vec3 diffuseLight = vec3(diffuse) * diff * vec3(startColourWithoutLight) * attenuation * colour;
    vec3 specularLight = vec3(specular) * spec * vec3(startColourWithoutLight) * attenuation * colour;
    vec3 total = ambientLight + diffuseLight + specularLight;

    return vec4(total, startColourWithoutLight.a);
}

void main(){
    vec4 start = texture2D(sampler, texCoord);

    vec4 result;
    if(useLight){
        bool lightsEnabled[10];
        float lightConstants[10];
        float lightLinears[10];
        float lightQuadratics[10];
        float lightAmbients[10];
        float lightDiffuse[10];
        float lightSpecular[10];
        vec3 lightColours[10];
        vec3 lightPositions[10];

        for(int i = 0; i < 10; i++){
            lightsEnabled[i] = lightInfoArray[i * 13 + 0] == 1.0;
            lightConstants[i] = lightInfoArray[i * 13 + 1];
            lightLinears[i] = lightInfoArray[i * 13 + 2];
            lightQuadratics[i] = lightInfoArray[i * 13 + 3];
            lightAmbients[i] = lightInfoArray[i * 13 + 4];
            lightDiffuse[i] = lightInfoArray[i * 13 + 5];
            lightSpecular[i] = lightInfoArray[i * 13 + 6];

            lightColours[i].x = lightInfoArray[i * 13 + 7];
            lightColours[i].y = lightInfoArray[i * 13 + 8];
            lightColours[i].z = lightInfoArray[i * 13 + 9];

            lightPositions[i].x = lightInfoArray[i * 13 + 10];
            lightPositions[i].y = lightInfoArray[i * 13 + 11];
            lightPositions[i].z = lightInfoArray[i * 13 + 12];
        }
        for(int i = 0; i < 10; i++){
            if(!lightsEnabled[i]){
                continue;
            }
            result = result + calculateLight(lightConstants[i], lightLinears[i], lightQuadratics[i], lightAmbients[i], lightDiffuse[i], lightSpecular[i], lightColours[i], lightPositions[i], start);
        }
    }
    if(!useLight){
        result = start;
    }

    gl_FragColor = result * vec4(multiplyColour, 1.0);
}