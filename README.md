INSTRUCTIVO
----------------------------------------------------
DESPLIEGUE:
	Se deberá compilar la aplicación para generar un archivo .car a partir del CompositeApplication, esto puede hacerse utilizando
	el IDE tools de WSO2 dando clic derecho al proyecto y seleccionando "Export Composite Application Project", 
	en caso de querer hacer un despliegue manual, deberá posicionarse dentro de la raiz del proyecto y correr el comando:
		> mvn clean install
	una vez que se haya acabado el build, deberá ingresar al proyecto de CompositeApplication y correr el comando:
		> mvn clean install
	
	resultado de esto se generará dentro de la carpeta target del CompositeApplication un archivo .car, para desplegar la aplicación deberá mover este
	archivo a la carpeta /repository/deployment/server/carbonapps dentro del EI y levantar el servidor de EI 
	el cual arrancará la aplicación y la configurará automáticamente
	
INVOCAR PING

para invocar la demo, deberá enviar un POST a  https://localhost:<8243 ó el puerto asignado para servicios>/services/ping-proxy
el cual devolverá un echo del objeto recibido, en cuyo caso podremos confirmar que nuestra instalación fue un éxito