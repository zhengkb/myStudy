package nio.buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 输出流的使用
 */
public class Buffer02 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\test_storage/nio1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        ByteBuffer bytebuffer = ByteBuffer.allocate((int) file.length());


        fileInputStreamChannel.read(bytebuffer);

        System.out.println(new String(bytebuffer.array()));
        fileInputStream.close();

    }
}
