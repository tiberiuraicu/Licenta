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
 
  transition(":enter", [style({ opacity:'0', transform : 'translate(0%,15%)'}),animate('0.5s ease-in' ,style({
    opacity:'1',transform : 'translate(0%,0%)'
  }))]),
  transition(":leave", animate('0.5s ease-out',style({
    opacity:'0',transform : 'translate(0%,15%)'
  }))),
]);