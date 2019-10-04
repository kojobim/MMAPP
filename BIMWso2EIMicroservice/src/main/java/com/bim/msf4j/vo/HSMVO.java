package com.bim.msf4j.vo;

import com.google.gson.Gson;

public class HSMVO {

	private String contrasena;
	private String contrasenaEncriptada;
	
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public String getContrasenaEncriptada() {
		return contrasenaEncriptada;
	}
	public void setContrasenaEncriptada(String contrasenaEncriptada) {
		this.contrasenaEncriptada = contrasenaEncriptada;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}	
}
