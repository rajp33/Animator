# Animator

<b>Model</b>

Helps to create simple but effective 2D animations from shapes.

The Animation interface represents one of these animations.

- The getShapes() method returns a list of the shapes currently in the animation.
- The createShape(Shape) method creates a shape in the animation.
- The addMotion(String, Motion) method adds a motion (to a shape with the given name) to be run 
during the animation.
- The setTime() method sets the time of the animation, updating all of the shapes based on the
motions in the animation.
- The status() method prints the animation as a string. It displays the output in a tabular format
similar to the one in its javadoc.

We decided to make the animation cs3500.animator.model in this way so that controllers can easily change, update, 
and pass-on the information in the animation using the given methods while also retaining privacy.
This also ensures all Animations will have the ability to create, update, and show the information 
of each individual shape in the animation while also being efficient in the way that it transmits 
data because the setTime method mutates the shapes instead of returning new shape objects every 
tick. 

The process for using an animation is to add shapes, and add motions. The shapes then are mutated
using the setTime(int) method.

The Animation class is tied to Shapes and Motions and while this makes the interface less 
usable, the animation class needs certain functionality from shapes (their parameters and their 
ability to respond to events from a Motion) and from motions that must be similar across all
implementations of animations.  

To ensure that all motions and shapes have the same basic functionality and information,
we created the Shape interface and the Motion interface. 

Motions are able to show their start and end parameters and are able to add subscribers. This allows
them to be decoupled from Shapes/other Objects.

Shapes have getters and setters for all of their parameters (height, width, position, color) and
extend the MotionListener interface. 

AShape was created to abstract out most of the code in concrete example of shapes.

AnAnimation was created to abstract out boilerplate code for an animation.

We made a MotionListener to be able to decouple shapes and motions. Having a Motion act as something 
that sends out events to all of its subscribers allows one motion to be able to influence multiple 
Shapes (and other types of objects in the future). Motions can send information via Parameters to
their respective subscribers.

Parameters was made to be able to transfer data from a Motion to a Shape and allows for some
flexibilty in the way that it is used because implementations of Shapes that require more 
information can have an implementation of Parameters to do so.

The two basic types of Shapes supported at the moment are Ellipses and Rectangles.

<b>View</b>

We decided to use the Model-ViewModel-View pattern in our design as there was no need for an actual
controller and the views were so different.

The ViewModel interface is represented by the AnimationView interface. 

The editor view allows for the user to edit the animation by modifying the shapes of an animation 
and the keyframes for each shape. We implemented buttons that allow the user to pause, play and 
restart the current animation, a table to edit the shapes and keyframes of each shape, a radio 
button that determines if the animation should loop or not, and sliders that allow the user to 
change the time and speed in the animation. 

Invalid inputs such as negative values for color, position, or size are not allowed by the program
and will not be accepted. They will also not crash the program.

This view works independently of the previous view implementations and the previous implementations 
are unchanged. The editor can be accessed in the command line with '-view edit'.


#Changelog
<b>assignment 6</b>
- added ShapeFactory enum to allow creation of shapes based on their type.
- implemented AnimationBuilder in the AnimationImpl class
- updated Motion#updateTo(MotionListener) with tweening function
- Added setBounds method to Animation interface
- added getCanvasParams to Animation interface
- added get/set Speed to model
- added draw and svgattribute methods in shape factory
- changed shape width/height to double values
- added Views

<b>assignment 7</b>
- added getendTime to Animation
- changed Timer from java.util.Timer to a swing timer (javax.swing.Timer)
- created AnimationEditor interface (extends Animation) and modified AnAnimation to implement that
    - so that we can retain functionality of animation while adding functionality for editor
- made motions consist of two keyframes
- added interface for Parameters class, called KeyFrameModel
- added controller
- added AnimationEditorView which extends AnimationView
- added read-only shapes and had normal shapes inherit from read-only
- added the ability to change keyframes.
- added the ability to add and remove shapes and keyframes.
- created components for editor view
- changed main to run controller

<b>
Owen LoveLuck and Raj Patel
</b>