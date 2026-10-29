package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorChat {
    private final GestorXML gestorXML = new GestorXML();

    // TODO Fase 1: comprobar duplicados y registrar el usuario en XML.
    public void registrarUsuario(String nombre) throws JAXBException {
        File archivo = new File("usuario.xml");
        ListaUsuarios listaUsuarios;

        JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);

        if (archivo.exists() && archivo.length() > 0){
            Unmarshaller unmarshaller = context.createUnmarshaller();
            listaUsuarios = (ListaUsuarios) unmarshaller.unmarshal(archivo);
        } else {
            listaUsuarios = new ListaUsuarios();
            if (listaUsuarios.getUsuarios()== null){
                listaUsuarios.setUsuarios(new ArrayList<>());
            }
        }
    }

    // TODO Fase 2: validar usuarios, crear mensaje con fecha y persistirlo.
    public void enviarMensaje(String emisor, String destinatario, String contenido) throws JAXBException {
        File archivoUsuarios = new File("usuario.xml");
        JAXBContext contextUsuario = JAXBContext.newInstance(ListaUsuarios.class);

        if (!archivoUsuarios.exists() || archivoUsuarios.length() == 0) {
            throw new IllegalArgumentException("No hay usuarios registrados en el sistema.");
        }
        Unmarshaller unmarshallerUsuario = contextUsuario.createUnmarshaller();
        ListaUsuarios listaUsuarios = (ListaUsuarios) unmarshallerUsuario.unmarshal(archivoUsuarios);

        if (listaUsuarios.getUsuarios() == null) {
            throw new IllegalArgumentException("La lista de usuarios está vacía.");
        }

        boolean emisorExiste = false;
        for (Usuario u : listaUsuarios.getUsuarios()) {
            if (u.getNombre().equals(emisor)) {
                emisorExiste = true;
            }
        }

        boolean destinatarioExiste = false;
        for (Usuario u : listaUsuarios.getUsuarios()) {
            if (u.getNombre().equals(destinatario)) {
                destinatarioExiste = true;
            }
        }

        if (!emisorExiste){
            throw new IllegalArgumentException("El emisor "+emisor+" no existe");
        }
        if (!destinatarioExiste){
            throw new IllegalArgumentException("El destinatario "+emisor+" no existe");
        }

        File archivoMensajes = new File("mensajes.xml");
        JAXBContext contextMensaje = JAXBContext.newInstance(ListaMensajes.class);
        ListaMensajes listaMensajes;

        if (archivoMensajes.exists() && archivoMensajes.length() > 0) {
            Unmarshaller unmarshallerMensaje = contextMensaje.createUnmarshaller();
            listaMensajes = (ListaMensajes) unmarshallerMensaje.unmarshal(archivoMensajes);
        } else {
            listaMensajes = new ListaMensajes();
            if (listaMensajes.getMensajes() == null) {
                listaMensajes.setMensajes(new java.util.ArrayList<>());
            }
        }
    }

    // TODO Fase 3: filtrar mensajes en ambos sentidos (A -> B y B -> A).
    public List<Mensaje> consultarConversacion(String usuarioA, String usuarioB) throws JAXBException {
        throw new UnsupportedOperationException("Pendiente: consultarConversacion");
    }

    // TODO Fase 4: utilizar Stream API para contar mensajes por emisor.
    public Map<String, Long> estadisticas(String usuarioA, String usuarioB) throws JAXBException {
        throw new UnsupportedOperationException("Pendiente: estadisticas");
    }

    // TODO Fase 4: exportar la conversación a un archivo de texto.
    public void exportarConversacion(String usuarioA, String usuarioB, Path destino)
            throws JAXBException, IOException {
        throw new UnsupportedOperationException("Pendiente: exportarConversacion");
    }

    // TODO Fase 5: validar extensión/tamaño, copiar con InputStream/OutputStream
    // a media/ y guardar los metadatos del adjunto en el mensaje XML.
    public void adjuntarArchivo(Mensaje mensaje, Path archivoOrigen) throws IOException, JAXBException {
        throw new UnsupportedOperationException("Pendiente: adjuntarArchivo");
    }
}
