package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestorChat {
    private final GestorXML gestorXML = new GestorXML();

    public void registrarUsuario(String nombre) {
        try {

        ListaUsuarios listaUsuarios = gestorXML.leerUsuarios();

        if (listaUsuarios.getUsuarios() == null){
            listaUsuarios.setUsuarios(new ArrayList<>());
        }

        for (Usuario u : listaUsuarios.getUsuarios()) {
            if (u.getNombre().equalsIgnoreCase(nombre)) {
                throw new IllegalArgumentException("El usuario " + nombre + " ya existe.");
            }
        }

        Usuario nuevoUsuario = new Usuario(nombre);
        listaUsuarios.getUsuarios().add(nuevoUsuario);

        gestorXML.guardarUsuarios(listaUsuarios);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    public void enviarMensaje(String emisor, String destinatario, String contenido) {
        try {


        ListaUsuarios listaUsuarios = gestorXML.leerUsuarios();

        if (listaUsuarios.getUsuarios() == null || listaUsuarios.getUsuarios().isEmpty()) {
            throw new IllegalArgumentException("No hay usuarios registrados en el sistema.");
        }

        boolean emisorExiste = false;
        for (Usuario u : listaUsuarios.getUsuarios()) {
            if (u.getNombre().equalsIgnoreCase(emisor)) {
                emisorExiste = true;
            }
        }

        boolean destinatarioExiste = false;
        for (Usuario u : listaUsuarios.getUsuarios()) {
            if (u.getNombre().equalsIgnoreCase(destinatario)) {
                destinatarioExiste = true;
            }
        }

        if (!emisorExiste){
            throw new IllegalArgumentException("El emisor " + emisor + " no existe");
        }
        if (!destinatarioExiste){
            throw new IllegalArgumentException("El destinatario " + destinatario + " no existe");
        }

        ListaMensajes listaMensajes = gestorXML.leerMensajes();
        if (listaMensajes.getMensajes() == null) {
            listaMensajes.setMensajes(new ArrayList<>());
        }

        Mensaje nuevoMensaje = new Mensaje(emisor, destinatario, contenido, java.time.LocalDateTime.now().toString());
        listaMensajes.getMensajes().add(nuevoMensaje);

        gestorXML.guardarMensajes(listaMensajes);
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    public List<Mensaje> consultarConversacion(String usuarioA, String usuarioB) {
        List<Mensaje> conversacion = new ArrayList<>();

        ListaMensajes listaMensajes = gestorXML.leerMensajes();

        if (listaMensajes.getMensajes() == null) {
            return conversacion;
        }

        for (Mensaje m : listaMensajes.getMensajes()) {
            boolean deA_a_B = m.getEmisor().equalsIgnoreCase(usuarioA) && m.getDestinatario().equalsIgnoreCase(usuarioB);
            boolean deB_a_A = m.getEmisor().equalsIgnoreCase(usuarioB) && m.getDestinatario().equalsIgnoreCase(usuarioA);

            if (deA_a_B || deB_a_A) {
                conversacion.add(m);
            }
        }

        return conversacion;
    }

    public Map<String, Long> estadisticas(String usuarioA, String usuarioB) {
        List<Mensaje> conversacion = consultarConversacion(usuarioA, usuarioB);

        return conversacion.stream()
                .collect(Collectors.groupingBy(
                        Mensaje::getEmisor,
                        Collectors.counting()
                ));
    }

    public void exportarConversacion(String usuarioA, String usuarioB, Path destino) {

        List<Mensaje> conversacion = consultarConversacion(usuarioA, usuarioB);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(destino.toFile()))) {
            for (Mensaje m : conversacion) {
                String linea = "[" + m.getFecha() + "] " + m.getEmisor() + " -> " + m.getDestinatario() + ": " + m.getContenido();
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void adjuntarArchivo(Mensaje mensaje, Path archivoOrigen) {
        try {
            File origenFile = archivoOrigen.toFile();

            if (!origenFile.exists() || !origenFile.isFile()) {
                throw new IllegalArgumentException("El archivo de origen no es válido o no se encuentra en la ruta: " + archivoOrigen);
            }

            // CORREGIDO: Multiplicación limpia sin guiones para los 10 MB
            long tamañoMaximo = 10L * 1024 * 1024;
            if (origenFile.length() > tamañoMaximo) {
                throw new IllegalArgumentException("El archivo supera el tamaño máximo permitido (10MB).");
            }

            String nombreArchivo = origenFile.getName();
            String extension = "";
            int indicePunto = nombreArchivo.lastIndexOf('.');
            if (indicePunto > 0) {
                extension = nombreArchivo.substring(indicePunto + 1).toLowerCase();
            }

            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("txt") && !extension.equals("pdf")) {
                throw new IllegalArgumentException("Extensión de archivo no permitida: " + extension);
            }

            File carpetaMedia = new File("C:\\Users\\Usuario\\IdeaProjects\\ChatOffline_Plantilla2\\src\\main\\prueba");
            if (!carpetaMedia.exists()) {
                boolean creada = carpetaMedia.mkdirs();
                if (creada) {
                    System.out.println("Se ha creado la carpeta 'prueba' correctamente.");
                }
            } else {
                System.out.println("La carpeta 'prueba' ya existía previamente.");
            }

            File archivoDestino = new File(carpetaMedia, nombreArchivo);

            // Copia binaria del fichero
            try (FileInputStream in = new FileInputStream(origenFile);
                 FileOutputStream out = new FileOutputStream(archivoDestino)) {

                byte[] buffer = new byte[1024];
                int bytesLeidos;
                while ((bytesLeidos = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesLeidos);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al copiar el archivo físico: " + e.getMessage(), e);
            }

            mensaje.setAdjuntoRuta(archivoDestino.getPath());
            mensaje.setAdjuntoNombre(nombreArchivo);
            mensaje.setAdjuntoTamano(origenFile.length());
            mensaje.setAdjuntoTipo(extension);

            ListaMensajes listaMensajes = gestorXML.leerMensajes();
            boolean encontradoEnXML = false;

            if (listaMensajes.getMensajes() != null) {
                for (Mensaje m : listaMensajes.getMensajes()) {
                    boolean coincide = m.getEmisor().equalsIgnoreCase(mensaje.getEmisor()) &&
                            m.getDestinatario().equalsIgnoreCase(mensaje.getDestinatario()) &&
                            m.getContenido().equals(mensaje.getContenido());

                    if (coincide) {
                        m.setAdjuntoRuta(archivoDestino.getPath());
                        m.setAdjuntoNombre(nombreArchivo);
                        m.setAdjuntoTamano(origenFile.length());
                        m.setAdjuntoTipo(extension);
                        encontradoEnXML = true;
                    }
                }
            }

            if (!encontradoEnXML) {
                System.out.println("Aviso: El archivo se copió, pero no se encontró el mensaje exacto en el XML para asociarlo.");
            }

            gestorXML.guardarMensajes(listaMensajes);
            System.out.println("¡Archivo adjuntado con éxito!");

        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error durante el proceso: " + e.getMessage());
        }
    }
}
