import java.net.*;
import java.io.*;

class MyThread extends Thread{  

	DatagramPacket request;
	DatagramSocket aSocket;
	int counter;

	MyThread(DatagramPacket r, DatagramSocket s, int c) {
		request = r;
		aSocket = s;
		counter = c;
	}

	public void run() {
		try{
			DatagramPacket reply = new DatagramPacket(request.getData(),
			request.getLength(), request.getAddress(), request.getPort());
			// aSocket.send(reply);
			try{
				System.out.println("worker thread:"+counter+"  blocking");
				aSocket.send(reply);
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("worker thread:"+counter+"  DONE!!");
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}
	
};

public class MultiThreadUDPServer{
	public static void main(String args[]){
		DatagramSocket aSocket = null;
		int serverPort = 6789;
		int threadCounter = 0;
		try{
			aSocket = new DatagramSocket(serverPort);
			byte[] buffer = new byte[1000];
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);		// wait for request here.block
				MyThread replyer = new MyThread(request, aSocket, threadCounter);
				replyer.start();
				threadCounter++;
			}
		} catch (SocketException e){
			System.out.println("Socket2: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO2: " + e.getMessage());
		}
	}
}