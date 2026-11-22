# Space Invader

## Game Description

Space Invaders is a text-based arcade shooter inspired by the 1978 classic. The player controls a ship that fires automatically, and must move left and right to destroy a squad of invaders while dodging the shots they drop. Each invader has 3 health points, and some defeated enemies drop power-ups that add a life, increase ship speed or activate a shield state. The game ends when the ship runs out of lives.

The game runs on a 120×40 Lanterna terminal (`spaceInvaders.ttf` and `pressStart2P.ttf` fonts), rendering the ship, invaders, bullets and power-ups as text characters.

This project was developed by *Rodrigo Faria* (up202507847@up.pt), *Francisca Vieira* (up202506275@up.pt) and *Francisco Carvalho* (up202504002@up.pt) for LDTS 2026/27.

**Space Invader** is a terminal-based arcade shoot-'em-up game developed in Java using the **Lanterna** library. The player pilots a `SpaceShip` (starting at `(10, 30)`), controlling its horizontal movement with the **left and right arrow keys**, stopping it with the **down arrow key**, while projectiles are fired automatically upward. Waves of `Enemy` ships maneuver across the arena while firing back at the player.

### Main Objective & Rules
- **Goal:** Destroy enemy spaceships while dodging incoming enemy fire.
- **Health & Lives:** Both `SpaceShip` (`MAX_LIVES = 3`) and `Enemy` (`MAX_HEALTH = 3`) start with **3 lives/health points** (requiring 3 hits to reach 0).
- **Defeat / Exit:** The game ends when `spaceShip.isDead()` is true (`lives <= 0`), or when exiting via `q`, `EOF`, or closing the window.

---

## Implemented Features

- **Player Movement & Braking:** Left (`-1`) and right (`1`) arrow keys set the horizontal direction (`d`) in `SpaceShip`, which moves every `4` frames (`movementDelay = 4`) unless stopped with the down arrow (`d = 0`) or blocked at the horizontal arena bounds (`0 <= x < width`).
- **Automatic Shooting:** `Arena.fireBullet()` spawns a `Bullet` above the ship whenever `fireCooldown == 0`, resetting `fireCooldown` to `12` frames (`COOLDOWN_FRAMES = 12`). Bullets move up `1` row per `moveBullets()` call and are removed when `y < 0`.
- **Enemy Spawning & Movement:** `Arena` spawns `15` enemies (`ENEMY_COUNT = 15`) at random non-overlapping positions in rows `1` to `10` (`ENEMY_MIN_Y = 1`, `ENEMY_MAX_Y = 10`). Each enemy advances its counter every `32` frames (`8 * 4`), proposes right/left/down moves (`possiblePositions()`), filters out-of-bounds or occupied cells in `Arena.moveEnemies()`, and picks one randomly (`chooseNextPosition()`).
- **Enemy Firing:** Each enemy has a `1` in `300` chance (`FIRE_CHANCE = 300`, `wantsToFire()`) to spawn an `EnemyBullet` at `(x, y + 1)`. Enemy bullets move down `1` row every `2` frames (`movementDelay = 2`) and are removed when `y >= height`.
- **Health, Collisions & Game Over:** `Arena` checks bullet-enemy collisions (decrementing enemy `health` via `getHit()` and removing enemies at `0` HP), enemy-bullet-ship collisions (calling `spaceShip.getHit()`), and sets `running = false` when `arena.isGameOver()` (`spaceShip.isDead()`) becomes true.
- **Power-Ups:** Defeating an enemy gives a `25%` chance (`random.nextDouble() < 0.25`) to spawn one of three `PowerUp` subclasses (`AddLife`, `Shield`, `SpeedUp`) that move down `1` row per `movePowerUps()` call and apply their effect on contact with `spaceShip`:
  - **Extra Life (`AddLife`):** Calls `spaceShip.addLife()` (`lives++`).
  - **Speed Up (`SpeedUp`):** Calls `spaceShip.increaseSpeed()` (decrements `movementDelay` down to a minimum of `1`).
  - **Shield (`Shield`):** Calls `spaceShip.activateShield()` (`shieldActive = true`).
- **Terminal GUI & Exit Handling:** Rendering via Lanterna's `Screen` and `TextGraphics` (`Arena.draw(graphics)`), with exit handling on `'q'`, `KeyType.EOF`, or window closing.

> 💡 **Screenshots / Gameplay Preview:**  
> <img width="1624" height="970" alt="Screenshots : Gameplay Preview" src="https://github.com/user-attachments/assets/a74228d1-f346-4832-a216-b67c8ba059f3" />

---

## General Architecture & Code Structure

To ensure high maintainability, modularity, and testability, the project separates execution control, arena coordination, domain entities, and 2D coordinates:

