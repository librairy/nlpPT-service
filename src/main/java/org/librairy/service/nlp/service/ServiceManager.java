package org.librairy.service.nlp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class ServiceManager {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceManager.class);

    @Value("#{environment['RESOURCE_FOLDER']?:'${resource.folder}'}")
    String resourceFolder;

    @Value("#{environment['SPOTLIGHT_ENDPOINT']?:'${spotlight.endpoint}'}")
    String endpoint;

    @Value("#{environment['SPOTLIGHT_THRESHOLD']?:${spotlight.threshold}}")
    Double threshold;

    @Value("#{environment['PARALLEL']?:${parallel}}")
    Integer maxParallel;

    LoadingCache<String, LemProService> lemProServices;

    LoadingCache<String, DBpediaService> dbpediaServices;

    @PostConstruct
    public void setup(){
        lemProServices = CacheBuilder.newBuilder()
                .maximumSize(maxParallel)
                .build(
                        new CacheLoader<String, LemProService>() {
                            public LemProService load(String key) {
                                LOG.info("Initializing LemPro service for thread: " + key);
                                LemProService service = new LemProService(resourceFolder);
                                service.setup();
                                return service;
                            }
                        });

        IntStream.range(0,maxParallel).forEach(i -> {
            try {
                lemProServices.get("thread" + i);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        dbpediaServices = CacheBuilder.newBuilder()
                .maximumSize(maxParallel)
                .build(
                        new CacheLoader<String, DBpediaService>() {
                            public DBpediaService load(String key) {
                                LOG.info("Initializing DBpedia service for thread: " + key);
                                return new DBpediaService(endpoint, threshold);
                            }
                        });

    }

    public LemProService getLemProService(Thread thread) {
        try {
            return lemProServices.get("thread"+(thread.getId()%maxParallel));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public DBpediaService getDBpediaService(Thread thread) {

        try {
            return dbpediaServices.get("thread"+(thread.getId()%maxParallel));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
