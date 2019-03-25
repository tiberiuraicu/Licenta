import { Surface, Path, Text, Group, geometry } from '@progress/kendo-drawing';
const { transform } = geometry;

export function drawScene(surface: Surface) {
  // Create a path and draw a straight line
  const pathSolar = new Path({
    stroke: {
      color: `#9999b6`,
      width: 2
    }
  });
  const pathNormal = new Path({
    stroke: {
      color: `#9999b6`,
      width: 2
    }
  });

  pathSolar.moveTo(50, 50).lineTo(50, 600).close();
  pathNormal.moveTo(90, 50).lineTo(90, 600).close();

  // Create the text
  const text = new Text(`Hello World!`, [20, 25], {
    font: `bold 15px Arial`
  });

  // Place all the shapes in a group
  const group = new Group();
  group.append(pathSolar,pathNormal);

  // Rotate the group
  

  // Render the group on the surface
  surface.draw(group);
}

