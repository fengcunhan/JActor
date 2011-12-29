/*
 * Copyright 2011 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */
package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

import java.util.HashMap;

/**
 * The state of a state machine.
 */
public class StateMachine {
    /**
     * The next operation to be executed.
     */
    public int programCounter = 0;

    /**
     * A table of partial results.
     */
    final public HashMap<String, Object> results = new HashMap<String, Object>();

    private _SMBuilder smBuilder;

    private JAIterator it = new JAIterator() {
        @Override
        protected void process(final ResponseProcessor rp1) throws Exception {
            if (programCounter >= smBuilder.operations.size()) rp1.process(new JANull());
            else {
                final _Operation o = smBuilder.operations.get(programCounter);
                programCounter += 1;
                o.call(StateMachine.this, rp1);
            }
        }
    };


    public StateMachine(_SMBuilder smBuilder) {
        this.smBuilder = smBuilder;
    }

    public void execute(ResponseProcessor rp) throws Exception {
        it.iterate(rp);
    }

    final public Object get(Object resultName) {
        return results.get(resultName);
    }
    
    final public Integer resolveLabel(String label) {
        return smBuilder.labels.get(label);
    }

    /**
     * Send a request to an actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void send(Actor actor, Object request, ResponseProcessor rp)
            throws Exception {
        smBuilder.send(actor, request, rp);
    }
}