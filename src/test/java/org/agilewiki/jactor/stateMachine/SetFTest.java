package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class SetFTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new SetF1(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(future.send(actor, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class SetF1 extends JLPCActor {

        SetF1(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._set(new ObjectFunc() {
                @Override
                public Object get(StateMachine stateMachine) {
                    System.out.println("Hello world!");
                    return null;
                }
            });
            smb._set(new ObjectFunc() {
                @Override
                public Object get(StateMachine stateMachine) {
                    return "42";
                }
            }, "r1");
            smb._return(new ObjectFunc() {
                @Override
                public Object get(StateMachine sm) {
                    return sm.get("r1");
                }
            });
            smb.call(rp);
            //Output:
            //Hello world!
            //42
        }
    }
}
