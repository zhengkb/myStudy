package nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Buffer03 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\test_storage/nio1.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\test_storage/nio2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            int read = inputStreamChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            System.out.println(read);
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
