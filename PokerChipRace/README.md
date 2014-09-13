
CondinGame — PokerChipRace
==========================

See the challenge's rules on [CodinGame](http://www.codingame.com/challenge/winamax).

## Explanations on the physics engine [from the forum](http://www.codingame.com/forum/t/pokerchiprace-physics-explanations/268)

- There is no friction on the table.

- The mass of an entity is equal to it's surface area.When 1 chip eats another entity, the chip gains the mass of the entity and its new position is the center of gravity between the 2 entities, the resulting velocity is the weighted average by the mass of the 2 velocities.

  ```
  V = (v1*m1 + v2*m2) / (m1+m2)
  ```

- When you propel a chip, it launches an entity of 1/15 of its mass, and shrinks accordingly. The launched entity always has a relative speed of 200 units/round. The force applied to the propelling chip is the same as the one applied to the propelled entity but in the opposite direction. So to the speed of the chip will be added 200/14 units/round, because it is 14 times bigger that what it expelled. You cannot fire when your radius is below 5 units.

- When you collide with a border or another entity with exactly the same size, you bounce off without any friction. The applied impulse is calculated with this formula :

  ```
  impulse = normalVector*2*(normalVector · relativeVelocity)/(1/entity1mass + 1/entity2mass)
  ```
