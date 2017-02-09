import deskcomm_restapi.support.L;
import deskcomm_restapi.support.PathDecoder;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Jay Rathod on 06-02-2017.
 */
public class PathDecoderTest {
    @Test
    public void getPathParams() {
        Map<Integer, String> pathParams = PathDecoder.getPathParams("message/jay1");
        L.P(pathParams.toString());

    }

}