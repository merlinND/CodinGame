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

var isMine = function(entity) {
  return (entity.owner == playerId);
};
var getMyEntities = function(allEntities) {
  return allEntities.filter(function(entity) {
    return isMine(entity);
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
  return (mine.radius * 0.87 > target.radius) || isMine(target);
};

/**
 * @return {Boolean} Whether or not the velocity is already high enough
 */
var canMove = function(mine) {
  // TODO: tweak max speed (should take target into account)
  var can = !mine.allowRedirect && (Math.sqrt(Math.pow(mine.vx, 2) + Math.pow(mine.vy, 2)) >= 20);
  mine.allowRedirect = false;
  return can;
};

/**
 * @TODO Do not target an entity which is fleeing away
 * @TODO Do not target an entity which is "protected" by an uneatable entity
 *
 * @warning Will return `null` if no entity is eatable
 */
var getNearestEatableEntity = function(entities, player) {
  // TODO: check
  var eatable = entities.filter(function(entity) {
    return isEatable(player, entity) && (entity.id != player.id);
  });

  var sorted = eatable.sort(function(a, b) {
    return distance(a, player) > distance(b, player);
  });

  return sorted[0];
};

var assignTarget = function(myEntity, target) {
  targets[myEntity.id] = (target ? target.id : null);
  // Allow to rectify course, even if we're already moving over target speed
  myEntity.allowRedirect = true;
  debug('Assigned my entity', myEntity.id, 'to target', targets[myEntity.id]);
};

/**
 * Assign a target to each controlled entity which is not already assigned.
 * If the current target is no longer eatable or no longer exists, reassign a new one.
 */
var assignTargets = function(myEntities, allEntities) {
  myEntities.forEach(function(mine) {
    var target;

    if(!targets[mine.id]) {
      target = getNearestEatableEntity(allEntities, mine);
      assignTarget(mine, target);
      return;
    }

    // Even if a target is already assigned, we must check that it exists
    // and is still eatable
    target = getEntityById(allEntities, targets[mine.id]);
    if(!target || !isEatable(mine, target)) {
      target = getNearestEatableEntity(allEntities, mine);
      assignTarget(mine, target);
      return;
    }
  });
};

/**
 * @param {Object} entity A moving entity. We assume it will stay on its current course.
 *   Properties `vx` and `vy` are expressed in units per round.
 * @param {Integer} [n] Number of rounds to predict over. Defaults to 1
 */
var estimatePosition = function(entity, n) {
  if(!n) {
    n = 1;
  }

  return {
    x: entity.x + n * entity.vx,
    y: entity.y + n * entity.vy
  };
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
  assignTargets(myEntities, entities);

  // ----- Output
  debug('There are', entities.length, 'in total (', myEntities.length, 'are mine).');
  myEntities.forEach(function(mine) {
    // One instruction per chip: 2 real numbers (x y) for a propulsion, or 'WAIT' to stay still
    // You can append a message to your line, it will get displayed over the entity

    var target = getEntityById(entities, targets[mine.id]);
    if(target && !canMove(mine)) {
      // TODO: move only if we're no longer on course and it's not too costly
      // TODO: estimate several rounds ahead
      var estimatedPosition = estimatePosition(target);
      print(estimatedPosition.x, estimatedPosition.y, target.id);
    }
    else {
      print('WAIT');
    }
  });
}
