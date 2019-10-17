package com.bim.commons.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TcpSocket {

	private Socket socket;
	private DataOutputStream flujoSalida;
	private DataInputStream flujoEntrada;
	private int totalLeidos = 0;
	private String sMsgErr = new String("");

	public TcpSocket() {
		socket = null;
		flujoSalida = null;
		flujoEntrada = null;
	}

	public int creaConexionSocket(String nombreEquipo, int numeroPuerto) {
		int exito = 0;

		if (socket != null) {
			exito = 1;
		} else {
			try {
				InetAddress iad = InetAddress.getByName(nombreEquipo);
				// System.out.println(iad.getHostAddress());
				socket = new Socket(iad, numeroPuerto);
				flujoEntrada = new DataInputStream(socket.getInputStream());
				flujoSalida = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				sMsgErr = "creaConexionSocket:" + e;
				exito = 1;
			}
		}
		return exito;
	}

	public void cierraConexionSocket() {
		try {
			flujoEntrada.close();
			flujoEntrada = null;
		} catch (Exception e1) {
			sMsgErr = "cierraConexionSocket,1:" + e1;
			e1.printStackTrace();
		}

		try {
			flujoSalida.close();
			flujoSalida = null;
		} catch (Exception e2) {
			sMsgErr = "cierraConexionSocket,2:" + e2;
			e2.printStackTrace();
		}

		try {
			socket.close();
			socket = null;
		} catch (Exception e3) {
			sMsgErr = "cierraConexionSocket,3:" + e3;
			e3.printStackTrace();
		}
	}

	public void enviaMensaje(String mensaje) {
		try {
			flujoSalida.writeBytes(mensaje);
		} catch (IOException e) {
			sMsgErr = "enviaMensaje:" + e;
			e.printStackTrace();
		}
	}

	public void enviaMensajeBytes(byte[] mensaje) {
		try {
			flujoSalida.write(mensaje, 0, mensaje.length);
		} catch (IOException e) {
			sMsgErr = "enviaMensaje:" + e;
			e.printStackTrace();
		}
	}

	public String recibeMensaje(int numBytes) {
		byte buffer[] = new byte[numBytes];
		String mensaje = new String();
		int leidos = 0;
		int totalBytes = 0;

		try {
			leidos = flujoEntrada.read(buffer, 0, numBytes);
			totalBytes = leidos;
			while (totalBytes < numBytes) {
				leidos = flujoEntrada.read(buffer, totalBytes, numBytes - totalBytes);
				totalBytes += leidos;
			}
		} catch (IOException e) {
			sMsgErr = "recibeMensaje:" + e;
			e.printStackTrace();
		}

		mensaje = new String(buffer);
		return mensaje;
	}

	public int creaConexionSocketTO(String nombreEquipo, int numeroPuerto, int timeOutMs) {
		int exito = 0;
		
		if (socket != null) {
			exito = 1;
		} else {
			try {
				socket = new Socket(InetAddress.getByName(nombreEquipo), numeroPuerto);
				socket.setSoTimeout(timeOutMs);
				flujoSalida = new DataOutputStream(socket.getOutputStream());
				flujoEntrada = new DataInputStream(socket.getInputStream());
			} catch (Exception e) {
				sMsgErr = "creaConexionSocketTO:" + e;
				exito = 1;
			}
		}
		return exito;
	}

	public String recibeMensajeVariable(int numBytes) {
		byte buffer[] = new byte[numBytes];
		String mensaje = new String();

		try {
			totalLeidos = flujoEntrada.read(buffer);
			mensaje = new String(buffer, 0, totalLeidos);
		} catch (IOException e1) {
			sMsgErr = "recibeMensajeVariable,1:" + e1;
			e1.printStackTrace();
		} catch (Exception e2) {
			sMsgErr = "recibeMensajeVariable,2:" + e2;
			e2.printStackTrace();
		}

		return mensaje;
	}

	public int getTotalLeidos() {
		return totalLeidos;
	}

	public String getMsgErr() {
		return sMsgErr;
	}

}
