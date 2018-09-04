package org.librairy.service.nlp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.librairy.service.nlp.Application;
import org.librairy.service.nlp.facade.model.Group;
import org.librairy.service.nlp.facade.model.PoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class NLPServiceTest {


    private static final Logger LOG = LoggerFactory.getLogger(NLPServiceTest.class);

    @Autowired
    private NLPServiceImpl service;


    @Test
    public void group() throws IOException {

        String text = "Na avaliação das complexas atitudes que as pessoas têm em relação ao meio ambiente , considera-se que os modelos baseados em uma estrutura multidimensional podem ser mais apropriados do que abordagens unidimensionais ou dicotômicas";

//        List<PoS> filter = Collections.emptyList();
        List<PoS> filter = Arrays.asList( new PoS[]{PoS.NOUN, PoS.VERB});

        List<Group> groups = service.groups(text, filter, false, false);

        groups.forEach(token -> LOG.info(token.toString()));

//        Assert.assertEquals(2, annotations.size());
    }
}