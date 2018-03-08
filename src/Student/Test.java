/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

/**
 *
 * @author Hamid
 */
public class Test {

    public static void main(String args[]) {

        Thread queryThread = new Thread() {
            public void run() {
                ConnectToServer1 client2obj = new ConnectToServer1("127.0.0.1", "Turab");
                client2obj.startProcess();
            }
        };
        queryThread.start();

        Thread queryThread2 = new Thread() {

            public void run() {
                ConnectToServer client1obj = new ConnectToServer("127.0.0.1", "Ghuffran");
                client1obj.startProcess();
            }
        };
        queryThread2.start();

    }
}
