# forcarbonit
coding exercice

# the game
Our adventurers are sent into a world full of treasures to collect them by following a path given by an old parchment.
The world is made of mountains and treasures.

# rules
Adventurers can't go to mountains, they need to bypass it.
Adventurers can spawn on a treasure.
Adventurers can only collect 1 treasure at a time, therefore they need to move out of a position to collect the other one.

# adventurer's moves
An adventurer can only move forward once during a round, but he can orientate left/right as many time as in the parchment.
During one round, if he need to go forward but can't move because of a mountain, he continues his path until he is unblocked.
A = go forward
G = orientation left
D = orientation right

# specificity
if an element's initial position is already occupied, a position near from it is assigned
if an adventurer is on an edge of the map exceeds it, he/she will go at the opposite.

# inputs
#a comment

{map} - {width} - {height}
example : C - 3 - 4

{mountain} - {pos x} - {pos y}
example : M - 1 - 0

{treasure} - {pos x} - {pos y} - {quantity}
example : T - 0 - 3 - 2

{adventurer} - {player name} - {pos x} - {pos y} - {orientation} - {path}
example : A - Lara - 1 - 1 - S - AADADAGGA

# output
C​ - 3 - 4
M - 1 - 0
M ​- 2 - 1
#{Treasure} - {Horizontal axis} - {Vertical axis} - {Treasure left}
T - 1 - 3 - 2
#{Adventurer} - {Name} - {Horizontal axis} - {Vertical Axis} - {Orientation} - {Treasures collected}
A ​- Lara - 0 - 3 - S - 3
