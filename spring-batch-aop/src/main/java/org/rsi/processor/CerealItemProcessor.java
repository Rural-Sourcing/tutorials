package org.rsi.processor;

import org.rsi.model.Cereal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class CerealItemProcessor implements ItemProcessor<Cereal, Cereal> {

    private static final Logger log = LoggerFactory.getLogger(CerealItemProcessor.class);

    public Cereal process(final Cereal cereal) throws Exception {
        final String name = cereal.getName().toUpperCase();
        final int numOfBoxes = cereal.getNumOfBoxes() * 100;

        final Cereal transformedCereal = new Cereal(name, numOfBoxes);

        log.info("Converting (" + cereal + ") into (" + transformedCereal + ")");

        return transformedCereal;
    }

}
