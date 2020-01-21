# SCC210 Game

## Building, Running, and Testing

Use `./gradlew build` to build the game

Use `./gradlew run` to build the game and run it

Use `./gradlew test` to run test cases

## Adding test cases

To add a test case, create a class in `src/test/java/scc210game/`

Import `org.junit.Test` and add `@Test` above any methods of that
class that are test cases.

Look at the existing test cases for examples.

## Adding features

1. Create a new branch based off master

1.1. `git checkout master` (if not already on master)

1.2. `git checkout -b do-my-thing`

2. Commit features to branch

3. At any time, push branch to remote

4. When pushed to remote, if not complete: create a merge request like: `WIP: Do
   my thing`
   
5. Otherwise, if the changes complete, create the merge request with the title
   `Do my thing`, and add everyone as an assignee and add the tag
   `waiting-on-review`

## Jargon

- State (`scc210game.state.State`) :: A state the game can be in, such as
  'Paused', 'MainMenu', 'MainGame', 'ViewingInventory'. States have associated
  data and determine how events and inputs are routed. Only one state can be
  active at any one time. The event handlers for states return 'Transitions'
  that tell the state machine what to do to determine the current state: such as
  moving to a new state, pushing a new state onto the state stack, and popping a
  state of the state stack.

- World (`scc210game.ecs.World`) :: A container for entities to exist in.
- Entity :: A unique identifier, has associated components. Entities are unique
  to a world.
- Component (`scc210game.ecs.Component`) :: A piece of data that is associated
  with an Entity, such as: Position, `scc210game.render.Renderable`. Empty
  Components may also be used to add flags to entities, such as:
  `scc210game.ui.Interactive`. Flag components may be added at game startup, or
  may be added/removed dynamically by other systems.
- Resource (`scc210game.ecs.Resource`) :: Similar to a component, but has no
  associated Entity.
- System (`scc210game.ecs.System`) :: Functions that run on every input/ game
  frame. They use Queries (`scc210game.ecs.Query`) to select the entities
  that have a given set of components, the idea is to break all game logic into
  systems that operate independently from each other. Examples are:
  `scc210game.render.RenderSystem`.
- Spawners (`scc210game.ecs.Spawner`) :: Functions that construct a 'type' of
  entity by adding all the required components as one, for example an entity
  used in the UI will be created using a spawner that attaches the required
  components for being a UI entity, such as `UIText`, `UITransform`, and
  `Renderable`. Examples are: `scc210game.ui.spawners.DialogueSpawner` and
  `scc210game.spawners.MapSpawner`.
  
## Common components

### scc210game.render.Renderable

This component is used to allow entities to be rendered to the screen.

It takes three parameters:

1.	includedViews :: Which views this renderable renders in
2.	height :: The height to render this at, renderables with higher height values
     render above those with lower.
3.	renderFn :: The function to call to render this entity

An example construction is located at: `scc210game.ui.spawners.DialogueSpawner`

## Common spawners

### scc210game.ui.spawners.DialogueSpawner

This spawner creates a dialogue box with a given piece of text.

## Creating components

A component is any class extending `scc210game.ecs.Component`, there is one
method that needs implementing which is `String serialize()`, but for now this
can be left as just returning null, eventually this will be used to allow the
entire game state to be stored in a database for later restoration.

So an example component is:

```java
class Position extends Component {
   public int x, y;
   
   public Position(int x, int y) {
      this.x = x;
      this.y = y;
   }

   @Override
   public String serialize() {
      return null;
   }
}
```

## Adding components to existing entities

This can be done using the
`World.addComponentToEntity(Entity e, Component component)` method.

## Fetching components

To fetch a component that an entity has: use the
`<T extends Component> T World.fetchComponent(Entity e, Class<T> componentType)`
method to retrieve the Component instance, the parameter `e` is the entity to
fetch the component for, and `componentType` is the Class object of the
component to fetch, for example:

```java
Position p = world.fetchComponent(e, Position.class);
```

This will retrieve the `Position` object associated with the entity `e`.

## Creating and Fetching Resources

Resources work the same as Components, they just don't have associated entities.

