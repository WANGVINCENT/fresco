package dk.alexandra.fresco.logging.binary;

import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.builder.binary.Comparison;
import dk.alexandra.fresco.framework.value.SBool;
import dk.alexandra.fresco.logging.PerformanceLogger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryComparisonLoggingDecorator implements PerformanceLogger, Comparison {

  public static final String ID = "PARTY_ID";
  public static final String BINARY_COMPARISON_EQ = "EQ_COUNT";
  public static final String BINARY_COMPARISON_GT = "GT_COUNT";

  private Comparison delegate;
  private int gtCount;
  private int eqCount;
  
  public BinaryComparisonLoggingDecorator(Comparison delegate) {
    this.delegate = delegate;
  }
  
  @Override
  public DRes<SBool> greaterThan(List<DRes<SBool>> inLeft, List<DRes<SBool>> inRight) {
    this.gtCount++;
    return this.delegate.greaterThan(inLeft, inRight);
  }

  @Override
  public DRes<SBool> equal(List<DRes<SBool>> inLeft, List<DRes<SBool>> inRight) {
    this.eqCount++;
    return this.delegate.equal(inLeft, inRight);
  }

  @Override
  public void reset() {
    this.gtCount = 0;
    this.eqCount = 0;
  }
  
  public void setDelegate(Comparison comp) {
    this.delegate = comp;
  }

  @Override
  public Map<String, Object> getLoggedValues(int myId) {
    Map<String, Object> values = new HashMap<>();
    values.put(ID, myId);
    values.put(BINARY_COMPARISON_EQ, this.eqCount);
    values.put(BINARY_COMPARISON_GT, this.gtCount);
    return values;
  }

}
