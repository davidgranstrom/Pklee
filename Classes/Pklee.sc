// ===========================================================================
// Title		 : Pklee
// Description	 : A (basic) Klee sequencer pattern implementation
// Copyright (c) : David Granström 2016 
// ===========================================================================

Pklee : FilterPattern {
    var <>sequence, <>activeSteps;

    *new {|pattern, sequence, activeSteps|
        ^super.new(pattern).sequence_(sequence.normalizeSum).activeSteps_(activeSteps);
    }

    storeArgs {
        ^[ pattern, sequence, activeSteps ];
    }

    filterEvent {|event, val|
        ^event[\kleeSum] = val;
    }

	embedInStream {|event|
        // TODO: Handle sequence and steps as streams
        var eventStream = pattern.asStream;
        var val, filteredEvent, inEvent;

        loop {
            var steps = activeSteps.collect {|step, i|
                // return the index
                (step > 0).if(i, nil);
            }.reject {|idx| idx.isNil };
            // get the sum of the values
            val = (sequence@steps).sum;
            // add it to the output stream
            event = event.copy;
            filteredEvent = this.filterEvent(event, val);
            // advance the sequencer
            activeSteps = activeSteps.rotate(1);

            inEvent = eventStream.next(filteredEvent);
            event = yield(inEvent);
        }
    }
}
