package dk.alexandra.fresco.suite.tinytables.util.ot.java;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.suite.tinytables.util.Encoding;
import dk.alexandra.fresco.suite.tinytables.util.ot.OTReceiver;
import dk.alexandra.fresco.suite.tinytables.util.ot.datatypes.OTSigma;
import edu.biu.scapi.exceptions.FactoriesException;
import edu.biu.scapi.exceptions.SecurityLevelException;
import edu.biu.scapi.interactiveMidProtocols.ot.otBatch.OTBatchOnByteArrayROutput;
import edu.biu.scapi.interactiveMidProtocols.ot.otBatch.OTBatchRBasicInput;
import edu.biu.scapi.interactiveMidProtocols.ot.otBatch.semiHonest.OTSemiHonestDDHBatchOnByteArrayReceiver;
import edu.biu.scapi.tools.Factories.KdfFactory;

/**
 * This OTReciever is a wrapper around SCAPI's
 * {@link OTSemiHonestDDHBatchOnByteArrayReceiver} based on an elliptic curve
 * over a finite field, namely the K-163 curve.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 */
public class JavaOTReceiver implements OTReceiver {

	private Network network;
	private int myId;
	private SecureRandom random;

	/*
	 * We keep a singleton of the actual receiver.
	 */
	private static OTSemiHonestDDHBatchOnByteArrayReceiver receiver;
	private OTSemiHonestDDHBatchOnByteArrayReceiver getInstance(SecureRandom random) {
		if (receiver == null) {
			try {
				receiver = new OTSemiHonestDDHBatchOnByteArrayReceiver(
						//new edu.biu.scapi.primitives.dlog.bc.BcDlogECF2m(),
						new edu.biu.scapi.primitives.dlog.openSSL.OpenSSLDlogECF2m(), 
						KdfFactory.getInstance()
						.getObject("HKDF(HMac(SHA-256))"), random);
			} catch (SecurityLevelException | FactoriesException | IOException e) {
				e.printStackTrace();
			}
		} 
		return receiver;
	}
	
	public JavaOTReceiver(Network network, int myId, SecureRandom random) {
		this.network = network;
		this.myId = myId;
		this.random = random;
	}
	
	@Override
	public List<boolean[]> receive(List<OTSigma> sigmas, int expectedLength) {
		
		OTSemiHonestDDHBatchOnByteArrayReceiver receiver = getInstance(random);

		ArrayList<Byte> s = new ArrayList<Byte>();
		for (OTSigma sigma : sigmas) {
			s.add(Encoding.encodeBoolean(sigma.getSigma()));
		}
		OTBatchRBasicInput input = new OTBatchRBasicInput(s);
		
		try {
			OTBatchOnByteArrayROutput output = (OTBatchOnByteArrayROutput) receiver.transfer(
					new NetworkWrapper(network, myId), input);
			
			List<boolean[]> results = new ArrayList<boolean[]>();
			for (int i = 0; i < sigmas.size(); i++) {
				results.add(Arrays.copyOf(Encoding.decodeBooleans(output.getXSigmaArr().get(i)), expectedLength));
			}
			
			return results;
		} catch (ClassNotFoundException | IOException e) {
			// Do nothing
		}
		
		return null;
	}

}
