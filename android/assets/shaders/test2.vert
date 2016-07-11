attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

varying vec4 v_color;     //used by pass color data to fragment shader
varying vec2 v_texCoord0;    //used by pass pixel coordinate data to fragment shader

uniform mat4 u_projTrans;    //used by libgdxs
uniform float itime;

void main() 
{
	gl_Position = u_projTrans * a_position;
	v_color = a_color;
	v_texCoord0 = a_texCoord0;
} 