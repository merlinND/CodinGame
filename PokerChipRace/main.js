/**
 * It's the survival of the biggest!
 * Propel your chips across a frictionless table top to avoid getting eaten by bigger foes.
 * Aim for smaller oil droplets for an easy size boost.
 * Tip: merging your chips will give you a sizeable advantage.
 **/

// Player identifiers range from 0 to 4
var playerId = parseInt(readline());

// Main game loop
while (true) {
  // The number of chips under your control
  var playerChipCount = parseInt(readline());
  // The total number of entities on the table, including your chips
  var entityCount = parseInt(readline());

  // ----- Input
  for (var i = 0; i < entityCount; i++) {
    var inputs = readline().split(' ');

    // Unique identifier for this entity
    var id = parseInt(inputs[0]);
    // The owner of this entity (-1 for neutral droplets)
    var player = parseInt(inputs[1]);
    // The current radius of this entity
    var radius = parseFloat(inputs[2]);

    // Position (x from 0 to 799, y from 0 to 514)
    var x = parseFloat(inputs[3]);
    var y = parseFloat(inputs[4]);
    // Speed
    var vx = parseFloat(inputs[5]);
    var vy = parseFloat(inputs[6]);
  }

  // ----- Game logic

  // ----- Output
  // To debug: printErr('Debug messages...');
  // Write an action using print()
  for (var i = 0; i < playerChipCount; i++) {
    // One instruction per chip: 2 real numbers (x y) for a propulsion, or 'WAIT' to stay still
    // TODO
    print('0 0');
  }
}
