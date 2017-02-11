package deskcomm_restapi.core;

import deskcomm_restapi.exceptions.InvalidParamException;
import org.junit.Test;

/**
 * Created by Jay Rathod on 12-02-2017.
 */
public class UserBuilderTest {
    @Test
    public void build() throws Exception {
        try {
            UserBuilder builder = new UserBuilder();
            builder.setFirstname("Sagar");
            builder.setLastname("Lachure");
            builder.setImage_url("aaaaaaaaaaaaaaa");
            builder.setMobile("1111111111111");
            builder.setUid("121212");
            builder.setPassword("12345");
            builder.setEmail("sl@sl.com");
            builder.build();
        } catch (InvalidParamException e) {
            e.printStackTrace();
        }

    }

}