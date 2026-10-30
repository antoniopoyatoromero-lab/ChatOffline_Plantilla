package es.iesfranciscodelosrios.chat;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class GestorXML {

    public void guardarUsuarios(ListaUsuarios usuarios) {
        try {

        File archivo = new File("usuario.xml");

        JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);

        Marshaller marshaller = context.createMarshaller();

        marshaller.marshal(usuarios, archivo);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public ListaUsuarios leerUsuarios() {
        try {

        File archivo = new File("usuario.xml");

        JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        ListaUsuarios usuarios;

        if (!archivo.exists()){
            usuarios = new ListaUsuarios();
        } else {
            usuarios = (ListaUsuarios) unmarshaller.unmarshal(archivo);
        }

        return usuarios;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarMensajes(ListaMensajes mensajes) {
        try {


        File archivo = new File("mensaje.xml");

        JAXBContext context = JAXBContext.newInstance(ListaMensajes.class);

        Marshaller marshaller = context.createMarshaller();

        marshaller.marshal(mensajes, archivo);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public ListaMensajes leerMensajes()  {
        File archivo = new File("mensaje.xml");

        try {
            JAXBContext context = JAXBContext.newInstance(ListaMensajes.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            ListaMensajes mensajes;

            if (!archivo.exists()){
                mensajes = new ListaMensajes();
            } else {
                mensajes = (ListaMensajes) unmarshaller.unmarshal(archivo);
            }
            return mensajes;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
