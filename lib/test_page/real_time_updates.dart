import 'dart:ui';

import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;

class RealTimeUpdatesPage extends StatefulWidget {
  @override
  _RealTimeUpdatesPageState createState() => _RealTimeUpdatesPageState();
}

class _RealTimeUpdatesPageState extends State<RealTimeUpdatesPage> {
  ARKitController arkitController;
  ARKitNode movingNode;
  bool busy = false;
  Scaffold scaffold;
  GlobalKey _key = GlobalKey();

  @override
  void dispose() {
    arkitController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    scaffold = Scaffold(
        appBar: AppBar(
          title: const Text('Real Time Updates Sample'),
        ),
        body: GestureDetector(
            key: _key,
            child: Container(
              child: ARKitSceneView(
                onARKitViewCreated: _onARKitViewCreated,
              ),
            ),
            onTapDown: handleTapDown
        ));
    return scaffold;
  }

  void _onARKitViewCreated(ARKitController arkitController) {
    final ARKitMaterial material = ARKitMaterial(
      diffuse: ARKitMaterialProperty(color: Colors.white),
    );

    final sphere = ARKitSphere(
      materials: [material],
      radius: 0.01,
    );

    movingNode = ARKitNode(
      geometry: sphere,
      position: vector.Vector3(0, 0, -0.25),
    );

    this.arkitController = arkitController;
    this.arkitController.updateAtTime = (time) {
//      print("update time");
//      print("is busy:"+busy.toString());
      if (busy == false) {
        busy = true;
        this.arkitController.performHitTest(x: 0.0001, y: 0.0001).then((results) {
          if (results.isNotEmpty) {
            final point = results.firstWhere(
                    (o) => o.type == ARKitHitTestResultType.featurePoint,
                orElse: () => null);
            if (point == null) {
              return;
            }
            //  print("point "+point.toString());

            final position = vector.Vector3(
              point.worldTransform
                  .getColumn(3)
                  .x,
              point.worldTransform
                  .getColumn(3)
                  .y,
              point.worldTransform
                  .getColumn(3)
                  .z,
            );

            /*  print("point world pos  x: "+position.x.toStringAsFixed(4)+
                "  y:"+position.y.toStringAsFixed(4) +
                "  z:"+position.z.toStringAsFixed(4));
*/
            final ARKitNode newNode = ARKitNode(
              geometry: sphere,
              position: position,
            );
            this.arkitController.remove(movingNode.name);
            movingNode = null;
            this.arkitController.add(newNode);
            movingNode = newNode;
          }
          // busy = false;
        });
      }
    };

    this.arkitController.add(movingNode);
  }

  void handleTapDown(TapDownDetails details) {
    print("on tap down global pos x:" +
        details.globalPosition.dx.toStringAsFixed(4) + "  y:" +
        details.globalPosition.dy.toStringAsFixed(4));
    print("on tap down local pos x:" +
        details.localPosition.dx.toStringAsFixed(4) + "  y:" +
        details.localPosition.dy.toStringAsFixed(4));

    final RenderBox box = _key.currentContext.findRenderObject();
    final size = box.size;
    // final topLeftPosition = box.localToGlobal(Offset.zero);
    print("total size width:" + size.width.toStringAsFixed(4) + "  height:" +
        size.height.toStringAsFixed(4));

    this.arkitController
        .performHitTest(x: details.localPosition.dx / size.width,
        y: details.localPosition.dy / size.height)
        .then((results) {
          print("hit test result:"+results.isNotEmpty.toString());
      if (results.isNotEmpty) {
        final ARKitMaterial material = ARKitMaterial(
          diffuse: ARKitMaterialProperty(color: Colors.white),
        );

        final sphere = ARKitSphere(
          materials: [material],
          radius: 0.01,
        );
        final point = results.firstWhere(
                (o) => o.type == ARKitHitTestResultType.featurePoint,
            orElse: () => null);
        if (point == null) {
          return;
        }
        //  print("point "+point.toString());

        final position = vector.Vector3(
          point.worldTransform
              .getColumn(3)
              .x,
          point.worldTransform
              .getColumn(3)
              .y,
          point.worldTransform
              .getColumn(3)
              .z,
        );

          print("point world pos  x: "+position.x.toStringAsFixed(4)+
                "  y:"+position.y.toStringAsFixed(4) +
                "  z:"+position.z.toStringAsFixed(4));

        final ARKitNode newNode = ARKitNode(
          geometry: sphere,
          position: position,
        );
        this.arkitController.remove(movingNode.name);
        movingNode = null;
        this.arkitController.add(newNode);
        movingNode = newNode;

      }
    });
  }
}
