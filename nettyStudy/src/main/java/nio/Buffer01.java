package nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Buffer01 {
    public static void main(String[] args) throws IOException {
        String str = "愿君长似少年时，初心不忘乐相知";

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\test_storage/nio1.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(str.getBytes().length);
        byteBuffer.put(str.getBytes());

        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }
}
