import java.io.*;
import java.util.*;
import java.net.*;

public class Chat_Server{
    //Globals 
    public static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
    public static ArrayList<String> CurrentUsers = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException {
        try {
            final int PORT = 3323;
            ServerSocket SERVER = new ServerSocket(PORT);
            System.out.println("Waiting for clients to join.....");

            while(true) {
                Socket SOCK = SERVER.accept();
                ConnectionArray.add(SOCK);

                System.out.println("Client connected from : " + SOCK.getLocalAddress().getHostName());
                AddUserName(SOCK);

                Chat_Server_Return CHAT = new Chat_Server_Return(SOCK);
                Thread X = new Thread(CHAT);
                X.start();
            }
        }catch(Exception e){  System.out.println(e); }
        
    } 
    public static void AddUserName(Socket X) throws IOException {
        Scanner INPUT = new Scanner(X.getInputStream());
        String UserName = INPUT.nextLine();
        CurrentUsers.add(UserName);
        
        for(int i = 1; i <= Chat_Server.ConnectionArray.size(); i++ ) {
            Socket SOCK_TEMP = Chat_Server.ConnectionArray.get(i-1);
            PrintStream OUT = new PrintStream(SOCK_TEMP.getOutputStream());
            OUT.println("#?!" + CurrentUsers);
            OUT.flush();
        }
        
    }
}