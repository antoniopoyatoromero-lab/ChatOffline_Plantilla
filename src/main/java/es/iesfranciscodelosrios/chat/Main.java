package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        GestorChat chat = new GestorChat();

        // TODO: implementar menú repetitivo con las opciones:
        // 1. Registrar usuario
        // 2. Enviar mensaje
        // 3. Consultar conversación
        // 4. Estadísticas
        // 5. Exportar conversación
        // 6. Adjuntar archivo
        // 0. Salir
        System.out.println("Chat Offline - Proyecto UD1");
        System.out.println("Menú pendiente de implementar.");
        teclado.close();

        ListaUsuarios usuarios = new ListaUsuarios(new ArrayList<>());



        try {
           usuarios = (ListaUsuarios) GestorXML.leerUsuarios();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        for(Usuario u : usuarios.getUsuarios()) {
            System.out.println(u.getNombre());
        }
    }
}
