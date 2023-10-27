package loadCplus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class ZXingBarcodeReader {

  public interface ZXingLibrary extends Library {
    ZXingLibrary INSTANCE = (ZXingLibrary) Native.loadLibrary("ZXingJava", ZXingLibrary.class);

    String Java_com_zxingcpp_BarcodeReader_readYBuffer(Pointer env, Pointer thiz, Pointer yBuffer,
        int rowStride, int left, int top, int width, int height, int rotation, String formats,
        boolean tryHarder, boolean tryRotate, boolean tryInvert, boolean tryDownscale,
        Pointer result);

    String[] JavaReadBarcodes(byte[] data, int width, int height, int format, String[] hints);
  }

  public static void main(String[] args) {
    File file = new File("/home/eduardo/Documentos/Data-Matrix-Detector/Image01.jpg");
    try {
      BufferedImage buffImage = ImageIO.read(file);
      byte[] data = Files.readAllBytes(file.toPath());

      int width = buffImage.getWidth(), height = buffImage.getHeight();
      int rowStride = width;

      ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
      buffer.put(data);
      buffer.rewind();

      Pointer yBuffer = new Memory(buffer.capacity());
      yBuffer.write(0L, data, buffer.capacity(), 0);

      Pointer result = new Memory(4096);
      System.out.println(yBuffer);
      String resultCode = ZXingLibrary.INSTANCE.Java_com_zxingcpp_BarcodeReader_readYBuffer(null,
          null, yBuffer, rowStride, 0, 0, width, height, 0, "", false, false, false, false, result);

      if (resultCode != null) {
        System.out.println(resultCode);
      } else {
        System.out.println("Erro ao decodificar o código de barras: " + resultCode);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
