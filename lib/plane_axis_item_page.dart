import 'package:arkit_plugin/arkit_plugin.dart';
import 'package:flutter/material.dart';
import 'package:vector_math/vector_math_64.dart' as vector;

class PlaneBuildAxisPage extends StatefulWidget {
  @override
  _PlaneBuildAxisPageState createState() => _PlaneBuildAxisPageState();
}

class _PlaneBuildAxisPageState extends State<PlaneBuildAxisPage> {
  ARKitController arkitController;
  bool anchorWasFound = false;
  ARKitSceneView containerView;
  ARKitPlaneAnchor planeAnchor;

  ARKitPlane plane;
  ARKitNode node;
  ARKitNode xNode;
  ARKitNode yNode;
  ARKitNode zNode;

  ARKitLine xAxis;
  ARKitLine yAxis;
  ARKitLine zAxis;

  ARKitSphere planeNodeSphere;

  String anchorId;

  @override
  void dispose() {
    arkitController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(title: const Text('Plane Detection')),
        body: Container(
          child: containerView = ARKitSceneView(
            showFeaturePoints: false,
            planeDetection: ARPlaneDetection.horizontal,
            detectionImagesGroupName: "AR Resources",
            onARKitViewCreated: onARKitViewCreated,
            showWorldOrigin: true,


          ),
        ),
      );

  void onARKitViewCreated(ARKitController arkitController) {
    this.arkitController = arkitController;
    this.arkitController.onAddNodeForAnchor = _handleAddAnchor;
    this.arkitController.onUpdateNodeForAnchor=_handleAnchorChange;
    
    print(containerView.detectionImagesGroupName);
  }

  void _handleAddAnchor(ARKitAnchor anchor) {
    if (anchor is ARKitImageAnchor) {
      _addImageAchor(arkitController, anchor);
    } else if (anchor is ARKitPlaneAnchor) {
      if (!anchorWasFound) _addPlane(arkitController, anchor);
    }
    /*  if (!(anchor is ARKitPlaneAnchor)) {
      return;
    }*/
    //
  }

  void _addPlane(ARKitController controller, ARKitPlaneAnchor anchor) {
    anchorWasFound = true;
  //  containerView.configuration.
    planeAnchor=anchor;
    print("plane anchor id:"+anchor.nodeName+"  postion:" +
        " x:" +
        anchor.center.x.toStringAsFixed(8) +
        " y:" +
        anchor.center.x.toStringAsFixed(8) +
        " z:" +
        anchor.center.x.toStringAsFixed(8));
    anchorId = anchor.identifier;
    final sphere = ARKitSphere(
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
      radius: 0.01,
    );
    /*  plane = ARKitPlane(
      width: anchor.extent.x,
      height: anchor.extent.z,
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
    );*/



    xAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0.3, 0, 0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.green),
        )
      ],
    );

    yAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0, 0.3, 0),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.yellow),
        )
      ],
    );

    zAxis = ARKitLine(
      fromVector: vector.Vector3(0, 0, 0),
      toVector: vector.Vector3(0, 0, 0.3),
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.lightBlue),
        )
      ],
    );
    node = ARKitNode(
      geometry: sphere,
      position: vector.Vector3(0, 0, 0),
      //position: vector.Vector3(anchor.center.x, anchor.center.y, anchor.center.z),
      //rotation: vector.Vector4(1, 0, 0, -math.pi / 2),
    );
    xNode = ARKitNode(
      geometry: xAxis,
    );
    yNode = ARKitNode(
      geometry: yAxis,
    );
    zNode = ARKitNode(
      geometry: zAxis,
    );
    controller.add(node, parentNodeName: anchor.nodeName);
    controller.add(xNode, parentNodeName: anchor.nodeName);
    controller.add(yNode, parentNodeName: anchor.nodeName);
    controller.add(zNode, parentNodeName: anchor.nodeName);
    print("z axis node" +
        " x:" +
        zNode.position.value.x.toStringAsFixed(3) +
        " y:" +
        zNode.position.value.y.toStringAsFixed(3) +
        " z:" +
        zNode.position.value.x.toStringAsFixed(3));

  }

  void _addImageAchor(
      ARKitController arkitController, ARKitImageAnchor anchor) {
    anchorId = anchor.identifier;
    final sphere = ARKitSphere(
      materials: [
        ARKitMaterial(
          transparency: 1,
          diffuse: ARKitMaterialProperty(color: Colors.red),
        )
      ],
      radius: 0.01,
    );
    anchor.referenceImageName;
    final imagePosition = anchor.transform.getColumn(3);
    final node = ARKitNode(
      geometry: sphere,
      position:
          vector.Vector3(imagePosition.x, imagePosition.y, imagePosition.z),
      eulerAngles: vector.Vector3.zero(),
    );
    arkitController.add(node);
  }

  void _handleAnchorChange(ARKitAnchor anchor) {
    //print("anchor change:"+anchor.toString()+"  anchor name:"+anchor.nodeName+  "   plane anchor name:"+planeAnchor.nodeName);
    if(anchor.nodeName == planeAnchor.nodeName){
      print("plane anchor:" +
          " x:" +
          planeAnchor.center.x.toStringAsFixed(8) +
          " y:" +
          planeAnchor.center.x.toStringAsFixed(8) +
          " z:" +
          planeAnchor.center.x.toStringAsFixed(8));
    }
  }
}
