/**
 * It's the survival of the biggest!
 * Propel your chips across a frictionless table top to avoid getting eaten by bigger foes.
 * Aim for smaller oil droplets for an easy size boost.
 * Tip: merging your chips will give you a sizeable advantage.
 **/

var playerId = parseInt(readline()); // your id (0 to 4)

// game loop
while (true) {
    var playerChipCount = parseInt(readline()); // The number of chips under your control
    var entityCount = parseInt(readline()); // The total number of entities on the table, including your chips
    for (var i = 0; i < entityCount; i++) {
        var inputs = readline().split(' ');
        var id = parseInt(inputs[0]); // Unique identifier for this entity
        var player = parseInt(inputs[1]); // The owner of this entity (-1 for neutral droplets)
        var radius = parseFloat(inputs[2]); // the radius of this entity
        var x = parseFloat(inputs[3]); // the X coordinate (0 to 799)
        var y = parseFloat(inputs[4]); // the Y coordinate (0 to 514)
        var vx = parseFloat(inputs[5]); // the speed of this entity along the X axis
        var vy = parseFloat(inputs[6]); // the speed of this entity along the Y axis
    }
    for (var i = 0; i < playerChipCount; i++) {

        // Write an action using print()
        // To debug: printErr('Debug messages...');

        print('0 0'); // One instruction per chip: 2 real numbers (x y) for a propulsion, or 'WAIT'.
    }
}
