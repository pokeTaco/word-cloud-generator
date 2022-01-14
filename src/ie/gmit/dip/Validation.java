package ie.gmit.dip;

/**
 * <p>A description of the process used to safely interpret an unreliable object of type <code>U</code> as an object of another type <code>V</code> and verify that it meets <i>custom requirements</i>.</p>
 *
 * @param <U> The unreliable type.
 * @param <V> The verifiable type.
 * @version 1.0
 * @since 1.8
 */
public interface Validation<U, V> {
    /**
     * <p>Recasts the unreliable type and returns an object of the verifiable type.</p>
     *
     * @param unreliable An object that claims to be valid.
     * @return An object of verifiable type V which is assigned a type cast or interpretation of <code>unreliable</code> as an object of type V.
     */
    V recast(U unreliable);

    /**
     * <p>Verifies that the object obtained by the class meets a set of custom requirements.</p>
     *
     * @return True, if the verifiable type object meets the requirements; false, if it does not.
     */
    boolean validate(V verifiable);

    /**
     * <p>Returns the verifiable type object, if it meets the requirements; <code>null</code>, if it does not.</p>
     *
     * @return The verifiable type object, or <code>null</code>.
     */
    V result();
}