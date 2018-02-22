package dk.alexandra.fresco.suite.marlin;

import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.sce.evaluator.EvaluationStrategy;
import dk.alexandra.fresco.framework.util.AesCtrDrbg;
import dk.alexandra.fresco.lib.arithmetic.BasicArithmeticTests;
import dk.alexandra.fresco.lib.collections.io.CloseListTests.TestCloseAndOpenList;
import dk.alexandra.fresco.suite.ProtocolSuiteNumeric;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUInt128;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUInt128Factory;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUIntFactory;
import dk.alexandra.fresco.suite.marlin.datatypes.UInt64;
import dk.alexandra.fresco.suite.marlin.resource.MarlinResourcePool;
import dk.alexandra.fresco.suite.marlin.resource.MarlinResourcePoolImpl;
import dk.alexandra.fresco.suite.marlin.resource.storage.MarlinDataSupplier;
import dk.alexandra.fresco.suite.marlin.resource.storage.MarlinOpenedValueStore;
import java.util.function.Supplier;
import org.junit.Test;

public class TestMarlinBasicArithmetic128 extends AbstractMarlinTest<
    UInt64,
    UInt64,
    CompUInt128,
    MarlinResourcePool<UInt64, UInt64, CompUInt128>> {

  @Test
  public void testInput() {
    runTest(new BasicArithmeticTests.TestInput<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testInputThree() {
    runTest(new BasicArithmeticTests.TestInput<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Test
  public void testAdd() {
    runTest(new BasicArithmeticTests.TestAdd<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testAddThree() {
    runTest(new BasicArithmeticTests.TestAdd<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Test
  public void testMultiply() {
    runTest(new BasicArithmeticTests.TestMultiply<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testKnown() {
    runTest(new BasicArithmeticTests.TestKnownSInt<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testKnownThree() {
    runTest(new BasicArithmeticTests.TestKnownSInt<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Test
  public void testMultiplyThree() {
    runTest(new BasicArithmeticTests.TestMultiply<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Test
  public void testInputOutputMany() {
    runTest(new TestCloseAndOpenList<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testInputOutputManyThree() {
    runTest(new TestCloseAndOpenList<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Test
  public void testMultiplyMany() {
    runTest(new BasicArithmeticTests.TestLotsMult<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 2,
        false);
  }

  @Test
  public void testMultiplyManyThree() {
    runTest(new BasicArithmeticTests.TestLotsMult<>(), EvaluationStrategy.SEQUENTIAL_BATCHED, 3,
        false);
  }

  @Override
  protected MarlinResourcePool<UInt64, UInt64, CompUInt128> createResourcePool(int playerId,
      int noOfParties, MarlinOpenedValueStore<CompUInt128> store,
      MarlinDataSupplier<CompUInt128> supplier,
      CompUIntFactory<UInt64, UInt64, CompUInt128> factory, Supplier<Network> networkSupplier) {
    MarlinResourcePool<UInt64, UInt64, CompUInt128> resourcePool =
        new MarlinResourcePoolImpl<>(
            playerId,
            noOfParties, null, store, supplier, factory);
    resourcePool.initializeJointRandomness(networkSupplier, AesCtrDrbg::new, 32);
    return resourcePool;
  }

  @Override
  protected CompUIntFactory<UInt64, UInt64, CompUInt128> createFactory() {
    return new CompUInt128Factory();
  }

  @Override
  protected ProtocolSuiteNumeric<MarlinResourcePool<UInt64, UInt64, CompUInt128>> createProtocolSuite() {
    return new MarlinProtocolSuite<>();
  }


}