- **Application Entry & Execution Control (`Application`, `Game`):**
  - `Application` serves as the entry point (`main`), instantiating `Game` and calling `game.run()`.
  - `Game` configures the Lanterna terminal and `Screen`, runs the `while (running)` loop, polls keyboard input (`screen.pollInput()`), advances `Arena` state, calls `draw()`, and sleeps `30` ms per iteration.
- **Arena Coordinator (`Arena`):**
  - `Arena` holds `width`, `height`, `spaceShip`, `bullets`, `enemies`, `enemyBullets`, `powerUps`, `fireCooldown`, and `random`, coordinating movement, firing, power-up spawning, collisions, game-over checks, and drawing.
- **Domain Entities (`Element`, `SpaceShip`, `Enemy`, `Bullet`, `EnemyBullet`, `PowerUp`):**
  - `Element` is the abstract base class storing `Position position` and declaring `abstract void draw(TextGraphics graphics)`.
  - `SpaceShip` handles horizontal direction (`d`), movement frame timing (`updateMovementCounter()`), `nextPosition()`, lives (`getLives()`, `getHit()`, `isDead()`, `addLife()`), speed (`increaseSpeed()`), and shield flag (`activateShield()`, `hasShield()`).
  - `Enemy` handles health (`getHealth()`, `getHit()`, `isDead()`), movement frame timing (`updateMovementCounter()`), firing (`wantsToFire()`, `fire()`), and candidate positions (`possiblePositions()`, `chooseNextPosition()`).
  - `Bullet` and `EnemyBullet` encapsulate vertical movement (`move()`) and boundary checks (`isOffScreen(...)`).
  - `PowerUp` is the abstract base class for `AddLife`, `Shield`, and `SpeedUp`, defining `move()`, `isOffScreen(int height)`, and `abstract void applyEffect(SpaceShip spaceShip)`.
- **Geometry & Value Object (`Position`):**
  - `Position` encapsulates `x` and `y` coordinates with getters/setters and overrides `equals(Object o)` for position and collision comparisons.

> **Overall System Class Architecture**  
> <img width="2216" height="2605" alt="codeStructure" src="https://github.com/user-attachments/assets/02c4ddf9-2d1c-4f4f-91ab-0d9c831de814" />

- A comprehensive UML class diagram illustrating the structural hierarchy, relationships, and dependencies across the entire codebase, from the entry point (Application) down to core game entities (Element) and spatial coordinates (Position).

---

## Design Patterns

To solve recurring architectural challenges cleanly and avoid tight coupling between components, core design patterns were implemented:

### 1. Game Loop Pattern
- **Problem in Context:** An interactive real-time game requires continuous state updates (ship movement, automatic firing cooldowns, enemy movement and firing, bullet and power-up movement, and collision checks) and screen refreshes without blocking while waiting for keyboard input.
- **The Pattern:** The **Game Loop** pattern decouples simulation progression from blocking input by running a continuous loop (`while (running)`) that polls input (`screen.pollInput()`), updates state, renders the frame, and paces execution (`Thread.sleep(30)`).
- **Implementation:** Implemented in `Game.run()`, using Lanterna's `Screen` (`pollInput()`, `clear()`, `newTextGraphics()`, `refresh()`) and delegating state updates and drawing to `Arena` on each tick, with distinct frame cadences per entity (`4` frames for `SpaceShip`, `2` frames for `EnemyBullet`, `32` frames for `Enemy`, and `12` frames cooldown for `Arena.fireBullet()`).

> **Game Loop Pattern Structure**  
> <img width="1257" height="964" alt="gameLoop" src="https://github.com/user-attachments/assets/3f3d47ff-485c-43ea-aedf-87b6535dab57" />

- A structural diagram depicting how the Game class decouples real-time execution from user input and rendering by coordinating continuous state updates with Arena and terminal I/O operations with Lanterna's Screen interface.

- **Consequences:**
  - *Benefits:* Keeps simulation running continuously even when no key is pressed, isolates Lanterna calls in `Game`/`Arena.draw`, and makes relative speeds easy to tune via frame counters.
  - *Liabilities:* Fixed `Thread.sleep(30)` does not subtract elapsed update/render time, and combining input handling, simulation calls, and rendering in `Game.run()` makes `Game` harder to unit test.

