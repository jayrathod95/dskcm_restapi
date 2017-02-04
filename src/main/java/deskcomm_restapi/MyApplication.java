package deskcomm_restapi;

import deskcomm_restapi.exposed.user.MultiPartResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jay Rathod on 02-02-2017.
 */
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register resources and features
        classes.add(MultiPartFeature.class);
        classes.add(MultiPartResource.class);
        classes.add(LoggingFilter.class);
        return classes;
    }
}
