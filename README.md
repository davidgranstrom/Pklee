Pklee
=====

Pklee sequences an array of values according to the principles of a *Klee sequencer*.

The Klee sequencer operates off of a shift register that can have more than one active bit in the register. Meaning that, we can have **multiple active sequencer steps**. If we only have on active step it will behave just like a "regular" sequencer.

The sum of the values will be stored in the special key `\kleeSum`, which can be accessed from the input pattern.

Examples
--------

See the native documentation for more examples.

    (
    p = Pklee(
        Pbind(
            \amp, Pfunc {|event|
                ControlSpec.specs[\amp].map(event.kleeSum);
            },
        ),
        [ 0, 1, 0, 0.5, 0, 0.25, 1, 0 ], // sequence
        [ 1, 0, 1, 0,   0, 0,    0, 0 ], // two active steps
    ).asStream;
    )

    p.next(()); // pass in an event ()
