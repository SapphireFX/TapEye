#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoord0;
varying vec4 v_color;

uniform LOWP sampler2D u_texture;
uniform vec2 iResolution;
uniform LOWP float itime;

void main (void)
{
	//vec2 st = v_texCoord0.xy/iResolution.xy;
    vec3 color = vec3(0.0);

    vec2 pos = vec2(0.5)-v_texCoord0;

    float r = length(pos)*2.0;
    float a = atan(pos.y,pos.x);

    float f = cos(a*3.);
     f = abs(cos(a*3.));
     f = abs(cos(a*2.5))*.5+.3;
     f = abs(cos(a*12.)*sin(a*itime))*.8+.1;
    // f = abs(cos(a*12.)*sin(a*3.))*.8+.1;
    // f = smoothstep(-.5,1., cos(a*10.))*0.2+0.5;

    color = vec3( 1.-smoothstep(f,f+0.02,r) );

	gl_FragColor = texture2D(u_texture, v_texCoord0 + color);
} 