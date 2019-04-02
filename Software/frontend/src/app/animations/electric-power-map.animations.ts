import {
  trigger,
  transition,
  animate,
  style,
  state
} from "@angular/animations";

export let extend = trigger("extend", [
  state(
    "retracted",
    style({
      marginBottom: "1%",
      width: "73%",
      float: "right",
      marginLeft: "1%",
      height: "10%"
    })
  ),
  state(
    "expanded",
    style({
      transform: "translate(calc(0%), calc(-{{offsetTop}}px))",
      marginTop: "20px",
      width: "73%",
      float: "right",
      marginLeft: "1%",
      height: "100%"
    }),
    { params: { offsetTop: 0 } }
  ),
  transition("retracted <=> expanded", animate(800))
]);

export let fade = trigger("fade", [
  state(
    "hidden",
    style({
      opacity: 0,
      pointerEvents: "none"
    })
  ),
  state(
    "visible",
    style({
      opacity: 1
    })
  ),
  transition("visible => hidden", animate(500)),
  transition("hidden => visible", animate(1000))
]);

export let cardEnter = trigger("cardEnter", [
 
  transition(":enter", [style({ opacity:'0'}),animate(1000,style({
    opacity:'1'
  }))]),
  transition(":leave", animate(1000,style({
    opacity:'0'
  }))),
]);