package dk.alexandra.fresco.framework.builder.binary;

import java.util.List;

import dk.alexandra.fresco.framework.Computation;
import dk.alexandra.fresco.framework.util.Pair;
import dk.alexandra.fresco.framework.value.SBool;

public interface BinaryBuilderAdvanced {

  /**
   * Appends a OR protocol to the current protocol.
   *
   * @param left the Computation holding the left argument.
   * @param right the Computation holding the right argument.
   * @return an computation holding the output of the appended protocol.
   */
  Computation<SBool> or(Computation<SBool> left, Computation<SBool> right);
  
  Computation<SBool> or(Computation<SBool> left, boolean right);
  
  
  
  Computation<SBool> xnor(Computation<SBool> left, Computation<SBool> right);
  
  Computation<SBool> xnor(Computation<SBool> left, boolean right);
  
  Computation<SBool> nand(Computation<SBool> left, Computation<SBool> right);
  
  Computation<SBool> nand(Computation<SBool> left, boolean right);
  
  
  /**
   * Appends a conditional select protocol to the current protocol. The output
   * of this protocol on inputs a and b and condition bit c is the bit r := c ?
   * a : b.
   *
   * @param condition the Computation holding the condition on which to select.
   * @param left the Computation holding the left argument.
   * @param right the Computation holding the right argument.
   * @return an computation holding the output of the appended protocol.
   */
  Computation<SBool> condSelect(Computation<SBool> condition, Computation<SBool> left, Computation<SBool> right);

  //TODO create condSelect for arrays
  
  /**
   * Appends a greater-than protocol to the current protocol. The output of this
   * protocol is the greater-than (<) relation between its two input strings.
   *
   * @param left the SBool list holding the left argument.
   * @param right the SBool list holding the right argument.
   * @return an SBool holding the output of the appended protocol.
   */
  Computation<SBool> greaterThan(Computation<List<SBool>> left, Computation<List<SBool>> right);
  
  /**
   * Appends a equals protocol to the current protocol. The output of this
   * protocol is the equals (==) relation between its two input strings.
   *
   * @param left the SBool list holding the left argument.
   * @param right the SBool list holding the right argument.
   * @return an SBool holding the output of the appended protocol.
   */
  Computation<SBool> equals(Computation<List<SBool>> left, Computation<List<SBool>> right);

  /**
   * Appends a copy protocol to the current protocol copying the value of one
   * computation to an other.
   *
   * @param src the source computation
   * @return a computation holding the copy of the source
   */
  Computation<SBool> copy(Computation<SBool> src);
  
  
  /**
   * Appends a keyed compare and swap protocol. This protocol swaps two
   * key-value pairs to that the left pair becomes the pair with the largest
   * key.
   *
   * @param leftKey an SBool array representing the key of the left pair
   * @param leftValue an SBool array representing the value of the left pair
   * @param rightKey an SBool array representing the key of the right pair
   * @param rightValue an SBool array representing the value of the right pair
   */
 //keyedCompareAndSwap(SBool[] leftKey, SBool[] leftValue, SBool[] rightKey, SBool[] rightValue) {

  
  /**
   * Half adder which returns the result in the 0'th position and the carry in the 1'st position of
   * the array.
   * 
   * @param left The first input.
   * @param right The second input.
   * @return A computation which yields the result and the carry.
   */
  Computation<Pair<SBool, SBool>> oneBitHalfAdder(Computation<SBool> left,
      Computation<SBool> right);

  /**
   * Same as {@link #oneBitHalfAdder(Computation, Computation)}, but with an option to also add a
   * potential carry to the addition.
   * 
   * @param left The first input.
   * @param right The second input.
   * @param carry The potential carry from a previous adder.
   * @return A computation which yields the result and the carry.
   */
  Computation<SBool[]> oneBitFullAdder(Computation<SBool> left, Computation<SBool> right,
      Computation<SBool> carry);

  /**
   * Full adder which works with any number of inputs to the addition. The lefts and rights must
   * have the same size. The Output is a computation which contains an array of size n, where n is
   * the length of the lefts and rights inputs + 1, where the 0'th entry is the carry and the rest
   * is the result. An example: lefts: 1100101 rights: 1101001 inCarry: 1 => output:11001111
   * 
   * @param lefts The first inputs.
   * @param rights The second inputs.
   * @param inCarry The potential carry from a previous adder.
   * @return A computation which yields the results and the carry.
   */
  Computation<SBool[]> fullAdder(Computation<SBool[]> lefts, Computation<SBool[]> rights,
      Computation<SBool> inCarry);

  /**
   * Multiplies the left and right numbers and leaves the result in the output. The inputs are not
   * required to have the same size.
   * 
   * @param lefts The left input
   * @param rights The right input
   * @return An array of size lefts.size+rights.size containing the multiplication of the two
   *         numbers.
   */
  Computation<SBool[]> binaryMult(SBool[] lefts, SBool[] rights);

  /**
   * Computes the logarithm base 2 of the input number. It is currently up to the application
   * programmer to check if the input number is 0, since the result will otherwise be 0 which is
   * incorrect as log_2(0) = NaN.
   * 
   * @param number The number to compute log_2 on.
   * @return An array containing the log_2(number).
   */
  Computation<SBool[]> logProtocol(SBool[] number);


}
