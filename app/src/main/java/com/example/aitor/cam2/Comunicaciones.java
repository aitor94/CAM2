package com.example.aitor.cam2;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;

import CAM_PDU_Descriptions.*;

/**
 * Created by aitor on 16/6/16.
 */

public class Comunicaciones
{
    private String server;
    private int port;
    private Socket s;
    private ServerSocket ss;

    public Comunicaciones(String server,int port)
    {
        this.port = port;
        this.server = server;
    }

    public void conectar()
    {
        try
        {
            System.out.println("Conectando");
            s = new Socket(server,port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void enviarCAM(CAM cam)
    {
        try
        {
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(os);

            //ObjectMapper mapper = new ObjectMapper();

            //String json = mapper.writeValueAsString(cam);

            oo.writeObject(cam);

            System.out.println("cam enviado con exito");

            oo.close();
            os.close();
            s.close();
            VariablesGlobales.envio = true;
        }
        catch (IOException e)
        {
            System.out.println("error donde siempre");
            VariablesGlobales.envio = false;
            VariablesGlobales.error = e;
        }
    }

    public void recibirCAM()
    {
        try
        {
            ss = new ServerSocket(port);
            System.out.println(ss.getInetAddress().toString());
            System.out.println(ss.isBound());
            Socket sck = ss.accept();

            InputStream is = sck.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            System.out.println("esperando recepcion");
            CAM cam = (CAM)ois.readObject();
            System.out.println("cam recibido->"+cam);
            VariablesGlobales.cam = cam;

            ois.close();
            is.close();
            sck.close();
            ss.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void desconectar()
    {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