### 2. Mediator Pattern
- **Problem in Context:** Entities (`SpaceShip`, `Enemy`, `Bullet`, `EnemyBullet`, `PowerUp`) must respect boundaries, avoid overlapping enemies, resolve projectile hits, and apply collected power-ups without each element referencing every other element directly.
- **The Pattern:** The **Mediator** pattern centralizes interactions through a single coordinator object so colleagues remain loosely coupled.
- **Implementation:** `Arena` acts as the **Mediator** over `Element` subclasses (`SpaceShip`, `Enemy`, `Bullet`, `EnemyBullet`, `PowerUp`). Elements compute local proposals or movements (`nextPosition()`, `possiblePositions()`, `chooseNextPosition()`, `move()`), while `Arena` enforces bounds and coordinates interactions (`moveSpaceShip()`, `canSpaceShipMove()`, `moveEnemies()`, `isOccupied()`, `isInside()`, `checkBulletCollisions()`, `checkEnemyBulletCollisions()`, `checkPowerUpCollisions()`).

> **Arena Mediator Pattern Structure**  
> <img width="2229" height="1266" alt="arenaMediator" src="https://github.com/user-attachments/assets/a2f58ac5-61f5-43d5-8afa-91fe130688f2" />

- A class diagram illustrating the Mediator design pattern, where Arena acts as the central coordinator managing interactions, movement boundaries, and collision detection among decoupled Element colleagues (SpaceShip, Enemy, Bullet, and EnemyBullet).

- **Consequences:**
  - *Benefits:* Keeps element classes focused and easier to test in isolation, while centralizing movement and collision rules in `Arena`.
  - *Liabilities:* Concentrates many responsibilities in `Arena` (*Large Class*), and local `Random` instances in `Enemy` and `Arena` make random moves, shots, and power-up drops non-deterministic unless controlled in tests.

### 3. Command Pattern (Power-Ups)
- **Problem in Context:** Collecting different power-ups (`AddLife`, `Shield`, `SpeedUp`) triggers different actions on `SpaceShip`. Checking concrete types via conditionals inside `Arena.checkPowerUpCollisions()` would couple collision handling to each power-up type.
- **The Pattern:** The **Command** pattern encapsulates the action of applying a power-up effect behind a common method.
- **Implementation:** `PowerUp` declares `abstract void applyEffect(SpaceShip spaceShip)`, implemented by `AddLife` (`spaceShip.addLife()`), `Shield` (`spaceShip.activateShield()`), and `SpeedUp` (`spaceShip.increaseSpeed()`), and invoked by `Arena` via `powerUp.applyEffect(spaceShip)`.

---

## Planned Design Changes

- Split the code into **Model-View-Controller** packages to decouple model logic from Lanterna and test game rules without a terminal.
- Hide Lanterna behind a `GUI` interface (**Facade**) that can be mocked in tests.
- Introduce the **State** pattern for game screens (menu, gameplay, game over), wave progression, and shield consumption/damage absorption when taking a hit (`hasShield()`).
- Extract power-up creation into a **Factory Method** and inject `Random` instances for deterministic testing.

---

## Known Code Smells

- **Duplicated Code:** Frame-counter logic (`speedCounter`, `movementDelay`, `updateMovementCounter()`) is repeated across `SpaceShip`, `Enemy`, and `EnemyBullet`, and `draw(TextGraphics)` is identical across `AddLife`, `Shield`, and `SpeedUp` (`'|'`).
- **Large Class:** `Arena` handles spawning, movement validation, firing cooldowns, three collision checks, power-up drops, game-over state, and rendering.
- **Divergent Change:** `Element` subclasses (`SpaceShip`, `Enemy`, `Bullet`, `EnemyBullet`, `AddLife`, `Shield`, `SpeedUp`) import Lanterna (`TextGraphics`, `TextCharacter`) and `Arena` draws the background, coupling model elements to rendering.
- **Switch Statements:** Conditional chains (`if/else`) in `Game.run()` for key handling and in `Arena.spawnPowerUp()` for power-up selection require edits when adding new keys or power-ups.
- **Dead Code:** `movementCounter` and `MOVEMENT_DELAY` in `PowerUp` are declared but unused in `move()` / `movePowerUps()`.

---

## Testing

The project includes 56 unit tests using **JUnit Jupiter** and **Mockito** across `src/test/java` (`SpaceShipTest`, `EnemyTest`, `BulletTest`, `EnemyBulletTest`, `ArenaTest`), using mocks such as `TextGraphics` (including `InOrder` verification) and `Position` to test behavior without opening a terminal.

### Screenshot of coverage report

<img width="2902" height="390" alt="JaCoCo Coverage Report" src="https://github.com/user-attachments/assets/f08ff064-e3fb-47be-b0c8-2b1f9625d8e5" />


### Link to mutation testing report

[Mutation Testing Report](./reports/pitest/index.html)

<img width="1420" height="612" alt="Pitest Mutation Coverage Report" src="https://github.com/user-attachments/assets/04e76cf7-46c3-43b8-9df2-56a82b92144c" />


---

## Self-evaluation

- Rodrigo Faria: 33.3%
- Francisca Vieira: 33.3%
- Francisco Carvalho: 33.3%

