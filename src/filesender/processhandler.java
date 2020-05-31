/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filesender;
import java.net.*;
import java.io.*;


/**
 *
 * @author Randomvi
 */
public class processhandler {
    
  private final int port=9080;
  private String realfilename;
  
    public void Sender(String path,String name,String serveraddress)
    {    
        String fullpath=path+name;
        try {
            Socket client=new Socket(serveraddress, port);             
         int count;
            // use buffers instead ::: data sender
            byte[] buffer=new byte[1024];
          FileInputStream freader=new FileInputStream(fullpath);
          OutputStream out=client.getOutputStream();  

          while((count=freader.read(buffer))>0)
          { 
              out.write(buffer,0, count); 
          } 
            out.flush();
            out.close(); 
           freader.close();
            System.out.println("Data was sent sent");
            client.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
    }
   
    public void Reciever(String pathe)
    {
        String fullpath;
        String filenameFromTheNet;// file name we got from the network
        try {
            ServerSocket server=new ServerSocket(port);
         
            Socket client=server.accept();
    
            // have to use the buffer because its faster
            InputStream iput=client.getInputStream();
             fullpath=pathe;
              
            FileOutputStream fwriter=new FileOutputStream(fullpath);
            // byte and buffered reciever
             byte[] buffer=new byte[1024];
            BufferedOutputStream bos=new BufferedOutputStream(fwriter);
            int count=0;
              while((count=iput.read(buffer))>0)
              {
                  fwriter.write(buffer);
                  fwriter.flush();
              }
              bos.flush();
              
            //.......................................................
            fwriter.close();   
            iput.close();;
            client.close();
            server.close();
            
        } catch (IOException e) {
             System.out.println(e.getMessage());     
        }
    }
    
    public String getIp() throws UnknownHostException
    {
         InetAddress ipAddress;
         ipAddress=InetAddress.getLocalHost();
        String ipString=ipAddress.getHostAddress();
        String[]ips=ipString.split("\\.");
       
        String newip=ips[0]+"."+ips[1]+"."+ips[2]+".";
       System.out.println(newip);
        for (int i = 0; i < 254; i++) {
            String ipp=newip+""+i;
            try {
                if(InetAddress.getByAddress(ipp.getBytes()).isReachable(3000)==true)
                       {
                         // System.out.println(ipp);
                       } 
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return newip;
    }
    
    // Create a full socket just to send the filename first to the other host
 
    public void FilenameSender(String address,String filename,String pathsize)
    {   
        File sizesend=new File(pathsize+filename);
       long fsize= sizesend.length();
        
        try {
            Socket client=new Socket(address,port);            
      DataOutputStream dos=new DataOutputStream(client.getOutputStream());
          dos.writeBytes(fsize+"#"+filename);
                dos.flush();   
                dos.close();
                client.close();
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
   
    String filesize;
    
    public String FilenameGetter()
    {
        String filenameFromTheNet;
        String[]split;
        try {
            ServerSocket server=new ServerSocket(port);
           
           Socket client=server.accept();
           DataInputStream dis=new DataInputStream(client.getInputStream());// gets the file name
           filenameFromTheNet= dis.readLine();// set the file name
          client.close();
          server.close();
            
           System.out.println(filenameFromTheNet);// check if we are getting the name  
           split=filenameFromTheNet.split("#");
           realfilename=split[1];
           filesize=split[0];
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return realfilename;
    }
    public long FileSive()
    {
        long c=Long.parseLong(filesize);
        return c;
    }
    
}
