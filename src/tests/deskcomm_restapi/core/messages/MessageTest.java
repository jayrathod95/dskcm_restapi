package deskcomm_restapi.core.messages;

import org.junit.Test;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class MessageTest {
    @Test
    public void saveToDatabase() throws Exception {
        Message message=new PersonalMessage(
                "aaaaaaaaaaaaa",
                "81a8b89e-cff0-41e9-b6b6-3e57d493012a",
                "81a8b89e-cff0-41e9-b6b6-3e57d493012a",
                "qqqqqqqqqqqq"
        );
        message.saveToDatabase();

    }

}