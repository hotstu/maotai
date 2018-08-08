package github.hotstu.maotai;

import com.google.gson.Gson;

import org.junit.Test;

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
}