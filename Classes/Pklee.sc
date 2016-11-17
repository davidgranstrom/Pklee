// ===========================================================================
// Title         : Pklee
// Description   : A (basic) Klee sequencer pattern implementation
// Copyright (c) : David Granström 2016
// ===========================================================================

Pklee : FilterPattern {
    var <>sequence, <>steps;

    *new {|pattern, sequence, steps|
        ^super.new(pattern).sequence_(sequence).steps_(steps);
    }

    storeArgs {
        ^[ pattern, sequence, steps ];
    }

    filterEvent {|event, val|
        event[\kleeSum] = val;
    }

    embedInStream {|event|
        var val, outEvent, activeSteps;
        var argSteps = steps;
        var stream = pattern.asStream;
		var cleanup = EventStreamCleanup.new;

        loop {
            // get the indices
            activeSteps = argSteps.indicesOfEqual(1) ?? { [] };
            // get the sum of the values
            val = (sequence@activeSteps).sum.clip(0, 1);
            // add it to the output stream
            event = event.copy;
            this.filterEvent(event, val);

            outEvent = stream.next(event);
            if (outEvent.isNil) { ^cleanup.exit(event) };
			cleanup.update(outEvent);
            // advance the sequencer
            argSteps = argSteps.rotate(1);
            event = outEvent.yield;
        }
    }
}
