package github.hotstu.maotai;

import junit.framework.Assert;

import org.junit.Test;

import java.net.URI;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //URI uri = URI.create("FILE:///android_asset/widget");
        String s = "intent://scan/#Intent;scheme=zxing;package=com.google.zxing.client.android;end";
        URI uri = URI.create(s);
        Assert.assertEquals(uri.toString(),s);
        System.out.println("getScheme-->"+uri.getScheme());
        System.out.println("getSchemeSpecificPart-->"+uri.getSchemeSpecificPart());
        System.out.println("getAuthority-->"+uri.getAuthority());
        System.out.println("getUserInfo-->"+uri.getUserInfo());
        System.out.println("getFragment-->"+uri.getFragment());
        System.out.println("getHost-->"+uri.getHost());
        System.out.println("getPath-->"+uri.getPath());
        System.out.println("getPort-->"+uri.getPort());
        System.out.println("getQuery-->"+uri.getQuery());
        System.out.println(new URI("file", "", "/android_asset/widget", null).toString());
    }

    @Test
    public void testJson()  {
        String s = "javascript:_JSNativeBridge._handleMessageFromNative('{\"responseId\":\"cb_8_1532846768584\",\"data\":{\"values\":{\"ret\":\"[\\\"\\/sdcard\\/data\\/.push_deviceid\\\"]\",\"err\":\"null\"}}}')";
        String ss = String.format("--%s--", s);
        System.out.println(ss);
    }

    @Test
    public void testEncode() {
        String s = "a";
        //String s = "\u00a0";
        String ret = String.format("\\u%04x", (int) s.charAt(0));// --> \u00a0
        System.out.println(ret);
        System.out.println((int) s.charAt(0));
        System.out.println("\u6c49");
        System.out.println("\037");
    }
}