package com.buihha.audiorecorder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConvertUtil {
    public static void pcm2Mp3(String pcmPath,String mp3Path,int sampleRate,int channels){
       pcm2Mp3(pcmPath, mp3Path, sampleRate, channels,96,6);
    }
    public static void pcm2Mp3(String pcmPath,String mp3Path,int sampleRate,int channels,int outBitrate,int quality){
        SimpleLame.init(sampleRate,channels,sampleRate,outBitrate,quality);
        FileInputStream fis=null;
        FileOutputStream fos=null;
        byte[] bufferSize=new byte[1024];
        try {
           fis=new FileInputStream(pcmPath);
           fos=new FileOutputStream(mp3Path);
           int size=0;
           short[] shortArr;
           byte[] mp3Buffer=new byte[(int) (7200 + bufferSize.length * 2 * 1.25)];
           while (true){
               size=fis.read(bufferSize);
               shortArr=new short[size/2];
               if (size>0){
                   ByteBuffer.wrap(bufferSize).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArr);
                   int encodedSize= SimpleLame.encode(shortArr,shortArr,shortArr.length,mp3Buffer);
                   if (encodedSize>0){
                       fos.write(mp3Buffer,0,encodedSize);
                   }
               }else {
                   SimpleLame.flush(mp3Buffer);
                   fos.write(mp3Buffer);
                   break;
               }
           }

        }catch (Exception e){}
        finally {
            try {
                if (fis!=null){
                    fis.close();
                    fis=null;
                }
                if (fos!=null){
                    fos.close();
                    fos=null;
                }
                SimpleLame.close();
            }catch (Exception e){

            }

        }
    }
}
