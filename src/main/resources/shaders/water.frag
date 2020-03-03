#version 110

uniform sampler2D texture;

uniform float rot;

#define AMP 0.1
#define WAVELENGTH 0.4

void main()
{
    float x = gl_TexCoord[0].x;
    float y = mod(gl_TexCoord[0].y - sin(2.0 * 3.14 * (x + rot) / WAVELENGTH) * AMP, 1.0);

    vec4 pixel = gl_Color * texture2D(texture, vec2(x, y));

    gl_FragColor = pixel;
}
