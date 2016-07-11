#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

const float PI = 3.1415926535;

#define PI 3.14159265359

varying vec2 v_texCoord0;
varying vec4 v_color;

uniform sampler2D u_texture;
uniform float u_angle;

mat2 rotate2d(float _angle)
{
    return mat2(cos(_angle),-sin(_angle), 
                sin(_angle),cos(_angle));
}

void main()
{
    vec2 st = v_texCoord0;
    st -= vec2(0.5);
    st = rotate2d(u_angle) * st;
    st += vec2(0.5);
    gl_FragColor = texture2D(u_texture, st);
}