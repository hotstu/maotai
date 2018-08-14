package github.hotstu.maotai;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;

import java.net.URI;

import github.hotstu.maotai.engine.MDConfig;
import github.hotstu.maotai.engine.MDSourceType;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Gson gson = new Gson();
        System.out.println(gson.toJson(MDSourceType.ASSETS));
    }

    @Test
    public void testMDConfig() throws Exception {
        MDConfig config = new MDConfig(0);
        String expet = "file:///android_asset/widget/index.html";
        URI uri = URI.create(expet);
        System.out.println(uri.toString());

        Assert.assertEquals(expet,config.getRealPath("index.html") );
        Assert.assertEquals(expet,config.getRealPath("/index.html"));
        //Assert.assertEquals(expet,config.getRealPath("./index.html"));
        Assert.assertEquals(expet,config.getRealPath("wgt:///index.html"));
        Assert.assertEquals(expet,config.getRealPath("wgt:/index.html"));
    }
}