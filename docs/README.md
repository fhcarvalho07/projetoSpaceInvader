# Space Invader

## Game Description
The player controls a spaceship that moves left and right using the **arrow keys** and shoots bullets using the **spacebar**. Waves of enemy spaceships also move horizontally across the screen while firing back at the player.

### Main Objective & Rules
* **Goal:** Destroy all enemy spaceships while we try to dodge their bullet shots.
* **Lives:** Both the player's ship and each enemy ship have **3 lives** (it takes 3 bullet shots to destroy a ship).
* **Victory / Defeat:** The player wins by eliminating all enemies, and loses if their spaceship runs out of lives.

## Implemented Features

* **Player Movement:** Real-time handling of keyboard arrow keys for smooth horizontal movement within screen bounds.
* **Shooting Mechanism:** Spacebar input mapping to spawn and propel player bullets upward.
* **Enemy Wave Logic:** Automatic horizontal tracking, movement bounds, and enemy firing behavior.
* **Health & Damage System:** Entity collision detection and life-decrement logic (tracking 3 hits per ship).
* **Infinite Level Progression:** Dynamic difficulty scaling that increases enemy speed and fire rate upon clearing levels.
* **Terminal GUI (Lanterna):** Complete rendering engine abstraction using Lanterna to draw characters, positions, and user interface elements in the terminal.

> 💡 **Screenshots / Gameplay Preview:**  
> IMAGEM DO JOGO (PRE_VIZUALIZAÇÃO)
>



### General Architecture (Layered / MVC-Inspired Pattern)

To ensure high maintainability, modularity, and testability, the project follows a clean separation of concerns inspired by the **Model-View-Controller (MVC)** and **Layered Architecture** principles. By decoupling the core game rules from the terminal rendering engine (Lanterna) and the main execution loop, each class is assigned a single, well-defined responsibility:

- **Application & Game Loop Control (`Application`, `Game`):** 
  - `Application` serves as the entry point of the program, responsible for initializing the terminal environment and starting the game engine.
  - `Game` controls the core **Game Loop**, managing the frame rate (ticks), capturing real-time keyboard inputs from Lanterna, and coordinating the continuous cycle of updating the game state and refreshing the screen.
- **Game World & Business Logic (`Arena`, `SpaceShip`, `Bullet`):** 
  - `Arena` acts as the central model and coordinator of the playable world. It holds the screen boundaries, tracks all active entities, manages wave progression (scaling enemy speed and fire rates across infinite levels), and resolves collision detection between projectiles and ships.
  - `SpaceShip` encapsulates the state and behavior of both the player's vessel and enemy ships, including horizontal movement constraints, shooting triggers, and the 3-life health system.
  - `Bullet` represents active projectiles in the arena, managing their vertical trajectory (upward for player shots, downward for enemy fire) and active status.
- **Domain Support & Geometry (`Position`):** 
  - `Position` provides a dedicated abstraction for 2D grid coordinates `(x, y)` on the terminal screen. Centralizing coordinate logic and equality checks in a single value-like object avoids code duplication and simplifies collision and movement calculations across all game entities.

```mermaid
classDiagram
    class Application {
        +main(String[] args)
    }

    class Game {
        +start()
        +run()
    }

    class Arena {
        -width: int
        -height: int
        +step()
        +verifyCollisions()
    }

    class SpaceShip {
        -health: int
        +moveLeft()
        +moveRight()
        +shoot()
    }

    class Bullet {
        +moveUp()
        +moveDown()
    }

    class Position {
        -x: int
        -y: int
        +getX()
        +getY()
    }

    Application --> Game : initializes
    Game --> Arena : runs
    Arena --> SpaceShip : contains
    Arena --> Bullet : contains
    SpaceShip --> Position : has
    Bullet --> Position : has
    ```
