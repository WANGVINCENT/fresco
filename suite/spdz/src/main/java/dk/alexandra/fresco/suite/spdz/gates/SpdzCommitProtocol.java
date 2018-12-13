package dk.alexandra.fresco.suite.spdz.gates;

import dk.alexandra.fresco.framework.MaliciousException;
import dk.alexandra.fresco.framework.builder.numeric.field.FieldDefinition;
import dk.alexandra.fresco.framework.builder.numeric.field.FieldElement;
import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.suite.spdz.SpdzResourcePool;
import dk.alexandra.fresco.suite.spdz.datatypes.SpdzCommitment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpdzCommitProtocol extends SpdzNativeProtocol<Map<Integer, FieldElement>> {

  private SpdzCommitment commitment;
  private Map<Integer, FieldElement> comms;
  private byte[] broadcastDigest;

  public SpdzCommitProtocol(SpdzCommitment commitment) {
    this.commitment = commitment;
    this.comms = new HashMap<>();
  }

  @Override
  public Map<Integer, FieldElement> out() {
    return comms;
  }

  @Override
  public EvaluationStatus evaluate(int round, SpdzResourcePool spdzResourcePool,
      Network network) {
    int players = spdzResourcePool.getNoOfParties();
    FieldDefinition definition = spdzResourcePool.getFieldDefinition();
    if (round == 0) {
      network.sendToAll(definition.serialize(commitment.computeCommitment(definition)));
      return EvaluationStatus.HAS_MORE_ROUNDS;
    } else if (round == 1) {

      List<byte[]> commitments = network.receiveFromAll();
      for (int i = 0; i < commitments.size(); i++) {
        comms.put(i + 1, definition.deserialize(commitments.get(i)));
      }
      if (players < 3) {
        return EvaluationStatus.IS_DONE;
      } else {
        broadcastDigest = sendBroadcastValidation(
            definition,
            spdzResourcePool.getMessageDigest(), network, comms.values()
        );
        return EvaluationStatus.HAS_MORE_ROUNDS;
      }
    } else {
      if (!receiveBroadcastValidation(network, broadcastDigest)) {
        throw new MaliciousException(
            "Malicious activity detected: Broadcast of commitments was not validated.");
      }
      return EvaluationStatus.IS_DONE;
    }
  }
}
