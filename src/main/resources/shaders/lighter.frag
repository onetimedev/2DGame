uniform sampler2D texture;

void main()
{
    vec4 pixel = gl_Color * texture2D(texture, gl_TexCoord[0].xy);
    vec3 col = clamp(pixel.rgb * 1.2, 0.0, 1.0);

    gl_FragColor = vec4(col, pixel.a);
}
