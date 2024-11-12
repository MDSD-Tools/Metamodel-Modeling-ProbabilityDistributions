package tools.mdsd.probdist.api.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.parser.ParameterParser.Sample;
import tools.mdsd.probdist.distributionfunction.DistributionfunctionFactory;
import tools.mdsd.probdist.distributionfunction.ParameterType;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;

public class DefaultParameterParserTest {
    private DefaultParameterParser parser;

    private SimpleParameter param;

    @Before
    public void setUp() {
        param = DistributionfunctionFactory.eINSTANCE.createSimpleParameter();

        parser = new DefaultParameterParser();
    }

    @Test
    public void parseSampleSpaceOne() {
        param.setType(ParameterType.SAMPLESPACE);
        param.setValue("{1,1}");

        Set<Sample> actualSamples = parser.parseSampleSpace(param);

        Sample[] expectedSamples = Arrays.array(createSample("1", 1.0));
        assertThat(actualSamples).contains(expectedSamples);
    }

    @Test
    public void parseSampleSpaceMulti() {
        param.setType(ParameterType.SAMPLESPACE);
        param.setValue("{-2,0.2};{-1,0.2};{0,0.2};{1,0.2};{2,0.2}");

        Set<Sample> actualSamples = parser.parseSampleSpace(param);

        Sample[] expectedSamples = Arrays.array(createSample("-2", 0.2), createSample("-1", 0.2),
                createSample("0", 0.2), createSample("1", 0.2), createSample("2", 0.2));
        assertThat(actualSamples).contains(expectedSamples);
    }

    private Sample createSample(String value, double probability) {
        return new Sample(CategoricalValue.create(value), probability);
    }
}
