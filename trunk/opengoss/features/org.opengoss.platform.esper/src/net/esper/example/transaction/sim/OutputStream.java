/*
 * Created on Apr 23, 2006
 *
 */
package net.esper.example.transaction.sim;

import java.io.IOException;
import java.util.List;

import net.esper.example.transaction.TxnEventBase;

/** Interface to output events in your preferred format.
 * 
 *
 */
public interface OutputStream {
    public void output(List<TxnEventBase> bucket) throws IOException;
}
