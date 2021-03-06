package com.theboreddev.fixture;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomTest {

    @Test
    public void shouldGenerateARandomAlphanumericOfAGivenSize() {

        String randomAlphaNumeric = Random.randomAlphaNumeric(10);

        assertThat(randomAlphaNumeric).hasSize(10); //TODO - Improve matcher - check that it only contains characters or numbers?
    }

    @Test
    public void shouldGenerateARandomNumber() {

        int randomInt = Random.randomInt(100);

        assertThat(randomInt).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(100);
    }

    @Test
    public void shouldGenerateARandomBigDecimal() {

        BigDecimal bigDecimal = Random.randomBigDecimal(new BigDecimal(100));

        assertThat(bigDecimal).isBetween(BigDecimal.ONE, new BigDecimal(100));
    }
}
