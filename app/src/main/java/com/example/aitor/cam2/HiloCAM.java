package com.example.aitor.cam2;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import CAM_PDU_Descriptions.CAM;

/**
 * Created by aitor on 16/6/16.
 */
public class HiloCAM extends Thread
{
    private Context c;
    private Comunicaciones com;

    public HiloCAM(Context c,Comunicaciones com)
    {
        this.c = c;
        this.com = com;
    }

    public void run()
    {
        List<CAM> hist = new ArrayList<CAM>();
        Principal p=new Principal(c);
        CAM cur_cam;
        com.conectar();
        VariablesGlobales.stop_check = true;
        VariablesGlobales.camsEnviados = 0;

        while(VariablesGlobales.stop_check)
        {
            cur_cam = p.generarCAM();
            hist.add(cur_cam);
            com.enviarCAM(cur_cam);
            VariablesGlobales.camsEnviados++;
            System.out.println("Cam enviado");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            VariablesGlobales.stop_check=false;
        }
        com.desconectar();
    }
}