To insert a resource into the world, use `World.addResource(Resource r)`.
To fetch a resource, use:
`<T extends Resource> T World.fetchResource(Class<T> resourceType)`.

## Creating entities

You can create an entity anywhere you have access to a `World` by using the
`entityBuilder()` method, which returns a `scc210game.ecs.World.EntityBuilder`
object.

To add components to the entity you are building, call the `with(Component c)`
or `with(Spawner s)` methods of the entity builder, once all initial components
are added, call the `build()` method on the entity builder to insert the entity
into the ECS.

### Examples

This is the example of creating a dialogue using the spawner.

```java
world.entityBuilder()
     .with(new DialogueSpawner("Test"))
     .build();
```

This is the example of creating a dialogue with the spawner inlined.

```java
world.entityBuilder()
     .with(new UITransform(0, 0.8f, 0, 1.0f, 0.2f))
     .with(new UIText("Test"))
     .with(new Renderable(Set.of(ViewType.MAIN), 2, (Entity e, RenderWindow rw, World w) -> {
        var trans = w.fetchComponent(e, UITransform.class);
        var textContent = w.fetchComponent(e, UIText.class);
        var rect = new RectangleShape(UiUtils.convertUiSize(rw, trans.size())) {{
           setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
           setFillColor(UiUtils.transformColor(Color.LIGHT_GRAY));
           setOutlineColor(UiUtils.transformColor(Color.BLACK));
        }};

        rw.draw(rect);

        var text = new Text(textContent.text, font, 24) {{
           setPosition(UiUtils.convertUiPosition(rw, trans.pos()));
        }};

        rw.draw(text);
     }));

```

## Creating systems

To create a system, make a class that implements the `scc210game.ecs.System`
interface.

The required method is `run`:

```java
public void run(@Nonnull World world, @Nonnull Duration timeDelta)
```

The parameter `world` is the current world the game is in.
The parameter `timeDelta` is the time since the system last ran.

The `run` method is called whenever the game engine requires your system to run,
which is whenever a user input happens, or every 1/60th of a second.

Inside `run` you should do stuff such as:

- Querying for entities that have a set of components
- Reading all events destined for the system and react to them.

### Examples

#### A system that just processes entities

The render (`scc210game.render.RenderSystem`) system is a good example of a system that just operates on a list of
entities matching criteria (the criteria being that they need to be rendered).

First a Query is constructed, this is used to fetch entities from the world:

```java
    private Query q = Query.builder()
            .require(Renderable.class)
            .build();
```

This creates a query that fetches all entities that have an associated
`Renderable` component.

Then this query can be applied to the world using:

```java
Stream<Entity> renderEntities = world.applyQuery(q);
```

<https://docs.oracle.com/javase/10/docs/api/java/util/stream/Stream.html>

You can then apply operations such as filtering, mapping, and sorting to the
stream.

To loop over every item in the stream, use the `.forEach` method of the stream:

```java
 entities
 .forEach(t -> {
     for (ViewType vt : t.r.includedViews) {
         renderWindow.setView(this.views.get(vt));
         t.r.renderFn.accept(t.l, renderWindow, world);
     }
 });
```

#### A system that processes events

An example of a system that processes events is the UI Interaction Handler
System (`scc210game.ui.systems.HandleInteraction`).

It receives user input events from JSFML and turns them into events that target
specific entities, such as clicking an entity, dragging an entity, and hovering
over an entity.

First it creates a listener on the event queue:

```java
this.eventReader = EventQueue.makeReader();
EventQueue.listen(this.eventReader, StateEvent.class);
```

This creates an Event Queue Reader object, and then using `EventQueue.listen`
tells the event queue that the reader should receive all events that are
instances of the `StateEvent` class.

To then loop over all the `StateEvent`s the following code is used:

```java
for (Iterator<Event> it = EventQueue.getEventsFor(this.eventReader); it.hasNext(); ) {
    Event e = it.next();
    this.handleEvent(world, e);
}
```

The important part of this is `EventQueue.getEventsFor(this.eventReader)` which
returns an iterator over events that the reader is listening for.

The loop works to fetch every event from the queue, and pass it to the
`handleEvent` method of `HandleInteraction`, which has a switch statement on
types of events.
