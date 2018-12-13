package dk.alexandra.fresco.lib.statistics;

import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.builder.numeric.field.BigIntegerFieldDefinition;
import dk.alexandra.fresco.framework.builder.numeric.field.BigIntegerModulus;
import dk.alexandra.fresco.framework.util.ModulusFinder;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.lib.statistics.DeaSolver.AnalysisType;
import dk.alexandra.fresco.suite.dummy.arithmetic.DummyArithmeticSInt;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomDataDeaTest {

  private List<List<DRes<SInt>>> inputValues = new ArrayList<>();
  private List<List<DRes<SInt>>> outputValues = new ArrayList<>();
  private List<List<DRes<SInt>>> inputBasis = new ArrayList<>();
  private List<List<DRes<SInt>>> outputBasis = new ArrayList<>();
  private BigIntegerModulus modulus = new BigIntegerModulus(ModulusFinder.findSuitableModulus(8));
  private BigIntegerFieldDefinition definition = new BigIntegerFieldDefinition(modulus);

  @Before
  public void setup() {
    inputValues = new ArrayList<>();
    outputValues = new ArrayList<>();
    inputBasis = new ArrayList<>();
    outputBasis = new ArrayList<>();
    inputValues.add(new ArrayList<>());
    outputValues.add(new ArrayList<>());
    inputBasis.add(new ArrayList<>());
    outputBasis.add(new ArrayList<>());
  }

  @Test
  public void testConsistentData() {
    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues,
          inputBasis,
          outputBasis);
    } catch (IllegalArgumentException e) {
      Assert.fail("Consistent data should be accepted");
    }
  }

  @Test
  public void testBasisMismatch() {
    inputBasis.add(new ArrayList<>());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues,
          inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testQueryMismatch() {
    inputValues.add(new ArrayList<>());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues,
          inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testInputMismatchWithBasis() {
    inputValues.get(0).add(getValue());
    inputBasis.get(0).add(getValue());
    inputBasis.get(0).add(getValue());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  private DummyArithmeticSInt getValue() {
    return new DummyArithmeticSInt(definition.createElement(1));
  }

  @Test
  public void testOutputMismatchWithBasis() {
    outputValues.get(0).add(getValue());
    outputBasis.get(0).add(getValue());
    outputBasis.get(0).add(getValue());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testInconsistentInputBasis() {
    outputBasis.add(new ArrayList<>());
    inputValues.get(0).add(getValue());
    inputValues.get(0).add(getValue());
    inputBasis.add(new ArrayList<>());
    inputBasis.get(0).add(getValue());
    inputBasis.get(0).add(getValue());
    inputBasis.get(1).add(getValue());
    inputBasis.get(1).add(getValue());
    inputBasis.get(1).add(getValue());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testInconsistentOutputBasis() {
    inputBasis.add(new ArrayList<>());
    outputValues.get(0).add(getValue());
    outputValues.get(0).add(getValue());
    outputBasis.add(new ArrayList<>());
    outputBasis.get(0).add(getValue());
    outputBasis.get(0).add(getValue());
    outputBasis.get(1).add(getValue());
    outputBasis.get(1).add(getValue());
    outputBasis.get(1).add(getValue());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues,
          inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testIncosistentOutputValues() {
    outputBasis.get(0).add(getValue());
    outputBasis.get(0).add(getValue());
    outputValues.get(0).add(getValue());
    outputValues.get(0).add(getValue());
    outputValues.get(0).add(getValue());

    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Inconsistent data should not be accepted");
    } catch (IllegalArgumentException e) {
      Assert.assertThat(e.getMessage(), Is.is("Inconsistent dataset / query data"));
    }
  }

  @Test
  public void testEmptyDataset() {
    inputValues = new ArrayList<>();
    outputValues = new ArrayList<>();
    inputBasis = new ArrayList<>();
    outputBasis = new ArrayList<>();
    //  inputBasis.add(new ArrayList<SInt>()); //Changed
    //  outputBasis.add(new ArrayList<SInt>()); //changed
    inputValues.add(new ArrayList<>());
    outputValues.add(new ArrayList<>());
    outputValues.get(0).add(getValue());
    inputValues.get(0).add(getValue());
    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Empty data should not be accepted");
    } catch (Exception e) {
      //  Assert.assertThat(e.getMessage(), Is.is("Empty dataset / query data"));
    }
  }

  @Test
  public void testEmptyQuery() {
    inputValues = new ArrayList<>();
    outputValues = new ArrayList<>();
    inputBasis = new ArrayList<>();
    outputBasis = new ArrayList<>();
    inputBasis.add(new ArrayList<>());
    outputBasis.add(new ArrayList<>());
    inputValues.add(new ArrayList<>()); //Changed
    outputValues.add(new ArrayList<>()); //changed
    outputBasis.get(0).add(getValue());
    inputBasis.get(0).add(getValue());
    try {
      new DeaSolver(DeaSolver.AnalysisType.INPUT_EFFICIENCY, inputValues, outputValues, inputBasis,
          outputBasis);
      Assert.fail("Empty data should not be accepted");
    } catch (Exception e) {
      //Assert.assertThat(e.getMessage(), Is.is("Empty dataset / query data"));
    }
  }

  @Test
  public void testAnalysisType() {
    Assert
        .assertThat(DeaSolver.AnalysisType.INPUT_EFFICIENCY.toString(), Is.is("INPUT_EFFICIENCY"));
    Assert.assertThat(DeaSolver.AnalysisType.valueOf("INPUT_EFFICIENCY"),
        Is.is(AnalysisType.INPUT_EFFICIENCY));
  }
}
