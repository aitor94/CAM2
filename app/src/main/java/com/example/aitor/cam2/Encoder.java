package com.example.aitor.cam2;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by aitor on 17/6/16.
 */
public class Encoder
{
    public  static String encode(Object object)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(object);
            byte[] data = out.toByteArray();

            obj.close();
            out.close();

            return new String(Base64.encode(data,Base64.DEFAULT));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Object decode(String encodedObject)
    {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(Base64.decode(encodedObject,Base64.DEFAULT));
            ObjectInputStream input = new ObjectInputStream(bin);

            return input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
