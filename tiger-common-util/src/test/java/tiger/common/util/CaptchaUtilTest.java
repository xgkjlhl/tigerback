package tiger.common.util;

import org.junit.Before;

import javax.imageio.stream.FileImageOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Jaric Liao on 2015/12/14.
 */
public class CaptchaUtilTest {

    String id;
    String captcha;

    public static void main(String[] args) throws Exception {
        String id = "testID";
        byte[] image = new CaptchaUtil().getImageChallengeForID(id);
        FileImageOutputStream imageOutput = new FileImageOutputStream(new File("captcha.jpg"));//��������
        imageOutput.write(image, 0, image.length);//��byteд��Ӳ��
        imageOutput.close();
        BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
        String captcha = strin.readLine();
        boolean result = new CaptchaUtil().validateImageChallengeForID(id, captcha);
        System.out.print(result);
    }

    @Before
    public void setUp() {
        id = "testID";
    }

//    @Test
//    public void testValidateImageChallengeForID() throws Exception{
//        BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
//        captcha = strin.readLine();
//        boolean result = CaptchaUtil.validateImageChallengeForID(id,captcha);
//        System.out.print(result);
//    }
}
