package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner teclado = new Scanner(System.in);
        GestorChat chat = new GestorChat();

        int opciones;

        do {
            System.out.println("1. Registrar usuario");
            System.out.println("2. Enviar mensaje");
            System.out.println("3. Consultar conversación");
            System.out.println("4. Estadísticas");
            System.out.println("5. Exportar conversación");
            System.out.println("6. Adjuntar archivo");
            System.out.println("0. Salir");
            System.out.println("Introduce la opcion deseada");
            opciones = teclado.nextInt();
            teclado.nextLine();


            switch (opciones){
                case 1:
                    System.out.println("Introduce el nombre del usuario");
                    String nombre = teclado.nextLine();
                    chat.registrarUsuario(nombre);
                    break;

                case 2:
                    System.out.println("Introduce el nombre del usuario");
                    String usuario = teclado.nextLine();
                    System.out.println("Introduce el nombre del usuario que quieras mandar el mensaje");
                    String destinatario = teclado.nextLine();
                    System.out.println("Introduce el mensaje");
                    String mensaje = teclado.nextLine();
                    chat.enviarMensaje(usuario,destinatario,mensaje);
                    break;

                case 3:
                    System.out.println("Introduce el nombre del usuario");
                    usuario = teclado.nextLine();
                    System.out.println("Introduce el nombre del usuario del que quieres comprobar los mensajes");
                    destinatario = teclado.nextLine();
                    List<Mensaje> mensajes = new ArrayList<>();
                    mensajes = chat.consultarConversacion(usuario,destinatario);
                    for (Mensaje m : mensajes) {
                        System.out.println(m.getContenido());
                    }
                    break;

                case 4:
                    System.out.println("Introduce el nombre del usuario");
                    usuario = teclado.nextLine();
                    System.out.println("Introduce el nombre del usuario del que quieres comprobar tus estadisticas");
                    destinatario = teclado.nextLine();
                    System.out.println(chat.estadisticas(usuario,destinatario));
                    break;

                case 5:
                    System.out.println("Introduce el nombre del usuario");
                    usuario = teclado.nextLine();
                    System.out.println("Introduce el nombre del usuario del que quieres guardar la conversacion");
                    destinatario = teclado.nextLine();
                    System.out.println("Indica la ruta del archivo");
                    String  ruta = teclado.nextLine();

                        chat.exportarConversacion(usuario,destinatario, Path.of(ruta));


                    break;

                case 6:
                    System.out.print("Introduce el emisor del mensaje al que adjuntar: ");
                    usuario = teclado.nextLine();
                    System.out.print("Introduce el destinatario: ");
                    destinatario = teclado.nextLine();
                    System.out.print("Introduce el mensaje al que quieres adjuntar: ");
                    String contenidoBuscado = teclado.nextLine();

                    List<Mensaje> conversacion = chat.consultarConversacion(usuario, destinatario);
                    Mensaje mensajeEncontrado = null;
                    boolean encontrado = false;

                    for (Mensaje m : conversacion) {
                        if (!encontrado && m.getContenido().equals(contenidoBuscado)) {
                            mensajeEncontrado = m;
                            encontrado = true;
                        }
                    }

                    if (mensajeEncontrado != null) {
                        System.out.print("Indica la ruta del archivo a adjuntar: ");
                        String rutaArchivo = teclado.nextLine();
                        chat.adjuntarArchivo(mensajeEncontrado, Path.of(rutaArchivo));
                    } else {
                        System.out.println("Error: No se ha encontrado ningún mensaje con ese contenido entre esos usuarios.");
                    }
                    break;
            }
        }while(opciones!=0);
        teclado.close();
    }
}
