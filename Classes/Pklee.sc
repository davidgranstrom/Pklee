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
        ^event[\kleeSum] = val;
    }

    embedInStream {|event|
        // TODO: Handle sequence and steps as streams
        var eventStream = pattern.asStream;
        var val, filteredEvent, inEvent, inSteps;

        loop {
            // return the indices
            inSteps = steps.indicesOfEqual(1);
            // get the sum of the values
            val = (sequence@inSteps).sum.clip(0, 1);
            // add it to the output stream
            event = event.copy;
            filteredEvent = this.filterEvent(event, val);
            // advance the sequencer
            steps = steps.rotate(1);

            inEvent = eventStream.next(filteredEvent);
            event = yield(inEvent);
        }
    }
}
