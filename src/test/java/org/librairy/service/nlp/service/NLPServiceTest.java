package org.librairy.service.nlp.service;

import org.junit.Test;
import org.librairy.service.nlp.facade.model.Group;
import org.librairy.service.nlp.facade.model.PoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class NLPServiceTest {


    private static final Logger LOG = LoggerFactory.getLogger(NLPServiceTest.class);

    private NLPServiceImpl service;


    @Test
    public void group() throws IOException {

        String text = "I have a mice at my home";

//        List<PoS> filter = Collections.emptyList();
        List<PoS> filter = Arrays.asList( new PoS[]{PoS.NOUN, PoS.VERB});

        List<Group> groups = service.groups(text, filter, false, false);

        groups.forEach(token -> LOG.info(token.toString()));

//        Assert.assertEquals(2, annotations.size());
    }
}