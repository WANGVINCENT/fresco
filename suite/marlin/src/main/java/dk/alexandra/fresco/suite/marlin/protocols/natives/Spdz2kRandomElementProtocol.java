package dk.alexandra.fresco.suite.marlin.protocols.natives;

import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUInt;
import dk.alexandra.fresco.suite.marlin.resource.Spdz2kResourcePool;

public class Spdz2kRandomElementProtocol<PlainT extends CompUInt<?, ?, PlainT>>
    extends Spdz2kNativeProtocol<SInt, PlainT> {

  private SInt element;

  @Override
  public EvaluationStatus evaluate(int round, Spdz2kResourcePool<PlainT> resourcePool,
      Network network) {
    this.element = resourcePool.getDataSupplier().getNextRandomElementShare();
    return EvaluationStatus.IS_DONE;
  }

  @Override
  public SInt out() {
    return element;
  }

}