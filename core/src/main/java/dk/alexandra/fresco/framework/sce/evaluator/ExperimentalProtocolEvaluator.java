package dk.alexandra.fresco.framework.sce.evaluator;

import dk.alexandra.fresco.framework.ProtocolEvaluator;
import dk.alexandra.fresco.framework.ProtocolProducer;
import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.sce.resources.ResourcePool;
import dk.alexandra.fresco.suite.ProtocolSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Protocol evaluator implementation which works by evaluating native protocols in batches of a
 * certain batch size. How each batch is evaluated is up to the given batch evaluation strategy.
 * Each batch is required to contain only functionally independent native protocols.
 *
 * @param <ResourcePoolT> The resource pool type to use
 */
public class ExperimentalProtocolEvaluator<ResourcePoolT extends ResourcePool>
    implements ProtocolEvaluator<ResourcePoolT> {

  private Logger logger = LoggerFactory.getLogger(ExperimentalProtocolEvaluator.class);

  private final BatchEvaluationStrategy<ResourcePoolT> batchEvaluator;
  private final ProtocolSuite<ResourcePoolT, ?> protocolSuite;
  private final int maxBatchSize;

  public ExperimentalProtocolEvaluator(
      BatchEvaluationStrategy<ResourcePoolT> batchEvaluator,
      ProtocolSuite<ResourcePoolT, ?> protocolSuite) {
    this(batchEvaluator, protocolSuite, 4096);
  }

  public ExperimentalProtocolEvaluator(
      BatchEvaluationStrategy<ResourcePoolT> batchEvaluator,
      ProtocolSuite<ResourcePoolT, ?> protocolSuite, int maxBatchSize) {
    this.batchEvaluator = batchEvaluator;
    this.maxBatchSize = maxBatchSize;
    this.protocolSuite = protocolSuite;
  }

  @Override
  public EvaluationStatistics eval(ProtocolProducer protocolProducer, ResourcePoolT resourcePool,
      Network network) {
    int batch = 0;
    int totalProtocols = 0;
    int totalBatches = 0;

    NetworkBatchDecorator networkBatchDecorator = createSceNetwork(resourcePool, network);
    ProtocolSuite.RoundSynchronization<ResourcePoolT> roundSynchronization =
        protocolSuite.createRoundSynchronization();
    do {
      ProtocolCollectionList<ResourcePoolT> protocols = new ProtocolCollectionList<>(maxBatchSize);
      protocolProducer.getNextProtocols(protocols);
      int size = protocols.size();

      roundSynchronization.beforeBatch(protocols, resourcePool, network);
      batchEvaluator.processBatch(protocols, resourcePool, networkBatchDecorator);
      logger.trace("Done evaluating batch: " + batch++ + " with " + size + " native protocols");
      if (size == 0) {
        logger.debug("Batch " + batch + " is empty");
      }
      totalProtocols += size;
      totalBatches += 1;
      roundSynchronization.finishedBatch(size, resourcePool, network);
    } while (protocolProducer.hasNextProtocols());

    roundSynchronization.finishedEval(resourcePool, network);
    return new EvaluationStatistics(totalProtocols, totalBatches);
  }

  private NetworkBatchDecorator createSceNetwork(ResourcePool resourcePool, Network network) {
    return new FakeNetworkBatchDecorator(resourcePool.getNoOfParties(), network);
  }

  private class FakeNetworkBatchDecorator extends NetworkBatchDecorator {

    public FakeNetworkBatchDecorator(int noOfParties, Network network) {
      super(noOfParties, network);
    }

    @Override
    public byte[] receive(int id) {
      return network.receive(id);
    }

    @Override
    public void send(int id, byte[] data) {
      network.send(id, data);
    }

    @Override
    public void flush() {
    }

  }
}