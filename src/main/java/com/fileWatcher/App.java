package com.filewatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException
    {
    	// TODO Auto-generated method stub
    			WatchService watchService = FileSystems.getDefault().newWatchService();
    			System.out.println(System.getProperty("user.home"));

    			Path path = Paths.get(System.getProperty("user.home"));

    			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

//    			multiThread(watchService, path);
    			
    			List<String> abc = new ArrayList<String>();
    			System.out.println(abc.stream().findFirst().get());

    		}

    		public static void multiThread(WatchService watchService, Path path) throws InterruptedException, IOException {
    			WatchKey key;

    			while ((key = watchService.take()) != null) {
    	System.out.println(System.currentTimeMillis() % 1000);
    				for (WatchEvent<?> event : key.pollEvents()) {
    					System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
    					lockFile(Paths.get(System.getProperty("user.home") + '/' + event.context()));
    				}
    				key.reset();
    			}
    		}

    		public static void lockFile(Path path) throws IOException, InterruptedException {
    			FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.READ);
    			FileLock lock = fileChannel.lock();
    			System.out.println(new Date());
    			System.out.println("Lock acquired: " + lock.isValid());
    			System.out.println("Lock is shared: " + lock.isShared());
    			ByteBuffer buffer = ByteBuffer.allocate(20);
    			int noOfBytesRead = fileChannel.read(buffer);
    			System.out.println("Buffer contents: ");

    			while (noOfBytesRead != -1) {

    				buffer.flip();
    				System.out.print("    ");

    				while (buffer.hasRemaining()) {

    					System.out.print((char) buffer.get());
    				}

    				System.out.println(" ");

    				buffer.clear();
//    				Thread.sleep(1000);
    				noOfBytesRead = fileChannel.read(buffer);
    			}

    			fileChannel.close(); // also releases the lock
    			System.out.print("Closing the channel and releasing lock.");
    			System.out.println(new Date());
    		}

}
