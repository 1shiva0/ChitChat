import java.io.*;
import java.util.*;
import java.net.*;

public class Chat_Server_Return implements Runnable {
    //Globals
    Socket SOCK;
    private Scanner INPUT;
    private PrintWriter OUT;
    String MESSAGE = "";
    
    public Chat_Server_Return(Socket X) {
        this.SOCK = X;
    }
    public void CheckConnection() throws IOException {
        if(!SOCK.isConnected()) {
            for (int i = 0; i < Chat_Server.ConnectionArray.size(); i++) {
                if (Chat_Server.ConnectionArray.get(i) == SOCK) {
                    Chat_Server.ConnectionArray.remove(i);
                }
            }

            for (int i = 1; i <= Chat_Server.ConnectionArray.size(); i++) {
                Socket TEMP_SOCK = (Socket) Chat_Server.ConnectionArray.get(i - 1);
                PrintStream TEMP_OUT = new PrintStream(TEMP_SOCK.getOutputStream());
                TEMP_OUT.println(TEMP_SOCK.getLocalAddress().getHostName() + " disconnected!");
                TEMP_OUT.flush();
                //on Server
                System.out.println(TEMP_SOCK.getLocalAddress().getHostName() + " disconnected!");
            }
        }
    }

    public void run() {
        try { 
            try {
                INPUT = new Scanner(SOCK.getInputStream());
                OUT = new PrintWriter(SOCK.getOutputStream());

                while(true) {
                    CheckConnection();

                    if(!INPUT.hasNext()) { return;}
                    
                    MESSAGE = INPUT.nextLine();
                    System.out.println("Client said : " + MESSAGE);

                    for(int i = 1; i <= Chat_Server.ConnectionArray.size(); i++) {
                        Socket TEMP_SOCK = (Socket) Chat_Server.ConnectionArray.get(i - 1);
                        PrintStream TEMP_OUT = new PrintStream(TEMP_SOCK.getOutputStream());
                        TEMP_OUT.println(MESSAGE);
                        TEMP_OUT.flush();
                        System.out.println("Send to: " + TEMP_SOCK.getLocalAddress().getHostName());
                    }
                }
            }finally {
                SOCK.close();
            }
        }catch(Exception e) { System.out.println(e);}
    }

}