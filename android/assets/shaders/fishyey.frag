#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

const float PI = 3.1415926535;

varying vec2 v_texCoord0;
varying vec4 v_color;

uniform sampler2D u_texture;
uniform float u_time;

void main()
{
    //float aperture = 178.0;
    //float apertureHalf = 0.5 * aperture * (PI / 180.0);
    //float maxFactor = sin(apertureHalf * sin(u_time) );
//
    //vec2 uv;
    //vec2 xy = 2.0 * v_texCoord0.xy - 1.0;
    //float d = length(xy);
    //if (d < (2.0-maxFactor))
    //{
    //    d = length(xy * maxFactor);
    //    float z = sqrt(1.0 - d * d);
    //    float r = atan(d, z) / PI;
    //    float phi = atan(xy.y, xy.x);
//
    //    uv.x = r * cos(phi) + 0.5;
    //    uv.y = r * sin(phi) + 0.5;
    //}
    //else
    //{
    //    uv = v_texCoord0.xy;
    //}
    //vec4 c = texture2D(u_texture, uv);
    //
//
//
//
    //gl_FragColor = c;

    vec2 tc = v_texCoord0.xy;
    vec2 p = -1.0 + 2.0 * tc;
    float len = length(p);
    vec2 uv = tc + (p/len)*cos(len*12.0-u_time*4.0)*0.03;
    vec3 col = texture2D(u_texture,uv).xyz;
    gl_FragColor = vec4(col,1.0);  
}