package com.example.aitor.cam2;

/**
 * Created by aitor on 3/7/16.
 */
public class Hilo_recepcion extends Thread
{
    int port;

    public Hilo_recepcion(int port)
    {
        this.port = port;
    }

    public void run()
    {
        Comunicaciones c = new Comunicaciones("",port);
        c.recibirCAM();

        System.out.println("cam bien recibido!!!!!!!!");
    }
}
