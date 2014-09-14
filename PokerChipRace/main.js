/**
 * It's the survival of the biggest!
 * Propel your chips across a frictionless table top to avoid getting eaten by bigger foes.
 * Aim for smaller oil droplets for an easy size boost.
 * Tip: merging your chips will give you a sizeable advantage.
 **/

var debug = function() {
  var args = arguments;
  var message = Object.keys(args).map(function(arg) { return args[arg]; }).join(' ');
  printErr(message, '\n');
};

var distance = function(from, to) {
  return Math.sqrt( Math.pow(from.x - to.x, 2) + Math.pow(from.y - to.y, 2) );
};

var getMyEntities = function(allEntities) {
  return allEntities.filter(function(entity) {
    return (entity.owner == playerId);
  });
};

/**
 * @return {Object|null} The entity with this id or null if not found (e.g. has been eaten)
 */
var getEntityById = function(allEntities, id) {
  for(var i = allEntities.length - 1; i >= 0; i--) {
    if(allEntities[i].id == id) {
      return allEntities[i];
    }
  }
  return null;
};

var isEatable = function(mine, target) {
  // TODO: take into account the prospective size
  // (i.e. the size after using matter to reach the target)
  return (mine.radius > target.radius);
};

/**
 * @TODO Do not target an entity which is fleeing away
 *
 * @warning Will return `null` if no entity is eatable
 */
var getNearestEatableEntity = function(entities, player) {
  var eatable = entities.filter(function(entity) {
    return isEatable(player, entity);
  });

  var sorted = eatable.sort(function(a, b) {
    return distance(a, player) <= distance(b, player);
  });

  return sorted[0];
};

/**
 * Assign a target to each controlled entity which is not already assigned.
 * If the current target is no longer eatable or no longer exists, reassign a new one.
 */
var assignTargets = function(myEntities, allEntities) {
  myEntities.forEach(function(mine) {
    if(!targets[mine.id]) {
      targets[mine.id] = getNearestEatableEntity(allEntities, mine);
      debug('Assigned my entity', mine.id, 'to target', targets[mine.id].id);
      return;
    }

    // Even if a target is already assigned, we must check that it exists
    // and is still eatable
    var target = getEntityById(targets[mine.id]);
    if(!target || !isEatable(mine, target)) {
      targets[mine.id] = getNearestEatableEntity(allEntities, mine);
      debug('REassigned my entity', mine.id, 'to target', targets[mine.id].id);
      return;
    }
  });
};

// Player identifiers range from 0 to 4
var playerId = parseInt(readline());
/**
 * My entity id => its target entity's id
 * This cannot be a simple property of the entities because they're reloaded
 * at each game turn.
 */
var targets = {};

// Main game loop
while (true) {
  // The number of chips under your control
  var playerChipCount = parseInt(readline());
  // The total number of entities on the table, including your chips
  var entityCount = parseInt(readline());

  // ----- Input
  var entities = [];
  for (var i = 0; i < entityCount; i++) {
    var inputs = readline().split(' ');
    var entity = {
      // Unique identifier for this entity
      id: parseInt(inputs[0]),
      // The owner of this entity (-1 for neutral droplets)
      owner: parseInt(inputs[1]),
      // The current radius of this entity
      radius: parseFloat(inputs[2]),

      // Position (x from 0 to 799, y from 0 to 514)
      x: parseFloat(inputs[3]),
      y: parseFloat(inputs[4]),
      // Speed
      vx: parseFloat(inputs[5]),
      vy: parseFloat(inputs[6]),
    };
    entities.push(entity);
  }

  // ----- Game logic
  var myEntities = getMyEntities(entities);

  debug('There are', entities.length, 'in total (', myEntities.length, 'are mine).');

  assignTargets(myEntities, entities);

  // ----- Output
  myEntities.forEach(function(mine) {
    // One instruction per chip: 2 real numbers (x y) for a propulsion, or 'WAIT' to stay still
    // You can append a message to your line, it will get displayed over the entity
    var target = targets[mine.id];
    if(target) {
      // TODO: aim for the prospective position (taking into account the target's speed)
      // TODO: do not move if we're already on course
      print(target.x, target.y);
    }
    else {
      print('WAIT');
    }
  });
}
