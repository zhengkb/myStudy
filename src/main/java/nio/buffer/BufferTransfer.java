package nio.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BufferTransfer {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("d:\\test_storage/t1.png");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\test_storage/t2.png");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());
        fileInputStream.close();
        fileOutputStream.close();
    }
}
